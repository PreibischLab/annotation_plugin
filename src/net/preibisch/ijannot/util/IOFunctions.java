package net.preibisch.ijannot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import ij.IJ;

public class IOFunctions {
	public static boolean printIJLog = true;

	public static void println(final String string) {
		if (printIJLog) {
			if (SwingUtilities.isEventDispatchThread())
				IJ.log(string);
			else
				SwingUtilities.invokeLater(() -> IJ.log(string));
		} else
			System.out.println(string);
	}

	public static <T> void generateCSV(List<T> list, File file, Boolean resave) {
		println("creating CSV: " + file.getAbsolutePath());
		if (resave)
			if (file.exists()) {
				try {
					Files.delete(file.toPath());
				} catch (IOException e) {
					Log.error(e.toString());
					return;
				}
			}

		try (FileWriter csvWriter = new FileWriter(file, true)) {

			for (Object a : list) {
				String s = a.toString();
				csvWriter.append(s);
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			Log.error(e.toString());
		}
	}

	public static void mkdir(File file) {
		try {
			if (file.exists())
				FileUtils.deleteDirectory(file);
			file.mkdir();
		} catch (IOException e) {
			Log.error("Can't delete folder : " + e.toString());
		}
	}

	public static Map<String, Integer> getCSV(File f) throws IOException {
		Map<String, Integer> result = new HashMap<>();

		BufferedReader csvReader = new BufferedReader(new FileReader(f));
		String row;
		while ((row = csvReader.readLine()) != null) {
			String[] data = row.split(",");
			String key = data[0].substring(1, data[0].length() - 1);
			// System.out.println(key);
			result.put(key, Integer.parseInt(data[1]));
		}
		csvReader.close();
		return result;
	}

	public static void main(String[] args) throws IOException {
		getCSV(new File("/Users/Marwan/Desktop/block_annotation.csv"));
	}

}
