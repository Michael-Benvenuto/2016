package org.usfirst.frc.team1124.robot.commands.shooter;

import org.usfirst.frc.team1124.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Shoots a ball at a specific rate (using PID)
 */
public class BringShooterToSpeed extends Command {
	
    public BringShooterToSpeed(double setpoint){
    	requires(Robot.shooter_pid);
    	
    	// TODO set this!
    	//setpoint = Robot.camera.getRateForShooterToScore();
    	
    	Robot.shooter_pid.setSetpoint(setpoint);
    	
    	setInterruptible(true);
    }

    protected void initialize(){
    	Robot.shooter_pid.enable();
    }

    protected void execute() {}

    protected boolean isFinished(){
    	return false;//Math.abs(Robot.shooter_pid.getRate() - Robot.shooter_pid.getSetpoint()) >= Robot.shooter_pid.SETPOINT_TOLERANCE;
    }

    protected void end() {}

    protected void interrupted(){
    	end();
    }
}
