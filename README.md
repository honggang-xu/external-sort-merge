# external-sort-merge
External sort merge program to sort lines of text into ascending order based on the judgement of Java String compareTo() method.


MinHeap maintains the smallest item as the root and root of every subheap is the smallest of all in that subheap.

MakeRuns accepts standard inputs as the size of the MinHeap to use and the file to process.
Then it creates initial runs using the replacement selection strategy.
These runs are written to a single temporary file. It also output the number of runs.
* To use MakeRuns, type command:

  Java MakeRuns \<size of the heap> \<file to read>

PolyMerge accepts a single integer argument specifying how many temporary files to use for a polyphase sort merge
(Note: the number of temporary files to use must be bigger than or equal to 3).
It also obtains the number of runs produced by MakeRuns,
then calculates a good distribution of the intial runs for the number of available files. 
It then distribute the runs accordingly and execute a polyphase sort merge, 
producing as output the final sorted input.
* To use PolyMerge, type command:

  Java PolyMerge \<number of files> \<number of runs> \<file to read>
