package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.geometry.Point;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.renderers.G2DRendererImpl;
import hr.fer.zemris.ooup.lab4.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

public final class Canvas extends JComponent {
    private final GUI gui;
    private final DocumentModel documentModel;

    public Canvas(GUI gui, DocumentModel documentModel) {
        this.gui = gui;
        this.documentModel = documentModel;

        documentModel.addDocumentModelListener(this::repaint);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gui.getCurrentState()
                   .keyPressed(e.getKeyCode());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();

                if (e.getButton() == MouseEvent.BUTTON1)
                    gui.getCurrentState()
                       .mouseDown(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    gui.getCurrentState()
                       .mouseUp(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & BUTTON1_DOWN_MASK) == 0)
                    return;

                int mouseX = e.getX();
                if (mouseX < 0)
                    mouseX = 0;
                if (mouseX >= getWidth())
                    mouseX = getWidth() - 1;

                int mouseY = e.getY();
                if (mouseY < 0)
                    mouseY = 0;
                if (mouseY >= getHeight())
                    mouseY = getHeight() - 1;

                gui.getCurrentState().mouseDragged(new Point(mouseX, mouseY));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (GraphicalObject object : documentModel.list()) {
            object.render(r);
            gui.getCurrentState().afterDraw(r, object);
        }

        gui.getCurrentState().afterDraw(r);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}
