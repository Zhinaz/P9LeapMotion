import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;

import libsvm.svm_model;
import org.eclipse.wb.swt.SWTResourceManager;

public class Main {

	private final static String ATTENTIVE = "ATTENTIVE";
	private final static String INATTENTIVE = "INATTENTIVE";

	// UI components
	protected static Shell shell;
	private static Button btnStart;
	private static Button btnStop;
	private static Label lblstate;
	private static Label state;
	
	public static Boolean collectingBool = false;
	public static Boolean testBool = false;

	static Timer timer;
	static BluetoothClient bluetoothClient;
	static JFileChooser fc;
	
	// Training sets
	static String rightTrainingSet = "src/data/Final/passatR.csv";
	static String leftTrainingSet = "src/data/Final/passatL.csv";

	// Model trainer
	static SVMTrainer trainer = new SVMTrainer();
	
	// right and left model creation
	static DataReader reader = new DataReader(rightTrainingSet);
	static ArrayList<double[]> buildData = reader.getParsedData();
	static svm_model model = trainer.svmTrain(buildData);
	static DataReader reader2 = new DataReader(leftTrainingSet);
	static ArrayList<double[]> buildDataLeft = reader2.getParsedData();
	static svm_model modelLeft = trainer.svmTrain(buildDataLeft);

	
	public static void main(String[] args) {
		System.out.println("\n\n\n\n");
		
		open();

		//testSampleSet();
		//testSampleSetLeft();
	}

