// Least recently used pager
public class LruPager implements MemoryPager {
	// pageTable is bound rather than set in the constructor so it can be initialized before the table
	private PageTable pageTable;
	
	public LruPager() {}
	
	// Returns the page that has been unused the longest
	@Override
	public int pickPage() {
		if(pageTable == null)
			return -1;
		
		// Find the page that has been unused the longest
		int page = 0;
		for(int i = 1; i < pageTable.size(); i++) {
			if(pageTable.getTimeSinceAccess(i) > pageTable.getTimeSinceAccess(page)) {
				page = i;
			}
		}
		return page;
	}
	
	@Override
	public void bindPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}
	
	@Override
	public void tick() {}
}
