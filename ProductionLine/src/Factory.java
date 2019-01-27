import java.util.ArrayList;
import javax.swing.*;

public class Factory {
	public static void main(String[] args) {
		// Prompt the user for the number of widgets to produce and size of the belts
		int widgetsToProduce = -1;
		while(widgetsToProduce <= 0) {
			widgetsToProduce = Integer.parseInt(showInputDialog("Enter number of widgets to produce:"));
		}
		int conveyerBeltSize = -1;
		while(conveyerBeltSize <= 0) {
			conveyerBeltSize = Integer.parseInt(showInputDialog("Enter size of conveyer belts:"));
		}
		
		// Initialize the belts and the finished bin
		ConveyerBelt beltAB = new ConveyerBelt("Belt A-B", conveyerBeltSize);
		ConveyerBelt beltBC = new ConveyerBelt("Belt B-C", conveyerBeltSize);
		ConveyerBelt beltCD = new ConveyerBelt("Belt C-D", conveyerBeltSize);
		ArrayList<Widget> finishedBin = new ArrayList<>();
		
		// Initialize the workers
		Worker workerA = new Worker("A", widgetsToProduce, beltAB);
		Worker workerB = new Worker("B", beltAB, beltBC);
		Worker workerC = new Worker("C", beltBC, beltCD);
		Worker workerD = new Worker("D", beltCD, finishedBin);
		
		// Set up the GUI
		FactoryGUI.setupFactoryGUI(new Worker[] {workerA, workerB, workerC, workerD}, new ConveyerBelt[] {beltAB, beltBC, beltCD}, finishedBin);
		
		// Start each worker
		workerA.start();
		workerB.start();
		workerC.start();
		workerD.start();
		
		// Wait for each successive worker to finish and tell the next one to halt when its belt is done
		try {
			workerA.join();
			workerB.haltWhenPrevBeltEmpty();
			workerB.join();
			workerC.haltWhenPrevBeltEmpty();
			workerC.join();
			workerD.haltWhenPrevBeltEmpty();
			workerD.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Prompt the user for a string
	private static String showInputDialog(String prompt) {
		JFrame frame = new JFrame();
		return JOptionPane.showInputDialog(frame, prompt); 
	}
}
