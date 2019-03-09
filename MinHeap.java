

public class MinHeap
{
	//array to represent the MinHeap
	private String [] data;
	//the index for the available spot
	private int pointer;

	//constructor that initialize the size of the array and set pointer to 0
	public MinHeap(int size)
	{
		data = new String [size];
		pointer = 0;
	}

	//put String in the next available spot.
	//while the String is smaller than its parent and not at root,
	//swap with parent
	public void insert(String input)
	{
		int parent = (pointer - 1) / 2;
		int child = pointer;

		if (pointer < data.length)
		{
			data[pointer] = input;
			pointer++;
		}

		while (child != 0 && smallerThan(child, parent))
		{
			swap(child, parent);
			child = parent;
			parent = (child - 1) / 2;
		}

	}

	public void remove()
	{

	}

	public void replace(String input)
	{

	}

	public void downHeap()
	{

	}

	public void upHeap()
	{

	}
	//use compareTo() method from String to compare
	public boolean smallerThan(int a, int b)
	{
		if (data[a].compareTo(data[b]) < 0)
			return true;
		return false;
	}

	//swap the items between position a and b
	public void swap(int a, int b)
	{
		String tempA = data[a];
		String tempB = data[b];
		data[a] = tempB;
		data[b] = tempA;
	}

	///////////////////a test program for MinHeap///////////////////

	public String get(int position)
	{
		return data[position];
	}
	public static void main(String [] args)
	{
		MinHeap heapT = new MinHeap(5);
		heapT.insert("bbdsf");
		heapT.insert("yzdd");
		heapT.insert("aadd");
		heapT.insert("ccccdd");
		heapT.insert("zzzzz");
		for (int i = 0; i < 5; i++)
		System.out.println(heapT.get(i));
	}

}