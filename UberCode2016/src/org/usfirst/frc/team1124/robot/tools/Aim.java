package org.usfirst.frc.team1124.robot.tools;

public class Aim {
	private double[] outputs = new double[10];
	
	private int tick = 0;
	private int tick_time = 5; // run 10 times in a second
	
	private double stop_voltage = 0.14;
	
	public static void main(String args[]){
		System.out.println("Calculating speed for 15 pixels away...");
		
		Aim aim = new Aim(15);
		
		for(int i = 0; i < 50; i++){
			System.out.println(aim.getOutput());
		}
	}
	
	/**
	 * Aim at a target, provided pixel error
	 * @param pixel_pos The center of mass of the target.
	 */
	public Aim(double pixel_pos){
		// negative is to the left, positive is to the right
		double pos = -1 * (pixel_pos - 160);
		
		for(int i = 0; i < 10; i++){
			outputs[i] = calculate(pos, i);
		}
	}
	
	public double getOutput(){
		double speed = outputs[Math.floorDiv(tick, tick_time)];
		tick++;
		
		return speed;
	}
	
	public double calculate(double x, int tick){
		double speed = 0;
		double sign = 0;
		
		sign = Math.signum(x);
		x = Math.abs(x);
		
		if(tick == 0){
			if(x > 80){
				speed = 0.8 * sign;
			}else if(x > 10){
				speed = 0.00000202852 * Math.pow((sign * x), 3) + -0.000314 * Math.pow((sign * x), 2) + 0.0197 * (sign * x) + 0.195;
			}else if(x > 2){
				speed = -0.0007867 * Math.pow((sign * x), 4) + 0.020648 * Math.pow((sign * x), 3) + -0.1848 * Math.pow((sign * x), 2) + 0.6149 * (sign * x) + -0.05; 
			}else{
				speed = stop_voltage;
			}
		}else if(tick <= 4){
			if(x > 80){
				speed = 0.8 * sign;
			}else if(x > 10){
				speed = 0.00000202852 * Math.pow((sign * x), 3) + -0.000314 * Math.pow((sign * x), 2) + 0.0197 * (sign * x) + 0.195;
			}else if(x > 2){
				speed = -0.0007867 * Math.pow((sign * x), 4) + 0.020648 * Math.pow((sign * x), 3) + -0.1848 * Math.pow((sign * x), 2) + 0.6149 * (sign * x) + -0.05;
			}else{
				speed = stop_voltage;
			}
			
			if(x > 2){
				speed -= sign * (0.4);
			}
		}else if(tick <= 9){
			if(x > 80){
				speed = 0.8 * sign;
			}else if(x > 10){
				speed = 0.00000202852 * Math.pow((sign * x), 3) + -0.000314 * Math.pow((sign * x), 2) + 0.0197 * (sign * x) + 0.195;
			}else if(x > 2){
				speed = -0.0007867 * Math.pow((sign * x), 4) + 0.020648 * Math.pow((sign * x), 3) + -0.1848 * Math.pow((sign * x), 2) + 0.6149 * (sign * x) + -0.05;
			}else{
				speed = stop_voltage;
			}
			
			if(x > 2){
				speed -= sign * (0.4);
				speed /= -0.1 * (tick - 5);
			}
		}
		
		return speed;
	}
}
