package org.usfirst.frc.team1124.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Delays execution. </br> 
 * Use this in a command group using addSequential() so the next command has to wait.
 */
public class CommandDelay extends Command {
	/**
	 * Delay the next sequential command in a command group.
	 * @param time The time to wait in seconds.
	 */
    public CommandDelay(double time) {
        setTimeout(time);
    }

    protected void initialize() {}

    protected void execute() {}

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {}

    protected void interrupted() {}
}
