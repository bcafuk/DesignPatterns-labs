package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.graphicalObjects.LineSegment;
import hr.fer.zemris.ooup.lab4.graphicalObjects.Oval;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class GUI extends JFrame {
    public GUI(List<GraphicalObject> prototypes) {
        DocumentModel documentModel = new DocumentModel();

        setSize(800, 600);
        setTitle("Vector editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        Canvas canvas = new Canvas(documentModel);
        getContentPane().add(canvas, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        for (GraphicalObject prototype : prototypes) {
            JButton button = new JButton(prototype.getShapeName());
            toolBar.add(button);
        }
    }

    public static void main(String[] args) {
        List<GraphicalObject> objects = new ArrayList<>();

        objects.add(new LineSegment());
        objects.add(new Oval());

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(objects);
            gui.setVisible(true);
        });
    }
}
