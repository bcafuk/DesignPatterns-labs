package hr.fer.zemris.ooup.lab4.geometry;

public final class GeometryUtil {
    private GeometryUtil() {}

    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.hypot(point1.x() - point2.x(), point1.y() - point2.y());
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        Point lineDir = e.difference(s);
        Point pDir = p.difference(s);

        // The dot product of lineDir and pDir
        int tNumerator = lineDir.x() * pDir.x() + lineDir.y() * pDir.y();
        // The square of the length of lineDir
        int tDenominator = lineDir.x() * lineDir.x() + lineDir.y() * lineDir.y();

        double t = (double) tNumerator / tDenominator;

        if (t < 0)
            return distanceFromPoint(s, p);
        if (t > 1)
            return distanceFromPoint(e, p);

        return Math.hypot(s.x() + t * lineDir.x() - p.x(), s.y() + t * lineDir.y() - p.y());
    }
}
