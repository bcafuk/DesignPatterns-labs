import java.util.List;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public record UnaryOperatorExpression(IntUnaryOperator operator,
                                      Expression operand) implements Expression {
    public UnaryOperatorExpression {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operand);
    }

    @Override
    public int evaluate(Sheet sheet) {
        return operator.applyAsInt(operand.evaluate(sheet));
    }

    @Override
    public List<String> getReferences() {
        return operand.getReferences();
    }
}
