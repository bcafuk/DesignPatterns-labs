package hr.fer.zemris.ooup.lab3.textEditor;

import java.util.*;

public final class TextEditorModel {
    private List<String> lines;
    private Location cursorLocation = new Location(0, 0);
    private LocationRange selectionRange = null;

    public final ClipboardStack clipboardStack = new ClipboardStack();
    public final UndoManager undoManager = new UndoManager();

    private final List<CursorObserver> cursorObservers = new ArrayList<>();
    private final List<TextObserver> textObservers = new ArrayList<>();

    public TextEditorModel(String text) {
        lines = new ArrayList<>(Arrays.asList(text.split("\r?\n", -1)));
    }

    public void setText(String text) {
        Location oldCursor = cursorLocation;
        LocationRange oldSelection = selectionRange;
        List<String> oldLines = lines;

        List<String> newLines = new ArrayList<>(Arrays.asList(text.split("\r?\n", -1)));

        undoManager.push(new UndoManager.EditAction() {
            @Override
            public void executeDo() {
                cursorLocation = new Location(0, 0);
                selectionRange = null;
                lines = newLines;

                notifyTextObservers();
                notifyCursorObservers();
            }

            @Override
            public void executeUndo() {
                cursorLocation = oldCursor;
                selectionRange = oldSelection;
                lines = oldLines;

                notifyTextObservers();
                notifyCursorObservers();
            }
        });
    }

