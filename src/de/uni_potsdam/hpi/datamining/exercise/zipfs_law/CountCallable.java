package de.uni_potsdam.hpi.datamining.exercise.zipfs_law;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountCallable implements Callable<Map<String, Integer>> {
	private final File file;

	public CountCallable(File file) {
		this.file = file;
	}

	@Override
	public Map<String, Integer> call() throws IOException {
		Map<String, Integer> results = new HashMap<>();
		BufferedReader br;
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
		return results;
	}
}
