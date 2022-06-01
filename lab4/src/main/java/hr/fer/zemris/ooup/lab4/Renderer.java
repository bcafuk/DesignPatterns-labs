package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.geometry.Point;

public interface Renderer {
    void drawLine(Point s, Point e);

    void fillPolygon(Point[] points);
}
