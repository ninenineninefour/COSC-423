// Interface used for memory paging schemes
public interface MemoryPager {
	// Select a page to overwrite
	public int pickPage();
	// Bind a page table to the pager
	public void bindPageTable(PageTable pageTable);
	// Notify the pager that time has advanced
	public void tick();
}
