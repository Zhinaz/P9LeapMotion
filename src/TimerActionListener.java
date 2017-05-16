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
	
	private final static String ATTENTIVE = "ATTENTIVE";
	private final static String INATTENTIVE = "INATTENTIVE";
	private final static String RIGHT = "RIGHT";
	private final static String LEFT = "LEFT";
	
	private int rightSecondaryCounter = 0;
	
	BluetoothClient mBluetoothClient;

	TimerActionListener(svm_model model, svm_model modelLeft, BluetoothClient bluetoothClient) {
		this.model = model;
		this.modelLeft = modelLeft;
		this.mBluetoothClient = bluetoothClient;
	}
	
	
	public void actionPerformed(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		SVMTrainer trainer = new SVMTrainer();

		double predictedRight = -1.0;
		double predictedLeft = -1.0;
		
		boolean rightSeen = false;
		boolean leftSeen = false;
		boolean noHandsOnWheel = false;
		
		if (frame.hands().count() >= 1) {
			// Add time stamp
			
			if (frame.hands().get(0).isRight()) {
				predictedRight = trainer.svmPredict(Main.getSample(0), model);
				rightSeen = true;
				final String output = "\tRight: " + doubleToAction(predictedRight);
				//System.out.println(output);
				if (frame.hands().count() > 1 && frame.hands().get(1).isLeft()) {
					predictedLeft = trainer.svmPredict(Main.getSample(1), modelLeft);
					leftSeen = true;
					//System.out.println("\tRight: " + doubleToAction(predictedLeft));
				}
			}
			if (frame.hands().get(0).isLeft()) {
				predictedLeft = trainer.svmPredict(Main.getSample(0), modelLeft);
				leftSeen = true;
				final String output = "\tLeft: " + doubleToAction(predictedLeft);
				//System.out.println(output);
				if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					predictedRight = trainer.svmPredict(Main.getSample(1), model);
					rightSeen = true;
					//System.out.println("\tRight: " + doubleToAction(predictedRight));
				}
			}
			
			String message = ATTENTIVE;
			
			if (rightSeen || leftSeen) {//predictedLeft != -1.0 || predictedRight != -1.0) {
				
				// Check right hand
				if (predictedRight == 3.0) {
					rightSecondaryCounter++;
				} else if (predictedRight == 1.0 || predictedRight == 2.0 || predictedRight == 4.0){
					rightSecondaryCounter = 0;
				}
				
				// Both hands resting / other tasks
				if (predictedLeft == 2.0 && (predictedRight == 2.0 || predictedRight == 3.0)) {
					message = INATTENTIVE;
					noHandsOnWheel = true;
				}
				// Right hand inattentive for too long
				else if (rightSecondaryCounter >= 6) {
					message = INATTENTIVE;
				}
				// Right hand secondary tasks
				else if (predictedRight == 3.0) {
					message = INATTENTIVE;
				}
				
				System.out.println("Trying to send message: " + message + " " + predictedRight + " " + predictedLeft);
				mBluetoothClient.sendMessage(message + " " + predictedRight + " " + predictedLeft);
			}
			
			
			
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    }
			});
		}
	}
	
	public String doubleToAction(double d) {
		String s = "None";
		
		if (d == 1.0)
			s = "Steering wheel";
		else if (d == 2.0)
			s = "Resting";
		else if (d == 3.0)
			s = "Secondary";
		else if (d == 4.0)
			s = "Gear stick";
		else if (d == -1.0)
			s = "None";
		
		return s;
	}
}
