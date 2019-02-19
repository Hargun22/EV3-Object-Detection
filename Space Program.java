

import java.util.ArrayList;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Driver {
	
	/**EV3ColorSensor color = new EV3ColorSensor(SensorPort.S3);
	EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S1);
	EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S2);
	EV3UltrasonicSensor ultra = new EV3UltrasonicSensor(SensorPort.S4);

	EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.D);
	EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.A);
	EV3MediumRegulatedMotor lift = new EV3MediumRegulatedMotor(MotorPort.B);*/

	
	EV3ColorSensor color;
	EV3TouchSensor touch;
	EV3GyroSensor gyro;
	EV3UltrasonicSensor ultra;

	EV3LargeRegulatedMotor left;
	EV3LargeRegulatedMotor right;
	EV3MediumRegulatedMotor lift;
	SampleProvider us;
	SampleProvider cs ;
	SampleProvider gs;
	SampleProvider ts;
	
	float[] ultraSample;
	float[] touchSample;
	float[] colorSample;
	float[] gyroSample;

	public Driver() {
		color = new EV3ColorSensor(SensorPort.S3);
		touch = new EV3TouchSensor(SensorPort.S1);
		gyro = new EV3GyroSensor(SensorPort.S2);
		ultra = new EV3UltrasonicSensor(SensorPort.S4);

		left = new EV3LargeRegulatedMotor(MotorPort.D);
		right = new EV3LargeRegulatedMotor(MotorPort.A);
		lift = new EV3MediumRegulatedMotor(MotorPort.B);
		us = ultra.getDistanceMode();
		cs = color.getColorIDMode();
		gs = gyro.getAngleMode();
		ts = touch.getTouchMode();
		
		ultraSample = new float[us.sampleSize()];
		touchSample = new float[touch.sampleSize()];
		colorSample = new float[color.sampleSize()];
		gyroSample = new float[gs.sampleSize()];

	}
	
	public Driver(Port newLeft, Port newRight, Port newArm, Port newColour, Port newTouch, Port newUltra, Port newGyro) {
		color = new EV3ColorSensor(newColour);
		touch = new EV3TouchSensor(newTouch);
		gyro = new EV3GyroSensor(newGyro);
		ultra = new EV3UltrasonicSensor(newUltra);

		left = new EV3LargeRegulatedMotor(newLeft);
		right = new EV3LargeRegulatedMotor(newRight);
		lift = new EV3MediumRegulatedMotor(newArm);
		
		us = ultra.getDistanceMode();
		cs = color.getColorIDMode();
		gs = gyro.getAngleMode();
		ts = touch.getTouchMode();
		
		ultraSample = new float[us.sampleSize()];
		touchSample = new float[touch.sampleSize()];
		colorSample = new float[color.sampleSize()];
		gyroSample = new float[gs.sampleSize()];
	}
	
	public Driver(String newLeft, String newRight,String newArm, int newColour, int newTouch, int newUltra, int newGyro) {
		
		color = new EV3ColorSensor(sensorPort(newColour));
		touch = new EV3TouchSensor(sensorPort(newTouch));
		gyro = new EV3GyroSensor(sensorPort(newGyro));
		ultra = new EV3UltrasonicSensor(sensorPort(newUltra));

		left = new EV3LargeRegulatedMotor(motorPort(newLeft));
		right = new EV3LargeRegulatedMotor(motorPort(newRight));
		lift = new EV3MediumRegulatedMotor(motorPort(newArm));
		
		us = ultra.getDistanceMode();
		cs = color.getColorIDMode();
		gs = gyro.getAngleMode();
		ts = touch.getTouchMode();
		
		ultraSample = new float[us.sampleSize()];
		touchSample = new float[touch.sampleSize()];
		colorSample = new float[color.sampleSize()];
		gyroSample = new float[gs.sampleSize()];
	}
	
	public Port motorPort(String letter) {
		Port p = MotorPort.A;
		
		switch(letter.charAt(0)) {
			
			case 'A':
				p = MotorPort.A;
				break;
		
			case 'B':
				p = MotorPort.B;
				break;
				
			case 'C':
				p = MotorPort.C;
				break;
		
			case 'D':
				p = MotorPort.D;
				break;
		}
		return p;
	}
	
	public Port sensorPort(int number) {
		Port p = SensorPort.S1;
		
		switch(number) {
			
			case 1:
				p = SensorPort.S1;
				break;
		
			case 2:
				p = SensorPort.S2;
				break;
				
			case 3:
				p = SensorPort.S3;
				break;
		
			case 4:
				p = SensorPort.S4;
				break;
		}
		return p;
	}

	

	ArrayList<String> colours = new ArrayList<String>();

	final float axle = (float) 11.175;

	final float angleConversion = (float) (360f / (Math.PI * 5.5));
	final double conversion = ((Math.PI) / 180);
	
	 ArrayList<Float> ultraD = new ArrayList<Float>();
	 ArrayList<Float> gyroT = new ArrayList<Float>();
	 
	 Float[][] finalMeasurements;
	 
	public float getTouch() {
		ts.fetchSample(touchSample, 0);
		return touchSample[0];
	}

	public float getGyro() {
		gs.fetchSample(gyroSample, 0);
		return gyroSample[0];
	}

	public float getColor() {
		cs.fetchSample(colorSample, 0);
		return colorSample[0];
	}

	public float getUltra() {
		us.fetchSample(ultraSample, 0);
		return ultraSample[0];
	}

	public void touch() {
		// Driver robot = new Driver();

		while (true) {
			if (getTouch() == 1) {
				left.stop(true);
				right.stop();

				break;
			} else {
				left.forward();
				right.forward();

			}
		}
	}

	public void color() {

		while (getColor() == 6) {
			left.forward();
			right.forward();
		}
		{
			left.stop(true);
			right.stop();
		}
	}

	public void exit() {

		if (Button.ESCAPE.isDown() == true) {
			ultra.close();
			color.close();
			gyro.close();
			touch.close();
			System.exit(0);
		}
	}

	public void addColours(int code){
		String colour;
		
		
		
		switch(code){
		
		case 0:
			colour = "Red";
			break;
		case 3:
			colour = "Yellow";
			break;
		case 6:
			colour = "Blue";
			break;
		case 7:
			colour = "Black";
			break;
		default:
			colour = "White";
		}
		
		System.out.println(code + ":"+ colour);
		
		colours.add(colour);
		
		
	}
	
	public void ArrayCleaner(ArrayList<Float> arrDistance, ArrayList<Float> arrAngles){
		for (int i = 0; i < arrDistance.size()-1; i++){
			if (Math.abs(arrDistance.get(i) - arrDistance.get(i + 1)) <= 0.16
					
					&& Math.abs((arrAngles.get(i+1) - arrAngles.get(i))) <= 34
					|| (Float.isInfinite(arrDistance.get(i))
					|| arrAngles.get(i) == 0)){
				if (i < 1){
					
				arrDistance.remove(i);
				arrAngles.remove(i);
				} else {
					
					arrDistance.remove(i+1);
					arrAngles.remove(i+1);
					i--;
				}
			}
		}
		
		for (int i = 0; i < arrDistance.size()-1 ; i++){
			if  (Float.isInfinite(arrDistance.get(i)) || arrDistance.get(i)  == 0 || arrDistance.get(i) - arrDistance.get(i + 1)  == 0){
				
				arrDistance.remove(i);
				arrAngles.remove(i);
				i--;
			}
		}
			for (int i = 0; i < arrDistance.size()-1 ; i++){
				if  (Float.isInfinite(arrDistance.get(i))){
					
					arrDistance.remove(i);
					arrAngles.remove(i);
					i--;
				}
			}
	
	if (arrDistance.size() == 3) {
			arrDistance.remove(2);
			arrAngles.remove(2);
		}
		
		for (int i = 0; i < arrDistance.size(); i++){
			System.out.println(arrDistance.get(i));
		}
		
		for (int i = 0; i < arrAngles.size(); i++){
			System.out.println(arrAngles.get(i));
		}
		
		System.out.println(arrDistance.size() + "," + arrAngles.size());
		
	}
	
	public void twoDArray() {
		 finalMeasurements = new Float[ultraD.size()][gyroT.size()];
		
		for(int i = 0; i <ultraD.size(); i ++) {
			finalMeasurements[0][i] = ultraD.get(i);
			finalMeasurements[1][i] = gyroT.get(i);
		}
		
	}
	
	
	public void run2(){
		sort();
		
		float drivex = 0f;
		float drivey = 0f;
		float drive = 0f;
		int angle = 0;
		for(int i = 0; i<finalMeasurements[0].length; i++){
			
			if(i == 0) {
				drive = (float) finalMeasurements[0][i];
				angle = finalMeasurements[1][i].intValue();
			} else {
				drivex = (float) (finalMeasurements[0][i]* Math.sin(Math.toRadians(finalMeasurements[1][i])) - finalMeasurements[0][i-1]* Math.sin(Math.toRadians(finalMeasurements[1][i-1])));
				drivey = (float) (finalMeasurements[0][i]* Math.cos(Math.toRadians(finalMeasurements[1][i])) - finalMeasurements[0][i-1]* Math.cos(Math.toRadians(finalMeasurements[1][i-1])));
				drive = (float) Math.sqrt(Math.pow(drivex,2)+ Math.pow(drivey,2));
				angle = (int) (Math.atan(Math.abs(drivex/drivey)) *180/Math.PI);
				
				if(finalMeasurements[0][i]* Math.sin(Math.toRadians(finalMeasurements[1][i])) < finalMeasurements[0][i-1]* Math.sin(Math.toRadians(finalMeasurements[1][i-1])) 
						&& finalMeasurements[0][i]* Math.cos(Math.toRadians(finalMeasurements[1][i])) > finalMeasurements[0][i-1]* Math.cos(Math.toRadians(finalMeasurements[1][i-1]))) {
					angle = 360 - angle;
			
				} else if(finalMeasurements[0][i]* Math.sin(Math.toRadians(finalMeasurements[1][i])) > finalMeasurements[0][i-1]* Math.sin(Math.toRadians(finalMeasurements[1][i-1])) 
						&& finalMeasurements[0][i]* Math.cos(Math.toRadians(finalMeasurements[1][i])) < finalMeasurements[0][i-1]* Math.cos(Math.toRadians(finalMeasurements[1][i-1]))) {
					angle = 180 - angle;
				
				} else if(finalMeasurements[0][i]* Math.sin(Math.toRadians(finalMeasurements[1][i])) < finalMeasurements[0][i-1]* Math.sin(Math.toRadians(finalMeasurements[1][i-1])) 
						&& finalMeasurements[0][i]* Math.cos(Math.toRadians(finalMeasurements[1][i])) < finalMeasurements[0][i-1]* Math.cos(Math.toRadians(finalMeasurements[1][i-1]))) {
					angle = 180 + angle;
				}
				
				
			}
		
			System.out.println(angle);
			System.out.println(drivex);
			System.out.println(drivey);
			System.out.println(drive);
			this.turn2('T', angle + 10, 'L', 0);
			this.drive('F', drive*100);
			addColours((int) getColor());
			this.turn('T', angle + 10, 'R', 0);
			
		}
	}
	
	public Float[][] sort(){
		
	Float[][] n = new Float[finalMeasurements.length][finalMeasurements[0].length];
	
		
		while(complete(finalMeasurements,n)){
			
			for(int k = 0; k < finalMeasurements.length; k++){
				for(int j = 0; j < finalMeasurements[0].length; j++){
					n[k][j] = finalMeasurements[k][j];
				}
			
			}

			for (int k = 0; k < finalMeasurements[0].length - 1; k++) {
				if (finalMeasurements[0][k] > finalMeasurements[0][k + 1]) {
					
					Float t = finalMeasurements[0][k];
					finalMeasurements[0][k] = finalMeasurements[0][k + 1];
					finalMeasurements[0][k + 1] = t;
					
					Float u = finalMeasurements[1][k];
					finalMeasurements[1][k] = finalMeasurements[1][k + 1];
					finalMeasurements[1][k + 1] = u;
					
				}
			}		
		}
				
		return finalMeasurements;
	}

	public boolean complete(Float[][] m, Float[][] n){
		
		for(int k = 0; k < m.length; k++){
			for(int j = 0; j < m[0].length; j++){
				
				if(n[k][j] != m[k][j]){
					return true;
					
				}
			}
		
		}
	
		return false;
	}
	
	
	public void setSpeed(float speed) {
		float speed1 = speed * angleConversion;
		left.setSpeed(speed1);
		right.setSpeed(speed1);
	}

	public void setAcceleration(float acceleration) {
		float acceleration1 = acceleration * angleConversion;
		left.setAcceleration((int) acceleration1);
		right.setAcceleration((int) acceleration1);
	}

	/*
	 * public void liftArm(int angle) { lift.rotate(angle); }
	 */

	public void drive(char type, float distance) {
		
		right.setSpeed(180);
		left.setSpeed(180);
		int angle = (int) (angleConversion * distance);
		this.setSpeed(6);
		if (type == 'F') {

			left.rotate(angle, true);
			right.rotate(angle);


		}

		if (type == 'B') {

			left.rotate(-angle, true);
			right.rotate(-angle);

		}
	}
	
	public void turn2(char type, int degrees, char direction, float radius) {
		float deg = (float) degrees;
		this.setSpeed(1);
		if (type == "T".charAt(0)) {
			if (direction == "L".charAt(0)) {
				this.setSpeed(3);
				gyro.reset();
				while (((getGyro() - deg + 8) != 0f)) {
					
					right.forward();
					left.backward();
				}  
				

				exit();
						
					
				} {

			right.stop(true);
			left.stop();
				}
		}
	}

	public void turn(char type, int degrees, char direction, float radius) {
		float deg = (float) degrees;
		this.setSpeed(6);
		if (type == "P".charAt(0)) {
			gyro.reset();
			if (direction == "L".charAt(0)) {

				while ((Math.ceil(getGyro() - degrees + 6) != 0f)) {
					right.forward();
					System.out.println(getGyro());

				}
				right.stop();
			}

			if (direction == "R".charAt(0)) {

				gyro.reset();
				while (((getGyro() + degrees - 6) != 0f)) {
					left.forward();
					System.out.println(getGyro());

				}
				left.stop();
			}
		}
		if (type == "T".charAt(0)) {
			if (direction == "L".charAt(0)) {
				this.setSpeed(1f);
				gyro.reset();
				while (((getGyro() - deg + 5) != 0f)) {
					
					right.forward();
					left.backward();
				
					if (getUltra() < 1.45 && getUltra() > 0) {
						
						ultraD.add(getUltra());	
						gyroT.add(getGyro());
			
				}  
				
						
			
				
					exit();
							
						
					} {

				
	
				right.stop(true);
				left.stop();
				ArrayCleaner(ultraD, gyroT);
	
					}
			}

			if (direction == "R".charAt(0)) {

				this.setSpeed(5);
				gyro.reset();
				while (((getGyro() + deg - 7) != 0f)) {
					left.forward();
					right.backward();
	

				}
				right.stop(true);
				left.stop();
			}
		}

		if (type == "C".charAt(0)) {
			this.setSpeed(4);
			if (direction == "L".charAt(0)) {
				float lengthC1 = (float) (degrees * conversion * (radius - ((axle) / 2)));
				float lengthC2 = (float) (degrees * conversion * (radius + ((axle) / 2)));

				float angle3 = (angleConversion * lengthC2);
				float angle = (angleConversion * lengthC1);

				float v1 = left.getSpeed();
				float v2 = (angle3 * v1) / angle;

				left.setSpeed(v1);
				right.setSpeed(v2);

				gyro.reset();

				while ((Math.floor(getGyro() - deg + 8) != 0f)) {
					right.forward();
					left.forward();
					System.out.println(getGyro());

				}
				right.stop(true);
				left.stop();
				this.setSpeed(6);

			}

			if (direction == "R".charAt(0)) {
				float lengthC1 = (float) (degrees * conversion * (radius + ((axle) / 2)));
				float lengthC2 = (float) (degrees * conversion * (radius - ((axle) / 2)));

				float angle3 = (angleConversion * lengthC1);
				float angle = (angleConversion * lengthC2);

				float v1 = left.getSpeed();
				float v2 = (angle * v1) / angle3;

				left.setSpeed(v1);
				right.setSpeed(v2);

				gyro.reset();

				while (((getGyro() + deg - 10) != 0f)) {
					right.forward();
					left.forward();
					System.out.println(getGyro());

				}
				right.stop(true);
				left.stop();
				this.setSpeed(6);
			}

		}
		

	}
	 public void run() {

		this.turn('T', 90, 'L', 0);
		this.turn('T', 90, 'R', 0);

		twoDArray();
		run2();
		
		this.turn2('T', finalMeasurements[1][finalMeasurements[1].length-1].intValue(), 'L', 0f);
		this.drive('F', -100 * finalMeasurements[0][finalMeasurements[0].length-1]);
		this.turn('T', finalMeasurements[1][finalMeasurements[1].length-1].intValue()+7, 'R', 0f);
		
	 }
	 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Driver robot = new Driver();
		 robot.run();
		 robot.exit();

	}

}