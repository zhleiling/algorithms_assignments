import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 */

/**
 * @author zhanglei01
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] itemArray;
	private int size;

	/**
	 * 
	 */
	public RandomizedQueue() {
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}

		if (isEmpty()) {
			itemArray = (Item[]) new Object[1];
		} else if (itemArray.length == size) {
			Item[] newItemArray = (Item[]) new Object[2 * size];
			for (int i = 0; i < size; i++) {
				newItemArray[i] = itemArray[i];
			}
			itemArray = newItemArray;
		}

		itemArray[size++] = item;

	}

	public Item dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		int rIndex = StdRandom.uniform(size);
		Item oldItem = itemArray[rIndex];
		itemArray[rIndex] = itemArray[size - 1];
		itemArray[size - 1] = null;
		size--;
		return oldItem;
	}

	public Item sample() {
		int rIndex = StdRandom.uniform(size);
		return itemArray[rIndex];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<Item> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
