/**
 * @author zhanglei01
 * @time 2014-9-12 11:58:34
 */
public class Percolation {

    private WeightedQuickUnionUF uf;
    // avoid backwash import another union-find object,the only difference with uf is it has no virtual-bottom-site
    private WeightedQuickUnionUF ufNoBottom;
    private int dim;
    private boolean[][] grid;

    /**
     * create N-by-N grid, with all sites blocked
     * @param N
     */
    public Percolation(int N) {

        if (N <= 0) {
            throw new IllegalArgumentException("arguments illegal,N:" + N);
        }

        dim = N;
        grid = new boolean[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                grid[i][j] = false;
            }
        }

        // initial uf, contain N^2 + 2 elements, including 2 virtual sites,
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufNoBottom = new WeightedQuickUnionUF(N * N + 1);

    }

    /**
     * open site (row i, column j) if it is not already
     * 
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        // valid test
        if (!valid(i, j)) {
            throw new IndexOutOfBoundsException("index i,j out of bounds,i:"
                    + i + " j:" + j);
        }

        grid[i][j] = true;

        // if site in bottom or top ,union to top/bottom-virtual-site
        if (i == 1) {
            uf.union(xyTo1D(i, j), dim * dim);
            ufNoBottom.union(xyTo1D(i, j), dim * dim);
        }
        if (i == dim) {
            uf.union(xyTo1D(i, j), dim * dim + 1);
        }

        // up
        if (valid(i - 1, j) && isOpen(i - 1, j)) {
            uf.union(xyTo1D(i - 1, j), xyTo1D(i, j));
            ufNoBottom.union(xyTo1D(i - 1, j), xyTo1D(i, j));
        }
        // down
        if (valid(i + 1, j) && isOpen(i + 1, j)) {
            uf.union(xyTo1D(i + 1, j), xyTo1D(i, j));
            ufNoBottom.union(xyTo1D(i + 1, j), xyTo1D(i, j));
        }
        // left
        if (valid(i, j - 1) && isOpen(i, j - 1)) {
            uf.union(xyTo1D(i, j - 1), xyTo1D(i, j));
            ufNoBottom.union(xyTo1D(i, j - 1), xyTo1D(i, j));
        }
        // right
        if (valid(i, j + 1) && isOpen(i, j + 1)) {
            uf.union(xyTo1D(i, j + 1), xyTo1D(i, j));
            ufNoBottom.union(xyTo1D(i, j + 1), xyTo1D(i, j));
        }
    }

    /**
     * @param i
     * @param j
     * @return is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        if (!valid(i, j)) {
            throw new IndexOutOfBoundsException("index i,j out of bounds,i:"
                    + i + " j:" + j);
        }
        return grid[i][j];
    }

    /**
     * @param i
     * @param j
     * @return is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        if (!valid(i, j)) {
            throw new IndexOutOfBoundsException("index i,j out of bounds,i:"
                    + i + " j:" + j);
        }

        return isOpen(i, j) && ufNoBottom.connected(xyTo1D(i, j), dim * dim);
    }

    /**
     * @return does the system percolate? (if top connected to bottom)
     */
    public boolean percolates() {
        return uf.connected(dim * dim, dim * dim + 1);
    }

    /**
     * 2D to 1D
     * 
     * @param i
     * @param j
     * @return 1D int in UF array
     */
    private int xyTo1D(int i, int j) {
        if (!valid(i, j)) {
            throw new IndexOutOfBoundsException("index i,j out of bounds,i:"
                    + i + " j:" + j);
        }
        return (i - 1) * dim + j - 1;
    }

    /**
     * @param i
     *            row
     * @param j
     *            column
     * @return if i,j in index bound
     */
    private boolean valid(int i, int j) {
        return i <= dim && j <= dim && i > 0 && j > 0;
    }

}
