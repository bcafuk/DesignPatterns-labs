package hr.fer.zemris.ooup.lab4.states;

import hr.fer.zemris.ooup.lab4.Renderer;
import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;

public final class IdleState implements State {
    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {}

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
