//Salim Al Farsi (1258330)
//Honggang Xu (1369387)

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/*
MakeRuns accepts standard inputs as the size of the MinHeap to use and the file to process.
Then it creates initial runs using the replacement selection strategy.
These runs are written to a single temporary file.
It also output the number of runs.
*/
public class MakeRuns
{
	private static int size;
	private static int runs;
	private static MinHeap heap;

	/*
	accept a single integer argument specifying the size of the MinHeap
	and a single file name as the input file
	*/
	public static void main(String [] args)
	{
		try
		{
			//size fo the heap
			size = Integer.parseInt(args[0]);
			heap = new MinHeap(size);

			String inputFileName = args[1];
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));

			FileWriter file = new FileWriter("runs.txt");
			PrintWriter writer = new PrintWriter(file);

			replacementSelection(reader, writer);
			System.out.println("Process Finished");
			System.out.println("Number of Runs: " + runs);
		}
		catch (Exception ex)
		{
			System.err.println("Usage: Java MakeRuns <number> <file>");
			ex.printStackTrace();
		}		
	}

	//replacement selection strategy to produce output
	private static void replacementSelection(BufferedReader inputFile, PrintWriter writer)
	{
		String line = null;
		int removed = 0;
		try 
		{
			String lastOutput = null;
			boolean hasInput = true;
			//fill up the heap
			while (heap.capacity() < size)
			{
				//read a string
				if ((line = inputFile.readLine()) != null)
					heap.insert(line);
				else
				{
					hasInput = false;
					break;
				}
			}

			//while has input
			while (hasInput)
			{	
				//while root can go into current run
				if (lastOutput == null || !heap.smallerThan(heap.peek(), lastOutput))
				{
					if ((line = inputFile.readLine()) != null)
					{
						//use replace method to put the String in heap
						lastOutput = heap.replace(line);
						writer.println(lastOutput);
					}
					else
					{
						hasInput= false;
					}
					
				}
				//if root cannot go into the current run
				else if (heap.smallerThan(heap.peek(), lastOutput))
				{
					if (line == null)
						hasInput= false;
					heap.remove();
					removed++;
				}

				//check if heap's capacity is reduced to 0
				if (heap.capacity() == 0)
				{
					//then one run is finished
					writer.println("-end of run");
					runs++;
					heap.reheap(size);
					heap.setCapacity(size);
					lastOutput = null;
					removed = 0;
				}
			}
			//when inputFile is empty, reheap the heap and output what's left in the heap
			heap.setCapacity(heap.capacity() + removed);
			heap.reheap(heap.capacity());

			if (heap.capacity() > 0)
			{
				if (removed != 0)
				{
					writer.println("-end of run");
					runs++;
				}
				
				while (heap.capacity() != 0)
				{
					String out = heap.remove();
					if (out != null)
						writer.println(out);
				}
				writer.print("-end of run");
				runs++;
			}
			writer.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}	
	}
}