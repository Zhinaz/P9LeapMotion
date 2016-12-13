import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import libsvm.svm_model;
import org.eclipse.swt.widgets.Label;

public class CollectionTimer extends TimerTask {

	private final svm_model model;
	private Label lblData;
	private double getSample[];
	private volatile String output;
	
	CollectionTimer (svm_model model, Label lblData, double getSample[]) {
		this.model = model;
		this.lblData = lblData;
		this.getSample = getSample;
	}
	
	@Override
	public void run() {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		
		if (frame.hands().count() == 1) {
			// Add time stamp
			final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String lineTime = formatForLines.format(timestamp);
			
			output = lineTime + "\t" + SVMTrainer.svmPredict(getSample, model);
			System.out.println(output);
			
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	lblData.setText(output);
	            }
	          });
			
			//updateGUI(output);
		}
	}
	
	public void updateGUI(final String data) {
		//SwingUtilities.invokeLater(this);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	labelUpdate(data);
            }
          });
	}
	
	private synchronized void labelUpdate(String data) {
	   lblData.setText(data);
	}
}