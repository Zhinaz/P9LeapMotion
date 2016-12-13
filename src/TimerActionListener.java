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
	private Label lblData;
	private double getSample[];

	TimerActionListener(svm_model model, Label lblData, double getSample[]) {
		this.model = model;
		this.lblData = lblData;
		this.getSample = getSample;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		
		if (frame.hands().count() == 1) {
			// Add time stamp
			final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String lineTime = formatForLines.format(timestamp);
			
			final String output = lineTime + "\t" + SVMTrainer.svmPredict(getSample, model);
			System.out.println(output);
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	lblData.setText(output);
			    }
			});
		}
	}
}