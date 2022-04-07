import java.util.List;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class UnaryOperatorExpression implements Expression {
    private final IntUnaryOperator operator;
    private final Expression operand;

    public UnaryOperatorExpression(IntUnaryOperator operator, Expression operand) {
        this.operator = Objects.requireNonNull(operator);
        this.operand = Objects.requireNonNull(operand);
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
