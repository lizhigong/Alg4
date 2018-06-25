/*************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    none
 *  Dependencies: Point.java, LineSegment.java
 *
 *  Author: Zhigong Li
 *  Date : 2018.06.23
 *************************************************************************/

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private int numberOfSegments;
    private List<LineSegment> segments;

    /**
     * Compute line segments.
     *
     * @param points input points
     */
    public BruteCollinearPoints(Point[] points) {
        numberOfSegments = 0;
        segments = new ArrayList<>();

        if (points == null) {
            throw new IllegalArgumentException("points cannot be null");
        }

        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException("any point cannot be null");
        }

        if (points.length < 4) {
            return;
        }

        int len = points.length;

        for (int i = 0; i <= len - 4; i++)
            for (int j = i + 1; j <= len - 3; j++)
                for (int k = j + 1; k <= len - 2; k++)
                    for (int l = k + 1; l <= len - 1; l++) {
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[l];

                        double slope1 = p1.slopeTo(p2);
                        double slope2 = p1.slopeTo(p3);
                        double slope3 = p1.slopeTo(p4);

                        // Check duplicate points

                        if (slope1 == Double.NEGATIVE_INFINITY ||
                                slope2 == Double.NEGATIVE_INFINITY ||
                                slope3 == Double.NEGATIVE_INFINITY)
                            throw new IllegalArgumentException("duplicate points found");

                        if (slope1 == slope2 && slope1 == slope3) {
                            numberOfSegments++;

                            // Put min point to p1 and max point to p2
                            // If duplicate point found,
                            Point min = p1, max = p1;

                            if (p1.compareTo(p2) < 0) {
                                min = p1;
                                max = p2;
                            } else {
                                max = p1;
                                min = p2;
                            }

                            if (p3.compareTo(p4) < 0) { // p3 < p4
                                if (min.compareTo(p3) > 0) // min > p3
                                    min = p3;

                                if (max.compareTo(p4) < 0) // max < p4
                                    max = p4;
                            } else { // p3 > p4
                                if (min.compareTo(p4) > 0)
                                    min = p4;

                                if (max.compareTo(p3) < 0)
                                    max = p3;
                            }

                            segments.add(new LineSegment(min, max));
                        }
                    }

    }

    /**
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return numberOfSegments;
    }

    /**
     * @return found line segments
     */
    public LineSegment[] segments() {
        return (LineSegment[]) segments.toArray();
    }
}
