import java.util.Arrays;

public class Fast {

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

		//TODO
	}
}
