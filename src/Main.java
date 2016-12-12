import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

import libsvm.svm_model;
import org.eclipse.wb.swt.SWTResourceManager;

public class Main {
	protected Shell shell;
	private Button btnStart;
	private Label lblsamplesCollected;

	private int numberOfSamples = 4800; //4800 = 20minutes
	public Boolean collectingBool = false;
	private Label lblData;
	private Label samplesCollected;
	
	public static double[] getSample() {
		Controller controller = new Controller();
		Frame frame = controller.frame();
		double[] temp = { 1.0 };
		
		if (frame.hands().count() == 1) {
			temp = new double[]{ 0.0, frame.hands().get(0).grabStrength(), frame.hands().get(0).palmNormal().getX(), 
					frame.hands().get(0).palmNormal().getY(), frame.hands().get(0).palmNormal().getZ(), frame.hands().get(0).pinchStrength(),
					frame.hands().get(0).direction().getX(), frame.hands().get(0).direction().getY(), frame.hands().get(0).direction().getZ(),
					frame.hands().get(0).direction().pitch(), frame.hands().get(0).direction().roll(), frame.hands().get(0).direction().yaw() };
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
				//display.sleep();
			}
		}
	}
	
	/*private Thread thread = new Thread(new Runnable() {
		public void run() {
			DataReader reader = new DataReader("src/testdata.csv");
			ArrayList<double[]> data = reader.getParsedData();
			SVMTrainer trainer = new SVMTrainer();
			svm_model model = trainer.svmTrain(data);
			
			final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
			
			PrintWriter pw = null;
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm");
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String filename = sdf.format(timestamp);
				File testfile = new File("files/test" + filename + ".csv");
				pw = new PrintWriter(testfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			
			int j = 0;
			while (j < numberOfSamples) {
				// Append all data for each set
				Controller controller = new Controller();
				Frame frame = controller.frame();
				
				if (frame.hands().count() == 1) {
					// Add time stamp
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String lineTime = formatForLines.format(timestamp);
					sb.append(lineTime + "\t");
					// Prediction
					sb.append(SVMTrainer.svmPredict(getSample(), model));
					
					System.out.println(lineTime + "\t" + SVMTrainer.svmPredict(getSample(), model));
				} 
				
				// Busy wait between data - avoids similar data sets
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	j++;
				sb.append('\n');
			}
			
			pw.write(sb.toString());
			pw.close();
		}});*/
	
	public void dataCollection() {
		DataReader reader = new DataReader("src/testdata.csv");
		ArrayList<double[]> data = reader.getParsedData();
		SVMTrainer trainer = new SVMTrainer();
		svm_model model = trainer.svmTrain(data);
		
		final SimpleDateFormat formatForLines = new SimpleDateFormat("HH:mm ss.SSS");
		
		PrintWriter pw = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String filename = sdf.format(timestamp);
			File testfile = new File("files/test" + filename + ".csv");
			pw = new PrintWriter(testfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		
		int j = 0;
		while (j < numberOfSamples) {
			// Append all data for each set
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() == 1) {
				// Add time stamp
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String lineTime = formatForLines.format(timestamp);
				sb.append(lineTime + "\t");
				// Prediction
				sb.append(SVMTrainer.svmPredict(getSample(), model));
				
				System.out.println(lineTime + "\t" + SVMTrainer.svmPredict(getSample(), model));
				lblData.setText(lineTime + "\t" + SVMTrainer.svmPredict(getSample(), model));
			} 
			
			// Busy wait between data - avoids similar data sets
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	j++;
        	samplesCollected.setText(j + " / " + numberOfSamples);
			sb.append('\n');
		}
		
		pw.write(sb.toString());
		pw.close();
	}

	protected void createContents() {
		shell = new Shell();
		shell.setSize(400, 200);
		shell.setText("Data collection");
		
		btnStart = new Button(shell, SWT.BUTTON1);
		btnStart.setText("Start collecting");
		btnStart.setBounds(274, 117, 100, 34);
		btnStart.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!collectingBool) {
					System.out.println("Start collecting");
					collectingBool = true;
					dataCollection();
				}
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
