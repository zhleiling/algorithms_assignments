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
 * Description : KdTree <br/>
 * date: Oct 17, 2014 4:22:38 PM <br/>
 * 
 * @author zhanglei01
 * @version
 * @since JDK 1.6
 */
public class KdTree {
    private Node root;

    private int size;

    private static class Node {
        private Point2D p; // the point
        private RectHV rectHV; // the axis-aligned rectangle corresponding to
                               // this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

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
        size = 0;
    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return number of points in the set.
     */
    public int size() {
        // return size(root);
        return size;
    }

    // private int size(Node node) {
    // if (node == null) {
    // return 0;
    // } else {
    // return 1 + size(node.lb) + size(node.rt);
    // }
    // }

    /**
     * add the point to the set (if it is not already in the set).
     * 
     * @param p
     */
    public void insert(Point2D p) {

        if (p == null)
            return;
        root = insert(root, p, true);

    }

    private Node insert(Node node, Point2D p, boolean vertcial) {
        if (node == null){
            size++;
            if (node == root) {
                return new Node(p, new RectHV(0, 0, 1, 1), null, null);
            }else{
                return new Node(p, null, null, null);
            }
        }

        if (p.equals(node.p))
            return node;

        // vertical
        if (vertcial) {
            if (p.x() < node.p.x()) {
                node.lb = insert(node.lb, p, false);
                node.lb.rectHV = new RectHV(node.rectHV.xmin(),
                        node.rectHV.ymin(), node.p.x(), node.rectHV.ymax());
            } else {
                node.rt = insert(node.rt, p, false);
                node.rt.rectHV = new RectHV(node.p.x(), node.rectHV.ymin(),
                        node.rectHV.xmax(), node.rectHV.ymax());
            }
            // horizontal
        } else {
            if (p.y() < node.p.y()) {
                node.lb = insert(node.lb, p, true);
                node.lb.rectHV = new RectHV(node.rectHV.xmin(),
                        node.rectHV.ymin(), node.rectHV.xmax(), node.p.y());
            } else {
                node.rt = insert(node.rt, p, true);
                node.rt.rectHV = new RectHV(node.rectHV.xmin(), node.p.y(),
                        node.rectHV.xmax(), node.rectHV.ymax());
            }
        }

        return node;

    }

    /**
     * @param p
     * @return does the set contain point p?
     */
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean vertical) {
        if (node == null || p == null) {
            return false;
        }
        if (p.equals(node.p)) {
            return true;
        }

        if (vertical) {
            if (p.x() < node.p.x()) {
                return contains(node.lb, p, false);
            } else {
                return contains(node.rt, p, false);
            }
        } else {
            if (p.y() < node.p.y()) {
                return contains(node.lb, p, true);
            } else {
                return contains(node.rt, p, true);
            }
        }

    }

    /**
     * draw all points to standard draw.
     */
    public void draw() {
        if (root != null) {
            root.rectHV.draw();
            draw(root, true);
        }
    }

    private void draw(Node node, boolean vertical) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(node.p.x(), node.p.y());

        StdDraw.setPenRadius();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rectHV.ymin(), node.p.x(),
                    node.rectHV.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rectHV.xmin(), node.p.y(), node.rectHV.xmax(),
                    node.p.y());
        }

        draw(node.lb, !vertical);
        draw(node.rt, !vertical);

    }

    /**
     * @param rect
     * @return all points that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        RangeIterable<Point2D> rangeIterable = new RangeIterable<Point2D>();
        rangeIterable.setRectHV(rect);
        return rangeIterable;
    }

    private class RangeIterable<Point2D> implements Iterable<Point2D> {

        private RectHV rectHV;
        private Queue<Point2D> matchedQ = new Queue<Point2D>();

        @Override
        public Iterator<Point2D> iterator() {
            matchedQ = match(rectHV, root, matchedQ);
            return matchedQ.iterator();
        }

        public RectHV getRectHV() {
            return rectHV;
        }

        public void setRectHV(RectHV rectHV) {
            this.rectHV = rectHV;
        }

        private Queue<Point2D> match(RectHV rect, Node node,
                Queue<Point2D> queue) {

            if (node == null || !node.rectHV.intersects(rect)) {
                return queue;
            }

            if (rect.contains(node.p)) {
                queue.enqueue((Point2D) node.p);
            }

            queue = match(rect, node.lb, queue);
            queue = match(rect, node.rt, queue);
            return queue;
        }
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
        Node nearesNode = nearest(root, p, root.p.distanceSquaredTo(p));
        return nearesNode.p;
    }

    private Node nearest(Node node, Point2D p, double minDistance) {
        
        double min = minDistance;
        if (node.p.distanceSquaredTo(p) < minDistance) {
            min = node.p.distanceSquaredTo(p);
        }
        
        double lbMin = min;
        double rtMin = min;
        Node lbNearest = node;
        Node rtNearest = node;
        
        if (node.lb != null
                && node.lb.rectHV.distanceSquaredTo(p) < min) {
            lbNearest = nearest(node.lb, p, min);
            lbMin = lbNearest.p.distanceSquaredTo(p);
        }

        if (node.rt != null
                && node.rt.rectHV.distanceSquaredTo(p) < minDistance) {
            rtNearest = nearest(node.rt, p, min);
            rtMin = rtNearest.p.distanceSquaredTo(p);
        }

        if (lbMin <= rtMin && lbMin < min) {
            return lbNearest;
        } else if (rtMin < lbMin && rtMin < min) {
            return rtNearest;
        } else {
            return node;
        }
    }

    /**
     * unit testing of the methods。
     * 
     * @param args
     */
    public static void main(String[] args) {
        // simple test for insert and contains
        KdTree kdTree = new KdTree();
        StdOut.println(kdTree.contains(new Point2D(0.5, 0.5)));
        kdTree.insert(new Point2D(0.5, 0.8));
        StdOut.println(kdTree.contains(new Point2D(0.5, 0.6)));
        StdOut.println(kdTree.contains(new Point2D(0.5, 0.5)));
        StdOut.println(kdTree.contains(new Point2D(0.5, 0.8)));
        StdOut.println(kdTree.contains(new Point2D(0.5, 0.0)));

        // simple test for draw
        kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        System.out.println(kdTree.size);
        kdTree.insert(new Point2D(0.5, 0.4));
        System.out.println(kdTree.size);
        kdTree.insert(new Point2D(0.2, 0.3));
        System.out.println(kdTree.size);
        kdTree.insert(new Point2D(0.4, 0.7));
        System.out.println(kdTree.size);
        kdTree.insert(new Point2D(0.9, 0.6));
        System.out.println(kdTree.size);
        kdTree.draw();

        // simple test for draw
        System.out.println(kdTree.nearest(new Point2D(0.3, 0.2)));
        System.out.println(kdTree.nearest(new Point2D(0.95, 0.6)));
        System.out.println(kdTree.nearest(new Point2D(0.95, 0.2)));
        System.out.println(kdTree.nearest(new Point2D(0.1, 0.1)));

        // test for range
        System.out.println("----------------");
        Iterable<Point2D> iterable = kdTree
                .range(new RectHV(0.2, 0.2, 0.3, 0.3));
        for (Point2D point2d : iterable) {
            System.out.println(point2d);
        }

    }

}
