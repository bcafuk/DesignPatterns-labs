import hr.fer.zemris.ooup.lab3.textEditor.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class TitleCasePlugin implements Plugin {
    private final Pattern wordBegin = Pattern.compile("\\b[a-z]");

    @Override
    public String getName() {
        return "Convert to title case";
    }

    @Override
    public String getDescription() {
        return "Capitalizes all words in the document";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        List<Location> nonCapitals = new ArrayList<>();

        int lineIndex= 0;
        Iterator<String> it = model.allLines();
        while (it.hasNext()) {
            String line = it.next();

            int finalLineIndex = lineIndex;
            wordBegin.matcher(line)
                     .results()
                     .map(mr -> new Location(finalLineIndex, mr.start()))
                     .forEach(nonCapitals::add);

            lineIndex++;
        }

        undoManager.push(new UndoManager.EditAction() {
            @Override
            public void executeDo() {
                for (Location location : nonCapitals)
                    model.setCharacterAt(location, Character.toUpperCase(model.getCharacterAt(location)));
            }

            @Override
            public void executeUndo() {
                for (Location location : nonCapitals)
                    model.setCharacterAt(location, Character.toLowerCase(model.getCharacterAt(location)));
            }
        });
    }
}
