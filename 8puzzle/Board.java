import java.util.Iterator;

/**
 * 
 * @author zhanglei
 * 
 */
public class Board {
    private int N;
    private int[][] blocks;

    private Queue<Board> neighborQueue = new Queue<Board>();

    /**
     * constructor.
     * 
     * @param blocks
     */
    public Board(int[][] blocks) {
        N = blocks.length;
        this.blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.blocks[i][j] = blocks[i][j];
       
    }

    /**
     * @return board dimension N.
     */
    public int dimension() {
        return N;
    }

    /**
     * @return number of blocks out of place.
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1)
                    break;
                count += (blocks[i][j] == i * N + j + 1 ? 0 : 1);
            }

        return count;
    }

    /**
     * @return sum of Manhattan distances between blocks and goal.
     */
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int value = blocks[i][j];
                if (value == 0)
                    continue;
                int x = (value - 1) / N;
                int y = (value - 1) % N;
                int dx = Math.abs(x - i);
                int dy = Math.abs(y - j);
                count += dx + dy;
            }

        return count;
    }

    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * @return a board that is obtained by exchanging two adjacent blocks in the
     *         same row
     */
    public Board twin() {
        int[][] twinBlock = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                twinBlock[i][j] = blocks[i][j];

        if (twinBlock[0][0] != 0 && twinBlock[0][1] != 0) {
            int temp = twinBlock[0][0];
            twinBlock[0][0] = twinBlock[0][1];
            twinBlock[0][1] = temp;
        } else {
            int temp = twinBlock[1][0];
            twinBlock[1][0] = twinBlock[1][1];
            twinBlock[1][1] = temp;
        }
        return new Board(twinBlock);
    }

    /**
     * does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == null)
            return false;

        if (this == y)
            return true;

        if (this.getClass() != y.getClass())
            return false;

        Board yBoard = (Board) y;
        if (yBoard.N != N)
            return false;
        int[][] yMatrix = yBoard.blocks;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != yMatrix[i][j])
                    return false;
            }
        }

        return true;
    }

    /**
     * all neighboring boards.
     * 
     * @return
     */
    public Iterable<Board> neighbors() {
        getNeighbors();
        return new Iterable<Board>() {

            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {

                    @Override
                    public void remove() {
                    }

                    @Override
                    public Board next() {
                        return neighborQueue.dequeue();
                    }

                    @Override
                    public boolean hasNext() {
                        return !neighborQueue.isEmpty();
                    }
                };
            }
        };
    }

    private void getNeighbors() {
        int[][] neighbor = new int[N][N];
        int x = 0;
        int y = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                neighbor[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    x = i;
                    y = j;
                }

            }
        }

        if (x > 0) {
            swap(neighbor, x, y, x - 1, y);
            neighborQueue.enqueue(new Board(neighbor));
            swap(neighbor, x, y, x - 1, y);
        }

        if (x < N - 1) {
            swap(neighbor, x, y, x + 1, y);
            neighborQueue.enqueue(new Board(neighbor));
            swap(neighbor, x, y, x + 1, y);
        }
        
        if (y > 0) {
            swap(neighbor, x, y, x, y - 1);
            neighborQueue.enqueue(new Board(neighbor));
            swap(neighbor, x, y, x, y - 1);
        }
        
        if (y < N - 1) {
            swap(neighbor, x, y, x, y + 1);
            neighborQueue.enqueue(new Board(neighbor));
            swap(neighbor, x, y, x, y + 1);
        }

    }

    private void swap(int[][] block, int x1, int y1, int x2, int y2) {
        int temp = block[x1][y1];
        block[x1][y1] = block[x2][y2];
        block[x2][y2] = temp;
    }

    /**
     * string representation of this board (in the output format specified
     * below)
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(N + "\n");

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(String.format("%2d", blocks[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * main for unit tests
     * 
     * @param args
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // test
        StdOut.println(initial.dimension());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        StdOut.println(initial.toString());
        StdOut.println(initial.twin().toString());

    }

}
