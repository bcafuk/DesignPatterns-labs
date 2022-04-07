import java.util.List;

public record ConstantExpression(int value) implements Expression {
    @Override
    public int evaluate(Sheet sheet) {
        return value;
    }

    @Override
    public List<String> getReferences() {
        return List.of();
    }
}
