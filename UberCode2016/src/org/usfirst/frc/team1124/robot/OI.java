package org.usfirst.frc.team1124.robot;

import org.usfirst.frc.team1124.robot.commands.arm.MoveArm;
import org.usfirst.frc.team1124.robot.enums.ArmState;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
	private Joystick js1 = new Joystick(0);
	private Joystick js2 = new Joystick(1);
	private Joystick js3 = new Joystick(2);
	
	private Button button_1 = new JoystickButton(js1, 1);
	private Button button_10 = new JoystickButton(js1, 10);
	private Button button_11 = new JoystickButton(js1, 11);
	
	public int id = 1;
	
	public OI(){
		// setup drive mode control (bind buttons)

		//button_10.whenReleased(new MoveArm(ArmState.Up));
		//button_11.whenReleased(new MoveArm(ArmState.Down));
	}
	
	// joysticks/controllers
	public Joystick getController(){
		return js1;
	}
	
	public Joystick getJS1(){
		return js2;
	}
	
	public Joystick getJS2(){
		return js3;
	}
}

