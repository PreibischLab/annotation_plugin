package net.preibisch.ijannot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.SwingUtilities;

import ij.IJ;
import net.preibisch.ijannot.models.Annot;

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

	public static void generateCSV(List<Annot> allAnnot, File file) {
		println("creating CSV: " + file.getAbsolutePath());
		
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			Log.error(e.toString());
			return;
		}
			
		try (FileWriter csvWriter = new FileWriter(file)) {

			for (Annot a : allAnnot) {
				String s = a.toString();
				csvWriter.append(s);
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			Log.error(e.toString());
		}
	}

}
