package test;


import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

@State(Scope.Thread)
public class SortBenchmark {

    private final int[] primitives = new int[10_000_000];

    @Setup
    public void fill() {
        Random rand = new Random();
        for (int i = 0; i < primitives.length; ++i) {
            primitives[i] = rand.nextInt();
        }
    }

    @Benchmark
    public int[] testSortObjects() {
        List<Integer> values = Lists.newArrayListWithCapacity(primitives.length);
        for (int i = 0; i < primitives.length; ++i) {
            values.add(primitives[i]);
        }
        Collections.sort(values);
        return Ints.toArray(values);
    }

    @Benchmark
    public int[] testSortObjectView() {
        List<Integer> values = Ints.asList(primitives.clone());
        Collections.sort(values);
        return Ints.toArray(values);
    }

    public static void main(String... args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(SortBenchmark.class.getSimpleName())
                .mode(Mode.AverageTime)
                .warmupIterations(3)
                .measurementIterations(7)
                .jvmArgs("-server")
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}