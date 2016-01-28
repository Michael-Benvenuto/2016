package org.usfirst.frc.team1124.robot;

// commands
import org.usfirst.frc.team1124.robot.commands.Autonomous;
import org.usfirst.frc.team1124.robot.commands.UpdatePositionTracking;
import org.usfirst.frc.team1124.robot.commands.drive.ArcadeDriveJoystick;
// subsystems
import org.usfirst.frc.team1124.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1124.robot.subsystems.IntakeBelts;
import org.usfirst.frc.team1124.robot.subsystems.ShooterPID;
import org.usfirst.frc.team1124.robot.subsystems.ArmActuatorPID;
import org.usfirst.frc.team1124.robot.subsystems.ArmPistons;
import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.wpilibj.vision.USBCamera;

// tools
import org.usfirst.frc.team1124.robot.tools.ConfigIO;

// dashboard
import org.usfirst.frc.team1124.robot.dashboard.DashboardConnection;

// wpilib components
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	// operator interface
	public static OI oi;
	
	// configuration interface
	public static ConfigIO configIO;
	
	// subsystems
	public static DriveTrain drivetrain;
	public static ArmPistons arm_pistons;
	public static ArmActuatorPID arm_actuator_pid;
	public static IntakeBelts intake_belts;
	public static ShooterPID shooter_pid;
	public static USBCamera camera;
	public static PositionTracker positionTracker;
	
	// components
	public static Compressor compressor;
	public static PowerDistributionPanel pdp;
	
	// camera
	public DashboardConnection db_connection = new DashboardConnection();
	
	// autonomous
    Command autonomousCommand;
    
    // this revision of code (displayed on dashboard)
    public static String codeRevision = "[v1.0.6]:build-season";

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	// instantiate configuration interface
    	configIO = new ConfigIO();
    	
    	// instantiate subsystems
		drivetrain = new DriveTrain();
		pdp = new PowerDistributionPanel();
		
		// instantiate operator interface
		oi = new OI();

		// start camera stream to driver station
		//camera = db_connection.initCamera();
		db_connection.initCamera();

        // instantiate the command used for the autonomous period
        autonomousCommand = new Autonomous();

		// Set up position tracking
		positionTracker = new PositionTracker();
        Scheduler.getInstance().add(new UpdatePositionTracking());
    }
	
	public void disabledPeriodic() {
    	db_connection.updateDashboard();
    	
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command
    	drivetrain.setBrake();
    	
        if(autonomousCommand != null){
        	autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	db_connection.updateDashboard();
    	
    	db_connection.getImage();
    	
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
    	drivetrain.setCoast();
    	
        if(autonomousCommand != null){
        	autonomousCommand.cancel();
        }
        
        Scheduler.getInstance().add(new ArcadeDriveJoystick());
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	db_connection.updateDashboard();
    	
    	db_connection.getImage();
    	
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
