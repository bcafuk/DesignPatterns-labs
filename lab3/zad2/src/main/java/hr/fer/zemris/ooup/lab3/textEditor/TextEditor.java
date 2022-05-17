package hr.fer.zemris.ooup.lab3.textEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public final class TextEditor extends JComponent {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final Color selectionColor = new Color(85, 85, 170);
    public final TextEditorModel model;

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    handleMovement(e);
                    break;

                case KeyEvent.VK_DELETE:
                    if (model.getSelectionRange() == null)
                        model.deleteAfter();
                    else
                        model.deleteSelection();
                    break;

                case KeyEvent.VK_BACK_SPACE:
                    if (model.getSelectionRange() == null)
                        model.deleteBefore();
                    else
                        model.deleteSelection();
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Disregard all control characters except newlines
            if (Character.isISOControl(e.getKeyChar()) && e.getKeyChar() != '\n')
                return;

            model.insert(e.getKeyChar());
        }
    };

    public TextEditor(String text) {
        model = new TextEditorModel(text);
        model.addCursorObserver((Location location) -> repaint());

        addKeyListener(keyListener);

        requestFocusInWindow();
    }

    private void handleMovement(KeyEvent e) {
        Location oldCursorLocation = model.getCursorLocation();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> model.moveCursorLeft();
            case KeyEvent.VK_RIGHT -> model.moveCursorRight();
            case KeyEvent.VK_UP -> model.moveCursorUp();
            case KeyEvent.VK_DOWN -> model.moveCursorDown();
            default -> throw new IllegalArgumentException("Unsupported movement key: " + e.getKeyCode());
        }

        if (e.isShiftDown()) {
            LocationRange selection = model.getSelectionRange();

            if (selection == null)
                model.setSelectionRange(new LocationRange(oldCursorLocation, model.getCursorLocation()));
            else
                model.setSelectionRange(new LocationRange(selection.start(), model.getCursorLocation()));
        } else {
            model.setSelectionRange(null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        FontMetrics fm = getFontMetrics(getFont());
        int lineHeight = fm.getHeight();
        int charWidth = fm.getMaxAdvance();

        LocationRange selection = model.getSelectionRange();
        if (selection != null) {
            g.setColor(selectionColor);

            int i = selection.left().line();
            Iterator<String> lines = model.linesRange(selection.left().line(), selection.right().line() + 1);
            while (lines.hasNext()) {
                String line = lines.next();

                int left;
                if (i == selection.left().line())
                    left = selection.left().column();
                else
                    left = 0;

                int right;
                if (i == selection.right().line())
                    right = selection.right().column();
                else
                    right = line.length() + 1;

                g.fillRect(left * charWidth, i * lineHeight,
                        (right - left) * charWidth, lineHeight);

                i++;
            }
        }

        g.setColor(getForeground());
        g.setFont(getFont());

        int y = fm.getAscent();
        Iterator<String> lines = model.allLines();
        while (lines.hasNext()) {
            g.drawString(lines.next(), 0, y);
            y += lineHeight;
        }

        Location cursorLocation = model.getCursorLocation();
        g.drawLine(cursorLocation.column() * charWidth, cursorLocation.line() * lineHeight,
                cursorLocation.column() * charWidth, (cursorLocation.line() + 1) * lineHeight - 1);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
