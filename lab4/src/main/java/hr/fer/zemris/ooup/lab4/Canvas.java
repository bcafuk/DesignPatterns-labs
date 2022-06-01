package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;

import javax.swing.*;
import java.awt.*;

public final class Canvas extends JComponent {
    private final DocumentModel documentModel;

    public Canvas(DocumentModel documentModel) {
        this.documentModel = documentModel;
        documentModel.addDocumentModelListener(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (GraphicalObject object : documentModel.list())
            object.render(r);
    }
}
