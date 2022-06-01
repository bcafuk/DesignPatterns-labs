package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.graphicalObjects.LineSegment;
import hr.fer.zemris.ooup.lab4.graphicalObjects.Oval;
import hr.fer.zemris.ooup.lab4.states.AddShapeState;
import hr.fer.zemris.ooup.lab4.states.IdleState;
import hr.fer.zemris.ooup.lab4.states.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class GUI extends JFrame {
    private State currentState = new IdleState();

    public GUI(List<GraphicalObject> prototypes) {
        DocumentModel documentModel = new DocumentModel();

        setSize(800, 600);
        setTitle("Vector editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        Canvas canvas = new Canvas(this, documentModel);
        getContentPane().add(canvas, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        for (GraphicalObject prototype : prototypes) {
            JButton button = new JButton(prototype.getShapeName());
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setCurrentState(new AddShapeState(documentModel, prototype));
                }
            });

            toolBar.add(button);
        }

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    setCurrentState(new IdleState());
            }
        });
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

    State getCurrentState() {
        return currentState;
    }

    void setCurrentState(State state) {
        currentState.onLeaving();
        currentState = state;
    }
}
