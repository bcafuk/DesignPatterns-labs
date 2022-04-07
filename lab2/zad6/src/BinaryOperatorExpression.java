import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntBinaryOperator;

public record BinaryOperatorExpression(IntBinaryOperator operator,
                                       Expression leftOperand,
                                       Expression rightOperand) implements Expression {
    public BinaryOperatorExpression {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(leftOperand);
        Objects.requireNonNull(rightOperand);
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
