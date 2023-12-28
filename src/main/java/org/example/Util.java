package org.example;

import java.util.ArrayList;

public class Util {
    public static double calculateVariance(ArrayList<Double> values) {
        if (values == null || values.size() < 2) {
            throw new IllegalArgumentException("Insufficient data for variance calculation.");
        }

        // Calculate the mean
        double mean = calculateMean(values);

        // Calculate the sum of squared differences from the mean
        double sumSquaredDifferences = 0.0;
        for (double value : values) {
            double difference = value - mean;
            sumSquaredDifferences += difference * difference;
        }

        // Calculate the variance
        double variance = sumSquaredDifferences / values.size();

        return variance;
    }

    // Function to calculate the mean
    public static double calculateMean(ArrayList<Double> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate mean for empty or null list.");
        }

        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }

        return sum / values.size();
    }
}
