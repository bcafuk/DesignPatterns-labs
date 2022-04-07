import java.util.List;

public interface Expression {
    int evaluate(Sheet sheet);

    List<String> getReferences();
}
