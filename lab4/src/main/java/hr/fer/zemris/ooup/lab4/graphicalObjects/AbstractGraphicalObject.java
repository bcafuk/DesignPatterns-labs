package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.geometry.GeometryUtil;
import hr.fer.zemris.ooup.lab4.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private final Point[] hotPoints;
    private boolean selected;

    List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints.clone();
        selected = false;
    }

    @Override
    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;

        notifyListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected == selected)
            return;

        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++)
            hotPoints[i] = hotPoints[i].translate(delta);

        notifyListeners();
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        this.listeners.remove(l);
    }

    protected void notifyListeners() {
        for (GraphicalObjectListener l : listeners)
            l.graphicalObjectChanged(this);
    }

    protected void notifySelectionListeners() {
        for (GraphicalObjectListener l : listeners)
            l.graphicalObjectSelectionChanged(this);
    }
}
