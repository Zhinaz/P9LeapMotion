import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.leapmotion.leap.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class DataCollection {
	public Shell shell;
	private Label label;
	private Text labelText;
	private Button collect;
	private Label samplesCollected;
	
	private int samples = 10; 
	private Boolean collectingBool = false;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		        
		try {
			DataCollection window = new DataCollection();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		
		// Must be running, else windows fucks
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				Controller controller = new Controller();
		        Frame frame = controller.frame();
			}
		}
	}
	
	public void createContents() {
		shell = new Shell();
		shell.setSize(400, 400);
		shell.setText("Data collection");
		
		label = new Label(shell, SWT.NONE);
		label.setBounds(10, 25, 76, 21);
		label.setText("Set data label");
		
		labelText = new Text(shell, SWT.BORDER);
		labelText.setBounds(10, 50, 76, 21);
		
		collect = new Button(shell, SWT.BUTTON1);
		collect.setText("Start collecting");
		collect.setBounds(10, 75, 200, 50);
		collect.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				System.out.println("Start collecting");
				if (!collectingBool) {
					collectingBool = true;
					startCollecting();
				}
			}
		});
		
		samplesCollected = new Label(shell, SWT.NONE);
		samplesCollected.setBounds(10, 150, 100, 21);
		samplesCollected.setText("0 / " + samples);
	}
	
	public void startCollecting() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("#.###", otherSymbols);
		
		
		PrintWriter pw = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String filename = sdf.format(timestamp);
			File testfile = new File("files/test" + filename + ".csv");
			pw = new PrintWriter(testfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		while (collectingBool) {
			// Append al leap motion data vi skal bruge for hvert data sæt
			Controller controller = new Controller();
			Frame frame = controller.frame();
			
			if (frame.hands().count() == 1) {
				
				
				// label
				sb.append(labelText.getText() + ",");
				// grab strength
				sb.append(df.format(frame.hands().get(0).grabStrength()) + ",");
				// Palm normal x
				sb.append(df.format(frame.hands().get(0).palmNormal().getX()) + ",");
				// Palm normal y
				sb.append(df.format(frame.hands().get(0).palmNormal().getY()) + ",");
				// Palm normal z
				sb.append(df.format(frame.hands().get(0).palmNormal().getZ()) + ",");
				// pinch strength
				sb.append(df.format(frame.hands().get(0).pinchStrength()) + ",");
				// Palm direction x
				sb.append(df.format(frame.hands().get(0).direction().getX()) + ",");
				// Palm direction y
				sb.append(df.format(frame.hands().get(0).direction().getY()) + ",");
				// Palm direction z
				sb.append(df.format(frame.hands().get(0).direction().getZ()) + ",");
				
				// MERE DATA PLZ
			} 
			
			// Busy wait imellem data captures, for at undgå helt ens værdier
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
			sb.append('\n');
			i++;
			
			samplesCollected.setText(i + " / " + samples);
			if(i >= samples) 
				collectingBool = false;
		}
		
		pw.write(sb.toString());
		samplesCollected.setText(i + " / " + samples + " - Done!!");
		pw.close();
	}
}
