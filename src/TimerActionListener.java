import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.eclipse.swt.widgets.Display;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import libsvm.svm_model;

class TimerActionListener implements ActionListener {

	private final svm_model model;
	private final svm_model modelLeft;

	private final static String ATTENTIVE = "ATTENTIVE";
	private final static String INATTENTIVE = "INATTENTIVE";

	BluetoothClient mBluetoothClient;

	File outputFile = null;

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

		if (frame.hands().count() >= 1) {
			// Add time stamp
			
			
			if (frame.hands().get(0).isRight()) {
				predictedRight = trainer.svmPredict(Main.getSample(0), model);
				if (frame.hands().count() > 1 && frame.hands().get(1).isLeft()) {
					predictedLeft = trainer.svmPredict(Main.getSample(1), modelLeft);
				}
				// Extra check, such that the right hand detected is the right most (sometimesleft turns into right)
				else if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					
					if (frame.hands().get(0).sphereCenter().normalized().getX() > 
						frame.hands().get(1).sphereCenter().normalized().getX() && 
						frame.hands().get(0).palmNormal().getX() > 
						frame.hands().get(1).palmNormal().getX()) {
						predictedRight = trainer.svmPredict(Main.getSample(1), model);
					}
					
				}
			}
			
			if (frame.hands().get(0).isLeft()) {
				predictedLeft = trainer.svmPredict(Main.getSample(0), modelLeft);
				if (frame.hands().count() > 1 && frame.hands().get(1).isRight()) {
					predictedRight = trainer.svmPredict(Main.getSample(1), model);
				}
			}

			String message = ATTENTIVE;

			// Check right hand
			if (predictedRight == 3.0) {
				message = INATTENTIVE;
			}

			// Both hands resting / other tasks
			else if ((predictedLeft == -1.0 || predictedLeft == 2.0) && (predictedRight == -1.0 || predictedRight == 2.0 || predictedRight == 3.0 || predictedRight == 4.0)) {
				message = INATTENTIVE;
			}

			System.out.println("Trying to send message: " + message + " " + predictedLeft + " " + predictedRight);
			mBluetoothClient.sendMessage(message + " " + predictedRight + " " + predictedLeft);
			

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
