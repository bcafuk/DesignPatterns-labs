package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.DocumentModel;
import hr.fer.zemris.ooup.lab4.Renderer;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.geometry.Rectangle;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;

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
        if (selectedHotPoint != -1)
            return;

        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() == 1) {
            GraphicalObject selectedObject = selectedObjects.get(0);
            selectedHotPoint = model.findSelectedHotPoint(selectedObject, mousePoint);
        }

        if (selectedHotPoint != -1)
            return;

        if (!ctrlDown)
            deselectAll();

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

        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject selectedObject = selectedObjects.get(0);

        selectedObject.setHotPoint(selectedHotPoint, mousePoint);
    }

    @Override
    public void keyPressed(int keyCode) {}

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected())
            drawRectangle(r, go.getBoundingBox());
    }

    @Override
    public void afterDraw(Renderer r) {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject selectedObject = selectedObjects.get(0);

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
