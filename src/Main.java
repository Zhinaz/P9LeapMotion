import java.util.ArrayList;
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
	protected Shell shell;
	private Button btnStart;
	private Button btnStop;
	private Label lblsamplesCollected;

	public Boolean collectingBool = false;
	private Label lblData;
	private Label samplesCollected;

	Timer timer;

	static DataReader reader = new DataReader("src/data/builddata.csv");
	static ArrayList<double[]> buildData = reader.getParsedData();
	static SVMTrainer trainer = new SVMTrainer();
	static svm_model model = trainer.svmTrain(buildData);
	
	static DataReader reader2 = new DataReader("src/data/builddataLeft.csv");
	static ArrayList<double[]> buildDataLeft = reader2.getParsedData();
	static svm_model modelLeft = trainer.svmTrain(buildDataLeft);

	public static double[] getSample(int handNumber) {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };

		if (frame.hands().count() >= 1) {
			Hand hand = frame.hands().get(handNumber);

			temp = new double[] { 
					0.0, 
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
					hand.fingers().get(4).stabilizedTipPosition().normalized().getZ(), 
					};

		}
		return temp;
	}

	public static void main(String[] args) {
		
		
		System.out.println("\n\n\n\n");
		
		testSampleSet();
		testSampleSetLeft();
		
		/*
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	}

	public void open() {
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

	public void initiateTimer() {
		//DataReader reader = new DataReader("src/testdata.csv");
		//ArrayList<double[]> data = reader.getParsedData();
		//SVMTrainer trainer = new SVMTrainer();
		//svm_model model = trainer.svmTrain(data);
		
		//DataReader readerLeft = new DataReader("src/testdataLeft.csv");
		//ArrayList<double[]> dataLeft = readerLeft.getParsedData();
		//SVMTrainer trainerLeft = new SVMTrainer();
		//svm_model modelLeft = trainer.svmTrain(data);

		TimerActionListener timerAction = new TimerActionListener(model, modelLeft, lblData);
		timer = new Timer(250, timerAction);
		timer.start();
	}
	
	public static void testSampleSet() {
		SVMTrainer trainer = new SVMTrainer();
		DataReader reader2 = new DataReader("src/data/testdata.csv");
		ArrayList<double[]> testData = reader2.getParsedData();
		
		int numberOfNone = 0;
		int numberOfResting = 0;
		int numberOfSteering = 0;
		int numberOfNoneTotal = 0;
		int numberOfRestingCorrect = 0;
		int numberOfSteeringCorrect = 0;
		int numberOfNoneCorrect = 0;
		int numberOfFalse = 0;
		
		for (double[] d : testData) {
			double predicted = trainer.svmPredict(d, model);

			if (d[0] == 1.0) {
				numberOfSteering++;
				if (predicted == 1.0)
					numberOfSteeringCorrect++;
				else if (predicted == 0.0)
					numberOfNone++;
				else
					numberOfFalse++;
			}
			else if (d[0] == 2.0) {
				numberOfResting++;
				if (predicted == 2.0)
					numberOfRestingCorrect++;
				else if (predicted == 0.0)
					numberOfNone++;
				else
					numberOfFalse++;
			}
			else if (d[0] == 3.0) {
				numberOfNoneTotal++;
				if (predicted == 3.0)
					numberOfNoneCorrect++;
				else if (predicted == 0.0)
					numberOfNone++;
				else
					numberOfFalse++;
			}
		}
		
		System.out.println("Final results");
		System.out.println("number of not confident enough: " + numberOfNone);
		System.out.println("Number of steering correct: " + numberOfSteeringCorrect + "/" + numberOfSteering);
		System.out.println("Number of resting correct: " + numberOfRestingCorrect + "/" + numberOfResting);
		System.out.println("Number of none(gear, secondary, communication) correct: " + numberOfNoneCorrect + "/" + numberOfNoneTotal);
		System.out.println("number of false predictions: " + numberOfFalse + "/" + (numberOfResting + numberOfSteering + numberOfNoneTotal));
		System.out.println();
	}
	
	public static void testSampleSetLeft() {
		SVMTrainer trainer = new SVMTrainer();
		DataReader reader2 = new DataReader("src/data/testdataLeft.csv");
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
			}
			else if (d[0] == 2.0) {
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
		System.out.println("number of false predictions: " + numberOfFalse + "/" + (numberOfResting + numberOfSteering));
		System.out.println();
	}

	protected void createContents() {
		shell = new Shell();
		shell.setSize(400, 200);
		shell.setText("Data collection");

		btnStart = new Button(shell, SWT.BUTTON1);
		btnStart.setText("Start collecting");
		btnStart.setBounds(274, 79, 100, 34);
		btnStart.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!collectingBool) {
					System.out.println("Start collecting");
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
				collectingBool = false;
				timer.stop();
			}
		});

		lblsamplesCollected = new Label(shell, SWT.NONE);
		lblsamplesCollected.setBounds(10, 101, 100, 21);
		lblsamplesCollected.setText("Samples collected");

		Label lblNewData = new Label(shell, SWT.NONE);
		lblNewData.setText("Newest data");
		lblNewData.setBounds(10, 10, 86, 15);

		lblData = new Label(shell, SWT.CENTER);
		lblData.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblData.setBounds(10, 31, 364, 21);

		samplesCollected = new Label(shell, SWT.NONE);
		samplesCollected.setAlignment(SWT.CENTER);
		samplesCollected.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		samplesCollected.setBounds(10, 121, 121, 21);

	}
}
