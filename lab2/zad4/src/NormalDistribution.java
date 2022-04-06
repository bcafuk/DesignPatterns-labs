import java.util.Random;

public class NormalDistribution implements IntegerGenerator {
    private final double mean;
    private final double stdDev;
    private final int elementCount;

    public NormalDistribution(double mean, double stdDev, int elementCount) {
        this.mean = mean;
        this.stdDev = stdDev;
        this.elementCount = elementCount;
    }

    @Override
    public int[] generate() {
        Random random = new Random();
        int[] elements = new int[elementCount];

        for (int i = 0; i < elementCount; i++) {
            elements[i] = (int) Math.round(random.nextGaussian() * stdDev + mean);
        }

        return elements;
    }
}
