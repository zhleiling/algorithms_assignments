import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author zhanglei01
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

	private Node first;
	private Node last;
	private int size;

	/**
	 * Deque constructor, initialize member variables.
	 */
	public Deque() {
		first = new Node();
		last = new Node();
		first.next = last;
		last.prev = first;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void addFirst(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		Node newNode = new Node();
		newNode.value = item;
		Node oldSecFirst = first.next;
		newNode.next = oldSecFirst;
		oldSecFirst.prev = newNode;
		newNode.prev = first;
		first.next = newNode;
		size++;
	}

	public void addLast(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		Node newNode = new Node();
		newNode.value = item;
		Node oldSecLast = last.prev;
		newNode.prev = oldSecLast;
		oldSecLast.next = newNode;
		newNode.next = last;
		last.prev = newNode;
		size++;
	}

	public Item removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Node oldSecFirst = first.next;
		Item value = oldSecFirst.value;
		Node newSecFirst = oldSecFirst.next;
		oldSecFirst = null;
		first.next = newSecFirst;
		newSecFirst.prev = first;
		size--;
		return value;
	}

	public Item removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Node oldSecLast = last.prev;
		Item value = oldSecLast.value;
		Node newSecLast = oldSecLast.prev;
		oldSecLast = null;
		last.prev = newSecLast;
		newSecLast.next = last;
		size--;
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator<Item> iterator() {
		@SuppressWarnings("hiding")
		class DequeIterator<Item> implements Iterator<Item> {
			private Node cursor;

			public DequeIterator() {
				cursor = first;
			}

			@Override
			public boolean hasNext() {
				return cursor.next != last;
			}

			@Override
			public Item next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				Node node = cursor.next;
				cursor.next = node.next;
				return (Item) node.value;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		}
		return new DequeIterator();
	}

	/**
	 * @param args input parameters.
	 */
	public static void main(String[] args) {
		Deque<Integer> deque = new Deque<Integer>();
		deque.addFirst(1);
		deque.addFirst(1);
		deque.addFirst(3);
		deque.addLast(2);
		System.out.println(deque.size());
		System.out.println(deque.removeFirst());
		System.out.println(deque.removeLast());
		System.out.println(deque.removeLast());
		System.out.println(deque.removeLast());
		// System.out.println(deque.removeLast());

		deque.addFirst(1);
		deque.addFirst(1);
		deque.addFirst(3);
		deque.addLast(2);

//		Iterator<Integer> iterator = deque.iterator();
//		while (iterator.hasNext()) {
//			System.out.println(iterator.next());
//		}
//		
		for (Integer integer : deque) {
			System.out.println(integer);
		}
	}

	private class Node {
		Item value;
		Node next;
		Node prev;
	}
}
