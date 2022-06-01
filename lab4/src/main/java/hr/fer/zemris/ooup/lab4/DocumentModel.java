package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.geometry.GeometryUtil;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObjectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;

    private final List<GraphicalObject> objects = new ArrayList<>();
    private final List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    private final List<DocumentModelListener> listeners = new ArrayList<>();
    private final List<GraphicalObject> selectedObjects = new ArrayList<>();
    private final List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            int index = objects.indexOf(go);
            if (index == -1)
                return;

            if (go.isSelected())
                selectedObjects.add(go);
            else
                selectedObjects.remove(go);

            notifyListeners();
        }
    };

    public DocumentModel() {}

    public void clear() {
        for (GraphicalObject object : objects)
            object.removeGraphicalObjectListener(goListener);

        objects.clear();
        selectedObjects.clear();

        notifyListeners();
    }

    public void addGraphicalObject(GraphicalObject obj) {
        obj.addGraphicalObjectListener(goListener);

        objects.add(obj);
        if (obj.isSelected())
            selectedObjects.add(obj);

        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        obj.removeGraphicalObjectListener(goListener);

        objects.remove(obj);
        if (obj.isSelected())
            selectedObjects.remove(obj);

        notifyListeners();
    }

    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for (DocumentModelListener listener : listeners)
            listener.documentChange();
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index == -1)
            throw new IllegalArgumentException("The object is not part of the model");

        if (index == objects.size() - 1)
            return;

        Collections.swap(objects, index, index + 1);
    }

    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index == -1)
            throw new IllegalArgumentException("The object is not part of the model");

        if (index == 0)
            return;

        Collections.swap(objects, index, index - 1);
    }

    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject closestObject = null;
        double minimumDistance = Double.POSITIVE_INFINITY;

        for (GraphicalObject object : objects) {
            double distance = object.selectionDistance(mousePoint);

            if (distance <= SELECTION_PROXIMITY && distance < minimumDistance) {
                closestObject = object;
                minimumDistance = distance;
            }
        }

        return closestObject;
    }

    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int closestHotPoint = -1;
        double minimumDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double distance = GeometryUtil.distanceFromPoint(object.getHotPoint(i), mousePoint);

            if (distance <= SELECTION_PROXIMITY && distance < minimumDistance) {
                closestHotPoint = i;
                minimumDistance = distance;
            }
        }

        return closestHotPoint;
    }

}
