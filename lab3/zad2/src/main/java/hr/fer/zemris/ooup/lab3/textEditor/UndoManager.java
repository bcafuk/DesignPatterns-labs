package hr.fer.zemris.ooup.lab3.textEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class UndoManager {
    private final Stack<EditAction> undoStack = new Stack<>();
    private final Stack<EditAction> redoStack = new Stack<>();

    private final List<UndoObserver> undoObservers = new ArrayList<>();

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        EditAction action = undoStack.pop();
        redoStack.push(action);

        action.executeUndo();

        notifyUndoObservers();
    }

    public void redo() {
        EditAction action = redoStack.pop();
        undoStack.push(action);

        action.executeDo();

        notifyUndoObservers();
    }

    public void push(EditAction action) {
        action.executeDo();

        undoStack.push(action);
        redoStack.clear();

        notifyUndoObservers();
    }

    public interface EditAction {
        void executeDo();

        void executeUndo();
    }

    public interface UndoObserver {
        void stacksChanged(boolean canUndo, boolean canRedo);
    }

    public void addUndoObserver(UndoObserver observer) {
        undoObservers.add(observer);
    }

    public boolean removeUndoObserver(UndoObserver observer) {
        return undoObservers.remove(observer);
    }

    private void notifyUndoObservers() {
        for (UndoObserver observer : undoObservers)
            observer.stacksChanged(this.canUndo(), this.canRedo());
    }
}
