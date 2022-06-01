package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.DocumentModel;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

public final class AddShapeState implements State {
    private final GraphicalObject prototype;
    private final DocumentModel model;

    public AddShapeState(DocumentModel model, GraphicalObject prototype) {
        this.model = model;
        this.prototype = prototype;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject newObject = prototype.duplicate();
        newObject.translate(mousePoint);
        model.addGraphicalObject(newObject);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {}

    @Override
    public void mouseDragged(Point mousePoint) {}

    @Override
    public void keyPressed(int keyCode) {}

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {}

    @Override
    public void afterDraw(Renderer r) {}

    @Override
    public void onLeaving() {}
}
