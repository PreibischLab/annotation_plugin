package net.preibisch.ijannot.controllers.managers;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ij.ImagePlus;
import ij.io.Opener;
import net.preibisch.ijannot.controllers.listners.KeyboardClick;
import net.preibisch.ijannot.controllers.listners.MouseClick;
import net.preibisch.ijannot.models.Annot;
import net.preibisch.ijannot.util.IOFunctions;

public class CanvasManager {
	private static Annot currentAnnot;
	private static List<Annot> allAnnot;
	private static String path;
	private static ImagePlus imp;

	public static void start() throws IOException {
		allAnnot = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	public static void next() {
		allAnnot.add(currentAnnot);
		if (ImgManager.get().hasNext()) {
			if (imp != null)
				imp.close();
			path = ImgManager.get().next();
			IOFunctions.println("Current: " + path);
			currentAnnot = new Annot(path);
			imp = new Opener().openImage(path);
			imp.show();
			imp.getCanvas().addMouseListener(new MouseClick());
			imp.getCanvas().addKeyListener(new KeyboardClick());
		}else {
			IOFunctions.println("All done ! Generate file");
		}
	}

	public static void addRect(Rectangle r) {
		IOFunctions.println("New: " + r.toString());
		currentAnnot.addRect(r);
	}

	public static void undo() {
		imp.deleteRoi();
		currentAnnot.undo();
	}

}
