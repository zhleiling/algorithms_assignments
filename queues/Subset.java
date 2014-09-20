public class Subset {
    /**
     * a client program Subset.java that takes a command-line integer k; reads
     * in a sequence of N strings from standard input using StdIn.readString();
     * and prints out exactly k of them, uniformly at random
     * @param args the number to print
     */
    public static void main(String[] args) {
        if (args[0] == null) {
            throw new IllegalArgumentException("input number cant't be null");
        }
        int number = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String readString = StdIn.readString();
            queue.enqueue(readString);
        }
        for (int i = 0; i < number; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
