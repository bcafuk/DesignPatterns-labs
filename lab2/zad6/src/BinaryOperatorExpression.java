import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntBinaryOperator;

public class BinaryOperatorExpression implements Expression {
    private final IntBinaryOperator operator;
    private final Expression leftChild;
    private final Expression rightChild;

    public BinaryOperatorExpression(IntBinaryOperator operator, Expression leftChild, Expression rightChild) {
        this.operator = Objects.requireNonNull(operator);
        this.leftChild = Objects.requireNonNull(leftChild);
        this.rightChild = Objects.requireNonNull(rightChild);
    }

    @Override
    public int evaluate(Sheet sheet) {
        return operator.applyAsInt(leftChild.evaluate(sheet), rightChild.evaluate(sheet));
    }

    @Override
    public List<String> getReferences() {
        List<String> references = new ArrayList<>(leftChild.getReferences());
        references.addAll(rightChild.getReferences());
        return references;
    }
}
