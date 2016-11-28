import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Widget;
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class DemoWindow {

	public Shell shell;
	public static Text textTimeStamp;
	public static Text textFrame;
	public static Text textHands;
	public static Text textFingers;
	private Label lblHandOne;
	private Label lblHandTwo;
	private Label lblHandType;
	private Text textLeftHandtype;
	private Text textRightHandtype;
	private Label lblHandType_1;
	private Text textLeftPalm;
	private Label lblPalmPosition;
	private Text textLeftHandID;
	private Text textRightHandID;
	private Text textLeftVector;
	private Text textLeftDirection;
	private Text textLeftPitch;
	private Text textLeftRoll;
	private Text textLeftYaw;
	private Text textLeftElbow;
	private Text textLeftArm;
	private Text textLeftWrist;
	private Text textLeftFinger0Type;
	private Text textLeftFinger1Type;
	private Text textLeftFinger2Type;
	private Text textLeftFinger3Type;
	private Text textLeftFinger4Type;
	private Text textLeftFinger0ID;
	private Text textLeftFinger1ID;
	private Text textLeftFinger2ID;
	private Text textLeftFinger3ID;
	private Text textLeftFinger4ID;
	private Label lblType;
	private Label lblId;
	private Text textLeftFinger0Length;
	private Text textLeftFinger1Length;
	private Text textLeftFinger2Length;
	private Text textLeftFinger3Length;
	private Text textLeftFinger4Length;
	private Label lblLength;
	private Text textLeftFinger0Extended;
	private Text textLeftFinger1Extended;
	private Text textLeftFinger2Extended;
	private Text textLeftFinger3Extended;
	private Text textLeftFinger4Extended;
	private Label lblWidth;
	private Text textLeftFinger0JS0;
	private Text textLeftFinger0JD0;
	private Text textLeftFinger0JE0;
	private Text textLeftFinger0JE1;
	private Text textLeftFinger0JD1;
	private Text textLeftFinger0JS1;
	private Text textLeftFinger0JE2;
	private Text textLeftFinger0JD2;
	private Text textLeftFinger0JS2;
	private Text textLeftFinger1JE2;
	private Text textLeftFinger1JD2;
	private Text textLeftFinger1JS2;
	private Text textLeftFinger1JE1;
	private Text textLeftFinger1JD1;
	private Text textLeftFinger1JS1;
	private Text textLeftFinger1JE0;
	private Text textLeftFinger1JD0;
	private Text textLeftFinger1JS0;
	private Text textLeftFinger2JE2;
	private Text textLeftFinger2JD2;
	private Text textLeftFinger2JS2;
	private Text textLeftFinger2JE1;
	private Text textLeftFinger2JD1;
	private Text textLeftFinger2JS1;
	private Text textLeftFinger2JE0;
	private Text textLeftFinger2JD0;
	private Text textLeftFinger2JS0;
	private Text textLeftFinger3JE2;
	private Text textLeftFinger3JD2;
	private Text textLeftFinger3JS2;
	private Text textLeftFinger3JE1;
	private Text textLeftFinger3JD1;
	private Text textLeftFinger3JS1;
	private Text textLeftFinger3JE0;
	private Text textLeftFinger3JD0;
	private Text textLeftFinger3JS0;
	private Text textLeftFinger4JE2;
	private Text textLeftFinger4JD2;
	private Text textLeftFinger4JS2;
	private Text textLeftFinger4JE1;
	private Text textLeftFinger4JD1;
	private Text textLeftFinger4JS1;
	private Text textLeftFinger4JE0;
	private Text textLeftFinger4JD0;
	private Text textLeftFinger4JS0;
	private Label lblJointStart;
	private Label lblDirection_1;
	private Label lblJoinEnd;
	private Label label_6;
	private Label label_7;
	private Label label_8;
	private Label label_9;
	private Label label_10;
	private Label label_11;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		        
		try {
			DemoWindow window = new DemoWindow();
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
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				
				Controller controller = new Controller();
		        Frame frame = controller.frame();
				textFrame.setText(String.valueOf(frame.id()));
                textTimeStamp.setText(String.valueOf(frame.timestamp()));
                textHands.setText(String.valueOf(frame.hands().count()));
                textFingers.setText(String.valueOf(frame.fingers().count()));
				
                // Hand
                Hand hand0 = frame.hands().get(0);
                
                textLeftHandtype.setText(hand0.isLeft() ? "Left hand" : "Right hand");
                textLeftHandID.setText(String.valueOf(hand0.id()));
                textLeftPalm.setText(String.valueOf(hand0.palmPosition()));
            	textLeftVector.setText(String.valueOf(hand0.palmNormal()));
            	textLeftDirection.setText(String.valueOf(hand0.direction()));
            	textLeftPitch.setText(String.valueOf(hand0.direction().pitch()));
            	textLeftRoll.setText(String.valueOf(hand0.palmNormal().roll()));
            	textLeftYaw.setText(String.valueOf(hand0.direction().yaw()));
            	textLeftWrist.setText(String.valueOf(hand0.arm().wristPosition()));
            	textLeftArm.setText(String.valueOf(hand0.arm().direction()));
            	textLeftElbow.setText(String.valueOf(hand0.arm().elbowPosition()));
            	
            	// Fingers
            	Finger finger00 = hand0.fingers().get(0);
            	Finger finger01 = hand0.fingers().get(1);
            	Finger finger02 = hand0.fingers().get(2);
            	Finger finger03 = hand0.fingers().get(3);
            	Finger finger04 = hand0.fingers().get(4);
            		
        		textLeftFinger0Type.setText(String.valueOf(finger00.type()));
        		textLeftFinger1Type.setText(String.valueOf(finger01.type()));
        		textLeftFinger2Type.setText(String.valueOf(finger02.type()));
        		textLeftFinger3Type.setText(String.valueOf(finger03.type()));
        		textLeftFinger4Type.setText(String.valueOf(finger04.type()));
        		
        		textLeftFinger0ID.setText(String.valueOf(finger00.id()));
        		textLeftFinger1ID.setText(String.valueOf(finger01.id()));
        		textLeftFinger2ID.setText(String.valueOf(finger02.id()));
        		textLeftFinger3ID.setText(String.valueOf(finger03.id()));
        		textLeftFinger4ID.setText(String.valueOf(finger04.id()));
        		
        		textLeftFinger0Length.setText(String.valueOf(finger00.length()));
        		textLeftFinger1Length.setText(String.valueOf(finger01.length()));
        		textLeftFinger2Length.setText(String.valueOf(finger02.length()));
        		textLeftFinger3Length.setText(String.valueOf(finger03.length()));
        		textLeftFinger4Length.setText(String.valueOf(finger04.length()));
        		
        		textLeftFinger0Extended.setText(String.valueOf(finger00.isExtended()));
        		textLeftFinger1Extended.setText(String.valueOf(finger01.isExtended()));
        		textLeftFinger2Extended.setText(String.valueOf(finger02.isExtended()));
        		textLeftFinger3Extended.setText(String.valueOf(finger03.isExtended()));
        		textLeftFinger4Extended.setText(String.valueOf(finger04.isExtended()));

                // Bones
        		//Bone.Type bone00 = hand0.fingers().get(0).isExtended());
        		
        		
        		
        		for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger00.bone(boneType);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                }
        		
        		
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shell = new Shell();
		shell.setSize(972, 700);
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
		
		lblHandOne = new Label(shell, SWT.NONE);
		lblHandOne.setBounds(20, 52, 61, 15);
		lblHandOne.setText("Hand one");
		
		lblHandTwo = new Label(shell, SWT.NONE);
		lblHandTwo.setBounds(702, 52, 55, 15);
		lblHandTwo.setText("Hand two");
		
		lblHandType = new Label(shell, SWT.NONE);
		lblHandType.setBounds(31, 73, 55, 15);
		lblHandType.setText("Hand type");
		
		textLeftHandtype = new Text(shell, SWT.BORDER);
		textLeftHandtype.setBounds(31, 89, 76, 21);
		
		textRightHandtype = new Text(shell, SWT.BORDER);
		textRightHandtype.setBounds(679, 89, 76, 21);
		
		lblHandType_1 = new Label(shell, SWT.NONE);
		lblHandType_1.setBounds(679, 73, 55, 15);
		lblHandType_1.setText("Hand type");
		
		textLeftPalm = new Text(shell, SWT.BORDER);
		textLeftPalm.setBounds(92, 495, 199, 21);
		
		lblPalmPosition = new Label(shell, SWT.NONE);
		lblPalmPosition.setBounds(10, 498, 76, 15);
		lblPalmPosition.setText("Palm position");
		
		textLeftHandID = new Text(shell, SWT.BORDER);
		textLeftHandID.setBounds(116, 89, 76, 21);
		
		Label lblHandId = new Label(shell, SWT.NONE);
		lblHandId.setBounds(116, 73, 55, 15);
		lblHandId.setText("Hand ID");
		
		textRightHandID = new Text(shell, SWT.BORDER);
		textRightHandID.setBounds(597, 89, 76, 21);
		
		Label lblHandId_1 = new Label(shell, SWT.NONE);
		lblHandId_1.setBounds(597, 73, 55, 15);
		lblHandId_1.setText("Hand ID");
		
		textLeftVector = new Text(shell, SWT.BORDER);
		textLeftVector.setBounds(92, 468, 199, 21);
		
		Label lblNormalVector = new Label(shell, SWT.NONE);
		lblNormalVector.setBounds(10, 471, 76, 15);
		lblNormalVector.setText("Normal vector");
		
		textLeftDirection = new Text(shell, SWT.BORDER);
		textLeftDirection.setBounds(92, 522, 199, 21);
		
		Label lblDirection = new Label(shell, SWT.NONE);
		lblDirection.setBounds(31, 525, 55, 15);
		lblDirection.setText("Direction");
		
		textLeftPitch = new Text(shell, SWT.BORDER);
		textLeftPitch.setBounds(297, 485, 76, 21);
		
		textLeftRoll = new Text(shell, SWT.BORDER);
		textLeftRoll.setBounds(297, 509, 76, 21);
		
		textLeftYaw = new Text(shell, SWT.BORDER);
		textLeftYaw.setBounds(297, 533, 76, 21);
		
		Label lblPitchRoll = new Label(shell, SWT.NONE);
		lblPitchRoll.setBounds(297, 468, 97, 15);
		lblPitchRoll.setText("Pitch - Roll - Yaw");
		
		textLeftElbow = new Text(shell, SWT.BORDER);
		textLeftElbow.setBounds(92, 622, 199, 21);
		
		textLeftArm = new Text(shell, SWT.BORDER);
		textLeftArm.setBounds(92, 595, 199, 21);
		
		textLeftWrist = new Text(shell, SWT.BORDER);
		textLeftWrist.setText("");
		textLeftWrist.setBounds(92, 568, 199, 21);
		
		Label lblElbow = new Label(shell, SWT.NONE);
		lblElbow.setBounds(31, 625, 55, 15);
		lblElbow.setText("Elbow");
		
		Label lblArmDirection = new Label(shell, SWT.NONE);
		lblArmDirection.setBounds(31, 598, 55, 15);
		lblArmDirection.setText("Arm direction");
		
		Label lblWrist = new Label(shell, SWT.NONE);
		lblWrist.setBounds(31, 571, 55, 15);
		lblWrist.setText("Wrist");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 557, 461, 5);
		
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 454, 461, 5);
		
		Label label_2 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(478, 609, 2, 42);
		
		Label lblArm = new Label(shell, SWT.NONE);
		lblArm.setAlignment(SWT.CENTER);
		lblArm.setBounds(450, 595, 55, 15);
		lblArm.setText("Arm");
		
		Label label_3 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_3.setBounds(478, 519, 2, 78);
		
		Label lblHand = new Label(shell, SWT.NONE);
		lblHand.setAlignment(SWT.CENTER);
		lblHand.setBounds(450, 506, 55, 15);
		lblHand.setText("Hand");
		
		Label label_4 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_4.setBounds(478, 347, 2, 160);
		
		textLeftFinger0Type = new Text(shell, SWT.BORDER);
		textLeftFinger0Type.setBounds(67, 427, 76, 21);
		
		textLeftFinger1Type = new Text(shell, SWT.BORDER);
		textLeftFinger1Type.setBounds(149, 427, 76, 21);
		
		textLeftFinger2Type = new Text(shell, SWT.BORDER);
		textLeftFinger2Type.setBounds(231, 427, 76, 21);
		
		textLeftFinger3Type = new Text(shell, SWT.BORDER);
		textLeftFinger3Type.setBounds(313, 427, 76, 21);
		
		textLeftFinger4Type = new Text(shell, SWT.BORDER);
		textLeftFinger4Type.setBounds(395, 427, 76, 21);
		
		textLeftFinger0ID = new Text(shell, SWT.BORDER);
		textLeftFinger0ID.setBounds(67, 402, 76, 21);
		
		textLeftFinger1ID = new Text(shell, SWT.BORDER);
		textLeftFinger1ID.setBounds(149, 402, 76, 21);
		
		textLeftFinger2ID = new Text(shell, SWT.BORDER);
		textLeftFinger2ID.setBounds(231, 402, 76, 21);
		
		textLeftFinger3ID = new Text(shell, SWT.BORDER);
		textLeftFinger3ID.setBounds(313, 402, 76, 21);
		
		textLeftFinger4ID = new Text(shell, SWT.BORDER);
		textLeftFinger4ID.setBounds(395, 402, 76, 21);
		
		lblType = new Label(shell, SWT.NONE);
		lblType.setBounds(10, 430, 55, 15);
		lblType.setText("Type");
		
		lblId = new Label(shell, SWT.NONE);
		lblId.setBounds(10, 405, 55, 15);
		lblId.setText("ID");
		
		textLeftFinger0Length = new Text(shell, SWT.BORDER);
		textLeftFinger0Length.setBounds(67, 375, 76, 21);
		
		textLeftFinger1Length = new Text(shell, SWT.BORDER);
		textLeftFinger1Length.setBounds(149, 375, 76, 21);
		
		textLeftFinger2Length = new Text(shell, SWT.BORDER);
		textLeftFinger2Length.setBounds(231, 375, 76, 21);
		
		textLeftFinger3Length = new Text(shell, SWT.BORDER);
		textLeftFinger3Length.setBounds(313, 375, 76, 21);
		
		textLeftFinger4Length = new Text(shell, SWT.BORDER);
		textLeftFinger4Length.setBounds(395, 375, 76, 21);
		
		lblLength = new Label(shell, SWT.NONE);
		lblLength.setBounds(10, 378, 55, 15);
		lblLength.setText("Length");
		
		textLeftFinger0Extended = new Text(shell, SWT.BORDER);
		textLeftFinger0Extended.setBounds(67, 348, 76, 21);
		
		textLeftFinger1Extended = new Text(shell, SWT.BORDER);
		textLeftFinger1Extended.setBounds(149, 348, 76, 21);
		
		textLeftFinger2Extended = new Text(shell, SWT.BORDER);
		textLeftFinger2Extended.setBounds(231, 348, 76, 21);
		
		textLeftFinger3Extended = new Text(shell, SWT.BORDER);
		textLeftFinger3Extended.setBounds(313, 348, 76, 21);
		
		textLeftFinger4Extended = new Text(shell, SWT.BORDER);
		textLeftFinger4Extended.setBounds(395, 348, 76, 21);
		
		lblWidth = new Label(shell, SWT.NONE);
		lblWidth.setBounds(10, 351, 55, 15);
		lblWidth.setText("Extended");
		
		Label lblFingers_1 = new Label(shell, SWT.NONE);
		lblFingers_1.setAlignment(SWT.CENTER);
		lblFingers_1.setBounds(450, 326, 55, 15);
		lblFingers_1.setText("Fingers");
		
		Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		label_5.setBounds(478, 59, 2, 267);
		
		textLeftFinger0JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS0.setBounds(67, 305, 76, 21);
		
		textLeftFinger0JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD0.setBounds(67, 284, 76, 21);
		
		textLeftFinger0JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE0.setBounds(67, 263, 76, 21);
		
		textLeftFinger0JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE1.setBounds(67, 194, 76, 21);
		
		textLeftFinger0JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD1.setBounds(67, 215, 76, 21);
		
		textLeftFinger0JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS1.setBounds(67, 236, 76, 21);
		
		textLeftFinger0JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JE2.setBounds(67, 125, 76, 21);
		
		textLeftFinger0JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JD2.setBounds(67, 146, 76, 21);
		
		textLeftFinger0JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger0JS2.setBounds(67, 167, 76, 21);
		
		textLeftFinger1JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE2.setBounds(149, 125, 76, 21);
		
		textLeftFinger1JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD2.setBounds(149, 146, 76, 21);
		
		textLeftFinger1JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS2.setBounds(149, 167, 76, 21);
		
		textLeftFinger1JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE1.setBounds(149, 194, 76, 21);
		
		textLeftFinger1JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD1.setBounds(149, 215, 76, 21);
		
		textLeftFinger1JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS1.setBounds(149, 236, 76, 21);
		
		textLeftFinger1JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JE0.setBounds(149, 263, 76, 21);
		
		textLeftFinger1JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JD0.setBounds(149, 284, 76, 21);
		
		textLeftFinger1JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger1JS0.setBounds(149, 305, 76, 21);
		
		textLeftFinger2JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE2.setBounds(231, 125, 76, 21);
		
		textLeftFinger2JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD2.setBounds(231, 146, 76, 21);
		
		textLeftFinger2JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS2.setBounds(231, 167, 76, 21);
		
		textLeftFinger2JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE1.setBounds(231, 194, 76, 21);
		
		textLeftFinger2JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD1.setBounds(231, 215, 76, 21);
		
		textLeftFinger2JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS1.setBounds(231, 236, 76, 21);
		
		textLeftFinger2JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JE0.setBounds(231, 263, 76, 21);
		
		textLeftFinger2JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JD0.setBounds(231, 284, 76, 21);
		
		textLeftFinger2JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger2JS0.setBounds(231, 305, 76, 21);
		
		textLeftFinger3JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE2.setBounds(313, 125, 76, 21);
		
		textLeftFinger3JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD2.setBounds(313, 146, 76, 21);
		
		textLeftFinger3JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS2.setBounds(313, 167, 76, 21);
		
		textLeftFinger3JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE1.setBounds(313, 194, 76, 21);
		
		textLeftFinger3JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD1.setBounds(313, 215, 76, 21);
		
		textLeftFinger3JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS1.setBounds(313, 236, 76, 21);
		
		textLeftFinger3JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JE0.setBounds(313, 263, 76, 21);
		
		textLeftFinger3JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JD0.setBounds(313, 284, 76, 21);
		
		textLeftFinger3JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger3JS0.setBounds(313, 305, 76, 21);
		
		textLeftFinger4JE2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE2.setBounds(395, 125, 76, 21);
		
		textLeftFinger4JD2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD2.setBounds(395, 146, 76, 21);
		
		textLeftFinger4JS2 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS2.setBounds(395, 167, 76, 21);
		
		textLeftFinger4JE1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE1.setBounds(395, 194, 76, 21);
		
		textLeftFinger4JD1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD1.setBounds(395, 215, 76, 21);
		
		textLeftFinger4JS1 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS1.setBounds(395, 236, 76, 21);
		
		textLeftFinger4JE0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JE0.setBounds(395, 263, 76, 21);
		
		textLeftFinger4JD0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JD0.setBounds(395, 284, 76, 21);
		
		textLeftFinger4JS0 = new Text(shell, SWT.BORDER);
		textLeftFinger4JS0.setBounds(395, 305, 76, 21);
		
		lblJointStart = new Label(shell, SWT.NONE);
		lblJointStart.setBounds(10, 308, 55, 15);
		lblJointStart.setText("Joint start");
		
		lblDirection_1 = new Label(shell, SWT.NONE);
		lblDirection_1.setBounds(10, 287, 55, 15);
		lblDirection_1.setText("Direction");
		
		lblJoinEnd = new Label(shell, SWT.NONE);
		lblJoinEnd.setBounds(10, 266, 55, 15);
		lblJoinEnd.setText("Join end");
		
		label_6 = new Label(shell, SWT.NONE);
		label_6.setText("Join end");
		label_6.setBounds(10, 197, 55, 15);
		
		label_7 = new Label(shell, SWT.NONE);
		label_7.setText("Direction");
		label_7.setBounds(10, 218, 55, 15);
		
		label_8 = new Label(shell, SWT.NONE);
		label_8.setText("Joint start");
		label_8.setBounds(10, 239, 55, 15);
		
		label_9 = new Label(shell, SWT.NONE);
		label_9.setText("Join end");
		label_9.setBounds(10, 128, 55, 15);
		
		label_10 = new Label(shell, SWT.NONE);
		label_10.setText("Direction");
		label_10.setBounds(10, 149, 55, 15);
		
		label_11 = new Label(shell, SWT.NONE);
		label_11.setText("Joint start");
		label_11.setBounds(10, 170, 55, 15);

	}
}