	public static void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// display.sleep();
			}
		}
	}

	protected static void createContents() {
		shell = new Shell();
		shell.setSize(400, 314);
		shell.setText("Data collection");

		Button btnTest = new Button(shell, SWT.BUTTON1);
		btnTest.setText("Start test");
		btnTest.setBounds(274, 3, 100, 34);
		btnTest.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!testBool) {
					System.out.println("Test collecting");
					state.setText("Test collecting");
					testBool = true;
					
					TimerActionTest timerAction = new TimerActionTest(model, modelLeft);
					timer = new Timer(250, timerAction);
					timer.start();
				}
			}
		});

		Button btnStopTest = new Button(shell, SWT.BUTTON1);
		btnStopTest.setText("Stop test");
		btnStopTest.setBounds(274, 41, 100, 34);
		btnStopTest.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				System.out.println("Test stopped");
				state.setText("Test stopped");
				testBool = false;
				timer.stop();
			}
		});
		
		btnStart = new Button(shell, SWT.BUTTON1);
		btnStart.setText("Start collecting");
		btnStart.setBounds(274, 79, 100, 34);
		btnStart.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!collectingBool) {
					System.out.println("Start collecting");
					state.setText("Start collecting");
					collectingBool = true;
					initiateTimer();
				}
			}
		});

		btnStop = new Button(shell, SWT.BUTTON1);
		btnStop.setText("Stop collecting");
		btnStop.setBounds(274, 117, 100, 34);
		btnStop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				System.out.println("Collection stopped");
				state.setText("Collection stopped");
				collectingBool = false;
				timer.stop();
			}
		});

		Button btnCalibrate = new Button(shell, SWT.BUTTON1);
		btnCalibrate.setText("Calibrate");
		btnCalibrate.setBounds(274, 155, 100, 34);
		btnCalibrate.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				DataCollection dataCollection = new DataCollection();
				dataCollection.open();
				state.setText("Calibration program opened");
			}
		});

		Button btnLoadRight = new Button(shell, SWT.BUTTON1);
		btnLoadRight.setText("Right dataset");
		btnLoadRight.setBounds(274, 193, 100, 34);
		btnLoadRight.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				// Load data from a file, and use it for the right model
				FileChooser fc = new FileChooser();
				File file = fc.openFile();

				if (file != null) {
					System.out.println(file.getPath());

					DataReader readertest = new DataReader(file.getPath());
					ArrayList<double[]> buildDatatest = readertest.getParsedData();

					/*
					 * for (double[] d : buildDatatest) { for (double db : d) {
					 * System.out.print(db + ","); } System.out.println(); }
					 */

					model = trainer.svmTrain(buildDatatest);
					state.setText("Right dataset changed and trained");
				} else {
					state.setText("file is null");
				}
			}
		});

		Button btnLoadLeft = new Button(shell, SWT.BUTTON1);
		btnLoadLeft.setText("Left dataset");
		btnLoadLeft.setBounds(274, 231, 100, 34);
		btnLoadLeft.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				// Load data from a file, and use it for the right model
				FileChooser fc = new FileChooser();
				File file = fc.openFile();

				if (file != null) {
					System.out.println(file.getPath());

					DataReader readertest = new DataReader(file.getPath());
					ArrayList<double[]> buildDatatest = readertest.getParsedData();

					/*
					 * for (double[] d : buildDatatest) { for (double db : d) {
					 * System.out.print(db + ","); } System.out.println(); }
					 */

					modelLeft = trainer.svmTrain(buildDatatest);
					state.setText("Left dataset changed and trained");
				} else {
					state.setText("file is null");
				}
			}
		});

		lblstate = new Label(shell, SWT.NONE);
		lblstate.setBounds(10, 101, 100, 21);
		lblstate.setText("State");

		state = new Label(shell, SWT.NONE);
		state.setAlignment(SWT.CENTER);
		state.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		state.setBounds(10, 121, 121, 21);

	}

	// Start the bluetooth action
	public static void initiateTimer() {
		
		bluetoothClient = new BluetoothClient(); 
		try {
			bluetoothClient.initialise(); 
			TimerActionListener timerAction = new TimerActionListener(model, modelLeft, bluetoothClient);
			timer = new Timer(250, timerAction);
			timer.start(); 
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
	}

	// Testing the right hand model, the test set should be gathered along with the training data set, to ensure similar conditions (placement, car, etc.)
	public static void testSampleSet() {
		SVMTrainer trainer = new SVMTrainer();
		String rightTestSet = "src/data/Final/testdata.csv";
		DataReader reader2 = new DataReader(rightTestSet);
		ArrayList<double[]> testData = reader2.getParsedData();

		int notConfidentEnough = 0;
		int numberOfResting = 0;
		int numberOfSteering = 0;
		int numberOfSecondary = 0;
		int numberOfGearstick = 0;
		int numberOfRestingCorrect = 0;
		int numberOfSteeringCorrect = 0;
		int numberOfSecondaryCorrect = 0;
		int numberOfGearstickCorrect = 0;
		int numberOfFalse = 0;

		for (double[] d : testData) {
			double predicted = trainer.svmPredict(d, model);

			if (d[0] == 1.0) {
				numberOfSteering++;
				if (predicted == 1.0)
					numberOfSteeringCorrect++;
				else if (predicted == 0.0)
					notConfidentEnough++;
				else
					numberOfFalse++;
			} else if (d[0] == 2.0) {
				numberOfResting++;
				if (predicted == 2.0)
					numberOfRestingCorrect++;
				else if (predicted == 0.0)
					notConfidentEnough++;
				else
					numberOfFalse++;
			} else if (d[0] == 3.0) {
				numberOfSecondary++;
				if (predicted == 3.0)
					numberOfSecondaryCorrect++;
				else if (predicted == 0.0)
					notConfidentEnough++;
				else
					numberOfFalse++;
			} else if (d[0] == 4.0) {
				numberOfGearstick++;
				if (predicted == 4.0)
					numberOfGearstickCorrect++;
				else if (predicted == 0.0)
					notConfidentEnough++;
				else
					numberOfFalse++;
			}
		}

		System.out.println("Final results");
		System.out.println("Number of not confident enough: " + notConfidentEnough);
		System.out.println("Number of steering: " + numberOfSteeringCorrect + "/" + numberOfSteering);
		System.out.println("Number of resting: " + numberOfRestingCorrect + "/" + numberOfResting);
		System.out.println("Number of secondary: " + numberOfSecondaryCorrect + "/" + numberOfSecondary);
		System.out.println("Number of gearstick: " + numberOfGearstickCorrect + "/" + numberOfGearstick);
		System.out.println("Number of false predictions: " + numberOfFalse + "/"
				+ (numberOfResting + numberOfSteering + numberOfSecondary + numberOfGearstick));
		System.out.println();
	}

	// Test the left model, the samples should be gathered in the same car/setting, such that the leap motion has the correct position. 
	public static void testSampleSetLeft() {
		SVMTrainer trainer = new SVMTrainer();
		String leftTestSet = "src/data/Final/testdataLeft.csv";
		DataReader reader2 = new DataReader(leftTestSet);
		ArrayList<double[]> testData = reader2.getParsedData();

		int numberOfNone = 0;
		int numberOfResting = 0;
		int numberOfSteering = 0;
		int numberOfRestingCorrect = 0;
		int numberOfSteeringCorrect = 0;
		int numberOfFalse = 0;

		for (double[] d : testData) {
			double predicted = trainer.svmPredictLeft(d, modelLeft);

			if (d[0] == 1.0) {
				numberOfSteering++;
				if (predicted == 1.0)
					numberOfSteeringCorrect++;
				else if (predicted == 0.0)
					numberOfNone++;
				else
					numberOfFalse++;
			} else if (d[0] == 2.0) {
				numberOfResting++;
				if (predicted == 2.0)
					numberOfRestingCorrect++;
				else if (predicted == 0.0)
					numberOfNone++;
				else
					numberOfFalse++;
			}
		}

		System.out.println("Final results left");
		System.out.println("number of not confident enough: " + numberOfNone);
		System.out.println("Number of steering correct: " + numberOfSteeringCorrect + "/" + numberOfSteering);
		System.out.println("Number of resting correct: " + numberOfRestingCorrect + "/" + numberOfResting);
		System.out
				.println("number of false predictions: " + numberOfFalse + "/" + (numberOfResting + numberOfSteering));
		System.out.println();
	}

	// Get a sample from the leap motion controller
	public static double[] getSample(int handNumber) {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };

		if (frame.hands().count() >= 1) {
			Hand hand = frame.hands().get(handNumber);

			// Important that the data matches the number and order, with the training data collection 
			// the first 0.0 is supposed to be the prediction, however, in this case we want to predict, so it is set at 0.0
			temp = new double[] { 0.0,
					// Hand
					hand.sphereCenter().normalized().getX(), 
					hand.sphereCenter().normalized().getY(),
					hand.sphereCenter().normalized().getZ(), 
					hand.grabStrength(), 
					hand.pinchStrength(),
					// Palm
					hand.palmNormal().getX(), 
					hand.palmNormal().getY(), 
					hand.palmNormal().getZ(),
					hand.stabilizedPalmPosition().normalized().getX(),
					hand.stabilizedPalmPosition().normalized().getY(),
					hand.stabilizedPalmPosition().normalized().getZ(),
					// Direction
					hand.direction().getX(), 
					hand.direction().getY(), 
					hand.direction().getZ(), 
					hand.direction().pitch(),
					hand.direction().roll(), 
					hand.direction().yaw(),
					// Fingers
					hand.fingers().get(0).direction().getX(), 
					hand.fingers().get(0).direction().getY(),
					hand.fingers().get(0).direction().getZ(),
					hand.fingers().get(0).stabilizedTipPosition().normalized().getX(),
					hand.fingers().get(0).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(0).stabilizedTipPosition().normalized().getZ(),
					hand.fingers().get(1).direction().getX(), 
					hand.fingers().get(1).direction().getY(),
					hand.fingers().get(1).direction().getZ(),
					hand.fingers().get(1).stabilizedTipPosition().normalized().getX(),
					hand.fingers().get(1).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(1).stabilizedTipPosition().normalized().getZ(),
					hand.fingers().get(2).direction().getX(), 
					hand.fingers().get(2).direction().getY(),
					hand.fingers().get(2).direction().getZ(),
					hand.fingers().get(2).stabilizedTipPosition().normalized().getX(),
					hand.fingers().get(2).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(2).stabilizedTipPosition().normalized().getZ(),
					hand.fingers().get(3).direction().getX(), 
					hand.fingers().get(3).direction().getY(),
					hand.fingers().get(3).direction().getZ(),
					hand.fingers().get(3).stabilizedTipPosition().normalized().getX(),
					hand.fingers().get(3).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(3).stabilizedTipPosition().normalized().getZ(),
					hand.fingers().get(4).direction().getX(), 
					hand.fingers().get(4).direction().getY(),
					hand.fingers().get(4).direction().getZ(),
					hand.fingers().get(4).stabilizedTipPosition().normalized().getX(),
					hand.fingers().get(4).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(4).stabilizedTipPosition().normalized().getZ(), };

		}
		return temp;
	}
}
