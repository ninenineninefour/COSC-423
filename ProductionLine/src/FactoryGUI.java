import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// GUI for the factory (I.E. the program's frontend)
public class FactoryGUI {
	public static void setupFactoryGUI(Worker[] workers, ConveyerBelt[] belts, ArrayList<Widget> finishedBin) {
		// Set up the main JFrame
		JFrame frame = new JFrame("Production Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set up a pane variable, and set it to GridBagLayout
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		// Initialize a GridBagConstraints variable and set a few properties
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.anchor = GridBagConstraints.CENTER;
		cons.insets = new Insets(5,5,5,5);
		
		// Add worker and conveyer belt labels to the first row
		cons.gridy = 0;
		cons.gridx = 0;
		for(int i = 0; i < belts.length; i++) {
			pane.add(new Label("Worker " + workers[i], Label.CENTER), cons);
			cons.gridx++;
			pane.add(new Label(belts[i].toString(), Label.CENTER), cons);
			cons.gridx++;
		}
		pane.add(new Label("Worker " + workers[workers.length - 1], Label.CENTER), cons);
		// Finally, add the finished bin label
		cons.gridx++;
		pane.add(new Label("Finished bin", Label.CENTER), cons);
		
		// Now, add the text areas
		cons.anchor = GridBagConstraints.NORTH;
		cons.gridy = 1;
		// First, do the workers
		for(int i = 0; i < workers.length; i++) {
			cons.gridx = 2*i;
			JTextArea textArea = new JTextArea("idle");
			textArea.setPreferredSize(new Dimension(250, 20));
			pane.add(textArea, cons);
			workers[i].bindTextArea(textArea);
		}
		// Next, the belts
		for(int i = 0; i < belts.length; i++) {
			cons.gridx = 2*i + 1;
			JTextArea textArea = new JTextArea("empty");
			textArea.setPreferredSize(new Dimension(150, 80));
			pane.add(textArea, cons);
			belts[i].bindTextArea(textArea);
		}
		// Finally, do the finished bin. This one is more complicated since it also has a scrollbar
		cons.gridx = 2*(workers.length - 1) + 1;
		JTextArea textArea = new JTextArea("empty");
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(150, 80));
		pane.add(scrollPane, cons);
		workers[workers.length - 1].bindFinishedBinTextArea(textArea);
		
		// finalize the frame
		frame.pack();
		frame.setVisible(true);
	}
}
