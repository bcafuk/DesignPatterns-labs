package hr.fer.zemris.ooup.lab4.renderers;

import hr.fer.zemris.ooup.lab4.geometry.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SVGRendererImpl implements Renderer {
    private final List<String> lines = new ArrayList<>();
    private final String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        lines.add("<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
    }

    public void close() throws IOException {
        lines.add("</svg>");
        Files.write(Path.of(fileName), lines);
    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"blue\"/>",
                s.x(), s.y(), e.x(), e.y()));
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder sb = new StringBuilder("<polygon points=\"");

        for (Point point : points)
            sb.append(point.x())
              .append(',')
              .append(point.y())
              .append(' ');

        sb.append("\" fill=\"blue\" stroke=\"red\"/>");

        lines.add(sb.toString());
    }
}
