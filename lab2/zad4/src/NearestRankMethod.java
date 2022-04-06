import java.util.Arrays;

public class NearestRankMethod implements PercentileCalculator{
    @Override
    public int getPercentile(int[] integers, int percentile) {
        int[] sorted = Arrays.copyOf(integers, integers.length);
        Arrays.sort(sorted);

        float fractionalIndex = sorted.length * percentile / 100.0f - 0.5f;
        return sorted[Math.round(fractionalIndex)];
    }
}
