import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    private static final Pattern constantPattern = Pattern.compile("\\A[0-9]+");
    private static final Pattern referencePattern = Pattern.compile("\\A[A-Z]+[0-9]+");

    private static final List<Map<Character, IntBinaryOperator>> binaryOperators = List.of(
            Map.of('^', (l, r) -> (int) Math.pow(l, r)),
            Map.of(
                    '*', (l, r) -> l * r,
                    '/', (l, r) -> l / r,
                    '%', (l, r) -> l % r
            ),
            Map.of(
                    '+', (l, r) -> l + r,
                    '-', (l, r) -> l - r
            )
    );

    private static final Map<Character, IntUnaryOperator> prefixOperators = Map.of(
            '+', i -> i,
            '-', i -> -i
    );

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
        return parseBinaryExpression(binaryOperators.size() - 1);
    }

    private Expression parseBinaryExpression(int precedence) {
        if (precedence < 0)
            return parsePrefixExpression();

        Expression left = parseBinaryExpression(precedence - 1);

        while (!sequence.isEmpty()) {
            IntBinaryOperator operator = binaryOperators.get(precedence)
                                                        .get(sequence.charAt(0));

            if (operator == null)
                break;

            sequence = sequence.subSequence(1, sequence.length());
            Expression right = parseBinaryExpression(precedence - 1);
            left = new BinaryOperatorExpression(operator, left, right);
        }

        return left;
    }

    private Expression parsePrefixExpression() {
        IntUnaryOperator operator = prefixOperators.get(sequence.charAt(0));

        if (operator == null)
            return parseAtomic();

        sequence = sequence.subSequence(1, sequence.length());
        return new UnaryOperatorExpression(operator, parsePrefixExpression());
    }

    private Expression parseAtomic() {
        Matcher refMatcher = referencePattern.matcher(sequence);
        if (refMatcher.find())
            return parseReference(refMatcher);

        Matcher constMatcher = constantPattern.matcher(sequence);
        if (constMatcher.find())
            return parseConstant(constMatcher);

        if (sequence.charAt(0) == '(') {
            sequence = sequence.subSequence(1, sequence.length());
            Expression parenthesised = parseExpression();
            if (sequence.isEmpty() || sequence.charAt(0) != ')')
                throw new RuntimeException("Expected a closing parenthesis");
            sequence = sequence.subSequence(1, sequence.length());
            return parenthesised;
        }

        throw new RuntimeException("Expected reference, constant, or open parenthesis");
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
