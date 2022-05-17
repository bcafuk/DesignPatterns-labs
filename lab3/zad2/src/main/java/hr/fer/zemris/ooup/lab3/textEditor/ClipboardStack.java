package hr.fer.zemris.ooup.lab3.textEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class ClipboardStack {
    private final Stack<String> clipboardStack = new Stack<>();

    private final List<ClipboardObserver> clipboardObservers = new ArrayList<>();

    public void push(String text) {
        clipboardStack.push(text);
        notifyClipboardObservers();
    }

    public String pop() {
        String item = clipboardStack.pop();
        notifyClipboardObservers();

        return item;
    }

    public String peek() {
        return clipboardStack.peek();
    }

    public boolean isEmpty() {
        return clipboardStack.isEmpty();
    }

    public void clear() {
        clipboardStack.clear();
        notifyClipboardObservers();
    }

    public interface ClipboardObserver {
        void updateClipboard();
    }

    public void addClipboardObserver(ClipboardObserver observer) {
        clipboardObservers.add(observer);
    }

    public boolean removeClipboardObserver(ClipboardObserver observer) {
        return clipboardObservers.remove(observer);
    }

    private void notifyClipboardObservers() {
        for (ClipboardObserver observer : clipboardObservers)
            observer.updateClipboard();
    }
}
