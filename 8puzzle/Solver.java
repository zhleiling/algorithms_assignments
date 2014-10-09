import java.util.Comparator;
import java.util.Iterator;

/**
 * 
 * @author zhanglei
 * 
 */
public class Solver {
    private PriorityBoard searchNode;
    private int moves;

    private PriorityBoard twinSearchNode;

    private MinPQ<PriorityBoard> queue;
    private MinPQ<PriorityBoard> twinQueue;

    private boolean hasProcessed;
    private boolean solveable;
    private Stack<Board> solutionStack = new Stack<Board>();

    /**
     * constructor,to find a solution to the initial board (using the A*
     * algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {
        queue = new MinPQ(new BoardComparator<PriorityBoard>());
        twinQueue = new MinPQ(new BoardComparator<PriorityBoard>());
        this.searchNode = new PriorityBoard(initial, 0, null);
        twinSearchNode = new PriorityBoard(initial.twin(), 0, null);
        moves = 0;
        hasProcessed = false;
    }

    private void process() {
        if (hasProcessed) {
            return;
        }
        queue.insert(searchNode);
        twinQueue.insert(twinSearchNode);
        Thread detectThread = new DetectThread();
        Thread detectTwinThread = new DetectTwinThread();

        detectThread.start();
        detectTwinThread.start();

        while (detectThread.isAlive() && detectTwinThread.isAlive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (detectThread.isAlive()) {
            detectThread.interrupt();
            solveable = false;
        } else {
            detectTwinThread.interrupt();
            solveable = true;
        }
        hasProcessed = true;
    }

    /**
     * PriorityBoard, a Board with priority and previous pointer.
     * 
     * @author zhanglei01
     */
    private class PriorityBoard {
        private Board board;
        private int moves;
        private PriorityBoard previous;

        public PriorityBoard(Board board, int moves, PriorityBoard previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }

    private class BoardComparator<E> implements Comparator<PriorityBoard> {
        public int compare(PriorityBoard o1, PriorityBoard o2) {
            int p1 = o1.moves + o1.board.manhattan();
            int p2 = o2.moves + o2.board.manhattan();
            if (p1 < p2) {
                return -1;
            } else if (p1 > p2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * @return is the initial board solvable?
     */
    public boolean isSolvable() {
        if (!hasProcessed) {
            process();
        }
        return solveable;
    }

    private class DetectThread extends Thread {
        @Override
        public void run() {
            while (!searchNode.board.isGoal() && !isInterrupted()) {
                searchNode = queue.delMin();
                for (Board b : searchNode.board.neighbors()) {
                    if (searchNode.previous == null
                            || !b.equals(searchNode.previous.board)) {
                        queue.insert(new PriorityBoard(b, searchNode.moves + 1,
                                searchNode));
                    }
                }
                moves = searchNode.moves;
            }
        }
    }

    private class DetectTwinThread extends Thread {
        @Override
        public void run() {
            while (!twinSearchNode.board.isGoal() && !isInterrupted()) {
                twinSearchNode = twinQueue.delMin();
                for (Board b : twinSearchNode.board.neighbors()) {
                    if (twinSearchNode.previous == null
                            || !b.equals(twinSearchNode.previous.board)) {
                        twinQueue.insert(new PriorityBoard(b,
                                twinSearchNode.moves + 1, twinSearchNode));
                    }
                }
            }
        }
    }

    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @return
     */
    public int moves() {
        if (!hasProcessed) {
            process();
        }
        if (solveable) {
            return moves;
        } else {
            return -1;
        }
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!hasProcessed) {
            process();
        }
        if (!solveable) {
            return null;
        } else {
            solutionStack = new Stack<Board>();
            PriorityBoard currNode = new PriorityBoard(searchNode.board,
                    searchNode.moves, searchNode.previous);
            do {
                solutionStack.push(currNode.board);
                currNode = currNode.previous;
            } while (currNode != null);
            return new Iterable<Board>() {
                @Override
                public Iterator<Board> iterator() {
                    return solutionStack.iterator();
                }
            };
        }
    }

    /**
     * solve a slider puzzle (given below)
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(solver.moves());
                StdOut.println(board);
            }

        }
    }
}
