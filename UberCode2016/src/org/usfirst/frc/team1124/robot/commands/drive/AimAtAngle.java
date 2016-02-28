package org.usfirst.frc.team1124.robot.commands.drive;

import org.usfirst.frc.team1124.robot.Robot;
import org.usfirst.frc.team1124.robot.tools.VisionTools;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Aim at an angle using the gyro.
 */
public class AimAtAngle extends PIDCommand {
	//w/o oscillation: 0.02464, w/ oscillation: 0.064, small angle: 0.111
	private static final double P = 0.08422;
	private static final double I = 0.00003;
	private static final double D = 0.058;
	
	private double angle = 0;
	
	/**
	 * @param angle angle in degrees
	 */
    public AimAtAngle() {
    	super("AimAtAngle", P, I, D);
        requires(Robot.drivetrain);
        
        this.getPIDController().setOutputRange(-0.5, 0.5);
    }

    protected void initialize() {
    	try{
	    	double top_left_y = SmartDashboard.getNumber("vision_target_p1_y");
	    	double top_right_x = SmartDashboard.getNumber("vision_target_p2_x");
	    	double height = SmartDashboard.getNumber("vision_target_height");
	    	
	    	boolean top_left = SmartDashboard.getBoolean("vision_top_left");
	    	boolean bottom_right = SmartDashboard.getBoolean("vision_bottom_right");
	    	
	    	double[] goalDistances = new double[] {0,0};
	    	
	    	VisionTools.goalDistances(top_left, bottom_right, top_left_y, height, goalDistances);
	    	double angleToGoal = VisionTools.getAngleToGoal(goalDistances[0], goalDistances[1], top_right_x, true);
	    	double setpoint = angleToGoal - VisionTools.angleToGoalSetpoint(goalDistances[0], goalDistances[1], true);
    	}catch(Exception oh_no){
    		System.out.println("Fatal Targeting Error: Dashboard data not found.");
    	}
    	
    	Robot.drivetrain.resetGyro();
        
    	//angle = setpoint;
    	
        angle = SmartDashboard.getNumber("ANGLE");
        
        setSetpoint(angle);
        
        if(Math.abs(angle) <= 5){
        	double p_override = 0.102;
        	double i_override = 0.0004;
        	double d_override = 0.072;
        	
        	if(Math.signum(angle) < 0){
        		// things are different if left :(
        		d_override = 0.062;
        	}
        	
        	getPIDController().setPID(p_override, i_override, d_override);
        }else if(Math.signum(angle) < 0){
        	// TODO: overrides to code
        }
    }

    protected void execute() {
		try{
			double p = SmartDashboard.getNumber("P");
			double i = SmartDashboard.getNumber("I");
			double d = SmartDashboard.getNumber("D");
			
			if(SmartDashboard.getBoolean("SET_PID")){
				getPIDController().setPID(p, i, d);
				System.out.println(p + ", " + i + ", " + d);
			}
		}catch(Exception e){}
    }

	protected double returnPIDInput() {
    	return Robot.drivetrain.getFullAngle();
	}

	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("pid-output", output);
		Robot.drivetrain.drive_tank_auto(output, -output);
	}

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.drivetrain.stop();
    }

    protected void interrupted() {
    	end();
    }
}
