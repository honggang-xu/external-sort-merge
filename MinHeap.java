

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
			//upheap
			swap(child, parent);
			child = parent;
			parent = (child - 1) / 2;
		}

	}

	//remove and return the root element, put the last element in root and put the heap in order
	public String remove()
	{
		String result = null;
		if (pointer > 0)
		{
			if (pointer == 1)
			{
				pointer--;
				result = data[pointer];
			}
			else
			{
				String oldRoot = data[0];
				String oldLast = data[pointer - 1];
				data[0] = oldLast;
				data[pointer - 1] = oldRoot;
				pointer--;
				result = data[pointer];
				int parent = 0;
				//left child
				int childLeft = parent * 2 + 1;

				while (childLeft < pointer)
				{
					//right child
					int childRight = parent * 2 + 2;
					//assume the mininum of the two children is left child
					int childMin = childLeft;
					if (childRight < pointer)
					{
						if (smallerThan(childRight, childLeft))
						{
							childMin = childRight;
						}
					}

					if (smallerThan(childMin, parent))
					{
						//downheap
						swap(childMin, parent);
						parent = childMin;
						childLeft = parent * 2 + 1;
					}
				}	
			}
		}
		return result;
	}

	public void replace(String input)
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
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
	}

}