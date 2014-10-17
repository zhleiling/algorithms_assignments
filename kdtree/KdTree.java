import java.util.Iterator;

/** 
 * Project Name : algorithms_assignments 
 * File Name : KdTree.java 
 * Package Name : algorithms_assignments 
 * Date : Oct 17, 2014 4:22:38 PM 
 * Copyright (c) 2014, zhanglei083@gmail.com All Rights Reserved. 
 */

/**
 * ClassName : KdTree <br/>
 * Description : TODO ADD FUNCTION. <br/>
 * date: Oct 17, 2014 4:22:38 PM <br/>
 * 
 * @author zhanglei01
 * @version
 * @since JDK 1.6
 */
public class KdTree {
	private BST<Point2D, Node> kdTree;
	private Node root;
	
	private static class Node{
		private Point2D p;		// the point
		private RectHV rectHV;	// the axis-aligned rectangle corresponding to this node
		private Node lb;		// the left/bottom subtree
		private Node rt;		// the right/top subtree
		
		public Node(Point2D p, RectHV rectHV, Node lb, Node rt) {
			this.p = p;
			this.rectHV = rectHV;
			this.lb = lb;
			this.rt = rt;
		}
	}

	/**
	 * constructor
	 */
	public KdTree() {
		root = new Node(p, rectHV, null, null);
	}

	/**
	 * @return is the set empty?
	 */
	public boolean isEmpty() {
		return kdTree.size() == 0;
	}

	/**
	 * @return number of points in the set.
	 */
	public int size() {
		return kdTree.size();
	}

	/**
	 * add the point to the set (if it is not already in the set).
	 * 
	 * @param p
	 */
	public void insert(Point2D p) {
		
		if(root == null){
			root = 
			root.p = p;
		}
		
		kdTree.put(p, node);
	}

	/**
	 * @param p
	 * @return does the set contain point p?
	 */
	public boolean contains(Point2D p) {
		return kdTree.contains(p);
	}

	/**
	 * draw all points to standard draw.
	 */
	public void draw() {
		for (Point2D p : pointSET) {
			StdDraw.point(p.x(), p.y());
		}
	}

	/**
	 * @param rect
	 * @return all points that are inside the rectangle
	 */
	public Iterable<Point2D> range(RectHV rect) {
		
		final SET<Point2D> inSET = new SET<Point2D>();
		for (Point2D point2d : pointSET) {
			if (rect.contains(point2d)) {
				inSET.add(point2d);
			}
		}
		return new Iterable<Point2D>() {
			
			@Override
			public Iterator<Point2D> iterator() {

				return inSET.iterator();
			}
		};
	}

	/**
	 * @param p
	 * @return a nearest neighbor in the set to point p; null if the set is
	 *         empty.
	 */
	public Point2D nearest(Point2D p) {

		if (isEmpty()) {
			return null;
		}
		double min = -1.0;
		Point2D minPoint2d = null;

		for (Point2D point : pointSET) {
			if (min == -1.0 || point.distanceTo(p) < min) {
				min = point.distanceTo(p);
				minPoint2d = point;
			}
		}
		return minPoint2d;
	}

	/**
	 * unit testing of the methods。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
