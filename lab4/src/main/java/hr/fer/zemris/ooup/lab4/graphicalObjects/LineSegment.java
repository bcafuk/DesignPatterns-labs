package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.geometry.GeometryUtil;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.Renderer;

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
    public String getShapeName() {
        return "Linija";
    }
}
