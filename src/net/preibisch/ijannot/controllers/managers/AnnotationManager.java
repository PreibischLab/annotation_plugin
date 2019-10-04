package net.preibisch.ijannot.controllers.managers;

import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.preibisch.ijannot.util.IOFunctions;

public class AnnotationManager {
	private static RoiManager rm = null;

	public static void add(Roi roi) {
		// rm = RoiManager.getInstance();
		if (rm == null)
			rm = new RoiManager();
		rm.addRoi(roi);
		IOFunctions.println("RM added, new total : " + rm.getCount());
		// rm.removeAll();
		// Test roi
		// AnnotationManager.add(imp.getRoi());
		// rm.addRoi(imp.getRoi());
		// IOFunctions.println("raw: " + imp.getRoi().toString());
		// imp.saveRoi();
	}

	private AnnotationManager() {
		throw new IllegalStateException("Utility class");
	}
}
