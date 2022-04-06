public class SequentialGenerator implements IntegerGenerator {
    private final int min;
    private final int max;
    private final int step;

    public SequentialGenerator(int min, int max, int step) {
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public int[] generate() {
        int elementCount = (max-min) / step;
        int[] elements = new int[elementCount];

        for (int i = 0; i < elementCount; i++) {
            elements[i] = min + i * step;
        }

        return elements;
    }
}
