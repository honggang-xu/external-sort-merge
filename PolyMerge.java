import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*
Make sure the number of runs are distributed unevenly as Fibonacci number
for the the number of files and also leaving one file empty for output 
and then apply merge untill one input file is empty and then switch the roles
of that input file with the output file
*/

public class PolyMerge
{

	public static void main(String [] args)
	{
		int numberOfFiles;
		int numberOfRuns;
		try
		{
			numberOfFiles = Integer.parseInt(args[0]);
			numberOfRuns = Integer.parseInt(args[1]);
			String inputFileName = args[2];
			/*
			//not sure about this condition checking
			if(numberOfFiles >= numberOfRuns + 1)
			{
				System.out.println("The maximun number of files to use can only be one more than the number of runs");
				break;
			}
			*/
			int[] runsArray = calculateDistribution(numberOfRuns, numberOfFiles);
			for (int i : runsArray)
			{
				System.out.println(i);
			}

			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));

			FileWriter file = new FileWriter("result.txt");
			PrintWriter writer = new PrintWriter(file);

			File[] filesForTape = createFiles(numberOfFiles);
			PrintWriter[] tapes = createTapes(filesForTape);

			for (int i = 0; i < runsArray.length; i++)
			{
				int runsForFile = runsArray[i];
				int currentRunProcessed = 0;
				String line;

				while (currentRunProcessed < runsForFile)
				{
					if ((line = reader.readLine()) != null)
					{
						if (line.equals("-end of run"))
						{
							if (currentRunProcessed == runsForFile - 1)
							{
								tapes[i].print("-end of run");
								currentRunProcessed++;
							}
							else
							{
								tapes[i].println("-end of run");
								currentRunProcessed++;
							}
						}
						else
						{
							tapes[i].println(line);	
						}
					}
					else
					{
						if (currentRunProcessed == runsForFile - 1)
						{
							tapes[i].print("-end of run");
							currentRunProcessed++;
						}
						else
						{
							tapes[i].println("-end of run");
							currentRunProcessed++;
						}

					}
				}
				tapes[i].close();
			}

			//merge phase
			//merge(filesForTape, runsArray);
		}
		catch (Exception ex)
		{
			System.err.println("usage: <number of files> <number of runs> <file to read>");
			ex.printStackTrace();
		}
	}

	/*
	calculate a good distribution of the intial runs for the number of available files.
	return an array of distributed runs for each file to use.
	in each iteration, add the larget number in the array to each position except the one of 
	the postion representing the largest value, the value of this position becomes 0.
	dummy runs(run with no record in it) should be added if the initial number of runs is not 
	optimal
	*/
	private static int[] calculateDistribution(int numberOfRuns, int numberOfFiles)
	{
		int[] arrayRuns = new int[numberOfFiles];
		int largest = 0;

		//populate the array with one run for each index with 
		//exception of last index getting 0
		for (int i = 0; i < arrayRuns.length - 1; i++)
		{
			arrayRuns[i] = 1;
		}
		arrayRuns[arrayRuns.length - 1] = 0;

		//while the number of runs in the arrayRuns is less than the number of runs
		while (sumOfArray(arrayRuns) < numberOfRuns)
		{
			//find the largest value and its index
			int indexOfMax = getIndexOfMaxValue(arrayRuns);
			largest = arrayRuns[indexOfMax];

			//add the previous largest value to each value in the array except itself
			for (int i = 0; i < arrayRuns.length; i++)
			{
				if (i != indexOfMax)
				{
					arrayRuns[i] += largest;
				}
			}
			//set the value of the previous largest value to 0
			arrayRuns[indexOfMax] = 0;
		}
		return arrayRuns;
	}

	//return the sum of the values in the array
	private static int sumOfArray(int[] inputArray)
	{
		int result = 0;
		for (int i : inputArray)
		{
			result += i;
		}
		return result;
	}

	//return the index of the largest value in the array
	private static int getIndexOfMaxValue(int[] inputArray)
	{
		int maxIndex = 0;
		int maxValue = 0;
		for (int i = 0; i < inputArray.length; i++)
		{
			if (inputArray[i] > maxValue)
			{
				maxValue = inputArray[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private static File[] createFiles(int numberOfFiles) throws IOException
	{
		File[] fileArray = new File[numberOfFiles];
		for (int i = 0; i < numberOfFiles; i++)
		{
			fileArray[i] = new File("tape" + i +".txt");
		}
		return fileArray;
	}
	//create number of tapes based on the input
	private static PrintWriter[] createTapes(File[] inputArray) throws IOException
	{
		PrintWriter[] outputArray = new PrintWriter[inputArray.length];
		for (int i = 0; i < inputArray.length; i++)
		{
			outputArray[i] = new PrintWriter(new FileWriter(inputArray[i]));
		}

		return outputArray;
	}

	private static BufferedReader[] createReaders(File[] inputArray, int indexNotUsed) throws IOException
	{
		BufferedReader[] outputArray = new BufferedReader[inputArray.length];
		for (int i = 0; i < inputArray.length; i++)
		{
			if (i != indexNotUsed)
				outputArray[i] = new BufferedReader(new FileReader(inputArray[i]));
		}

		return outputArray;
	}
	/*
	//merge one run from each of the input files untill one input file is empty
	private static void merge(File[] inputFiles, int[] inputRuns) throws IOException
	{
		//create a MinHeap to store the smallest item in one run for all the files 
		//!!!!!!!
		MinHeap heap = new MinHeap(100000000);
		System.out.println("heap created");

		//find the file that is going to be the output file
		int indexOut = 0;
		for (int i = 0; i < inputRuns.length; i++)
		{
			if (inputRuns[i] == 0)
				indexOut = i;
		}
			//set the output file
		PrintWriter output = new PrintWriter(new FileWriter(inputFiles[indexOut]));
			//set the input file
		BufferedReader[] input = createReaders(inputFiles, indexOut);

		String line;
		String outputLine;
		List<Integer> indexToSkip = new ArrayList<Integer>();

		int totalRuns = sumOfArray(inputRuns);
		while (totalRuns > 1)
		{
			//loop through each the input file
			for (int i = 0; i < input.length; i++)
			{
				if (i != indexOut)
				{
					if (!indexToSkip.contains(i))
					{
						if ((line = input[i].readLine()) != null)
						{
							if (line.equals( "-end of run"))
							{
								indexToSkip.add(i);
								totalRuns--;
							}
							else
							{
								heap.insert(line);
								System.out.println("one added");
							}
						}

						//all the runs in one file is exhausted meaning on phase is completed
						//save the index and use file as output file
						//also make the output file available to be used as input
						else if (totalRuns > inputFiles.length - 1)
						{
							output.close();
							input[indexOut] = new BufferedReader(new FileReader(inputFiles[indexOut]));
							output = new PrintWriter(new FileWriter(inputFiles[i]));
							indexOut = i;
						}
					}	
				}
				if (indexToSkip.size() == inputFiles.length - 1)
				{
					indexToSkip.clear();
				}
			}
			outputLine = heap.remove();
			System.out.println("output one item");
			System.out.println(indexOut);
			output.println(outputLine);
		}
		output.close();
	}
	*/
}