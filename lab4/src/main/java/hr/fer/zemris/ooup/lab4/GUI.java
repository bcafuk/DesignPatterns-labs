package hr.fer.zemris.ooup.lab4;

import hr.fer.zemris.ooup.lab4.graphicalObjects.CompositeShape;
import hr.fer.zemris.ooup.lab4.graphicalObjects.GraphicalObject;
import hr.fer.zemris.ooup.lab4.graphicalObjects.LineSegment;
import hr.fer.zemris.ooup.lab4.graphicalObjects.Oval;
import hr.fer.zemris.ooup.lab4.states.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;

public final class GUI extends JFrame {
    private final Map<String, GraphicalObject> loadingPrototypes = new HashMap<>();
    private final DocumentModel documentModel = new DocumentModel();
    private State currentState = new IdleState();

    public GUI(List<GraphicalObject> prototypes) {
        registerPrototype(new CompositeShape());
        for (GraphicalObject prototype : prototypes)
            registerPrototype(prototype);

        setSize(800, 600);
        setTitle("Vector editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        Canvas canvas = new Canvas(this, documentModel);
        getContentPane().add(canvas, BorderLayout.CENTER);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    setCurrentState(new IdleState());
            }
        });

        initToolBar(prototypes);
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

    private void registerPrototype(GraphicalObject prototype) {
        loadingPrototypes.put(prototype.getShapeID(), prototype);
    }

    private void initToolBar(List<GraphicalObject> prototypes) {
        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JButton loadButton = new JButton("Učitaj");
        loadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int chosenOption = fileChooser.showOpenDialog(GUI.this);

                if (chosenOption != JFileChooser.APPROVE_OPTION)
                    return;
                Path path = fileChooser.getSelectedFile().toPath();

                try {
                    loadImage(path);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(GUI.this,
                            "Greška pri pohrani u datoteku " + path,
                            "Greška",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBar.add(loadButton);

        JButton saveButton = new JButton("Pohrani");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int chosenOption = fileChooser.showSaveDialog(GUI.this);

                if (chosenOption != JFileChooser.APPROVE_OPTION)
                    return;
                Path path = fileChooser.getSelectedFile().toPath();

                try {
                    saveImage(path);
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

                try {
                    svgExport(fileName);
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

        JButton eraseButton = new JButton("Brisalo");
        eraseButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCurrentState(new EraserState(documentModel));
            }
        });
        toolBar.add(eraseButton);
    }

    private void loadImage(Path path) throws IOException {
        Stack<GraphicalObject> objectStack = new Stack<>();

        Files.lines(path)
             .forEach(line -> {
                 String[] split = line.split("\\s+", 2);
                 if (split.length < 1)
                     return;

                 GraphicalObject prototype = loadingPrototypes.get(split[0]);
                 if (prototype == null)
                     throw new NoSuchElementException("No object with ID " + split[0]);

                 if (split.length > 1)
                     prototype.load(objectStack, split[1]);
                 else
                     prototype.load(objectStack, "");
             });

        documentModel.clear();
        for (GraphicalObject object : objectStack)
            documentModel.addGraphicalObject(object);
    }

    private void saveImage(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (GraphicalObject object : documentModel.list())
            object.save(lines);
        Files.write(path, lines);
    }

    private void svgExport(String fileName) throws IOException {
        SVGRendererImpl renderer = new SVGRendererImpl(fileName);
        for (GraphicalObject object : documentModel.list())
            object.render(renderer);
        renderer.close();
    }

    State getCurrentState() {
        return currentState;
    }

    private void setCurrentState(State state) {
        currentState.onLeaving();
        currentState = state;
    }
}
