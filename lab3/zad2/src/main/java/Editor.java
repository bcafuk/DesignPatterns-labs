import javax.swing.*;
import java.awt.*;

public final class Editor extends JFrame {
    public Editor() {
        setSize(800, 600);
        setTitle("Text editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();

        TextEditor textEditor = new TextEditor("Hello\nworld\n\nLorem ipsum\ndolor sit amet.\n");
        textEditor.setOpaque(true);
        textEditor.setForeground(Color.WHITE);
        textEditor.setBackground(Color.BLACK);
        textEditor.setFont(new Font("Ubuntu Mono", Font.PLAIN, 16));
        cp.add(textEditor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Editor();
            frame.setVisible(true);
        });
    }
}
