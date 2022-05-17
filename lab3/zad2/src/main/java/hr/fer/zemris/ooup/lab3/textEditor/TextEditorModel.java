package hr.fer.zemris.ooup.lab3.textEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class TextEditorModel {
    private final List<String> lines;
    private Location cursorLocation = new Location(0, 0);
    private LocationRange selectionRange = null;

    private final List<CursorObserver> cursorObservers = new ArrayList<>();
    private final List<TextObserver> textObservers = new ArrayList<>();

    public TextEditorModel(String text) {
        lines = new ArrayList<>(Arrays.asList(text.split("\r?\n", -1)));
    }

    public Location getCursorLocation() {
        return cursorLocation;
    }

    private Location prevLocation() {
        if (cursorLocation.line() == 0 && cursorLocation.column() == 0)
            return null;

        if (cursorLocation.column() != 0) {
            return new Location(cursorLocation.line(), cursorLocation.column() - 1);
        } else {
            int lineLength = lines.get(cursorLocation.line() - 1).length();
            return new Location(cursorLocation.line() - 1, lineLength);
        }
    }

    private Location nextLocation() {
        int lineLength = lines.get(cursorLocation.line()).length();
        if (cursorLocation.line() == lines.size() - 1 && cursorLocation.column() == lineLength)
            return null;

        if (cursorLocation.column() != lineLength)
            return new Location(cursorLocation.line(), cursorLocation.column() + 1);
        else
            return new Location(cursorLocation.line() + 1, 0);
    }

    public void moveCursorLeft() {
        Location newLocation = prevLocation();
        if (newLocation == null)
            return;

        cursorLocation = newLocation;
        notifyCursorObservers();
    }

    public void moveCursorRight() {
        Location newLocation = nextLocation();
        if (newLocation == null)
            return;

        cursorLocation = newLocation;
        notifyCursorObservers();
    }

    public void moveCursorUp() {
        if (cursorLocation.line() == 0)
            return;

        int lineLength = lines.get(cursorLocation.line() - 1).length();
        if (cursorLocation.column() <= lineLength)
            cursorLocation = new Location(cursorLocation.line() - 1, cursorLocation.column());
        else
            cursorLocation = new Location(cursorLocation.line() - 1, lineLength);

        notifyCursorObservers();
    }

    public void moveCursorDown() {
        if (cursorLocation.line() == lines.size() - 1)
            return;

        int lineLength = lines.get(cursorLocation.line() + 1).length();
        if (cursorLocation.column() <= lineLength)
            cursorLocation = new Location(cursorLocation.line() + 1, cursorLocation.column());
        else
            cursorLocation = new Location(cursorLocation.line() + 1, lineLength);

        notifyCursorObservers();
    }

    public void deleteBefore() {
        Location end = prevLocation();
        if (end != null)
            deleteRange(new LocationRange(cursorLocation, end));
    }

    public void deleteAfter() {
        Location end = nextLocation();
        if (end != null)
            deleteRange(new LocationRange(cursorLocation, end));
    }

    public void deleteRange(LocationRange range) {
        String firstLine = lines.get(range.left().line());
        String lastLine = lines.get(range.right().line());
        String joinedLines = firstLine.substring(0, range.left().column()) + lastLine.substring(range.right().column());
        lines.set(range.left().line(), joinedLines);

        lines.subList(range.left().line() + 1, range.right().line() + 1).clear();

        cursorLocation = range.left();

        notifyTextObservers();
        notifyCursorObservers();
    }

    public void insert(char c) {
        insert(String.valueOf(c));
    }

    public void insert(String text) {
        String[] textLines = text.split("\r?\n", -1);
        if (textLines.length == 0)
            return;

        Location oldCursorLocation = cursorLocation;

        String oldCursorLine = lines.get(oldCursorLocation.line());
        String firstLine = oldCursorLine.substring(0, oldCursorLocation.column()) + textLines[0];

        List<String> newLines = new ArrayList<>();

        for (int i = 1; i < textLines.length - 1; i++)
            newLines.add(oldCursorLocation.line() + i, textLines[i]);

        if (textLines.length > 1) {
            String lastLine = textLines[textLines.length - 1] + oldCursorLine.substring(oldCursorLocation.column());
            newLines.add(lastLine);

            cursorLocation = new Location(oldCursorLocation.line() + textLines.length - 1, textLines[textLines.length - 1].length());
        } else {
            firstLine += oldCursorLine.substring(oldCursorLocation.column());

            cursorLocation = new Location(oldCursorLocation.line(), oldCursorLocation.column() + textLines[0].length());
        }

        lines.addAll(oldCursorLocation.line() + 1, newLines);

        lines.set(oldCursorLocation.line(), firstLine);

        notifyTextObservers();
        notifyCursorObservers();
    }

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public void setSelectionRange(LocationRange range) {
        selectionRange = range;
        notifyTextObservers();
    }

    public Iterator<String> allLines() {
        return linesRange(0, lines.size());
    }

    public Iterator<String> linesRange(int start, int end) {
        return new LineIterator(start, end);
    }

    private class LineIterator implements Iterator<String> {
        private final int end;
        private int current;

        public LineIterator(int start, int end) {
            this.end = end;
            this.current = start;
        }

        @Override
        public boolean hasNext() {
            return current < end;
        }

        @Override
        public String next() {
            String line = lines.get(current);
            current++;

            return line;
        }
    }

    public interface CursorObserver {
        void updateCursorLocation(Location location);
    }

    public void addCursorObserver(CursorObserver observer) {
        cursorObservers.add(observer);
    }

    public boolean removeCursorObserver(CursorObserver observer) {
        return cursorObservers.remove(observer);
    }

    private void notifyCursorObservers() {
        for (CursorObserver observer : cursorObservers)
            observer.updateCursorLocation(cursorLocation);
    }

    public interface TextObserver {
        void updateText();
    }

    public void addTextObserver(TextObserver observer) {
        textObservers.add(observer);
    }

    public boolean removeTextObserver(TextObserver observer) {
        return textObservers.remove(observer);
    }

    private void notifyTextObservers() {
        for (TextObserver observer : textObservers)
            observer.updateText();
    }
}
