import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import libsvm.svm_model;

class TimerActionListener implements ActionListener {
		
	private final svm_model model;
	private final svm_model modelLeft;
	private Label lblData;
	
	private final static String ATTENTIVE = "ATTENTIVE";
	private final static String INATTENTIVE = "INATTENTIVE";
	
	private int rightSecondaryCounter = 0;
	
	BluetoothClient mBluetoothClient;

	TimerActionListener(svm_model model, svm_model modelLeft, Label lblData, BluetoothClient bluetoothClient) {
		this.model = model;
		this.modelLeft = modelLeft;
		this.lblData = lblData;
		this.mBluetoothClient = bluetoothClient;
	}
	
	
	public void actionPerformed(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		SVMTrainer trainer = new SVMTrainer();

		double predictedRight = -1.0;
		double predictedLeft = -1.0;
		
		if (frame.hands().count() >= 1) {
			// Add time stamp
			
			if (frame.hands().get(0).isRight()) {
				predictedRight = trainer.svmPredict(Main.getSample(0), model);
				final String output = "\tRight: " + doubleToAction(predictedRight);
				System.out.println(output);
				String additionalOutput = "";
				if (frame.hands().count() > 1 && frame.hands().get(1).isLeft()) {
					predictedLeft = trainer.svmPredict(Main.getSample(1), modelLeft);
					additionalOutput = "\tLeft: " + doubleToAction(predictedLeft);
					System.out.println(additionalOutput);
				}
			}
			if (frame.hands().get(0).isLeft()) {
				predictedLeft = trainer.svmPredict(Main.getSample(0), modelLeft);
				final String output = "\tLeft: " + doubleToAction(predictedLeft);
				System.out.println(output);
				String additionalOutput = "";
				if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					predictedRight = trainer.svmPredict(Main.getSample(1), model);
					additionalOutput = "\tRight: " + doubleToAction(predictedRight);
					System.out.println(additionalOutput);
				}
			}
			
			
			if (predictedLeft != -1.0 || predictedRight != -1.0) {
				
				// Check right hand
				if (predictedRight == 3.0) {
					rightSecondaryCounter++;
				} else if (predictedRight == 1.0 || predictedRight == 2.0){
					rightSecondaryCounter = 0;
				}
				
				// Both hands resting / other tasks
				if (predictedLeft == 2.0 && (predictedRight == 2.0 || predictedRight == 3.0)) {
					mBluetoothClient.sendMessage(INATTENTIVE);
					mBluetoothClient.sendMessage(INATTENTIVE);
				}
				// Right hand inattentive for too long
				else if (rightSecondaryCounter >= 6) {
					mBluetoothClient.sendMessage(INATTENTIVE);
				}
				// Right hand secondary tasks
				else if (predictedRight == 3.0) {
					mBluetoothClient.sendMessage(INATTENTIVE);
				}
				// Left hand on wheel + Right hand back onto wheel or resting
				else if (predictedLeft == 1.0 && (predictedRight == 2.0 || predictedRight == -1.0)) {
					mBluetoothClient.sendMessage(ATTENTIVE);
				}
				// Right on wheel
				else if (predictedRight == 1.0) {
					mBluetoothClient.sendMessage(ATTENTIVE);
				}
				
			}
			
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	//lblData.setText(output);
			    }
			});
		}
	}
	
	public String doubleToAction(double d) {
		String s = "None";
		
		if (d == 1.0)
			s = "Holding the steering wheel";
		else if (d == 2.0)
			s = "Resting";
		else if (d == 3.0)
			s = "Gear, secondary, communication";
		
		return s;
	}
	
	
	/*public void actionPerformed2(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		SVMTrainer trainer = new SVMTrainer();
		
		final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String lineTime = formatForLines.format(timestamp);
		
		
		if (frame.hands().count() >= 1) {
			// Add time stamp
			
			if (frame.hands().get(0).isRight()) {
				final String output = lineTime + "\tRight: " + doubleToAction(trainer.svmPredict(Main.getSample(0), model));
				System.out.println(output);
				final String additionalOutput;
				if (frame.hands().count() > 1 && frame.hands().get(1).isLeft()) {
					additionalOutput = lineTime + "\tLeft: " + doubleToAction(trainer.svmPredict(Main.getSample(1), modelLeft));
					System.out.println(additionalOutput);
				}
			}
			if (frame.hands().get(0).isLeft()) {
				final String output = lineTime + "\tLeft: " + doubleToAction(trainer.svmPredict(Main.getSample(0), modelLeft));
				System.out.println(output);
				final String additionalOutput;
				if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					additionalOutput = lineTime + "\tRight: " + doubleToAction(trainer.svmPredict(Main.getSample(1), model));
					System.out.println(additionalOutput);
				}
			}
				
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	//lblData.setText(output);
			    }
			});
		}
	}*/
}
