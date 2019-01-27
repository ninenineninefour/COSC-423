// First-in-first-out pager
public class FifoPager implements MemoryPager {
	// pageTable is bound rather than set in the constructor so it can be initialized before the table
	private PageTable pageTable;
	private int firstIn = 0;
	
	public FifoPager() {}
	
	// Returns the oldest page
	@Override
	public int pickPage() {
		if(pageTable == null) // check that pageTable is bound
			return -1;
		
		int page = firstIn;
		firstIn = (firstIn + 1)%pageTable.size();
		return page;
	}

	@Override
	public void bindPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}
	
	@Override
	public void tick() {}
}
