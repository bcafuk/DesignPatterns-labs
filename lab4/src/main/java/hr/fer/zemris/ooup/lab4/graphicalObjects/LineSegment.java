package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.geometry.GeometryUtil;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

import java.util.List;
import java.util.Stack;

public final class LineSegment extends AbstractGraphicalObject {
    public LineSegment() {
        this(new Point(0, 0), new Point(10, 0));
    }

    public LineSegment(Point start, Point end) {
        super(new Point[]{start, end});
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public Rectangle getBoundingBox() {
        int left = Math.min(getHotPoint(0).x(), getHotPoint(1).x());
        int top = Math.min(getHotPoint(0).y(), getHotPoint(1).y());
        int right = Math.max(getHotPoint(0).x(), getHotPoint(1).x());
        int bottom = Math.max(getHotPoint(0).y(), getHotPoint(1).y());

        return new Rectangle(left, top, right - left, bottom - top);
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] args = data.split("\\s+");
        if (args.length != 4)
            throw new IllegalArgumentException("Expected 4 arguments, but got " + args.length);

        stack.push(new LineSegment(
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
        return "Linija";
    }
}
