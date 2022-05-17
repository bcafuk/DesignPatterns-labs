package hr.fer.zemris.ooup.lab3.textEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Editor extends JFrame {
    private final TextEditor textEditor = new TextEditor("");

    public Editor() {
        setSize(800, 600);
        setTitle("Text editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initGUI();
        pack();
    }

    private static ImageIcon loadIcon(String path) {
        Objects.requireNonNull(path, "The path must not be null");

        try (InputStream is = Editor.class.getResourceAsStream(path)) {
            if (is == null)
                throw new IllegalArgumentException("Missing icon resource: " + path);
            byte[] bytes = is.readAllBytes();

            return new ImageIcon(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void initGUI() {
        Container innerPane = new JPanel(new BorderLayout());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(innerPane, BorderLayout.CENTER);

        textEditor.setOpaque(true);
        textEditor.setForeground(Color.WHITE);
        textEditor.setBackground(Color.BLACK);
        textEditor.setFont(new Font("Ubuntu Mono", Font.PLAIN, 16));
        innerPane.add(textEditor, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        innerPane.add(toolBar, BorderLayout.PAGE_START);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        initFileMenu(menuBar);
        initEditMenu(toolBar, menuBar);
        initMoveMenu(menuBar);

        initStatusBar();
    }

    private void initFileMenu(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JFileChooser fileChooser = new JFileChooser();

        AbstractAction openAction = new AbstractAction("Open", loadIcon("icons/open.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chosenOption = fileChooser.showOpenDialog(Editor.this);

                if (chosenOption == JFileChooser.APPROVE_OPTION) {
                    Path path = (fileChooser.getSelectedFile()).toPath();

                    try {
                        textEditor.model.setText(Files.readString(path));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Editor.this,
                                "Could not open the file " + path,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

        AbstractAction saveAction = new AbstractAction("Save", loadIcon("icons/saveAs.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chosenOption = fileChooser.showSaveDialog(Editor.this);

                if (chosenOption == JFileChooser.APPROVE_OPTION) {
                    Path path = (fileChooser.getSelectedFile()).toPath();

                    try {
                        Files.writeString(path, textEditor.model.getText());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Editor.this,
                                "Could not save to the file " + path,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

        AbstractAction exitAction = new AbstractAction("Exit", loadIcon("icons/exit.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(exitAction);
    }

    private void initEditMenu(JToolBar toolBar, JMenuBar menuBar) {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);

        AbstractAction cutAction = new AbstractAction("Cut", loadIcon("icons/cut.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.cut();
            }
        };
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        cutAction.setEnabled(false);
        textEditor.model.addCursorObserver((location, selection) -> cutAction.setEnabled(selection != null));

        AbstractAction copyAction = new AbstractAction("Copy", loadIcon("icons/copy.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.copy();
            }
        };
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        copyAction.setEnabled(false);
        textEditor.model.addCursorObserver((location, selection) -> copyAction.setEnabled(selection != null));

        AbstractAction pasteAction = new AbstractAction("Paste", loadIcon("icons/paste.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.paste();
            }
        };
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
        pasteAction.setEnabled(false);
        textEditor.model.clipboardStack.addClipboardObserver(pasteAction::setEnabled);

        AbstractAction popPasteAction = new AbstractAction("Paste and take", loadIcon("icons/popPaste.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.popPaste();
            }
        };
        popPasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        popPasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_K);
        popPasteAction.setEnabled(false);
        textEditor.model.clipboardStack.addClipboardObserver(popPasteAction::setEnabled);

        AbstractAction undoAction = new AbstractAction("Undo", loadIcon("icons/undo.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.undoManager.undo();
            }
        };
        undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
        undoAction.setEnabled(false);
        textEditor.model.undoManager.addUndoObserver((canUndo, canRedo) -> undoAction.setEnabled(canUndo));

        AbstractAction redoAction = new AbstractAction("Redo", loadIcon("icons/redo.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.undoManager.redo();
            }
        };
        redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        redoAction.setEnabled(false);
        textEditor.model.undoManager.addUndoObserver((canUndo, canRedo) -> redoAction.setEnabled(canRedo));

        AbstractAction deleteSelectionAction = new AbstractAction("Delete selection", loadIcon("icons/deleteSelection.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.deleteSelection();
            }
        };
        deleteSelectionAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        deleteSelectionAction.setEnabled(false);
        textEditor.model.addCursorObserver((location, selection) -> deleteSelectionAction.setEnabled(selection != null));

        AbstractAction clearDocumentAction = new AbstractAction("Clear document", loadIcon("icons/clearDocument.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.setText("");
            }
        };
        clearDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);

        toolBar.add(cutAction);
        toolBar.add(copyAction);
        toolBar.add(pasteAction);
        toolBar.addSeparator();
        toolBar.add(undoAction);
        toolBar.add(redoAction);

        editMenu.add(undoAction);
        editMenu.add(redoAction);
        editMenu.addSeparator();
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(popPasteAction);
        editMenu.addSeparator();
        editMenu.add(deleteSelectionAction);
        editMenu.add(clearDocumentAction);
    }

    private void initMoveMenu(JMenuBar menuBar) {
        JMenu moveMenu = new JMenu("Move");
        moveMenu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(moveMenu);

        AbstractAction cursorToStartAction = new AbstractAction("Cursor to document start", loadIcon("icons/cursorToStart.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.moveCursorToStart();
            }
        };
        cursorToStartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        cursorToStartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

        AbstractAction cursorToEndAction = new AbstractAction("Cursor to document end", loadIcon("icons/cursorToEnd.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textEditor.model.moveCursorToEnd();
            }
        };
        cursorToEndAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        cursorToEndAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        moveMenu.add(cursorToStartAction);
        moveMenu.add(cursorToEndAction);
    }

    private void initStatusBar() {
        JPanel statusBar = new JPanel(new GridLayout(1, 2, 3, 1));
        getContentPane().add(statusBar, BorderLayout.PAGE_END);

        statusBar.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));

        JLabel positionStatus = new JLabel("1:1");
        statusBar.add(positionStatus);
        textEditor.model.addCursorObserver((location, selection) -> positionStatus.setText((location.line() + 1) + ":" + (location.column() + 1)));

        JLabel lengthStatus = new JLabel("Lines: 1");
        statusBar.add(lengthStatus);
        textEditor.model.addTextObserver(lines -> lengthStatus.setText("Lines: " + lines.size()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Editor();
            frame.setVisible(true);
        });
    }
}
