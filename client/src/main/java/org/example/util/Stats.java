package org.example.util;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class Stats {
    private double count = 0;
    private double mean = 0;
    private double m2 = 0; // Variância acumulada (somatório das diferenças quadradas da média)

    public void addValue(double value) {
        count++;
        double delta = value - mean;
        mean += delta / count;
        double delta2 = value - mean;
        m2 += delta * delta2;
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        return count > 1 ? Math.sqrt(m2 / (count - 1)) : 0.0;
    }
}
