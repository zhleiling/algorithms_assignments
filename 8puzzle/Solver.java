import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author zhanglei
 * 
 */
public class Solver {
    private Board search;
    private int moves;

    private Board twinSearch;
    private int twinMoves;

    private MinPQ<PriorityBoard> queue;
    private MinPQ<PriorityBoard> twinQueue;

    private boolean solveable;

    private Queue<Board> solutionQueue = new Queue<Board>();

    /**
     * constructor,to find a solution to the initial board (using the A*
     * algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {
        this.search = initial;
        twinMoves = moves = 0;
        queue = new MinPQ(new BoardComparator<PriorityBoard>());
        twinQueue = new MinPQ(new BoardComparator<PriorityBoard>());
        queue.insert(new PriorityBoard(search, 0));
        twinSearch = search.twin();
        twinQueue.insert(new PriorityBoard(twinSearch, twinMoves));
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
    }

    private class PriorityBoard {
        public PriorityBoard(Board board, int moves) {
            this.board = board;
            this.moves = moves;
        }

        private Board board;
        private int moves;
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
        return solveable;

    }

    private class DetectThread extends Thread {
        @Override
        public void run() {
            Set<Board> previousSet = new HashSet<Board>();
            while (!search.isGoal() && !isInterrupted()) {
                // StdOut.println("Self:"+Thread.currentThread() + " : " +
                // search);
                previousSet.add(search);
                PriorityBoard pb = queue.delMin();
                search = pb.board;
                for (Board b : search.neighbors()) {
                    if (!previousSet.contains(b)) {
                        queue.insert(new PriorityBoard(b, pb.moves + 1));
                    }
                }
                solutionQueue.enqueue(search);
                moves = pb.moves;
            }
        }
    }

    private class DetectTwinThread extends Thread {
        @Override
        public void run() {
            Set<Board> previousSet = new HashSet<Board>();
            while (!twinSearch.isGoal() && !isInterrupted()) {
//                StdOut.println("Twin:" + Thread.currentThread() + " : "
//                        + twinSearch);
                previousSet.add(twinSearch);
                PriorityBoard pb = twinQueue.delMin();
                twinSearch = pb.board;
                for (Board b : twinSearch.neighbors()) {
                    if (!previousSet.contains(b)) {
                        twinQueue.insert(new PriorityBoard(b, pb.moves + 1));
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
        if (isSolvable()) {
            return moves;
        } else {
            return -1;
        }
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if(!solveable){
            return null;
        }else{
            return new solveIterable<Board>();
        }
    }

    private class solveIterable<E> implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new Iterator<Board>() {

                @Override
                public boolean hasNext() {
                    return !solutionQueue.isEmpty();
                }

                @Override
                public Board next() {
                    return solutionQueue.dequeue();
                }

                @Override
                public void remove() {
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
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
