public class DistributionTester {
    private final IntegerGenerator generator;
    private final PercentileCalculator percentileCalculator;

    public DistributionTester(IntegerGenerator generator, PercentileCalculator percentileCalculator) {
        this.generator = generator;
        this.percentileCalculator = percentileCalculator;
    }

    public void printPercentiles() {
        int[] numbers = generator.generate();

        for (int p = 10; p <= 90; p += 10) {
            System.out.printf("The %dth percentile is: %d\n", p, percentileCalculator.getPercentile(numbers, p));
        }
    }
}
