import java.util.List;
import java.util.Objects;

public record ReferenceExpression(String address) implements Expression {
    public ReferenceExpression {
        Objects.requireNonNull(address);
    }

    @Override
    public int evaluate(Sheet sheet) {
        return sheet.cell(address).getValue();
    }

    @Override
    public List<String> getReferences() {
        return List.of(address);
    }
}
