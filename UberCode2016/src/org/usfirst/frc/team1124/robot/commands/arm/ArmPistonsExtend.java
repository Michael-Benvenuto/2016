package org.usfirst.frc.team1124.robot.commands.arm;

import org.usfirst.frc.team1124.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Extend the arm pistons
 */
public class ArmPistonsExtend extends Command {

    public ArmPistonsExtend() {
    	requires(Robot.arm_pistons);
    	
    	setInterruptible(false);
    }

    protected void initialize() {}

    protected void execute() {
    	Robot.arm_pistons.extend();
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() {}
}
