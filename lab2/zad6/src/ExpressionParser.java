import java.util.function.IntBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    private static final Pattern constantPattern = Pattern.compile("\\A[0-9]+");
    private static final Pattern referencePattern = Pattern.compile("\\A[A-Z]+[0-9]+");

    private CharSequence sequence;

    public ExpressionParser(String string) {
        this.sequence = string;
    }

    public Expression parse() {
        Expression expression = parseExpression();

        if (!sequence.isEmpty())
            throw new RuntimeException("Expected EOF, but got " + sequence.charAt('0'));

        return expression;
    }

    private Expression parseExpression() {
        return parseAdditiveExpression();
    }

    private Expression parseAdditiveExpression() {
        Expression left = parseAtomic();

        while (!sequence.isEmpty()) {
            IntBinaryOperator operator = switch (sequence.charAt(0)) {
                case '+' -> (l, r) -> l + r;
                case '-' -> (l, r) -> l - r;
                default -> null;
            };

            if (operator == null)
                break;

            sequence = sequence.subSequence(1, sequence.length());
            Expression right = parseAtomic();
            left = new BinaryOperatorExpression(operator, left, right);
        }

        return left;
    }

    private Expression parseAtomic() {
        Matcher refMatcher = referencePattern.matcher(sequence);
        Matcher constMatcher = constantPattern.matcher(sequence);

        if (refMatcher.find())
            return parseReference(refMatcher);
        if (constMatcher.find())
            return parseConstant(constMatcher);

        throw new RuntimeException("Expected reference or constant");
    }

    private ReferenceExpression parseReference(Matcher matcher) {
        CharSequence literal = sequence.subSequence(0, matcher.end());
        sequence = sequence.subSequence(matcher.end(), sequence.length());

        return new ReferenceExpression(literal.toString());
    }

    private ConstantExpression parseConstant(Matcher matcher) {
        CharSequence literal = sequence.subSequence(0, matcher.end());
        sequence = sequence.subSequence(matcher.end(), sequence.length());

        return new ConstantExpression(Integer.parseInt(literal, 0, literal.length(), 10));
    }
}
