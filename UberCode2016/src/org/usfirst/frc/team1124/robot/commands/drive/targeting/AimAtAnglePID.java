package org.usfirst.frc.team1124.robot.commands.drive.targeting;

import org.usfirst.frc.team1124.robot.Robot;
import org.usfirst.frc.team1124.robot.tools.VisionTools;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Aim at an angle using the gyro.
 */
public class AimAtAnglePID extends PIDCommand {
	//w/o oscillation: 0.02464, w/ oscillation: 0.064, small angle: 0.111
	private static final double P = 0.08464;
	private static final double I = 0.00003;
	private static final double D = 0.057;
	
	private double angle = 0;
	private double pixelRate = 0;
	private double prevPixel = 0;
	
	private Timer t = new Timer();
	private boolean timerFirstCall = true;
	
    public AimAtAnglePID() {
    	super("AimAtAngle", P, I, D);
        requires(Robot.drivetrain);
        
        this.getPIDController().setOutputRange(-0.5, 0.5);
    }

    protected void initialize() {
    	double setpoint = 0;
    	
    	try{
    		/*
	    	double top_left_y = SmartDashboard.getNumber("vision_target_p1_y");
	    	double top_right_x = SmartDashboard.getNumber("vision_target_p2_x");
	    	double height = SmartDashboard.getNumber("vision_target_height");
	    	
	    	boolean top_left = SmartDashboard.getBoolean("vision_top_left");
	    	boolean bottom_right = SmartDashboard.getBoolean("vision_bottom_right");
	    	
	    	double[] goalDistances = new double[] {0,0};
	    	
	    	VisionTools.goalDistances(top_left, bottom_right, top_left_y, height, goalDistances);
	    	double angleToGoal = VisionTools.getAngleToGoal(goalDistances[0], goalDistances[1], top_right_x, true);
	    	double setpoint = angleToGoal - VisionTools.angleToGoalSetpoint(goalDistances[0], goalDistances[1], true);
	    	*/
    		
	    	double xlhsGoalBBox = SmartDashboard.getNumber("vision_target_left");
	    	double widthGoalBBox = SmartDashboard.getNumber("vision_target_width");
	    	
	    	System.out.println("Target Left X: " + xlhsGoalBBox + " Width: " + widthGoalBBox);
	    	
	    	setpoint = VisionTools.turnAngle(xlhsGoalBBox, widthGoalBBox, true);
	    	
	    	System.out.println("Setpoint: " + setpoint);
    	}catch(Exception oh_no){
    		System.out.println("Fatal Targeting Error: Dashboard data not found.");
    	}
    	
    	Robot.drivetrain.resetGyro();
        
    	angle = setpoint;
        
        setSetpoint(angle);
        
        if(Math.abs(angle) <= 5){
        	double p_override = 0.102;
        	double i_override = 0.0004;
        	double d_override = 0.072;
        	
        	if(Math.signum(angle) < 0){
        		// things are different if left
        		d_override = 0.062;
        	}
        	
        	getPIDController().setPID(p_override, i_override, d_override);
        }else if(Math.signum(angle) < 0){
    		// things are different if left
        	double p_override = 0.0826;
        	double i_override = 0.00002;
        	double d_override = 0.05;

        	getPIDController().setPID(p_override, i_override, d_override);
        }
    }

    protected void execute() {
    	//getToCloseTarget();
    }

	protected double returnPIDInput() {
    	return Robot.drivetrain.getFullAngle();
	}

	protected void usePIDOutput(double output) {
		Robot.drivetrain.drive_tank_auto(output, -output);
	}

    protected boolean isFinished() {
    	if(Math.abs(getSetpoint()) < 8){
    		return Math.abs(getPIDController().getError()) <= 2.0;
    	}else{
    		return Math.abs(getPIDController().getError()) <= 1.0;
    	}
    }

    protected void end() {
    	Robot.drivetrain.stop();
    	
    	this.getPIDController().reset();
    	
    	timerFirstCall = true;
    }

    protected void interrupted() {
    	end();
    }
    
    protected void getToCloseTarget(){
    	if(timerFirstCall){
	    	this.getPIDController().disable();
	    	
	    	t.start();
	    	
	    	timerFirstCall = false;

	    	prevPixel = Robot.camera.getTargetCenterOfMass()[0];
	    	pixelRate = 0;
    	}
    	
    	double output = 0;
		double center = Robot.camera.getTargetCenterOfMass()[0];
		double acceleration;
		
    	try{
    		acceleration = SmartDashboard.getNumber("error_acceleration");
    	}catch(Exception anish){
    	}finally{
    		acceleration = 0.02;
    	}
    	
		if(center > 160){
			output = acceleration * t.get();
		}else if(center < 160){
			output = -acceleration * t.get();
		}
		
	    Robot.drivetrain.drive_tank_auto((-1) * output, output);
    }
}
