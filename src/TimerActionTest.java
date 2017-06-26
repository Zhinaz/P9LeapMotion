import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.swt.widgets.Display;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

import libsvm.svm_model;

class TimerActionTest implements ActionListener {

	private final svm_model model;
	private final svm_model modelLeft;

	File outputFile = null;

	TimerActionTest(svm_model model, svm_model modelLeft) {
		this.model = model;
		this.modelLeft = modelLeft;
	}

	public void actionPerformed(ActionEvent arg0) {
		// Append all data for each set
		Controller controller = new Controller();
		Frame frame = controller.frame();
		SVMTrainer trainer = new SVMTrainer();

		final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss.SSS");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

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
		}

		PrintWriter pw = null;

		try {
 			if (outputFile == null) {
				String filename = sdf.format(timestamp);
				outputFile = new File("files/" + filename + ".csv");
			}

			FileWriter fw = new FileWriter(outputFile, true);
			BufferedWriter bw = new BufferedWriter(fw);

			pw = new PrintWriter(bw);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		sb.append(sdf.format(timestamp) + "\t Left: " + predictedLeft + " Right: " + predictedRight);
		sb.append('\n');

		pw.write(sb.toString());
		pw.close();

		System.out.println(sdf.format(timestamp) + "\t Left: " + predictedLeft + " Right: " + predictedRight);

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
			}
		});
	}
}
