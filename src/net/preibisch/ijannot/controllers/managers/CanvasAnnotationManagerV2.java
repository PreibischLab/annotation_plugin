package net.preibisch.ijannot.controllers.managers;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import net.preibisch.ijannot.controllers.listners.KeyboardClickV2;
import net.preibisch.ijannot.util.Default;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.view.AnnotationCategoriesView;

public class CanvasAnnotationManagerV2 {
	// private static Integer currentAnnot;
	private static List<String> allAnnot;
	private static String path;
	private static ImagePlus imp;
	private static File csv;
	private static Map<String, Point> positions;

	
	public static void start() throws IOException {
		csv = new File(ImgManager.get().getFolder(), Default.ANNOTARION_CSV_FILE);
		Map<String, Integer> list;
		
		File positionFile = new File(ImgManager.get().getFolder(), Default.ANNOTARION_CSV_FILE);
		if(positionFile.exists()) {
			positions = IOFunctions.getCSVDots(positionFile);
		}
		
		if (csv.exists()) {
			list = IOFunctions.getCSV(csv);
			ImgManager.get().ignore(list.keySet());
			getupdateTotals(list.values());
		} else
			list = new HashMap<>();
		allAnnot = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	private static void getupdateTotals(Collection<Integer> values) {
		
		try{
			List<Integer> totals =  new ArrayList<Integer>(Collections.nCopies(5, 0));
		
		Integer total = values.size();
		for(Integer value : values)
			totals.set(value, totals.get(value)+1);
		AnnotationCategoriesView.updateTotals(totals, total);
		}catch(Exception e) {
			IOFunctions.println(e.toString());
		}
	}

	public static void next() {
		try {
			if (imp != null)
				imp.close();
			if (ImgManager.get().hasNext()) {
				path = ImgManager.get().next();

				IOFunctions.println("Current: " + path);
				Point dot = positions.get(new File(path).getName());
				if(dot==null)
					dot = new Point(Default.blockSize, Default.blockSize);
				imp = new Opener().openImage(path);
				imp.show();
				imp.setRoi(new Rectangle(dot.x,dot.y,4,4));
				for (int i = 0; i < 4; i++)
					IJ.run("In [+]", "");
				imp.getCanvas().addKeyListener(new KeyboardClickV2());
			} else {
				exit();
			}
		} catch (Exception e) {
			exit();
		}
	}

	public static void exit() {
		IOFunctions.generateCSV(allAnnot, csv,false);
		IOFunctions.println("All done ! Generate file");
		if (imp != null)
			imp.close();
	}

	public static void add(int category) {
		allAnnot.add("\"" + new File(path).getName() + "\"" + "," + category + "\n");
	}

	public static void undo() {
		allAnnot.remove(allAnnot.size() - 1);
		ImgManager.get().undo();
		next();
	}

}
