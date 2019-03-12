//the size of each run may differ. only when the heap capacity is 0,]
//the run is finished
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MakeRuns
{
	private static int size;
	private static int runs;
	private static MinHeap heap;

	//accept a single integer argument specifying the size of the MinHeap
	//and a single file name as the input file
	public static void main(String [] args)
	{
		try
		{
			size = Integer.parseInt(args[0]);
			heap = new MinHeap(size);
			System.out.println("created heap");

			String inputFileName = args[1];
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			System.out.println("input file identified");

			FileWriter file = new FileWriter("runs.txt");
			PrintWriter writer = new PrintWriter(file);
			System.out.println("output file created");
			

			replacementSelection(reader, writer);
			System.out.println("Number of Runs:" + runs);
		}
		catch (Exception ex)
		{
			System.err.println("invalid input");
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
			System.out.println("finished fill up the heap");

			for (int i = 0; i < size; i++)
				System.out.println(heap.get(i));
			System.out.println("printed the heap");

			String lastOutput = null;
			boolean hasInput = true;
			while (hasInput)
			{	//System.out.println(line);
				//System.out.println("check if root can go into run");
				//System.out.println(heap.peek());
				//System.out.println("displayed the root");
				//while root can go into current run


				if (lastOutput == null || !heap.smallerThan(heap.peek(), lastOutput))
				{
					if ((line = inputFile.readLine()) != null)
					{
						//use replace method to put the String in heap
						lastOutput = heap.replace(line);
						writer.println(lastOutput);
						System.out.println(lastOutput);
						System.out.println("output one String");
					}
					else
					{
						hasInput= false;
					}
					
				}
				else if (heap.smallerThan(heap.peek(), lastOutput))
				{
					System.out.println("root cannot go into run");
					heap.remove();
					System.out.println("removed root from heap");
				}

				System.out.println("check if heap's capacity is reduced to 0");
				if (heap.capacity() == 0)
				{
					writer.println("end of run");
					runs++;
					heap.reheap(size);
					System.out.println("capacity hits 0 and reheap");
					heap.setCapacity(size);
					lastOutput = null;
				}
				System.out.println("finished reading one line");
			
			}
		}
		
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("the capacity of the heap is:");
		System.out.println(heap.capacity());
		//when inputFile is empty & heap is not empty
		
		//if (heap.capacity() != 0)
		//{
			System.out.println("entered if statement");


			//this reheap don't finish
			heap.reheap(size);
			System.out.println("finished reheap");

			for (int i = 0; i < size; i++)
			{
				System.out.println("entered for loop");
				writer.println(heap.get(i));
				System.out.println(heap.get(i));
			}
			writer.println("end of run");
			runs++;
			writer.println("end of file");
			System.out.println("all the runs have ended");
		//}
		writer.close();
	}

}