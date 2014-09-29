import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Fast {

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

		Arrays.sort(pointArray);

		for (int i = 0; i < N; i++) {
			Point p = pointArray[i];
			Point[] tempArray = new Point[N];
			for (int j = 0; j < N; j++) {
				tempArray[j] = pointArray[j];
			}

			Arrays.sort(tempArray, p.SLOPE_ORDER);

			double savedSlope = -0.0;
			int count = 1;
			int savedCount = 0;
			for (int k = 1; k < N; k++) {
				double slope = p.slopeTo(tempArray[k]);

				if (slope == savedSlope) {
					count++;
				} else {
					savedCount = count;
					count = 1;
					savedSlope = slope;
				}

				if (savedCount >= 3 || (k == N - 1 && count >= 3)) {
					int lineLength = savedCount >= 3 ? savedCount : count;
					Point[] linerArray = new Point[lineLength + 1];

					linerArray[0] = p;
					for (int l = 1; l <= lineLength; l++) {
						linerArray[l] = tempArray[k - l];
					}
					if (k == N - 1) {
						linerArray[lineLength - 1] = tempArray[k];
					}

					Arrays.sort(linerArray);

					String outString = linerArray[0].toString();
					for (int l = 1; l < linerArray.length; l++) {
						outString += " -> ";
						outString += linerArray[l].toString();
					}
					if (linerSet.contains(outString)) {
						savedCount = 0;
						continue;
					} else {
						linerSet.add(outString);
					}

					for (int l = 1; l < linerArray.length; l++) {
						linerArray[l].drawTo(linerArray[l - 1]);
					}
					StdOut.println(outString);
					savedCount = 0;
				}

			}
		}

	}
}
