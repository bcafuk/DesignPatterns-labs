import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntBinaryOperator;

public class BinaryOperatorExpression implements Expression {
    private final IntBinaryOperator operator;
    private final Expression leftOperand;
    private final Expression rightOperand;

    public BinaryOperatorExpression(IntBinaryOperator operator, Expression leftOperand, Expression rightOperand) {
        this.operator = Objects.requireNonNull(operator);
        this.leftOperand = Objects.requireNonNull(leftOperand);
        this.rightOperand = Objects.requireNonNull(rightOperand);
    }

    @Override
    public int evaluate(Sheet sheet) {
        return operator.applyAsInt(leftOperand.evaluate(sheet), rightOperand.evaluate(sheet));
    }

    @Override
    public List<String> getReferences() {
        List<String> references = new ArrayList<>(leftOperand.getReferences());
        references.addAll(rightOperand.getReferences());
        return references;
    }
}
