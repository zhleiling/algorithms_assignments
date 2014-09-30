import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Fast {

    private static final int MIN_LINE_LENGTH = 3;

    public static void main(String[] args) {

        Set<String> linerSet = new HashSet<String>();

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        // read in points
        String fileName = args[0];
        In in = new In(fileName);
        int N = in.readInt();
        Point[] pointArray = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pointArray[i] = new Point(x, y);
            pointArray[i].draw();
        }

        // Arrays.sort(pointArray);
        Point[] tempArray = new Point[N];
        for (int j = 0; j < N; j++) {
            tempArray[j] = pointArray[j];
        }

        for (int i = 0; i < N; i++) {
            Point p = pointArray[i];

            Arrays.sort(tempArray, p.SLOPE_ORDER);

            double savedSlope = -0.0;
            int count = 0;
            int savedCount = 0;
            point_loop: for (int k = 1; k < N; k++) {
                double slope = p.slopeTo(tempArray[k]);

                if (slope == savedSlope) {
                    count++;
                } else {
                    savedCount = count;
                    count = 1;
                    savedSlope = slope;
                }

                if (savedCount >= MIN_LINE_LENGTH
                        || (k == N - 1 && count >= MIN_LINE_LENGTH)) {

                    int lineLength = 0;
                    if (savedCount >= MIN_LINE_LENGTH) {
                        lineLength = savedCount;
                    } else {
                        lineLength = count;
                    }
                    Point[] linerArray = new Point[lineLength + 1];

                    linerArray[0] = p;
                    if (savedCount >= MIN_LINE_LENGTH) {
                        for (int l = 1; l <= lineLength; l++) {
                            linerArray[l] = tempArray[k - l];
                            if (linerSet.contains(linerArray[l].toString())) {
                                savedCount = 0;
                                continue point_loop;
                            }
                        }
                    } else {
                        for (int l = 1; l <= lineLength; l++) {
                            linerArray[l] = tempArray[k - l + 1];
                            if (linerSet.contains(linerArray[l].toString())) {
                                savedCount = 0;
                                continue point_loop;
                            }
                        }
                    }

                    Arrays.sort(linerArray);

                    String outString = linerArray[0].toString();
                    for (int l = 1; l < linerArray.length; l++) {
                        outString += " -> ";
                        outString += linerArray[l].toString();
                    }

                    linerArray[0].drawTo(linerArray[linerArray.length - 1]);
                    StdOut.println(outString);
                    savedCount = 0;
                }
            }
            linerSet.add(p.toString());
        }

    }
}
