import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

class LMCListener extends Listener {
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());

            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                }
            }
        }

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
    }
}

public class DemoWindow {

	public Shell shell;
	public static Text textTimeStamp;
	public static Text textFrame;
	public static Text textHands;
	public static Text textFingers;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a sample controller
		Controller controller = new Controller();
        Frame frame = controller.frame();
        
		try {
			DemoWindow window = new DemoWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
     
		while(frame.id() < 2000){
			textFrame.setText(String.valueOf(frame.id()));
	        textTimeStamp.setText(String.valueOf(frame.timestamp()));
	        textHands.setText(String.valueOf(frame.hands().count()));
	        textFingers.setText(String.valueOf(frame.fingers().count()));
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
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Hand Gesture Demonstration");
		
		textTimeStamp = new Text(shell, SWT.BORDER);
		textTimeStamp.setBounds(10, 25, 76, 21);
		
		Label lblTimestamp = new Label(shell, SWT.NONE);
		lblTimestamp.setBounds(10, 10, 76, 15);
		lblTimestamp.setText("TimeStamp");
		
		textFrame = new Text(shell, SWT.BORDER);
		textFrame.setBounds(92, 25, 76, 21);
		
		Label lblFrame = new Label(shell, SWT.NONE);
		lblFrame.setBounds(92, 10, 55, 15);
		lblFrame.setText("Frame");
		
		textHands = new Text(shell, SWT.BORDER);
		textHands.setBounds(174, 25, 76, 21);
		
		Label lblHands = new Label(shell, SWT.NONE);
		lblHands.setBounds(174, 10, 55, 15);
		lblHands.setText("# hands");
		
		textFingers = new Text(shell, SWT.BORDER);
		textFingers.setBounds(256, 25, 76, 21);
		
		Label lblFingers = new Label(shell, SWT.NONE);
		lblFingers.setBounds(256, 10, 55, 15);
		lblFingers.setText("# fingers");

	}
}
