import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTextArea;

// Class representing a worker who works on widgets.
public class Worker extends Thread {
	// Min and max work time, variation allows for more conflicts to occur
	private final static int MIN_SLEEP_TIME = 50;
	private final static int MAX_SLEEP_TIME = 1000;
	
	// Worker's name
	private String name;
	// Previous and next conveyer belts
	private ConveyerBelt prevBelt;
	private ConveyerBelt nextBelt;
	// Item currently being worked on
	private Widget currentItem;
	
	// These variables are only used by the first worker, who creates new widgets instead of acquiring them
	private int genId = 1;
	private int widgetsToMake;
	
	// This variable is used by the last worker, who places the widgets into it, representing all the finished widgets
	private ArrayList<Widget> finishedBin;
	
	// These variables let the worker know if it should halt
	private boolean haltWhenPrevBeltEmpty = false;
	private boolean running = false;
	
	// Text areas from the GUI bound to the worker to allow them to update the text
	private JTextArea boundTextArea;
	private JTextArea finishedBinTextArea;
	
	// Constructor for "middle" workers (I.E. Workers B and C)
	public Worker(String name, ConveyerBelt prevBelt, ConveyerBelt nextBelt) {
		this.name = name;
		this.prevBelt = prevBelt;
		this.nextBelt = nextBelt;
	}
	// Constructor for the first worker (I.E. Worker A)
	public Worker(String name, int widgetsToMake, ConveyerBelt nextBelt) {
		this.name = name;
		this.widgetsToMake = widgetsToMake;
		this.nextBelt = nextBelt;
	}
	// Constructor for the final worker (I.E. Worker D)
	public Worker(String name, ConveyerBelt prevBelt, ArrayList<Widget> finishedBin) {
		this.name = name;
		this.prevBelt = prevBelt;
		this.finishedBin = finishedBin;
	}
	
	// Get the next widget from the conveyer, or build a new one
	public synchronized void retrieveNext() {
		if(prevBelt == null) { // If prevBelt does not exist, create a new widget
			// Check if enough widgets have been made
			if(genId > widgetsToMake) {
				running = false;
				return;
			}
			// Make a new widget
			currentItem = new Widget(genId);
			genId++;
			System.out.println(this + " has created " + currentItem);
		} else {
			// Check if the previous belt is empty, and if so, wait until it has something
			if(prevBelt.isEmpty()) {
				if(haltWhenPrevBeltEmpty) {
					running = false;
					return;
				}
				System.out.println("WARNING: " + this + " is idle!");
				updateTextArea("Waiting on " + prevBelt + " to have items");
			}
			// Grab an item from the conveyer
			currentItem = prevBelt.pop();
			System.out.println(this + " is retrieving " + currentItem + " from the belt");
			updateTextArea("Working on " + currentItem);
		}
	}
	
	// Move the widget to the next conveyer or into the finished bin
	public synchronized void passItem() {
		// If the worker isn't working on anything, return
		if(currentItem == null)
			return;
		
		if(nextBelt == null) { // Check if worker is the last one, and if so, put it in the finished bin
			System.out.println(this + " has completed " + currentItem);
			finishedBin.add(currentItem);
			updateFinishedBinTextArea();
		} else {
			// Check if the next belt is full, and if so, wait for it to have room
			if(nextBelt.isFull()) {
				System.out.println("WARNING: Worker " + this + " is waiting to put " + currentItem + " on conveyer!");
				updateTextArea("Waiting on " + nextBelt + " to have room");
			}
			// Put the item on the belt
			System.out.println("Worker " + this + " is placing " + currentItem + " on the belt");
			nextBelt.push(currentItem);
		}
		currentItem = null;
	}
	
	// Work on the current widget
	public void workOnItem(Random r) {
		// If the worker doesn't have a widget, return
		if(currentItem == null)
			return;
		
		// Sleep for a random amount of time
		System.out.println("Worker " + this + " is working on " + currentItem);
		updateTextArea("Working on " + currentItem);
		try {
			// Sleep time varies between the min and max times
			Thread.sleep(MIN_SLEEP_TIME + r.nextInt(MAX_SLEEP_TIME - MIN_SLEEP_TIME));
		} catch (InterruptedException e) {}
		// Finally, update the widget
		currentItem.workUpon(name);
	}
	
	// Run the thread
	public void run() {
		// Thread runs until running is set to false
		running = true;
		Random r = new Random();
		
		while(running) {
			// grab or make new widget
			retrieveNext();
			// work on the widget and sleep
			workOnItem(r);
			// pass the widget to the next belt
			passItem();
		}
		
		// announce that the worker is done
		System.out.println("Worker " + this + " has finished production of widgets");
		updateTextArea("Finished working");
	}
	
	// Set the flag to halt when the previous belt is empty
	public void haltWhenPrevBeltEmpty() {
		haltWhenPrevBeltEmpty = true;
	}
	
	// Return the name
	public String toString() {
		return name;
	}
	
	// Bind a text area to the Worker
	public void bindTextArea(JTextArea textArea) {
		boundTextArea = textArea;
	}
	
	// Update the text area, if it exists
	private void updateTextArea(String s) {
		if(boundTextArea == null)
			return;
		boundTextArea.setText(s);
	}
	
	// Bind the finished bin's text area
	public void bindFinishedBinTextArea(JTextArea textArea) {
		finishedBinTextArea = textArea;
	}
	
	// Update the finished bin's text area, if it exists
	private void updateFinishedBinTextArea() {
		if(finishedBinTextArea == null)
			return;
		if(finishedBin.isEmpty()) {
			finishedBinTextArea.setText("empty");
			return;
		}
		finishedBinTextArea.setText("");
		for(Widget item : finishedBin) {
			finishedBinTextArea.append(item + "\n");
		}
	}
}
