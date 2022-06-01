package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

import java.util.List;
import java.util.Stack;

public final class Oval extends AbstractGraphicalObject {
    private final static int NUM_POINTS = 64;

    public Oval() {
        this(new Point(10, 0), new Point(0, 10));
    }

    public Oval(Point rightHotPoint, Point bottomHotPoint) {
        super(new Point[]{rightHotPoint, bottomHotPoint});
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        // This is an approximation! The exact solution would require solving 4th-degree polynomials.
        // See: https://stackoverflow.com/a/22961669

        Point center = getCenter();
        double cx = center.x();
        double cy = center.y();
        double a = Math.pow(getHalfWidth(), 2);
        double b = Math.pow(getHalfHeight(), 2);

        double r2 = Math.pow(mousePoint.x() - cx, 2) / a + Math.pow(mousePoint.y() - cy, 2) / b;
        if (r2 <= 1)
            // The point is inside the ellipse
            return 0;

        double s = Math.sqrt(r2) - 1;
        double dx = mousePoint.x() - cx;
        double dy = mousePoint.y() - cy;
        return Math.hypot(s * dx, s * dy);
    }

    @Override
    public void render(Renderer r) {
        Point center = getCenter();
        double halfWidth = getHalfWidth();
        double halfHeight = getHalfHeight();

        Point[] points = new Point[NUM_POINTS];
        for (int i = 0; i < NUM_POINTS; i++) {
            double angle = 2 * Math.PI * i / NUM_POINTS;
            double x = (double) center.x() + Math.cos(angle) * halfWidth;
            double y = (double) center.y() + Math.sin(angle) * halfHeight;
            points[i] = new Point((int) Math.round(x), (int) Math.round(y));
        }

        r.fillPolygon(points);
    }

    @Override
    public Rectangle getBoundingBox() {
        Point center = getCenter();
        int halfWidth = getHalfWidth();
        int halfHeight = getHalfHeight();

        return new Rectangle(center.x() - halfWidth, center.y() - halfHeight,
                2 * halfWidth, 2 * halfHeight);
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] args = data.split("\\s+");
        if (args.length != 4)
            throw new IllegalArgumentException("Expected 4 arguments, but got " + args.length);

        stack.push(new Oval(
                new Point(Integer.parseInt(args[0]), Integer.parseInt(args[1])),
                new Point(Integer.parseInt(args[2]), Integer.parseInt(args[3]))));
    }

    @Override
    public void save(List<String> rows) {
        rows.add(String.format("%s %d %d %d %d", getShapeID(),
                getHotPoint(0).x(), getHotPoint(0).y(),
                getHotPoint(1).x(), getHotPoint(1).y()));
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    private Point getCenter() {
        return new Point(getHotPoint(1).x(), getHotPoint(0).y());
    }

    private int getHalfWidth() {
        return Math.abs(getHotPoint(0).x() - getHotPoint(1).x());
    }

    private int getHalfHeight() {
        return Math.abs(getHotPoint(0).y() - getHotPoint(1).y());
    }
}