    public String getText() {
        return String.join("\n", lines);
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

    private void setCursorLocation(Location cursorLocation) {
        this.cursorLocation = cursorLocation;
        notifyCursorObservers();
    }

    public void moveCursorLeft() {
        Location newLocation = prevLocation();
        if (newLocation == null)
            return;

        setCursorLocation(newLocation);
    }

    public void moveCursorRight() {
        Location newLocation = nextLocation();
        if (newLocation == null)
            return;

        setCursorLocation(newLocation);
    }

    public void moveCursorUp() {
        if (cursorLocation.line() == 0)
            return;

        int lineLength = lines.get(cursorLocation.line() - 1).length();
        if (cursorLocation.column() <= lineLength)
            setCursorLocation(new Location(cursorLocation.line() - 1, cursorLocation.column()));
        else
            setCursorLocation(new Location(cursorLocation.line() - 1, lineLength));
    }

    public void moveCursorDown() {
        if (cursorLocation.line() == lines.size() - 1)
            return;

        int lineLength = lines.get(cursorLocation.line() + 1).length();
        if (cursorLocation.column() <= lineLength)
            setCursorLocation(new Location(cursorLocation.line() + 1, cursorLocation.column()));
        else
            setCursorLocation(new Location(cursorLocation.line() + 1, lineLength));
    }

    public void moveCursorToStart() {
        setCursorLocation(new Location(0, 0));
    }

    public void moveCursorToEnd() {
        setCursorLocation(new Location(lines.size() - 1, lines.get(lines.size() - 1).length()));
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

    public void deleteSelection() {
        deleteRange(selectionRange);
    }

    private void deleteRange(LocationRange range) {
        undoManager.push(makeDeleteRangeAction(range));
    }

    public UndoManager.EditAction makeDeleteRangeAction(LocationRange range) {
        String firstLine = lines.get(range.left().line());
        List<String> erasedLines = new ArrayList<>(lines.subList(range.left().line() + 1, range.right().line() + 1));

        Location oldCursor = cursorLocation;

        return new UndoManager.EditAction() {
            @Override
            public void executeDo() {
                String lastLine = lines.get(range.right().line());

                String joinedLines = firstLine.substring(0, range.left().column()) + lastLine.substring(range.right().column());
                lines.set(range.left().line(), joinedLines);

                lines.subList(range.left().line() + 1, range.right().line() + 1).clear();

                cursorLocation = range.left();
                selectionRange = null;

                notifyTextObservers();
                notifyCursorObservers();
            }

            @Override
            public void executeUndo() {
                lines.set(range.left().line(), firstLine);
                lines.addAll(range.left().line() + 1, erasedLines);

                cursorLocation = oldCursor;
                selectionRange = null;

                notifyTextObservers();
                notifyCursorObservers();
            }
        };
    }

    public void insert(char c) {
        insert(String.valueOf(c));
    }

    public void insert(String text) {
        String[] textLines = text.split("\r?\n", -1);

        if (textLines.length == 0) {
            if (selectionRange != null)
                deleteSelection();
            return;
        }

        UndoManager.EditAction deleteSelectionAction;
        if (selectionRange != null)
            deleteSelectionAction = makeDeleteRangeAction(selectionRange);
        else
            deleteSelectionAction = null;

        Location oldCursorLocation = cursorLocation;

        undoManager.push(new UndoManager.EditAction() {
            @Override
            public void executeDo() {
                if (deleteSelectionAction != null)
                    deleteSelectionAction.executeDo();

                List<String> newLines = new ArrayList<>();
                for (int i = 1; i < textLines.length - 1; i++)
                    newLines.add(textLines[i]);

                String oldCursorLine = lines.get(oldCursorLocation.line());

                String firstLine = oldCursorLine.substring(0, oldCursorLocation.column()) + textLines[0];
                Location newCursorLocation;

                if (textLines.length > 1) {
                    String lastLine = textLines[textLines.length - 1] + oldCursorLine.substring(oldCursorLocation.column());
                    newLines.add(lastLine);

                    newCursorLocation = new Location(oldCursorLocation.line() + textLines.length - 1, textLines[textLines.length - 1].length());
                } else {
                    firstLine += oldCursorLine.substring(oldCursorLocation.column());

                    newCursorLocation = new Location(oldCursorLocation.line(), oldCursorLocation.column() + textLines[0].length());
                }

                lines.addAll(oldCursorLocation.line() + 1, newLines);
                lines.set(oldCursorLocation.line(), firstLine);

                cursorLocation = newCursorLocation;

                notifyTextObservers();
                notifyCursorObservers();
            }

            @Override
            public void executeUndo() {
                String firstLine = lines.get(oldCursorLocation.line());
                String oldCursorLine = firstLine.substring(0, oldCursorLocation.column());

                if (textLines.length > 1)
                    oldCursorLine += lines.get(oldCursorLocation.line() + textLines.length - 1).substring(textLines[textLines.length - 1].length());

                lines.set(oldCursorLocation.line(), oldCursorLine);
                lines.subList(oldCursorLocation.line() + 1, oldCursorLocation.line() + textLines.length).clear();

                if (deleteSelectionAction != null)
                    deleteSelectionAction.executeUndo();

                cursorLocation = oldCursorLocation;

                notifyTextObservers();
                notifyCursorObservers();
            }
        });
    }

    public char getCharacterAt(Location location) {
        return lines.get(location.line()).charAt(location.column());
    }

    public void setCharacterAt(Location location, char character) {
        String oldLine = lines.get(location.line());
        String newLine = oldLine.substring(0, location.column()) + character + oldLine.substring(location.column() + 1);
        lines.set(location.line(), newLine);
        notifyTextObservers();
    }

    private String getTextFromRange(LocationRange range) {
        if (range.left().line() == range.right().line())
            return lines.get(range.left().line()).substring(range.left().column(), range.right().column());

        StringBuilder sb = new StringBuilder();

        sb.append(lines.get(range.left().line()).substring(range.left().column()))
          .append('\n');

        for (int i = range.left().line() + 1; i < range.right().line(); i++) {
            sb.append(lines.get(i))
              .append('\n');
        }

        sb.append(lines.get(range.right().line()), 0, range.right().column());

        return sb.toString();
    }

    public void cut() {
        if (selectionRange == null)
            return;

        String text = getTextFromRange(selectionRange);
        deleteSelection();
        clipboardStack.push(text);
    }

    public void copy() {
        if (selectionRange == null)
            return;

        clipboardStack.push(getTextFromRange(selectionRange));
    }

    public void paste() {
        insert(clipboardStack.peek());
    }

    public void popPaste() {
        insert(clipboardStack.pop());
    }

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public void setSelectionRange(LocationRange range) {
        selectionRange = range;
        notifyCursorObservers();
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
        void updateCursorLocation(Location location, LocationRange selection);
    }

    public void addCursorObserver(CursorObserver observer) {
        cursorObservers.add(observer);
    }

    public boolean removeCursorObserver(CursorObserver observer) {
        return cursorObservers.remove(observer);
    }

    private void notifyCursorObservers() {
        for (CursorObserver observer : cursorObservers)
            observer.updateCursorLocation(cursorLocation, selectionRange);
    }

    public interface TextObserver {
        void updateText(List<String> lines);
    }

    public void addTextObserver(TextObserver observer) {
        textObservers.add(observer);
    }

    public boolean removeTextObserver(TextObserver observer) {
        return textObservers.remove(observer);
    }

    private void notifyTextObservers() {
        if (textObservers.isEmpty())
            return;

        List<String> unmodifiableLines = Collections.unmodifiableList(lines);

        for (TextObserver observer : textObservers)
            observer.updateText(unmodifiableLines);
    }
}
