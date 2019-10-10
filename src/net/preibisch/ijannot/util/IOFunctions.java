package net.preibisch.ijannot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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

	public static <T> void generateCSV(List<T> list, File file) {
		println("creating CSV: " + file.getAbsolutePath());
		if (file.exists()) {
			try {
				Files.delete(file.toPath());
			} catch (IOException e) {
				Log.error(e.toString());
				return;
			}
		}

		try (FileWriter csvWriter = new FileWriter(file)) {

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

}
