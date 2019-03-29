//Salim Al Farsi (1258330)
//Honggang Xu (1369387)

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*
PolyMerge accepts a single integer argument specifying how many temporary files to use for a polyphase sort merge.
It also obtains the number of runs produced by MakeRuns, 
then calculates a good distribution of the intial runs for the number of available files. 
It then distribute the runs accordingly and execute a polyphase sort merge, 
producing as output the final sorted input
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
			
			//the number of files to use have to be bigger than or equal to 3
			if(numberOfFiles < 3)
			{
				System.err.println("The minium number of files to use have to be bigger than or equal to 3");
				return;
			}

			//calculate the initial distribution of runs and store them in an array
			int[] runsArray = calculateDistribution(numberOfRuns, numberOfFiles);

			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));

			//create temporary files/tapes to use
			File[] filesForTape = createFiles(numberOfFiles);
			PrintWriter[] tapes = createTapes(filesForTape);

			//write the runs to the file
			for (int i = 0; i < runsArray.length; i++)
			{
				//get the runs for this file
				int runsForFile = runsArray[i];
				int currentRunProcessed = 0;
				String line;

				//while the runs to put in this file is not finished
				while (currentRunProcessed < runsForFile)
				{
					//if line is not null
					if ((line = reader.readLine()) != null)
					{
						if (line.equals("-end of run"))
						{
							//if this run is the last run for the file
							if (currentRunProcessed == runsForFile - 1)
							{
								//print end of run and don't print new line
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
					//if the line is null
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
			merge(filesForTape, runsArray);
		}
		catch (Exception ex)
		{
			System.err.println("Usage: Java PolyMerge <number of files> <number of runs> <file to read>");
			ex.printStackTrace();
		}
	}

	/*
	Calculate a good distribution of the intial runs for the number of available files.
	Return an array of distributed runs for each file to use.
	In each iteration, add the larget number in the array to each position except the one of 
	the postions representing the largest value, the value of this position becomes 0.
	Dummy runs(run with no record in it) should be added if the initial number of runs is not 
	optimal
	*/
	private static int[] calculateDistribution(int numberOfRuns, int numberOfFiles)
	{
		int[] arrayRuns = new int[numberOfFiles];
		int largest = 0;

		//populate the array with one run for each index
		//with exception of last index getting 0
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

	//cretate files/tapes to use based on the number of files to use
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

	//create an array of reader to use, exclude the index passed by the parameter
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
	For each phase, merge one run from each of the input files to the output file untill one input file is empty.
	Then open the output file as input file and make the file whose runs is exhausted as the out put file
	*/
	private static void merge(File[] inputFiles, int[] inputRuns) throws IOException
	{
		//create a MinHeap to store the smallest item in one run for all the files 
		MinHeap heap = new MinHeap(inputFiles.length - 1);

		//count the number of times a file is opend for output after initial distribution
		int count = 0;
		//find the file that is going to be the output file
		int indexOut = 0;
		for (int i = 0; i < inputRuns.length; i++)
		{
			if (inputRuns[i] == 0)
				indexOut = i;
		}
		//set the output file
		PrintWriter output = new PrintWriter(new FileWriter(inputFiles[indexOut]));
		count++;
		//set the input file
		BufferedReader[] input = createReaders(inputFiles, indexOut);

		String line;
		String outputLine = null;
		//set teh fileToUse to be the number of files that are readers
		int fileToUse = inputFiles.length - 1;

		List<Integer> indexToSkip = new ArrayList<Integer>();

		//boolean value as flag
		boolean finalphase = false;
		boolean finished = false;

		//total runs remaining
		int totalRuns = sumOfArray(inputRuns);

		//an array to hold the value of last output for each file
		String[] miniumIndex = new String[inputFiles.length];
		
		//if the total runs remaining is equal to the number of files to read
		//the the next phase is the final phase
		if (totalRuns == inputFiles.length - 1)
			finalphase = true;

		//while not finished
		while (!finished)
		{
			int runsForPhase = totalRuns;

			//find the index of the output file and set it to indexOut
			for (int i = 0; i < inputRuns.length; i++)
			{
				if (inputRuns[i] == 0)
					indexOut = i;
			}

			//find the minium runs for this phase and set it to runsForPhase
			for (int i = 0; i < inputRuns.length; i++)
			{
				if (i != indexOut)
				{
					if (inputRuns[i] < runsForPhase)
					{
						runsForPhase = inputRuns[i];
					}
				}
			}

			//runsRead to count the runs that have been processed
			int runsRead = 0;

			//for each phase
			while (runsRead < runsForPhase * (inputFiles.length - 1))
			{
				//if the heap's capacity is less than the file to use
				if (heap.capacity() < fileToUse)
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
									//if the line is not the end of run
									if (!line.equals("-end of run"))
									{
										//insert the line into the heap
										heap.insert(line);
										//set the string as the last output from the file
										miniumIndex[i] = line;
									}
									else
									{
										//add the index of the file in the array to the array for skipping
										indexToSkip.add(i);
										runsRead++;
										fileToUse--;
									}
								}
							}	
						}
					}
					//store the root of the heap to outputLine
					outputLine = heap.peek();
				}
				else
				{
					int indexOfMin = 0;
					//find the index of the file that the last output belongs to
					for (int i = 0; i < miniumIndex.length; i++)
					{
						if (miniumIndex[i] == outputLine)
						{
							indexOfMin = i;
						}
					}
					//read one line from the file
					line = input[indexOfMin].readLine();

					if (line != null)
					{
						if (!line.equals("-end of run"))
						{
							outputLine = heap.replace(line);
							miniumIndex[indexOfMin] = line;
							output.println(outputLine);
							outputLine = heap.peek();
						}
						else
						{
							indexToSkip.add(indexOfMin);
							runsRead++;
							miniumIndex[indexOfMin] = null;
							outputLine = heap.remove();
							output.println(outputLine);
							fileToUse--;
							outputLine = heap.peek();
						}
					}
					//if the files to skip is equal to the files to read
					//meaning one run has been processed for each files
					if (indexToSkip.size() == inputFiles.length - 1)
					{
						fileToUse = inputFiles.length - 1;
						indexToSkip.clear();
						miniumIndex = new String[inputFiles.length];
						if (!finalphase)
							output.println("-end of run");
					}	
				}
			}

			//if the runs processed for this phase is over
			if (runsRead == runsForPhase * (inputFiles.length - 1))
			{
				//update the runs for the array that stores runs for files
				for (int i = 0; i < inputRuns.length; i++)
				{
					if (inputRuns[i] == 0)
					{
						inputRuns[i] = runsForPhase;
					}
					else
					{
						inputRuns[i] -= runsForPhase;
					}
				}

				//empty the heap
				while (heap.capacity() != 0)
				{
					output.println(heap.remove());
				}

				miniumIndex = new String[inputFiles.length];
				indexToSkip.clear();
				//if this phase is not the final phase
				if (!finalphase)
				{
					output.println("-end of run");
					output.close();
					//set output file as input
					input[indexOut] = new BufferedReader(new FileReader(inputFiles[indexOut]));

					for (int i = 0; i < inputRuns.length; i++)
					{
						if (inputRuns[i] == 0)
							indexOut = i;
					}
					//set the input file whose runs are exhausted as output file
					output = new PrintWriter(new FileWriter(inputFiles[indexOut]));
					count++;
					totalRuns = sumOfArray(inputRuns);
				}

				//if this phase is the final phase
				else
				{
					output.close();

					//set the output file as reader
					BufferedReader readerFinal = new BufferedReader(new FileReader(inputFiles[indexOut]));
					String lineFinal;
					
					//print the sorted file to the standard output
					while ((lineFinal = readerFinal.readLine()) != null)
					{
						System.out.println(lineFinal);
					}
					System.err.println("Number of times any file is opened for output: " + count);
					
					//destroy all the tapes
					for (File file : inputFiles)
					{
						file.delete();
					}
					totalRuns = sumOfArray(inputRuns);
				}
				
				if (totalRuns == inputFiles.length - 1)
				{
					finalphase = true;
				}
				
				if (totalRuns == 1)
				{
					finished = true;
				}
				
				line = null;
			}
		}
	}
}