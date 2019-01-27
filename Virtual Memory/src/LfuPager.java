// Least frequently used pager
public class LfuPager implements MemoryPager {
	// pageTable is bound rather than set in the constructor so it can be initialized before the table
	private PageTable pageTable;
	
	public LfuPager() {}
	
	// Returns the least frequently used page
	@Override
	public int pickPage() {
		if(pageTable == null)
			return -1;
		
		// find page with the lowest ratio
		int page = 0;
		for(int i = 1; i < pageTable.size(); i++) {
			if(getAccessRatio(i) < getAccessRatio(page)) {
				page = i;
			}
		}
		return page;
	}
	
	private double getAccessRatio(int page) {
		if(pageTable.getAge(page) == 0)
			return 0.0; // empty frames should be used first
		return ((double) pageTable.getAccesses(page))/pageTable.getAge(page);
	}
	
	@Override
	public void bindPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}

	@Override
	public void tick() {}
}
