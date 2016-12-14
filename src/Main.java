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

	public static double[] getSample(int handNumber) {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };

		if (frame.hands().count() >= 1) {
			Hand hand = frame.hands().get(handNumber);
			double isRightValue = 1.0;
			if (!hand.isRight())
				isRightValue = -1.0;

			temp = new double[] { 
					0.0, 
					isRightValue, 
					hand.sphereCenter().normalized().getX() * 8,
					hand.sphereCenter().normalized().getY() * 8,
					hand.sphereCenter().normalized().getZ() * 8,
					hand.grabStrength(), 
					hand.palmNormal().getX() * 8,
					hand.palmNormal().getY() * 8, 
					hand.palmNormal().getZ() * 8, 
					hand.pinchStrength(), 
					hand.direction().getX(),
					hand.direction().getY(), 
					hand.direction().getZ(), 
					hand.direction().pitch(), 
					hand.direction().roll(),
					hand.direction().yaw(), 
					hand.palmVelocity().getX(), 
					hand.palmVelocity().getY(),
					hand.palmVelocity().getZ(), 
					hand.fingers().get(0).direction().getX(),
					hand.fingers().get(0).direction().getY(), 
					hand.fingers().get(0).direction().getY(),
					//hand.fingers().get(0).stabilizedTipPosition().normalized().getX(),
					//hand.fingers().get(0).stabilizedTipPosition().normalized().getY(),
					//hand.fingers().get(0).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(1).direction().getX(), 
					hand.fingers().get(1).direction().getY(),
					hand.fingers().get(1).direction().getY(),
					//hand.fingers().get(1).stabilizedTipPosition().normalized().getX(),
					//hand.fingers().get(1).stabilizedTipPosition().normalized().getY(),
					//hand.fingers().get(1).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(2).direction().getX(), 
					hand.fingers().get(2).direction().getY(),
					hand.fingers().get(2).direction().getY(),
					//hand.fingers().get(2).stabilizedTipPosition().normalized().getX(),
					//hand.fingers().get(2).stabilizedTipPosition().normalized().getY(),
					//hand.fingers().get(2).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(3).direction().getX(), 
					hand.fingers().get(3).direction().getY(),
					hand.fingers().get(3).direction().getY(),
					//hand.fingers().get(3).stabilizedTipPosition().normalized().getX(),
					//hand.fingers().get(3).stabilizedTipPosition().normalized().getY(),
					//hand.fingers().get(3).stabilizedTipPosition().normalized().getY(),
					hand.fingers().get(4).direction().getX(), 
					hand.fingers().get(4).direction().getY(),
					hand.fingers().get(4).direction().getY(),
					//hand.fingers().get(4).stabilizedTipPosition().normalized().getX(),
					//hand.fingers().get(4).stabilizedTipPosition().normalized().getY(),
					//hand.fingers().get(4).stabilizedTipPosition().normalized().getY(), 
					};

		}
		return temp;
	}

	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		DataReader reader = new DataReader("src/testdata.csv");
		ArrayList<double[]> data = reader.getParsedData();
		SVMTrainer trainer = new SVMTrainer();
		svm_model model = trainer.svmTrain(data);

		TimerActionListener timerAction = new TimerActionListener(model, lblData);
		timer = new Timer(250, timerAction);
		timer.start();
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
