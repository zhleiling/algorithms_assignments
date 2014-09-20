import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A randomized queue is similar to a stack or queue, except that the item
 * removed is chosen uniformly at random from items in the data structure.
 * Create a generic data type RandomizedQueue.
 * @author zhanglei01
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    /** the item array to implement RandomizedQueue. */
    private Item[] itemArray;
    /** the number of actual exist items in RandomizedQueue. */
    private int size;
    /** The current length of the array. */
    private int N;

    /**
     * constructor,initial size to 0.
     */
    public RandomizedQueue() {
        size = 0;
    }

    /**
     * @return true if the randomize queue is empty, else false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the size of the randomize queue
     */
    public int size() {
        return size;
    }

    /**
     * add item to the randomize queue.
     * @param item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        // new a item[1] when array is empty; increase array total length by 2
        // times when array is full
        if (isEmpty()) {
            N = 1;
            itemArray = (Item[]) new Object[N];
        } else if (N == size) {
            N = 2 * size;
            Item[] newItemArray = (Item[]) new Object[N];
            for (int i = 0; i < size; i++) {
                newItemArray[i] = itemArray[i];
            }
            itemArray = newItemArray;
        }

        itemArray[size++] = item;

    }

    /**
     * remove a random item from the randomize queue.
     * @return the removed item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int rIndex = StdRandom.uniform(size);
        Item oldItem = itemArray[rIndex];
        itemArray[rIndex] = itemArray[size - 1];
        itemArray[size - 1] = null;
        size--;

        if (size <= N / 4 && size > 1) {
            N /= 2;
            Item[] newItemArray = (Item[]) new Object[N];
            for (int i = 0; i < size; i++) {
                newItemArray[i] = itemArray[i];
            }
            itemArray = newItemArray;
        }

        return oldItem;
    }

    /**
     * @return a random item in the randomize queue
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int rIndex = StdRandom.uniform(size);
        return itemArray[rIndex];
    }

    /**
     * main methods for unit tests.
     * @param args
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> rqueue = new RandomizedQueue<Integer>();
        rqueue.enqueue(1);
        rqueue.enqueue(1);
        rqueue.enqueue(3);
        rqueue.enqueue(2);
        System.out.println(rqueue.size());
        System.out.println(rqueue.dequeue());
        System.out.println(rqueue.dequeue());
        // System.out.println(rqueue.dequeue());
        // System.out.println(rqueue.dequeue());
        // System.out.println(rqueue.dequeue());
        //
        // rqueue.enqueue(1);
        // rqueue.enqueue(1);
        // rqueue.enqueue(3);
        // rqueue.enqueue(2);

        // for (Integer integer : rqueue) {
        // System.out.println(integer);
        // }
        //
        // for (Integer integer : rqueue) {
        // System.out.println(integer);
        // }

    }

    @Override
    /**
     * @return a iterator of  RandomizedQueue
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /**
     * RandomizedQueueIterator that implements Iterator.
     * @author zhanglei
     */
    private class RandomizedQueueIterator implements Iterator<Item> {

        /** the iteratorArray to hava a copy from the randomize queue. */
        private Item[] iteratorArray;
        /** the size of iteratorArray. */
        private int iteratorSize;

        /**
         * constructor, initial the iteratorArray by copying from the randomize
         * queue.
         */
        public RandomizedQueueIterator() {
            iteratorSize = size;
            iteratorArray = (Item[]) new Object[iteratorSize];
            for (int i = 0; i < iteratorSize; i++) {
                iteratorArray[i] = itemArray[i];
            }
            StdRandom.shuffle(iteratorArray);
        }

        @Override
        public boolean hasNext() {
            return iteratorSize != 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iteratorArray[--iteratorSize];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
