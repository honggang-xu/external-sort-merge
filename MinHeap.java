

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

		//try to upheap if the condition is true
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

				//try to downheap if the condition is true
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
					break;
				}	
			}
		}
		return result;
	}

	//output item at the root and insert new item at the root and downheap
	public String replace(String input)
	{
		String result = null;
		if (pointer != 0)
		{
			result = data[0];
			data[0] = input;

			int parent = 0;
			//left child
			int childLeft = parent * 2 + 1;

			//try to downheap if the condition is true
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
				break;
			}
		}
		return result;
	}

	//heap sort the items in the heap that is of a specified size
	public void reheap(int size) {
		//the first possible parent node is the middle point

		int middle = size / 2 - 1;

		while (middle >= 0)
		{
			int parent = middle;
			

			//left child
			int childLeft = parent * 2 + 1;
			
			//try to downheap if the condition is true
			while (childLeft < size)
			{
				

				//right child
				int childRight = parent * 2 + 2;
				

				//assume the mininum of the two children is left child
				int childMin = childLeft;
				

				if (childRight < size)
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
				break;
			}
			middle--;
			
		}
	}

	//use compareTo() method from String to compare
	public boolean smallerThan(int a, int b)
	{
		if (data[a].compareTo(data[b]) < 0)
			return true;
		return false;
	}

	//use compareTo() method from String to compare
	public boolean smallerThan(String a, String b)
	{
		if (a.compareTo(b) < 0)
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

	//return the root item
	public String peek()
	{
		String result = null;
		if (pointer > 0)
		{
			return data[0];
		}
		return result;
	}

	//return the current capacity of the heap
	public int capacity()
	{
		return pointer;
	}

	//set capacity of the heap
	public void setCapacity(int size)
	{
		pointer = size;
	}

	//get the String at specified position
	public String get(int position)
	{
		return data[position];
	}

	///////////////////a test program for MinHeap///////////////////

	
	public static void main(String [] args)
	{
		MinHeap heapT = new MinHeap(5);

		System.out.println("--- insert 5 items into the heap ---");
		heapT.insert("bbdsf");
		heapT.insert("yzdd");
		heapT.insert("aadd");
		heapT.insert("ccccdd");
		heapT.insert("zzzzz");

		System.out.println("--- display all the items in the heap ---");
		for (int i = 0; i < 5; i++)
			System.out.println(heapT.get(i));

		System.out.println("--- replace the root item in the heap and out put the old root ---");
		System.out.println(heapT.replace("ffffdd"));

		System.out.println("--- test if root is less than the last item ---");
		System.out.println(heapT.smallerThan(heapT.peek(), heapT.get(4)));

		System.out.println("--- remove all the items one by one ---");
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());
		System.out.println(heapT.remove());

		System.out.println("--- display the items in the heap(they should not be in ascending order) ---");
		for (int i = 0; i < 5; i++)
			System.out.println(heapT.get(i));

		System.out.println("--- reheap and display all the items ---");
		heapT.reheap(5);
		for (int i = 0; i < 5; i++)
			System.out.println(heapT.get(i));

	}

}