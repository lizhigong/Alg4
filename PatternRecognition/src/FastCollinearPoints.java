/*************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    none
 *  Dependencies: Point.java, LineSegment.java
 *
 *  Author: Zhigong Li
 *  Date : 2018.06.23
 *************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private int numberOfSegments;
    private List<LineSegment> segments;

    /**
     * Compute collinear points by sorting
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        numberOfSegments = 0;
        segments = new ArrayList<>();

        // O(1)
        if (points == null) {
            throw new IllegalArgumentException("points cannot be null");
        }

        int len = points.length;

        // O(n)
        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException("any point cannot be null");
        }

        // O(n^2log(n))

        // Sort the points by natural order first.
        sort(points);
        // Then for each point v, sort the points by their slopes to v.
        for (Point p : points) {
            Point[] copy = Arrays.copyOf(points, points.length);
            sort(copy, p.slopeOrder());

            // copy[0] should be the point itself.
            for (int i = 1; i < len - 3; ) {
                if (p.slopeTo(copy[i]) == p.slopeTo(copy[i + 3])) {
                    double slope = p.slopeTo(copy[i]);
                    int offset = 4;
                    // How to store the points and their slopes to v together?
                    while ((i + offset) < len && p.slopeTo(copy[i + offset]) == slope) {
                        offset++;
                    }

                    // Make sure that the merge sort is stable in order to
                    // easily pick the start and the end of the line segment.
                    segments.add(new LineSegment(copy[i], copy[i + offset - 1]));
                    i += offset;
                } else
                    i++;
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
     * @return the line segments
     */
    public LineSegment[] segments() {
        return (LineSegment[]) segments.toArray();
    }


    /**
     * Sort the array by natural order from array[start] to array[end]
     *
     * @param array
     */
    private void sort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];
        sort(array, aux, 0, array.length - 1);
    }

    /**
     * Merge with natural order
     *
     * @param array
     * @param aux
     * @param low
     * @param high
     */
    private void merge(Comparable[] array, Comparable[] aux, int low, int high) {
        if (low >= high) return;

        int mid = (low + high) / 2;
        // Copy to aux
        for (int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) array[k] = aux[j++];
            else if (j > high) array[k] = aux[i++];
            else if (aux[i].compareTo(aux[j]) <= 0) array[k] = aux[i++]; // <= makes it stable!!
            else array[k] = aux[j++];
        }
    }

    /**
     * Helper sort
     *
     * @param array
     * @param aux
     * @param lo
     * @param hi
     */
    private void sort(Comparable[] array, Comparable[] aux, int lo, int hi) {
        if (lo >= hi) return;

        int mid = (lo + hi) / 2;
        // sort lo - mid
        sort(array, aux, lo, mid);
        // sort mid+1 - hi
        sort(array, aux, mid + 1, hi);
        // merge
        merge(array, aux, lo, hi);
    }

    /**
     * Sort the array by <code>comparator</code>
     *
     * @param array
     * @param comparator
     * @return a sorted new array and leave <code>array</code> as it is.
     */
    private void sort(Object[] array, Comparator comparator) {
        Object[] aux = new Object[array.length];
        sort(array, aux, 0, array.length - 1, comparator);
    }

    /**
     * Helper sort
     *
     * @param array
     * @param aux
     * @param lo
     * @param hi
     * @param comparator
     */
    private void sort(Object[] array, Object[] aux, int lo, int hi, Comparator comparator) {
        if (lo >= hi) return;

        int mid = (lo + hi) / 2;
        sort(array, aux, lo, mid, comparator);
        sort(array, aux, mid + 1, hi, comparator);
        merge(array, aux, lo, hi, comparator);
    }

    /**
     * Merge with comparator
     *
     * @param array
     * @param aux
     * @param lo
     * @param hi
     * @param comparator
     */
    private void merge(Object[] array, Object[] aux, int lo, int hi, Comparator comparator) {
        if (lo >= hi) return;
        int mid = (lo + hi) / 2;

        for (int k = lo; k <= hi; k++) {
            aux[k] = array[k];
        }

        int i = lo;
        int j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mid) array[k] = aux[j++];
            else if (j > hi) array[k] = aux[i++];
            else if (comparator.compare(aux[i], aux[j]) <= 0) array[k] = aux[i++];
            else array[k] = aux[j++];
        }
    }

}
