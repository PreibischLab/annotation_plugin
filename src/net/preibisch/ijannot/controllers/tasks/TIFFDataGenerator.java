package net.preibisch.ijannot.controllers.tasks;

import java.io.File;
import java.io.IOException;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.ImgPlusProc;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Log;

public class TIFFDataGenerator {
	private static File folder;

	public static void start() throws IOException {
		folder = new File(new File(ImgManager.get().getFolder()).getParent(), "RAW_TIFF");
		IOFunctions.mkdir(folder);

		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	private static void next() {
		while (ImgManager.get().hasNext()) {
			String path = ImgManager.get().next();
			IOFunctions.println("File to process: " + path);
			process(new File(path));
		}
		Log.print("Done with all files");
	}

	private static void process(File file) {
		IOFunctions.println("Tif to process: " + file.getAbsolutePath());
		ImagePlus imp = ImgPlusProc.getComposite(file.getAbsolutePath());
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		try {
			String imPath = new File(folder, file.getName()).getAbsolutePath();
			IJ.save(imp, imPath);
		} catch (Exception exc) {
			String l = "Error: " + exc.toString() + "  " + file.getName();
		}
	}

}
