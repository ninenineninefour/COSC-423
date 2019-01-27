import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Main program class
public class VirtualMemory {
	// Main method
	public static void main(String[] args) {
		// Prompt the user for the input file
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter input filename:");
		String inputFilename = sc.nextLine();
		sc.close();
		
		// Attempt to access file
		File inputFile = new File(inputFilename);
		Scanner in = null;
		try {
			in = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find " + inputFilename);
			System.exit(-1);
		}
		
		// Run pagers until the file is complete
		while(in.hasNext()) {
			int[] data = getNextData(in);
			int[] faults = new int[4];
			System.out.println("\nFIFO:");
			faults[0] = runPager(data, new FifoPager());
			System.out.println("\nLRU:");
			faults[1] = runPager(data, new LruPager());
			System.out.println("\nLFU:");
			faults[2] = runPager(data, new LfuPager());
			System.out.println("\nOptimal:");
			faults[3] = runPager(data, new OptimalPager(data));
			
			String[] names = new String[] {"FIFO    ", "LRU     ", "LFU     ", "Optimal "};
			
			System.out.println("\nUsing " + data[0] + " frames, the reference string yielded:");
			System.out.println("Scheme  #Faults %Optimal");
			
			for(int i = 0; i < faults.length; i++) {
				System.out.println(names[i] + padRight("" + faults[i], 8) + String.format("%.1f", 100.0*faults[i]/faults[3]) + "%");
			}
		}
	}
	
	// Returns the next data to run
	// First int is the number of pages, the rest are frame accesses
	private static int[] getNextData(Scanner in) {
		ArrayList<Integer> list = new ArrayList<>();
		int next = Integer.parseInt(in.next());
		while(next != -1) {
			list.add(next);
			next = Integer.parseInt(in.next());
		}
		int[] data = new int[list.size()];
		for(int i = 0; i < data.length; i++) {
			data[i] = list.get(i);
		}
		return data;
	}
	
	// Runs a pager using the given data and MemoryPager
	private static int runPager(int[] data, MemoryPager pager) {
		PageTable pageTable = new PageTable(data[0], pager);
		int[][] tableStates = new int[data.length - 1][];
		boolean[] pageFaults = new boolean[data.length - 1];
		int maxLength = 0;
		
		for(int i = 1; i < data.length; i++) {
			pageFaults[i - 1] = pageTable.allocate(data[i]);
			tableStates[i - 1] = pageTable.getTable();
			int length = ("" + data[i]).length();
			if(length > maxLength) {
				maxLength = length;
			}
		}
		
		String line = " ";
		for(int i = 1; i < data.length; i++) {
			line += padRight("" + data[i], maxLength) + " ";
		}
		System.out.println(line);
		System.out.println(repeat('-', line.length()));
		
		for(int c = 0; c < tableStates[0].length; c++) {
			line = " ";
			for(int r = 0; r < tableStates.length; r++) {
				if(pageFaults[r]) {
					line += padRight("" + tableStates[r][c], maxLength) + " ";
				} else {
					line += padRight(" ", maxLength) + " ";
				}
			}
			System.out.println(line);
		}
		
		return pageTable.nPageFaults();
	}
	
	// Pad string with spaces on the right
	private static String padRight(String s, int length) {
		String out = s;
		while(out.length() < length) {
			out += " ";
		}
		return out;
	}
	
	// Repeat character n times 
	private static String repeat(char c, int n) {
		String s = "";
		for(int i = 0; i < n; i++) {
			s += c;
		}
		return s;
	}
}
