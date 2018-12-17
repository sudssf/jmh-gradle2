package test;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

@State(Scope.Thread)
@OutputTimeUnit(NANOSECONDS)
@BenchmarkMode(AverageTime)
@Fork(value = 1, jvmArgsAppend = {
		"-XX:+UseSuperWord",
		"-XX:+UnlockDiagnosticVMOptions",
		"-XX:CompileCommand=print,*BenchmarkSIMDBlog.array1"})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class BenchmarkSIMDBlog
{
	public static final int SIZE = 1024;

	@State(Scope.Thread)
	public static class Context
	{
		public final int[] values = new int[SIZE];
		public final int[] results = new int[SIZE];

		@Setup
		public void setup()
		{
			Random random = new Random();
			for (int i = 0; i < SIZE; i++) {
				values[i] = random.nextInt(Integer.MAX_VALUE / 32);
			}
		}
	}

	@Benchmark
	@Fork(jvmArgsAppend = {"-XX:-UseSuperWord",  "-XX:CompileCommand=print,*BenchmarkSIMDBlog.increment*"})
	public int[] increment_NO_SIMD(Context context)
	{
		for (int i = 0; i < SIZE; i++) {
			context.results[i] = context.values[i] + 1;
		}
		return context.results;
	}

	@Benchmark
	@Fork(jvmArgsAppend = {"-XX:+UseSuperWord",  "-XX:CompileCommand=print,*BenchmarkSIMDBlog.increment*"})
	public int[] incrementSIMD(Context context)
	{
		for (int i = 0; i < SIZE; i++) {
			context.results[i] = context.values[i] + 1;
		}
		return context.results;
	}

	public static void main(String args[]) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(BenchmarkSIMDBlog.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}
}
