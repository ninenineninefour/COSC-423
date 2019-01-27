import java.util.Arrays;

// Object representing a page table
public class PageTable {
	// Object controlling which page to select
	private MemoryPager pager;
	
	private int[] table;
	private int[] age;
	private int[] accesses;
	private int[] accessedOn;
	
	private int nPageFaults = 0;
	
	// Constructor
	public PageTable(int size, MemoryPager pager) {
		table = new int[size];
		Arrays.fill(table, 0);
		age = new int[size];
		accesses = new int[size];
		accessedOn = new int[size];
		
		this.pager = pager;
		pager.bindPageTable(this);
	}
	
	// Get current frame in the selected frame
	public int get(int page) {
		return table[page];
	}
	
	// Get age of selected frame
	public int getAge(int page) {
		return age[page];
	}
	
	// Get number of times page was accessed
	public int getAccesses(int page) {
		return accesses[page];
	}
	
	// Get time since the page was last accessed
	public int getTimeSinceAccess(int page) {
		if(table[page] == 0)
			return Integer.MAX_VALUE; // effectively infinity
		return age[page] - accessedOn[page];
	}
	
	// Get page from frame
	public int pageOf(int frame) {
		for(int i = 0; i < table.length; i++) {
			if(table[i] == frame)
				return i;
		}
		return -1;
	}
	
	// Find if frame is in table, and if so, access it.
	// Otherwise, choose a page to replace.
	// Returns true if there was a page fault.
	public boolean allocate(int frame) {
		int page = pageOf(frame);
		if(page == -1) {
			nPageFaults++;
			page = pager.pickPage();
			replace(page, frame);
			
			tick();
			return true;
		}
		access(page);
		tick();
		return false;
	}
	
	// Replace the contents of the selected page
	private void replace(int page, int frame) {
		table[page] = frame;
		age[page] = 0;
		accesses[page] = 1;
		accessedOn[page] = 0;
	}
	
	// Access the selected page
	private void access(int page) {
		accesses[page]++;
		accessedOn[page] = age[page];
	}
	
	// Age all the pages
	private void tick() {
		for(int i = 0; i < age.length; i++) {
			if(table[i] != 0)
				age[i]++;
		}
		pager.tick();
	}
	
	// Return copy of the current table
	public int[] getTable() {
		return table.clone();
	}
	
	// Return the size of the table
	public int size() {
		return table.length;
	}
	
	// Return the number of page faults
	public int nPageFaults() {
		return nPageFaults;
	}
}
