package hr.fer.zemris.ooup.lab4.renderers;

import hr.fer.zemris.ooup.lab4.geometry.Point;

import java.awt.*;

public final class G2DRendererImpl implements Renderer {
    private final Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(hr.fer.zemris.ooup.lab4.geometry.Point s, hr.fer.zemris.ooup.lab4.geometry.Point e) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.x(), s.y(), e.x(), e.y());
    }

    @Override
    public void fillPolygon(hr.fer.zemris.ooup.lab4.geometry.Point[] points) {
        Polygon polygon = new Polygon();
        for (Point point : points)
            polygon.addPoint(point.x(), point.y());

        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(polygon);

        g2d.setColor(Color.RED);
        g2d.drawPolygon(polygon);
    }
}
