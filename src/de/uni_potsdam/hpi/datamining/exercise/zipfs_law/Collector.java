package de.uni_potsdam.hpi.datamining.exercise.zipfs_law;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class Collector {
	public static final String DIRECTORY = "/home/fredrik/Downloads/OANC-GrAF/data/written_2/travel_guides/berlitz1";
	public static final String OUTPUT = "output.csv";
	public static final String[] EXT = { "txt" };

	private final int maxThreads = Runtime.getRuntime().availableProcessors();
	private final Map<String, Integer> results = new HashMap<>();
	private int activeThreads = 0;
	
	public Collector() {
		File directory = new File(DIRECTORY);
		for (File file : FileUtils.listFiles(directory, EXT, true)) {
			startThread(file);
		}
		
		waitForFinish();
		writeOutput();
	}
	
	private synchronized void waitForFinish() {
		while (activeThreads > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeOutput() {
		PrintWriter out;
		try {
			out = new PrintWriter(OUTPUT);
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
			if (!results.containsKey(string)) {
				results.put(string, count);
			} else {
				results.put(string, results.get(string) + count);
			}
		}
		activeThreads--;
		notify();
	}

	private synchronized void startThread(File file) {
		if (activeThreads >= maxThreads) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activeThreads++;
		new CountThread(this, file).start();
	}

	public static void main(String[] args) {
		new Collector();
	}
}
