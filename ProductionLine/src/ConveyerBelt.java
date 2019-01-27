import javax.swing.JTextArea;

// Class representing a conveyer belt
// Uses a circular array to implement a queue
public class ConveyerBelt {
	// The belt's name
	private String name;
	// Items on the belt, as a circular array
	private Widget[] items;
	// Variables used to manage the circular array
	private int nItems = 0;
	private int first = 0;
	private int last = 0;
	
	// The text area bound to the belt
	private JTextArea boundTextArea;
	
	// Constructor
	public ConveyerBelt(String name, int maxItems) {
		this.name = name;
		items = new Widget[maxItems];
	}
	
	// Add a new item to the array, waiting if full
	public synchronized boolean push(Widget item) {
		// Check if full, and if so, wait
		while(isFull()) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		// Add the item to the array
		items[last] = item;
		// Increment last and nItems
		last = inc(last);
		nItems++;
		// Update text area and notify any threads waiting for it
		updateTextArea();
		notifyAll();
		return true;
	}
	
	// Grab an item off of the array, waiting if empty
	public synchronized Widget pop() {
		// Check if the array is empty, and if so, wait
		while(isEmpty()) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		// Grab the first item from the array
		Widget item = items[first];
		// Clear the vacated slot
		items[first] = null;
		// Increment first and decrement nItems
		first = inc(first);
		nItems--;
		// Update text area and notify any threads waiting for it
		updateTextArea();
		notifyAll();
		return item;
	}
	
	// Return true if the array is empty
	public boolean isEmpty() {
		return nItems <= 0;
	}
	
	// Return true if the array is full
	public boolean isFull() {
		return items.length <= nItems;
	}
	
	// Return the name of the belt
	public String toString() {
		return name;
	}
	
	// Bind a text area to the belt
	public void bindTextArea(JTextArea textArea) {
		boundTextArea = textArea;
	}
	
	// Update the text area, if it exists
	private void updateTextArea() {
		// Check to see if the text area exists
		if(boundTextArea == null)
			return;
		// Check if the belt is empty
		if(nItems == 0) {
			boundTextArea.setText("empty");
			return;
		}
		// Iterate through each item in the array, in order
		boundTextArea.setText("");
		int index = first;
		for(int i = 0; i < nItems; i++) {
			boundTextArea.append(items[index] + "\n");
			index = inc(index);
		}
	}
	
	// Helper method to increment an index, wrapping if necessary
	private int inc(int n) {
		return (n + 1)%items.length;
	}
}
