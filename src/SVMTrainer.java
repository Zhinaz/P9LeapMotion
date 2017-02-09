import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

class SVMTrainer {
	// classification classes
	private static int numberOfClassesLeft = 2;
	private static int numberOfClasses = 4;
	private static double confidenceThreshold = 0.8;
	// Model
	//public static svm_model model;

	static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
	static DecimalFormat df = new DecimalFormat("0.00000", otherSymbols);
	
	/**
	 * The prediction algorithm
	 * 
	 * @param testset
	 *            The verification data, used to test the model
	 * @param model
	 *            Classifier model
	 * @return
	 */
	public double svmPredict(double[] testset, svm_model model, int numberOfClas) {
		svm_node[] nodes = new svm_node[testset.length - 1];

		for (int i = 1; i < testset.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = testset[i];

			nodes[i - 1] = node;
		}

		int[] labels;
		if (numberOfClas == 2) {
			labels = new int[numberOfClassesLeft];
		} else {
			labels = new int[numberOfClasses];
		}
		svm.svm_get_labels(model, labels);
		
		double[] prob_estimates;
		if (numberOfClas == 2) {
			prob_estimates = new double[numberOfClassesLeft];
		} else {
			prob_estimates = new double[numberOfClasses];
		}
		
		double v = svm.svm_predict_probability(model, nodes, prob_estimates);
		double highest_prob = 0.0;
		
		
		// Debug purposes
		for (int i = 0; i < numberOfClas; i++) {
			//System.out.print("(" + labels[i] + ":" + df.format(prob_estimates[i]) +") ");
			if (prob_estimates[i] > highest_prob)
				highest_prob = prob_estimates[i];
		}
		//System.out.println("\t(Actual:" + testset[0] + " Prediction:" + v + ")");

		if (highest_prob >= confidenceThreshold)
			return v;
		else 
			return 0.0;
	}

	/**
	 * train the model, based on a training set
	 * 
	 * @param trainingSet
	 *            the training set inputed as a double[][]
	 * @return a model used for predictions
	 */
	public svm_model svmTrain(ArrayList<double[]> trainingSet) {
		svm_problem prob = new svm_problem();
		int dataCount = trainingSet.size();
		int featureCount = trainingSet.get(0).length;
		prob.y = new double[dataCount];
		prob.l = dataCount;
		prob.x = new svm_node[dataCount][featureCount];

		for (int i = 0; i < dataCount; i++) {
			double[] features = trainingSet.get(i);
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
		param.gamma = 1;
		param.nu = 0.5;
		param.C = 1;
		param.svm_type = svm_parameter.POLY;
		param.kernel_type = svm_parameter.LINEAR;
		param.cache_size = 20000;
		param.eps = 0.001;

		svm_model model = svm.svm_train(prob, param);

		return model;
	}
}