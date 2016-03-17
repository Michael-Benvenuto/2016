package org.usfirst.frc.team1124.robot.commands.drive.targeting;

import org.usfirst.frc.team1124.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Hold a drivetrain voltage
 */
public class HoldAtVoltage extends Command {
	
	private double voltage = 0;
	
	AimAtAnglePID cmd;
	
    public HoldAtVoltage(AimAtAnglePID cmd) {
        requires(Robot.drivetrain);
        
        this.cmd = cmd;
        
        setInterruptible(true);
    }

    protected void initialize() {
        voltage = cmd.voltage;
    }

    protected void execute() {
    	Robot.drivetrain.drive_tank_auto((-1) * voltage, voltage);
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
