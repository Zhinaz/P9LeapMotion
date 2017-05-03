import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

import libsvm.svm_model;

class Test {
	public static double[] getSample() {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };
		
		if (frame.hands().count() == 1) {
			temp = new double[]{ 0.0, frame.hands().get(0).grabStrength(), frame.hands().get(0).palmNormal().getX(), 
					frame.hands().get(0).palmNormal().getY(), frame.hands().get(0).palmNormal().getZ(), frame.hands().get(0).pinchStrength(),
					frame.hands().get(0).direction().getX(), frame.hands().get(0).direction().getY(), frame.hands().get(0).direction().getZ(),
					frame.hands().get(0).direction().pitch(), frame.hands().get(0).direction().roll(), frame.hands().get(0).direction().yaw() };
			}
		return temp;
	}
	
	public static void main(String[] args) {
		DataReader reader = new DataReader("src/testdata.csv");
		ArrayList<double[]> data = reader.getParsedData();
		SVMTrainer trainer = new SVMTrainer();
		svm_model model = trainer.svmTrain(data);
		
		final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
		
		PrintWriter pw = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String filename = sdf.format(timestamp);
			File testfile = new File("files/test" + filename + ".csv");
			pw = new PrintWriter(testfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		int j = 0;
		
		while (j < 20) {
			// Append all data for each set
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() == 1) {
				// Timestamp
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String lineTime = formatForLines.format(timestamp);
				sb.append(lineTime + "\t");
				// Prediction
				sb.append(trainer.svmPredict(getSample(), model));
				
				System.out.println(lineTime + "\t" + trainer.svmPredict(getSample(), model));
			} 
			
			// Busy wait between data - avoids similar data sets
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
			sb.append('\n');
			j++;
		}
		
		pw.write(sb.toString());
		pw.close();
	}
}
/*
public static void testOClockSet() {
	SVMTrainer trainer = new SVMTrainer();
	DataReader reader2 = new DataReader("src/data/rightTrain.csv");
	ArrayList<double[]> testData = reader2.getParsedData();
	
	int numberOfNone = 0;
	int numberOfTwo = 0;
	int numberOfOne = 0;
	int numberOfThree = 0;
	int numberOfTwoCorrect = 0;
	int numberOfOneCorrect = 0;
	int numberOfThreeCorrect = 0;
	int numberOfFalse = 0;
	int numberOfFour = 0;
	int numberOfFourCorrect = 0;
	int numberOfTwelve = 0;
	int numberOfTwelveCorrect = 0;
	
	for (double[] d : testData) {
		double predicted = trainer.svmPredict(d, modelOClock);

		if (d[0] == 1.0) {
			numberOfOne++;
			if (predicted == 1.0)
				numberOfOneCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 2.0) {
			numberOfTwo++;
			if (predicted == 2.0)
				numberOfTwoCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 3.0) {
			numberOfThree++;
			if (predicted == 3.0)
				numberOfThreeCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 4.0) {
			numberOfFour++;
			if (predicted == 4.0)
				numberOfFourCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 12.0) {
			numberOfTwelve++;
			if (predicted == 12.0)
				numberOfTwelveCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
	}
	
	System.out.println("Final results");
	System.out.println("number of not confident enough: " + numberOfNone);
	System.out.println("Number of 1: " + numberOfOneCorrect + "/" + numberOfOne);
	System.out.println("Number of 2: " + numberOfTwoCorrect + "/" + numberOfTwo);
	System.out.println("Number of 3: " + numberOfThreeCorrect + "/" + numberOfThree);
	System.out.println("Number of 4: " + numberOfFourCorrect + "/" + numberOfFour);
	System.out.println("Number of 12: " + numberOfTwelveCorrect + "/" + numberOfTwelve);
	System.out.println("number of false predictions: " + numberOfFalse + "/" + (numberOfTwo + numberOfOne + numberOfThree + numberOfFour + numberOfTwelve));
	System.out.println();
}

public static void testOClockSetLeft() {
	SVMTrainer trainer = new SVMTrainer();
	DataReader reader2 = new DataReader("src/data/leftTrain.csv");
	ArrayList<double[]> testData = reader2.getParsedData();
	
	int numberOfNone = 0;
	int numberOfTwo = 0;
	int numberOfOne = 0;
	int numberOfThree = 0;
	int numberOfTwoCorrect = 0;
	int numberOfOneCorrect = 0;
	int numberOfThreeCorrect = 0;
	int numberOfFalse = 0;
	int numberOfFour = 0;
	int numberOfFourCorrect = 0;
	int numberOfTwelve = 0;
	int numberOfTwelveCorrect = 0;
	
	for (double[] d : testData) {
		double predicted = trainer.svmPredict(d, modelLeftOClock);

		if (d[0] == 8.0) {
			numberOfOne++;
			if (predicted == 8.0)
				numberOfOneCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 9.0) {
			numberOfTwo++;
			if (predicted == 9.0)
				numberOfTwoCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 10.0) {
			numberOfThree++;
			if (predicted == 10.0)
				numberOfThreeCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 11.0) {
			numberOfFour++;
			if (predicted == 11.0)
				numberOfFourCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
		else if (d[0] == 12.0) {
			numberOfTwelve++;
			if (predicted == 12.0)
				numberOfTwelveCorrect++;
			else if (predicted == 0.0)
				numberOfNone++;
			else
				numberOfFalse++;
		}
	}
	
	System.out.println("Final results");
	System.out.println("number of not confident enough: " + numberOfNone);
	System.out.println("Number of 8: " + numberOfOneCorrect + "/" + numberOfOne);
	System.out.println("Number of 9: " + numberOfTwoCorrect + "/" + numberOfTwo);
	System.out.println("Number of 10: " + numberOfThreeCorrect + "/" + numberOfThree);
	System.out.println("Number of 11: " + numberOfFourCorrect + "/" + numberOfFour);
	System.out.println("Number of 12: " + numberOfTwelveCorrect + "/" + numberOfTwelve);
	System.out.println("number of false predictions: " + numberOfFalse + "/" + (numberOfTwo + numberOfOne + numberOfThree + numberOfFour + numberOfTwelve));
	System.out.println();
}
*/

