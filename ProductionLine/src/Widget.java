import java.util.ArrayList;

// Class representing a single widget. Keeps track of all the workers who have worked on it.
public class Widget {
	private int id;
	private ArrayList<String> handledBy;
	
	public Widget(int id) {
		this.id = id;
		handledBy = new ArrayList<>();
	}
	
	// this method is called to "work on" the widget, adding the Worker to the list of Workers who have processed it
	public void workUpon(String worker) {
		handledBy.add(worker);
	}
	
	// Return a formatted string with the ID and state of the Widget
	public String toString() {
		String s = "widget" + id;
		
		if(handledBy.isEmpty())
			return s;
		
		s += "<";
		for(String worker : handledBy) {
			s += worker + ",";
		}
		s = s.substring(0, s.length() - 1);
		s += ">";
		return s;
	}
}
