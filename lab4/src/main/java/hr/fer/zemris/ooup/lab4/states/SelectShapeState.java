package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.DocumentModel;
import hr.fer.zemris.ooup.lab4.Renderer;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;

import java.awt.event.KeyEvent;
import java.util.List;

public final class SelectShapeState implements State {
    private final static int HOT_POINT_SIZE = 6;
    private final DocumentModel model;

    private int selectedHotPoint = -1;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!ctrlDown) {
            // Try to select a hot point
            GraphicalObject selectedObject = getSelectedObject();
            if (selectedObject != null) {
                selectedHotPoint = model.findSelectedHotPoint(selectedObject, mousePoint);

                if (selectedHotPoint != -1)
                    return;
            }

            // No hot point was selected at this point, deselect everything
            deselectAll();
        }

        GraphicalObject object = model.findSelectedGraphicalObject(mousePoint);
        if (object != null)
            object.setSelected(true);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        selectedHotPoint = -1;
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (selectedHotPoint == -1)
            return;

        GraphicalObject selectedObject = getSelectedObject();
        if (selectedObject == null)
            return;

        selectedObject.setHotPoint(selectedHotPoint, mousePoint);
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_RIGHT -> model.getSelectedObjects().forEach(go -> go.translate(new Point(1, 0)));
            case KeyEvent.VK_LEFT -> model.getSelectedObjects().forEach(go -> go.translate(new Point(-1, 0)));
            case KeyEvent.VK_DOWN -> model.getSelectedObjects().forEach(go -> go.translate(new Point(0, 1)));
            case KeyEvent.VK_UP -> model.getSelectedObjects().forEach(go -> go.translate(new Point(0, -1)));
            case KeyEvent.VK_EQUALS, KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> model.getSelectedObjects().forEach(model::increaseZ);
            case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> model.getSelectedObjects().forEach(model::decreaseZ);
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected())
            drawRectangle(r, go.getBoundingBox());
    }

    @Override
    public void afterDraw(Renderer r) {
        GraphicalObject selectedObject = getSelectedObject();
        if (selectedObject == null)
            return;

        for (int i = 0; i < selectedObject.getNumberOfHotPoints(); i++) {
            Point hotPoint = selectedObject.getHotPoint(i);
            drawRectangle(r, new Rectangle(hotPoint.x() - HOT_POINT_SIZE / 2, hotPoint.y() - HOT_POINT_SIZE / 2,
                    HOT_POINT_SIZE, HOT_POINT_SIZE));
        }
    }

    @Override
    public void onLeaving() {
        deselectAll();
    }

    private GraphicalObject getSelectedObject() {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() == 1)
            return selectedObjects.get(0);
        else
            return null;
    }

    private void deselectAll() {
        selectedHotPoint = -1;
        for (GraphicalObject selected : model.list())
            selected.setSelected(false);
    }

    private void drawRectangle(Renderer r, Rectangle rect) {
        Point[] corners = new Point[]{
                new Point(rect.x(), rect.y()),
                new Point(rect.x() + rect.width(), rect.y()),
                new Point(rect.x() + rect.width(), rect.y() + rect.height()),
                new Point(rect.x(), rect.y() + rect.height()),
        };

        for (int i = 0; i < corners.length; i++)
            r.drawLine(corners[i], corners[(i + 1) % corners.length]);
    }
}
