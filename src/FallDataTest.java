import java.io.File;
import java.util.Scanner;


public class FallDataTest {

	/**
	 * @param args
	 */
	public static double[] FT = new double[]{0,3.1,0.05,0.59,0,0,0};
	public static String fileString = "";
	public static Scanner inputScanner = new Scanner(System.in);
	public static int se=0,spec=0;
	public static void main(String[] args) {
		AutoProcess();
		System.out.println(se+"\t"+spec);
	}
	
	public static void AutoProcess() {
		for (int i = 1; i <=16; i++) {
			fileString = "/Users/Leon/Desktop/FallData/fallingDown_0";
			if (i<10) {
				fileString+="0";
			}
			fileString+=i;
			for (int j = 1; j <=3; j++) {
				if (i==10 || i==11 && j==3) {
					continue;
				}
				for (int j2 = 1; j2 <=3; j2++) {
					System.out.print(i+""+j+"1"+j2);
					if (process(fileString+j+"1"+j2+".xml")) {
						System.out.println("\tYes");
						if (i<=7) {
							se+=1;
						}
					}
					else {
						System.out.println("\tNo");
						if (i>7) {
							spec+=1;
						}
					}
				}
			}
		}
	}
	
	public static void SingleProcessByInputFilename(){
		while (inputScanner.hasNext()) {
			fileString = "/Users/Leon/Desktop/FallData/fallingData_"+inputScanner.next()+".xml";
			return;
		}
	}
	
	public static Boolean process(String filename){
		FallDataProcessor fallDataProcessor = new FallDataProcessor(filename,FT,5,0,5,0,5);
//		System.out.println("Angular Acceleration peak Value:\t"+fallDataProcessor.GetAccelerationPeakResValue());
//		System.out.println("Resultant Angular Velocity peak Value:\t"+fallDataProcessor.GetAngularVelocityeakResValue());
//		System.out.println("Resultant Angular Acceleration peak Value:\t"+fallDataProcessor.GetAngularAccelerationPeakResValue());
//		System.out.println("Total Angular Change peak Value:\t"+fallDataProcessor.GetAngelChangeTotalValue()*180/Math.PI);
//		System.out.println("Angle Change of Roll:\t"+fallDataProcessor.GetAngleChangeTotalRollValue()*180/Math.PI);
//		System.out.println("Angle Change of Pitch:\t"+fallDataProcessor.GetAngleChangeTotalPitchValue()*180/Math.PI);
//		System.out.println("Angle Change of Yaw:\t"+fallDataProcessor.GetAngleChangeTotalYawValue()*180/Math.PI);
		return fallDataProcessor.isFall();
	}
}
