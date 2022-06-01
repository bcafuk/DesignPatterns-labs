package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.graphicalObjects.LineSegment;
import hr.fer.zemris.ooup.lab4.graphicalObjects.Oval;
import hr.fer.zemris.ooup.lab4.states.AddShapeState;
import hr.fer.zemris.ooup.lab4.states.IdleState;
import hr.fer.zemris.ooup.lab4.states.SelectShapeState;
import hr.fer.zemris.ooup.lab4.states.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        JButton saveButton = new JButton("Pohrani");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int chosenOption = fileChooser.showSaveDialog(GUI.this);

                if (chosenOption != JFileChooser.APPROVE_OPTION)
                    return;
                Path path = fileChooser.getSelectedFile().toPath();

                List<String> lines = new ArrayList<>();
                for (GraphicalObject object : documentModel.list())
                    object.save(lines);

                try {
                    Files.write(path, lines);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(GUI.this,
                            "Greška pri pohrani u datoteku " + path,
                            "Greška",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBar.add(saveButton);

        JButton svgExportButton = new JButton("SVG export");
        svgExportButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int chosenOption = fileChooser.showSaveDialog(GUI.this);

                if (chosenOption != JFileChooser.APPROVE_OPTION)
                    return;
                String fileName = fileChooser.getSelectedFile().getPath();

                SVGRendererImpl renderer = new SVGRendererImpl(fileName);
                for (GraphicalObject object : documentModel.list())
                    object.render(renderer);
                try {
                    renderer.close();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(GUI.this,
                            "Greška pri izvozu u datoteku " + fileName,
                            "Greška",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBar.add(svgExportButton);

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

        JButton selectionButton = new JButton("Selektiraj");
        selectionButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCurrentState(new SelectShapeState(documentModel));
            }
        });
        toolBar.add(selectionButton);

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
