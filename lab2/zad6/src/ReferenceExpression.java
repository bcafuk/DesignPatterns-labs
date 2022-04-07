import java.util.List;
import java.util.Objects;

public class ReferenceExpression implements Expression {
    private final String address;

    public ReferenceExpression(String address) {
        this.address = Objects.requireNonNull(address);
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
