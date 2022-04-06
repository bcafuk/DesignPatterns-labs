import java.util.Arrays;

public class LinearInterpolationMethod implements PercentileCalculator{
    @Override
    public int getPercentile(int[] integers, int percentile) {
        int[] sorted = Arrays.copyOf(integers, integers.length);
        Arrays.sort(sorted);

        float fractionalIndex = sorted.length * percentile / 100.0f - 0.5f;

        if (fractionalIndex <= 0) {
            return sorted[0];
        }
        if (fractionalIndex >= sorted.length - 1) {
            return sorted[sorted.length - 1];
        }

        int lowerIndex = (int) Math.floor(fractionalIndex);
        int upperIndex = lowerIndex + 1;

        float t = fractionalIndex % 1.0f;
        return Math.round((1.0f - t) * sorted[lowerIndex] + t * sorted[upperIndex]);
    }
}
