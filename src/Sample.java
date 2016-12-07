import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Finger.Type;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

class SampleListener extends Listener {
	Boolean played = false;

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();

		for (Hand hand : frame.hands()) {
		
		}

		if (!frame.hands().isEmpty()) {
			System.out.println();
		}
	}
}


class CSVReader {
    ArrayList<double[]> data = new ArrayList<double[]>();

    public CSVReader(String filename) { 
        File file = new File(filename);
        Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Read the int on the next line to allocate arrays
        String numLine = scan.nextLine();
        int numberOfFeatures = numLine.split(",").length;
        
        // First line
        String[] line = numLine.split(",");
        double[] temp = new double[numberOfFeatures];
        for (int i = 0; i < numberOfFeatures; i++) {
        	double val = Double.parseDouble(line[i]);
        	temp[i] = val;
        }
        data.add(temp);
        
        //Now read until we run out of lines
        while(scan.hasNext()){
            line = scan.nextLine().split(",");
            double[] temp2 = new double[numberOfFeatures];
            for(int i = 0; i < numberOfFeatures; i++){
            	double val = Double.parseDouble(line[i]);
            	temp2[i] = val;
            }
            data.add(temp2);
        }
        scan.close();
    }

    public ArrayList<double[]> getParsedData(){
        return data;
    }
}


class Sample {
	// classification classes 
	private static int numberOfClasses = 2;
	
	/**
	 * The prediction algorithm
	 * @param testset The verification data, used to test the model
	 * @param model Classifier model
	 * @return 
	 */
	public static double svmPredict(double[] testset, svm_model model) {
		svm_node[] nodes = new svm_node[testset.length - 1];

		for (int i = 1; i < testset.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = testset[i];

			nodes[i - 1] = node;
		}

		int[] labels = new int[numberOfClasses];
		svm.svm_get_labels(model, labels);

		double[] prob_estimates = new double[numberOfClasses];
		double v = svm.svm_predict_probability(model, nodes, prob_estimates);

		for (int i = 0; i < numberOfClasses; i++) {
			System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
		}
		System.out.println("\t(Actual:" + testset[0] + " Prediction:" + v + ")");

		return v;
	}

	/**
	 * train the model, based on a training set
	 * 
	 * @param trainingSet the training set inputed as a double[][]
	 * @return a model used for predictions
	 */
	private static svm_model svmTrain(double[][] trainingSet) {
		svm_problem prob = new svm_problem();
		int dataCount = trainingSet.length;
		int featureCount = trainingSet[0].length;
		prob.y = new double[dataCount];
		prob.l = dataCount;
		prob.x = new svm_node[dataCount][featureCount];

		for (int i = 0; i < dataCount; i++) {
			double[] features = trainingSet[i];
			prob.x[i] = new svm_node[features.length - 1];
			for (int j = 1; j < features.length; j++) {
				svm_node node = new svm_node();
				node.index = j;
				node.value = features[j];
				prob.x[i][j - 1] = node;
			}
			prob.y[i] = features[0];
		}

		svm_parameter param = new svm_parameter();
		param.probability = 1;
		param.gamma = 0.5;
		param.nu = 0.5;
		param.C = 1;
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR;
		param.cache_size = 20000;
		param.eps = 0.001;

		svm_model model = svm.svm_train(prob, param);

		return model;
	}

	public static void main(String[] args) {
		
		CSVReader reader = new CSVReader("src/testdata.csv");
		ArrayList<double[]> data = reader.getParsedData();
		
		double[][] train = new double[100][];
		double[][] test2 = new double[20][];

		double[][] leaptrain = new double[20][];
		double[] vals = { 1.0,0.199,-0.48,0.853,0.202,0,-0.4,-0.008,-0.916 };
		leaptrain[0] = vals;
		double[] vals2 = { 1.0,0.199,-0.48,0.853,0.202,0,-0.4,-0.008,-0.916 };
		leaptrain[1] = vals2;
		double[] vals3 = { 1.0,0.177,-0.479,0.853,0.208,0,-0.401,-0.002,-0.916 };
		leaptrain[2] = vals3;
		double[] vals4 = { 1.0,0.224,-0.477,0.86,0.183,0,-0.403,-0.03,-0.915 };
		leaptrain[3] = vals4;
		double[] vals5 = { 1.0,0.128,-0.457,0.861,0.225,0,-0.423,0.012,-0.906 };
		leaptrain[4] = vals5;
		double[] vals6 = { 1.0,0.081,-0.46,0.855,0.239,0,-0.416,0.03,-0.909 };
		leaptrain[5] = vals6;
		double[] vals7 = { 1.0,0.015,-0.421,0.869,0.26,0,-0.459,0.044,-0.888 };
		leaptrain[6] = vals7;
		double[] vals8 = { 1.0,0,-0.362,0.87,0.336,0,-0.502,0.121,-0.856 };
		leaptrain[7] = vals8;
		double[] vals9 = { 1.0,0.038,-0.375,0.875,0.307,0,-0.424,0.132,-0.896 };
		leaptrain[8] = vals9;
		double[] vals10 = { 1.0,0.053,-0.451,0.851,0.269,0,-0.41,0.07,-0.909 };
		leaptrain[9] = vals10;
		double[] vals11 = { 0.0,0,-0.182,-0.976,-0.121,0,-0.447,0.192,-0.874 };
		leaptrain[10] = vals11;
		double[] vals12 = { 0.0,0,-0.15,-0.988,-0.032,0,-0.457,0.098,-0.884 };
		leaptrain[11] = vals12;
		double[] vals13 = { 0.0,0,-0.13,-0.992,0.002,0,-0.479,0.061,-0.875 };
		leaptrain[12] = vals13;
		double[] vals14 = { 0.0,0,-0.195,-0.981,0.02,0,-0.392,0.059,-0.918 };
		leaptrain[13] = vals14;
		double[] vals15 = { 0.0,0,-0.134,-0.99,-0.054,0,-0.447,0.108,-0.888 };
		leaptrain[14] = vals15;
		double[] vals16 = { 0.0,0,-0.146,-0.988,-0.043,0,-0.502,0.111,-0.857 };
		leaptrain[15] = vals16;
		double[] vals17 = { 0.0,0,-0.091,-0.994,0.065,0,-0.476,-0.014,-0.879 };
		leaptrain[16] = vals17;
		double[] vals18 = { 0.0,0,-0.136,-0.991,0.014,0,-0.47,0.052,-0.881 };
		leaptrain[17] = vals18;
		double[] vals19 = { 0.0,0,-0.19,-0.979,-0.071,0,-0.516,0.161,-0.841 };
		leaptrain[18] = vals19;
		double[] vals20 = { 0.0,0,-0.151,-0.989,0.005,0,-0.516,0.074,-0.853 };
		leaptrain[19] = vals20;
		
		double[][] leaptest = new double[4][];
		double[] valt = { 1.0,0,-0.481,0.799,0.362,0.844,-0.114,0.353,-0.929 };
		leaptest[0] = valt;
		double[] valt2 = { 1.0,0,-0.364,0.909,0.203,0.901,0.061,0.24,-0.969 };
		leaptest[1] = valt2;
		double[] valt3 = { 0.0,0,-0.071,-0.997,-0.019,0,-0.424,0.047,-0.904 };
		leaptest[2] = valt3;
		double[] valt4 = { 0.0,0,-0.114,-0.992,0.052,0,-0.403,-0.001,-0.915 };
		leaptest[3] = valt4;

		/*svm_model model = svmTrain(leaptrain);

		for (double[] d : leaptest) {
			svmPredict(d, model);
		}*/

	}
}
