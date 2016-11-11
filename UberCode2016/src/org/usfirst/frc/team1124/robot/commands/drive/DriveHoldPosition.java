package org.usfirst.frc.team1124.robot.commands.drive;

import org.usfirst.frc.team1124.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @deprecated
 * Starts a hold position loop that also incorporates feed-forward control from the driver
 */
public class DriveHoldPosition extends CommandGroup {
	private LeftDrivePID left_drive;
	private RightDrivePID right_drive;
	
	private double left_setpoint = 0;
	private double right_setpoint = 0;
	
	private boolean wasActive = false;
	private Timer timer;
	
	private double left_speed = 0;
	private double right_speed = 0;
	
	private double left_dist = 0;
	private double right_dist = 0;
	
	
	private boolean arcade = true;
	private double threshold = 0.2;
	
	/** New hold position command, arcade drive = true, threshold = 0.4*/
    public DriveHoldPosition() {
    	requires(Robot.drivetrain);
    	
    	setInterruptible(true);
    	
    	left_drive = new LeftDrivePID(left_setpoint);
    	right_drive = new RightDrivePID(right_setpoint);
    	
    	addParallel(left_drive);
    	addParallel(right_drive);
    }
	
    /** New hold position command, threshold = 0.4*/
    public DriveHoldPosition(boolean arcade) {
    	requires(Robot.drivetrain);
    	
    	setInterruptible(true);
    	
    	left_drive = new LeftDrivePID(left_setpoint);
    	right_drive = new RightDrivePID(right_setpoint);
    	
    	addParallel(left_drive);
    	addParallel(right_drive);
    	
    	this.arcade = arcade;
    	this.threshold = arcade ? 0.2 : 0.05;
    }
    
    /** New hold position command*/
    public DriveHoldPosition(boolean arcade, double threshold) {
    	requires(Robot.drivetrain);
    	
    	setInterruptible(true);
    	
    	left_drive = new LeftDrivePID(left_setpoint);
    	right_drive = new RightDrivePID(right_setpoint);
    	
    	addParallel(left_drive);
    	addParallel(right_drive);
    	
    	this.arcade = arcade;
    	this.threshold = threshold;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		timer = new Timer();
		
		left_drive.disableSafety();
		right_drive.disableSafety();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// we are not going to have control while in this mode, just hold still
    	double y = 0; //Robot.oi.getJS1().getY() * -1;
    	double x = 0; //Robot.oi.getJS1().getX() * -1;
    	
    	if(Math.abs(y) > threshold || Math.abs(x) > threshold){
    		if(!wasActive){
    			Robot.drivetrain.resetEncoders();
    			
    			timer.reset();
    			timer.start();
    			
    			left_dist = 0;
    			right_dist = 0;
        		
        		wasActive = true;
    		}
    		
    		if(this.arcade){
    			double[] vals = arcade(x, y);
    			
    			left_speed = vals[0];
    			right_speed = vals[1];
    		}else{
    			left_speed = y;
    			right_speed = x;
    		}
    		
    		left_dist += (left_speed / 2) * timer.get() * ((2.0/3.0) * 100);
    		right_dist += (right_speed / 2) * timer.get() * ((2.0/3.0) * 100);
    		
    		left_drive.updateSetpoint(left_dist);
    		right_drive.updateSetpoint(right_dist);
    		
    		Robot.drivetrain.drive_tank_auto(left_drive.getSpeed(), right_drive.getSpeed());
    	}else{
    		if(wasActive){
    			Robot.drivetrain.resetEncoders();
        		
        		left_drive.updateSetpoint(0);
        		right_drive.updateSetpoint(0);
    		}
    		
    		wasActive = false;
    		
    		Robot.drivetrain.drive_tank_auto(left_drive.getSpeed(), right_drive.getSpeed());
    	}
    }

    /** 
     * @param x = x axis
     * @param y = y axis
     * 
     * @return double array {left_speed, right_speed}*/
    private double[] arcade(double x, double y){
    	double leftMotorSpeed;
    	double rightMotorSpeed;
    	
	    double tx = (x * x);
	    double ty = (y * y);
	    
    	// arcade drive math
	    /*
		if(y >= 0.0){
			y = (y * y);
		}else{
			y = -(y * y);
		}
		
		if(x >= 0.0){
			x = (x * x);
		}else{
			x = -(x * x);
		}*/
	    
	    	//Definately not final, working on a revision.
	    	if(x < 0) tx = -tx;
	    	if(y < 0) ty = -ty;
	    
	    	leftMotorSpeed = Math.max(tx, ty);
	    	rightMotorSpeed = Math.max(tx, ty);
	    
	    	/*Rewriting
		if(y > 0){
			if(x > 0){
				leftMotorSpeed = ty - tx;
				rightMotorSpeed = Math.max(ty, tx);
			}else{
				leftMotorSpeed = Math.max(ty, tx);
				rightMotorSpeed = ty + tx;
			}
		}else{
			if(x > 0){
				leftMotorSpeed = -Math.max(ty, tx);
				rightMotorSpeed = ty + tx;
			}else{
				leftMotorSpeed = ty - tx;
				rightMotorSpeed = -Math.max(ty, tx);
			}
		}*/
		
		double[] speeds = {leftMotorSpeed, rightMotorSpeed};
		
		return speeds;
    }
    
    protected boolean isFinished() {
        return super.isFinished();
    }
    
    protected void end() {
    	Robot.drivetrain.stop();
    }

    protected void interrupted() {
    	end();
    }
}
