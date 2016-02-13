package org.usfirst.frc.team1124.robot.subsystems;

import org.usfirst.frc.team1124.robot.Robot;
import org.usfirst.frc.team1124.robot.commands.camera.StreamShooterCamera;
import org.usfirst.frc.team1124.robot.enums.CameraSelect;
import org.usfirst.frc.team1124.robot.tools.VisionTools;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera.ExposureControl;
import edu.wpi.first.wpilibj.vision.AxisCamera.Resolution;
import edu.wpi.first.wpilibj.vision.AxisCamera.WhiteBalance;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * The subsystem that manages camera streams and data processing
 */
public class Camera extends Subsystem {
	private Image intake_frame;
	private Image shooter_frame;
	
	private CameraSelect current_camera;
	
	// if camera is being held by auto code
	private boolean camera_held = false;

	public Camera(){
		super("camera");
		
		initShooterCamera();
		initIntakeCamera();
	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new StreamShooterCamera());
	}
	
	private void initShooterCamera(){
		try{
	        shooter_frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	        Robot.shooter_camera = new AxisCamera("10.11.24.78");
	        
	        Robot.shooter_camera.writeColorLevel(50);
	        Robot.shooter_camera.writeBrightness(50);
	        
	        Robot.shooter_camera.writeCompression(30);
	        
	        Robot.shooter_camera.writeResolution(Resolution.k320x240);
	        Robot.shooter_camera.writeExposureControl(ExposureControl.kHold);
	        
	        Robot.shooter_camera.writeWhiteBalance(WhiteBalance.kFixedFluorescent1);
	        Robot.shooter_camera.writeMaxFPS(20);
		}catch(Exception e) {}
	}
	
	private void initIntakeCamera(){
		try{
	        intake_frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			
			Robot.intake_camera = new USBCamera("cam0");
			Robot.intake_camera.openCamera();
		}catch(Exception e) {}
	}
	
	/**
	 * Select the camera to stream
	 * @param selection the enum of the camera you want to stream
	 */
	public void selectCamera(CameraSelect selection){
		current_camera = selection;
	}
	
	public CameraSelect getActiveCamera(){
		return current_camera;
	}
	
	public void setHeld(){
		camera_held = true;
	}
	
	public boolean isHeld(){
		return camera_held;
	}
	/** 
	 * Only run this method while the robot is ENABLED! This method gets the image and sends it to the dashboard for display and processing. 
	 */
	public void getImage(){
        try{
        	switch(current_camera){
        		case Intake:
                	Robot.intake_camera.getImage(intake_frame);
        			CameraServer.getInstance().setImage(intake_frame);
        		break;
        		case Shooter:
                	Robot.shooter_camera.getImage(shooter_frame);
                    CameraServer.getInstance().setImage(shooter_frame);
        		break;
        	}
        }catch(Exception e){}
	}
	
	/** 
	 * Polls the target dimensions from the dashboard.
	 * If they are not present, it returns {-1, -1}.
	 * 
	 * @return An array of { width, height }
	 * */
	public double[] getTargetDimensions(){
		double width = -1;
		double height = -1;
		
		try{
			width = SmartDashboard.getNumber("vision_target_width");
			height = SmartDashboard.getNumber("vision_target_height");
		}catch(Exception e){}
		
		double data[] = {width, height};
		
		return data;
	}
	
	/** 
	 * Polls the target dimensions from the dashboard.
	 * If they are not present, it returns {-1, -1}.
	 * 
	 * @return an array of the {x center of mass, y center of mass}
	 * */
	public double[] getTargetCenterOfMass(){
		double x = -1;
		double y = -1;
		
		try{
			x = SmartDashboard.getNumber("vision_target_x_cm");
			y = SmartDashboard.getNumber("vision_target_y_cm");
		}catch(Exception e){}
		
		double data[] = {x, y};
		
		return data;
	}
	
	/**
	 * Get the average distance to the target.
	 * @return The average distance (left edge distance and right edge distance averaged)
	 */
	public double getTargetAvgDistance(){
		double result = -1.0;
		
		/*
		 * What each point means on the image:
		 * 
		 *   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		 *   X point_1           point_2 X
		 *   X                           X
		 *   X                           X
		 *   X                           X
		 *   X                           X
		 *   X                           X
		 *   X                           X
		 *   X                           X
		 *   X point_4           point_3 X
		 *   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		 */
		
		try{
			double[] point_1 = {
					SmartDashboard.getNumber("vision_target_p1_x"),
					SmartDashboard.getNumber("vision_target_p1_y")
				};
			double[] point_2 = {
					SmartDashboard.getNumber("vision_target_p2_x"),
					SmartDashboard.getNumber("vision_target_p2_y")
				};
			double[] point_3 = {
					SmartDashboard.getNumber("vision_target_p3_x"),
					SmartDashboard.getNumber("vision_target_p3_y")
				};
			double[] point_4 = {
					SmartDashboard.getNumber("vision_target_p4_x"),
					SmartDashboard.getNumber("vision_target_p4_y")
				};
			
			double[] distances = VisionTools.goalDistances(point_1[0], point_1[1], point_2[0], point_2[1], point_4[0], point_4[1], point_3[0], point_4[1]);
			
			result = (distances[0] + distances[1] ) / 2;
		}catch(Exception e){}
		
		return result;
	}
}