import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue or deque (pronounced "deck") is a generalization of a
 * stack and a queue that supports inserting and removing items from either the
 * front or the back of the data structure. Create a generic data type Deque.
 * @author zhanglei01
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /** first point to the virtual head node. */
    private Node first;
    /** last point to the virtual head node. */
    private Node last;
    /** the number of nodes in the deque. */
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
    /**
     * @return true if the randomize queue is empty, else false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return current size of the deque
     */
    public int size() {
        return size;
    }

    /**
     * insert item from the front of deque.
     * @param item
     */
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

    /**
     * insert item from the back of deque.
     * @param item
     */
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

    /**
     * remove one item from the front of deque.
     * @return the removed item
     */
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

    /**
     * remove one item from the back of deque.
     * @return the removed item
     */
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


    @Override
    /** return a iterator of deque.   */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    /**
     * DequeIterator thats implements Iterator for deque.
     * @author zhanglei
     * @param <Item>
     */
    private class DequeIterator<Item> implements Iterator<Item> {
        /** the cursor of the iterator. */
        private Node cursor;
        /**
         * constructor,point cursor to the front of deque.
         */
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
            cursor = cursor.next;
            return (Item) cursor.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * main methods for unit tests.
     * @param args
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.addFirst(1);
        deque.addFirst(3);
        deque.addLast(2);
        // System.out.println(deque.size());
        // System.out.println(deque.removeFirst());
        // System.out.println(deque.removeLast());
        // System.out.println(deque.removeLast());
        // System.out.println(deque.removeLast());
        // System.out.println(deque.removeLast());

        // deque.addFirst(1);
        // deque.addFirst(1);
        // deque.addFirst(3);
        // deque.addLast(2);

        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        Iterator<Integer> iterator2 = deque.iterator();
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }
    }

    /**
     * node has two pointers.
     * @author zhanglei
     *
     */
    private class Node {
        /** the value of node. */
        private Item value;
        /** the pointer to next node. */
        private Node next;
        /** the pointer to previous node. */
        private Node prev;
    }
}
