package org.usfirst.frc.team1124.robot.commands.drive;

import org.usfirst.frc.team1124.robot.Robot;
import org.usfirst.frc.team1124.robot.tools.Safe;
import org.usfirst.frc.team1124.robot.dashboard.SafetyErrorLogger;
import org.usfirst.frc.team1124.robot.dashboard.SafetyErrorLogger.Error;
import org.usfirst.frc.team1124.robot.dashboard.SafetyErrorLogger.SafetySubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 * A PID command for the right drive train
 */
public class RightDrivePID extends PIDCommand implements Safe {
	private boolean done = false;
	
	private double speed = 0;
	private double setpoint = 0;
	
	private boolean safetyEnabled = false;
	private boolean safetyTripped = false;
	private double rate_threshold = 0.266;
	
	private static final double P = 0.027; //0.048
	private static final double I = 0.00023854167; //0.012
	private static final double D = 0.000027083; //0.0
	
    public RightDrivePID(double setpoint) {
		super("RightDrivePID", P, I, D);
		
        setInterruptible(true);
        
        getPIDController().setOutputRange(-0.75, 0.75);
        
        this.setpoint = setpoint;
        
        enableSafety();
    }
	
    public RightDrivePID(double setpoint, double minOutput, double maxOutput) {
		super("RightDrivePID", P, I, D);
		
        setInterruptible(true);
        
        getPIDController().setOutputRange(minOutput, maxOutput);
        
        this.setpoint = setpoint;
        
        enableSafety();
    }

    protected void initialize() {
    	Robot.drivetrain.resetEncoders();
    	
    	setSetpoint(setpoint);
    }

    protected void execute() {}

    public boolean isSideFinished() {
        return Math.abs(Robot.drivetrain.getRightEncoderDistance() - getSetpoint()) <= Robot.drivetrain.SETPOINT_TOLERANCE;
    }
    
    public void stop() {
    	done = true;
    }
    
    protected boolean isFinished() {
    	return done;
    }

    protected void end() {
    	Robot.drivetrain.stop();
    }

    protected void interrupted() {
    	end();
    }

	protected double returnPIDInput() {
		return Robot.drivetrain.getRightEncoderDistance();
	}

	protected void usePIDOutput(double output) {
		speed = output;
	}
	
	public PIDController getPID(){
		return this.getPIDController();
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void updateSetpoint(double setpoint){
		setSetpoint(setpoint);
	}

	// Safeties
	
	public void enableSafety() {
		safetyEnabled = true;
	}

	public void disableSafety() {
		safetyEnabled = false;
	}

	public boolean isSafetyEnabled() {
		return safetyEnabled;
	}

	public boolean isSafetyTripped() {
		return safetyTripped;
	}

	public void setRateCutoffThreshold(double threshold) {
		rate_threshold = threshold;
	}

	public double getRateCutoffThreshold() {
		return rate_threshold;
	}

	public double safeOutput(double output) {
		double safeOutput = 0;
		
		// rate safeties
		if(Math.abs(Robot.drivetrain.getRightEncoderRate()) > Double.MAX_VALUE / 4){
			// encoder was disconnected and is reading something around infinity
			safeOutput = 0;
			safetyTripped = true;
			
			SafetyErrorLogger.log(SafetySubsystem.DriveTrainRight, Error.HighRateDisconnection);
		}else{
			SafetyErrorLogger.reportNoError(SafetySubsystem.DriveTrainRight, Error.HighRateDisconnection);
		}
		
		if(Math.abs(output) > getRateCutoffThreshold() && Robot.drivetrain.getRightEncoderRate() == 0){
			// we are moving it but the encoder isn't reading it, not good
			safeOutput = 0;
			safetyTripped = true;
			
			SafetyErrorLogger.log(SafetySubsystem.DriveTrainRight, Error.NoRateDisconnection);
		}else{
			SafetyErrorLogger.reportNoError(SafetySubsystem.DriveTrainRight, Error.NoRateDisconnection);
		}
		
		// permanent disable if safety is tripped
		if(safetyTripped){
			safeOutput = 0;
		}
		
		return safeOutput;
	}
}
