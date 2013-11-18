package de.uni_potsdam.hpi.datamining.exercise.zipfs_law;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CountThread extends Thread {
	private final Collector collector;
	private final File file;
	private final Map<String, Integer> results = new HashMap<>();

	public CountThread(Collector collector, File file) {
		this.collector = collector;
		this.file = file;
	}

	@Override
	public void run() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			Pattern p = Pattern.compile("[a-zA-Z]\\w+");
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				Matcher m = p.matcher(line);
				while (m.find()) {
					String word = m.group().toLowerCase();
					if (!results.containsKey(word)) {
						results.put(word, 1);
					} else {
						results.put(word, results.get(word) + 1);
					}
				}
				String[] words = line.split("\\s+");
				for (String word : words) {
					if (!word.isEmpty()) {

					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		collector.addResults(results);
		System.out.println("Finished " + file.getName());
	}
}
