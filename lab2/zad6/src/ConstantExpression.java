import java.util.List;

public class ConstantExpression implements Expression {
    private final int value;

    public ConstantExpression(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(Sheet sheet) {
        return value;
    }

    @Override
    public List<String> getReferences() {
        return List.of();
    }
}
