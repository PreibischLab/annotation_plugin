package net.preibisch.ijannot.controllers;

import java.io.File;

import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.PlugIn;
import net.preibisch.ijannot.controllers.listners.KeyboardClick;
import net.preibisch.ijannot.controllers.listners.MouseClick;
import net.preibisch.ijannot.controllers.managers.AnnotationManager;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijnnot.models.Ext;

public class Main implements PlugIn {

	@Override
	public void run(String path) {

		if (!new File(path).exists()) {
			IOFunctions.println("Invalid Path file");
			return;
		}

		final ImagePlus imp = new Opener().openImage(path);
		imp.show();
		imp.getCanvas().addMouseListener(new MouseClick());
		imp.getCanvas().addKeyListener(new KeyboardClick());

		// Test roi
		AnnotationManager.add(imp.getRoi());
//		rm.addRoi(imp.getRoi());
		
		
		imp.setRoi(10, 10, 200, 200);
		IOFunctions.println("raw: " + imp.getRoi().toString());
		imp.saveRoi();

	}

	public static void main(String[] args) throws Exception {
		// String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/C1-ND8_DIV0+4h_20x_noConfinment_8_ch_4.tif";
//		String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/raw_Tiff/ND8_DIV0+4h_20x_noConfinment_8_ch_4.tif";
		String folder = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/raw_Tiff/";

		ImgManager.init(folder, Ext.TIFF);
		String path = ImgManager.get().next();
		System.out.println(path);
		path = ImgManager.get().next();
		System.out.println(path);
//		new ImageJ();

//		new Main().run(path);
	}
}
