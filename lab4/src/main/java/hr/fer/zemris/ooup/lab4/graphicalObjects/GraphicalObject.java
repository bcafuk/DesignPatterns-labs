package hr.fer.zemris.ooup.lab4.graphicalObjects;

import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

import java.util.List;
import java.util.Stack;

public interface GraphicalObject {
    boolean isSelected();

    void setSelected(boolean selected);

    int getNumberOfHotPoints();

    Point getHotPoint(int index);

    void setHotPoint(int index, Point point);

    double getHotPointDistance(int index, Point mousePoint);

    void translate(Point delta);

    Rectangle getBoundingBox();

    double selectionDistance(Point mousePoint);

    void render(Renderer r);

    void addGraphicalObjectListener(GraphicalObjectListener l);

    void removeGraphicalObjectListener(GraphicalObjectListener l);

    String getShapeName();

    GraphicalObject duplicate();

    String getShapeID();

    void load(Stack<GraphicalObject> stack, String data);

    void save(List<String> rows);
}
