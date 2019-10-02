package util;

import javax.swing.SwingUtilities;

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

}
