import hr.fer.zemris.ooup.lab3.textEditor.ClipboardStack;
import hr.fer.zemris.ooup.lab3.textEditor.TextEditorModel;
import hr.fer.zemris.ooup.lab3.textEditor.UndoManager;

import javax.swing.*;
import java.util.Iterator;
import java.util.regex.Pattern;

public final class StatsPlugin implements hr.fer.zemris.ooup.lab3.textEditor.Plugin {
    private final Pattern wordPattern = Pattern.compile("\\w+");

    @Override
    public String getName() {
        return "Statistics";
    }

    @Override
    public String getDescription() {
        return "Gathers information about the document";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        int lineCount = 0;
        int wordCount = 0;
        int charCount = 0;

        Iterator<String> it = model.allLines();
        while (it.hasNext()) {
            String line = it.next();

            lineCount++;
            wordCount += wordPattern.matcher(line).results().count();
            charCount += line.length();
        }

        String sb = "Lines: " + lineCount + "\nWords: " + wordCount + "\nCharacters: " + charCount;

        JOptionPane.showMessageDialog(null, sb, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
}
