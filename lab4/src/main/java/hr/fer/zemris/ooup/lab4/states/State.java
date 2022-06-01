package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

public interface State {
    void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    void mouseDragged(Point mousePoint);

    void keyPressed(int keyCode);

    void afterDraw(Renderer r, GraphicalObject go);

    void afterDraw(Renderer r);

    void onLeaving();
}
