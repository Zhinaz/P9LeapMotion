import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

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
		
		// Verification set
		/*double[][] leaptest = new double[4][];
		double[] valt = { 1.0,0,-0.481,0.799,0.362,0.844,-0.114,0.353,-0.929 };
		leaptest[0] = valt;
		double[] valt2 = { 1.0,0,-0.364,0.909,0.203,0.901,0.061,0.24,-0.969 };
		leaptest[1] = valt2;
		double[] valt3 = { 0.0,0,-0.071,-0.997,-0.019,0,-0.424,0.047,-0.904 };
		leaptest[2] = valt3;
		double[] valt4 = { 0.0,0,-0.114,-0.992,0.052,0,-0.403,-0.001,-0.915 };
		leaptest[3] = valt4;
		
		for (double[] d : leaptest) {
			double p = SVMTrainer.svmPredict(d, model);
			System.out.println("(Actual:" + d[0] + " Prediction:" + p + ")");
		}
		*/
		/*
		int i = 0;
		while (i < 20) {
			double p = SVMTrainer.svmPredict(getSample(), model);
			System.out.println("Prediction: " + p + ")");
			i++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
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
			// Append al leap motion data vi skal bruge for hvert data s�t
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() == 1) {
				// Timestamp
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String lineTime = formatForLines.format(timestamp);
				sb.append(lineTime + "\t");
				// Prediction
				sb.append(SVMTrainer.svmPredict(getSample(), model));
				System.out.println(lineTime + "\t" + SVMTrainer.svmPredict(getSample(), model));
			} 
			
			// Busy wait imellem data captures, for at undg� helt ens v�rdier
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
