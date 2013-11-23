package de.uni_potsdam.hpi.datamining.exercise.zipfs_law;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

public class Collector {
	public static final String DIRECTORY = "/home/fredrik/Downloads/OANC-GrAF/data/written_2/travel_guides/berlitz1";
	public static final String OUTPUT = "output.csv";
	public static final String[] EXT = { "txt" };

	private final int maxThreads = Runtime.getRuntime().availableProcessors();
	private final Map<String, Integer> results = new HashMap<>();

	public Collector(String directoryName, String[] extensions,
			String outputName) throws InterruptedException, ExecutionException {
		File directory = new File(directoryName);

		List<CountCallable> callables = new ArrayList<>();
		for (File file : FileUtils.listFiles(directory, extensions, true)) {
			callables.add(new CountCallable(file));
		}

		ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
		for (Future<Map<String, Integer>> future : executor
				.invokeAll(callables)) {
			addResults(future.get());
		}

		executor.shutdown();
		writeOutput(outputName);
	}

	private void writeOutput(String outputName) {
		PrintWriter out;
		try {
			out = new PrintWriter(outputName);
			for (Entry<String, Integer> result : results.entrySet()) {
				out.println(result.getKey() + "," + result.getValue());
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized void addResults(Map<String, Integer> threadResults) {
		for (Entry<String, Integer> entry : threadResults.entrySet()) {
			String string = entry.getKey();
			Integer count = entry.getValue();
			Integer oldCount = results.get(string);
			if (oldCount == null) {
				results.put(string, count);
			} else {
				results.put(string, oldCount + count);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		new Collector(DIRECTORY, EXT, OUTPUT);
	}
}
