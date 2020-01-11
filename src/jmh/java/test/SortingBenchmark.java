package test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@State(Scope.Thread)
public class SortingBenchmark {

    private int length = 100000;

    private Distribution distribution = Distribution.RANDOM;

    private int[] array;

    int i = 1;

    @Setup
    public void fill() {
        int[] tmp = distribution.create(length);
        array = Arrays.copyOf(tmp, tmp.length);
    }

    public enum Distribution {
        SAWTOOTH {
            @Override
            int[] create(int length) {
                int[] result = new int[length];
                for (int i = 0; i < length; i += 5) {
                    result[i] = 0;
                    result[i + 1] = 1;
                    result[i + 2] = 2;
                    result[i + 3] = 3;
                    result[i + 4] = 4;
                }
                return result;
            }
        },
        INCREASING {
            @Override
            int[] create(int length) {
                int[] result = new int[length];
                for (int i = 0; i < length; i++) {
                    result[i] = i;
                }
                return result;
            }
        },
        DECREASING {
            @Override
            int[] create(int length) {
                int[] result = new int[length];
                for (int i = 0; i < length; i++) {
                    result[i] = length - i;
                }
                return result;
            }
        },
        RANDOM {
            @Override
            int[] create(int length) {
                Random random = new Random();
                int[] result = new int[length];
                for (int i = 0; i < length; i++) {
                    result[i] = random.nextInt();
                }
                return result;
            }
        };

        abstract int[] create(int length);
    }

    @Benchmark
    public int timeHeapSort() {
        int[] sorted = Sorter.heapSort(array);
        return sorted[i];
    }

    @Benchmark
    public int timeMergeSort() {
        int[] sorted = Sorter.mergeSort(array);
        return sorted[i];
    }

    @Benchmark
    public int timeQuickSort() {
        int[] sorted = Sorter.quickSort(array);
        return sorted[i];
    }

    @Benchmark
    public int timeInsertionSort() {
        int[] sorted = Sorter.insertionSort(array);
        return sorted[i];
    }

    @Benchmark
    public int timeJDKSort() {
        Arrays.sort(array);
        return array[i];
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(".*" + SortingBenchmark.class.getSimpleName() + ".*").forks(1)
                .build();

        new Runner(opt).run();
    }

}