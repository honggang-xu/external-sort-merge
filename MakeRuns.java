import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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
			System.err.println("usage: <number> <file>");
			ex.printStackTrace();
		}		
	}

	private static void replacementSelection(BufferedReader inputFile, PrintWriter writer)
	{
		String line;
		try 
		{
			//fill up the heap
			while (heap.capacity() < size)
			{
				//read a string
				if ((line = inputFile.readLine()) != null)
					heap.insert(line);
			}

			String lastOutput = null;
			boolean hasInput = true;
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
					heap.remove();
				}

				//check if heap's capacity is reduced to 0
				if (heap.capacity() == 0)
				{
					writer.println("-end of run");
					runs++;
					heap.reheap(size);
					heap.setCapacity(size);
					lastOutput = null;
				}
			}
			//when inputFile is empty, reheap the heap and output the heap
			heap.reheap(size);

			for (int i = 0; i < size; i++)
			{
				writer.println(heap.get(i));
			}
			writer.println("-end of run");
			runs++;
			writer.println("-end of file");
			writer.println("-number of runs:");
			writer.println(runs);
			writer.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}	
	}
}