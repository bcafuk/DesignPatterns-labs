public class FibonacciGenerator implements IntegerGenerator {
    private final int elementCount;

    public FibonacciGenerator(int elementCount) {
        this.elementCount = elementCount;
    }

    @Override
    public int[] generate() {
        int[] elements = new int[elementCount];

        if (elementCount >= 1) {
            elements[0] = 1;
        }
        if (elementCount >= 2) {
            elements[1] = 1;
        }

        for (int i = 2; i < elementCount; i++) {
            elements[i] = elements[i - 2] + elements[i - 1];
        }

        return elements;
    }
}
