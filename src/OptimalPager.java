// Optimal pager
public class OptimalPager implements MemoryPager {
	// pageTable is bound rather than set in the constructor so it can be initialized before the table
	private PageTable pageTable;
	// To determine pages to update, the pager needs a record of all future accesses
	private int[] data;
	// Starts at index 1 because index 0 is number of frames
	private int time = 1;
	
	// Constructor requires data to be passed
	public OptimalPager(int[] data) {
		this.data = data;
	}
	
	// Returns the frame that will not be used for the longest time
	@Override
	public int pickPage() {
		int t = timeToNextUse(0);
		if(t == -1)
			return 0;
		int page = 0;
		for(int i = 1; i < pageTable.size(); i++) {
			int tNew = timeToNextUse(i);
			if(tNew == -1)
				return i;
			if(tNew > t) {
				page = i;
				t = tNew;
			}
		}
		return page;
	}
	
	// Returns the number of ticks until the next time the selected page is used
	private int timeToNextUse(int page) {
		int frame = pageTable.get(page);
		int i = time;
		while(data[i] != frame) {
			i++;
			// Return -1 if the frame is never used again
			if(i >= data.length)
				return -1;
		}
		return i;
	}

	@Override
	public void bindPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}
	
	// This is the only pager to actually use tick()
	@Override
	public void tick() {
		time++;
	}
}
