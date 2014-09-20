/**
 * @author zhanglei01
 * @time 2014-9-12 11:58:52
 */
public class PercolationStats {

    private double[] result;

    /**
     * perform T independent computational experiments on an N-by-N grid.
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("arguments illegal,N:" + N
                    + " T:" + T);
        }
        result = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            double t = 0;
            while (!p.percolates()) {
                int x = StdRandom.uniform(1, N + 1);
                int y = StdRandom.uniform(1, N + 1);
                if (!p.isOpen(x, y)) {
                    p.open(x, y);
                    t++;
                }
            }
            result[i] = t / (N * N);
        }
    }

    /**
     * test client
     * @param args
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats ps = new PercolationStats(N, T);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() + ", "
                + ps.confidenceHi());
        StdOut.println("elapsedTime: " + stopwatch.elapsedTime());
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(result);
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(result);
    }

    /**
     * @return returns lower bound of the 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(result.length);
    }

    /**
     * @return returns upper bound of the 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(result.length);
    }

}
