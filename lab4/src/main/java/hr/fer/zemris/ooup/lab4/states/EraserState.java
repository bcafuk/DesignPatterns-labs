package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.DocumentModel;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

public final class EraserState implements State {
    private final static int ERASING_DISTANCE = 2;

    private final DocumentModel model;
    private final List<Point> trail = new ArrayList<>();

    public EraserState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        trail.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for (Point point : trail)
            model.list()
                 .stream().filter(go -> go.selectionDistance(point) <= ERASING_DISTANCE)
                 .toList()
                 .forEach(model::removeGraphicalObject);

        trail.clear();
        model.notifyListeners();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        trail.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void keyPressed(int keyCode) {}

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {}

    @Override
    public void afterDraw(Renderer r) {
        for (int i = 1; i < trail.size(); i++)
            r.drawLine(trail.get(i - 1), trail.get(i));
    }

    @Override
    public void onLeaving() {
        trail.clear();
        model.notifyListeners();
    }
}
