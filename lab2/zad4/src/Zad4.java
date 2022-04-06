public class Zad4 {
    public static void main(String[] args) {
        IntegerGenerator generator = new NormalDistribution(100.0, 100.0, 1_000_000);
        PercentileCalculator percentileCalculator = new LinearInterpolationMethod();

        DistributionTester distributionTester = new DistributionTester(generator, percentileCalculator);
        distributionTester.printPercentiles();
    }
}
