import java.util.Arrays;

public class Brute {

	public static void main(String[] args) {
		StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
		
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

		for (int i = 0; i < pointArray.length; i++)
			for (int j = i + 1; j < pointArray.length; j++)
				for (int k = j + 1; k < pointArray.length; k++)
					for (int l = k + 1; l < pointArray.length; l++) {
						Point p1 = pointArray[i];
						Point p2 = pointArray[j];
						Point p3 = pointArray[k];
						Point p4 = pointArray[l];
						if (p1.slopeTo(p2) == p1.slopeTo(p3)
								&& p1.slopeTo(p2) == p1.slopeTo(p4)) {
							StdOut.println(p1 + " -> " + p2 + " -> " + p3
									+ " -> " + p4);
							p1.drawTo(p2);
							p2.drawTo(p3);
							p3.drawTo(p4);
						}
					}
	}
}
