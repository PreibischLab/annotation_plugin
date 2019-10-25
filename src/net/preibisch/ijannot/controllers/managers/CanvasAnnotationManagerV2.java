package net.preibisch.ijannot.controllers.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ij.ImagePlus;
import ij.io.Opener;
import net.preibisch.ijannot.controllers.listners.KeyboardClickV2;
import net.preibisch.ijannot.util.IOFunctions;

public class CanvasAnnotationManagerV2 {
//	private static  Integer currentAnnot;
	private static List<String> allAnnot;
	private static String path;
	private static ImagePlus imp;

	private static final String CSV_FILE = "block_annotation.csv";

	public static void start() throws IOException {
		allAnnot = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	public static void next() {
		if (imp != null)
			imp.close();
		if (ImgManager.get().hasNext()) {
			path = ImgManager.get().next();
			IOFunctions.println("Current: " + path);
			imp = new Opener().openImage(path);
			imp.show();
//			imp.getCanvas().addMouseListener(new MouseClick());
			imp.getCanvas().addKeyListener(new KeyboardClickV2());
		} else {
			exit();
		}
	}

	public static void exit() {	
			File csv = new File(new File(path).getParent(), CSV_FILE);
			IOFunctions.generateCSV(allAnnot, csv);
			IOFunctions.println("All done ! Generate file");	
	}

	public static void add(int category) {
		allAnnot.add("\""+new File(path).getName()+"\""+","+category+"\n");
	}

	public static void undo() {

		allAnnot.remove(allAnnot.size()-1);
		ImgManager.get().undo();
		next();
	}

}
