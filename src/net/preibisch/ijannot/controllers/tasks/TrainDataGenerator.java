package net.preibisch.ijannot.controllers.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import ij.ImagePlus;
import javafx.geometry.Point2D;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.ImgPlusProc;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Log;

public class TrainDataGenerator {
	private static List<String> log;
	private static List<String> total;
	private static File folder;

	public static void start() throws IOException {
		folder = new File(ImgManager.get().getFolder(), "blocks");
		IOFunctions.mkdir(folder);

		log = new ArrayList<>();
		total = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	private static void next() {
		while (ImgManager.get().hasNext()) {
			String path = ImgManager.get().next();
			process(path);
			return;
		}
		// File logFile = new File(ImgManager.get().getFolder(),"log.txt");
		// IOFunctions.generateCSV(log, logFile);
		// File totalFile = new File(ImgManager.get().getFolder(),"total.csv");
		// IOFunctions.generateCSV(total, totalFile);
		// Log.print("Done with all files");
	}

	private static void process(String csv) {

		File tif = new File(FilenameUtils.removeExtension(csv) + "." + Ext.TIFF);
		ImagePlus imp = ImgPlusProc.getChannel(tif.toString(), 2);
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		List<Point2D> list = getPointFromFile(csv);
		int i = 0;
		for (Point2D p : list) {
			RandomAccessibleInterval<UnsignedByteType> block = block(image,p,20);
			ImageJFunctions.show(block);
			if(i>5) return;
			i++;
		}
	}

	private static RandomAccessibleInterval<UnsignedByteType> block(Img<UnsignedByteType> image, Point2D p, int marge) {
		int x1 = Math.max(0, (int)(p.getX()-marge));
		int y1 = Math.max(0, (int)(p.getY()-marge));
		int x2 = Math.min((int)image.dimension(0), (int)(p.getX()+marge));
		int y2 = Math.min((int)image.dimension(1), (int)(p.getY()+marge));
		System.out.println(x1+"-"+y1+"  "+x2+"-"+y2);
		RandomAccessibleInterval< UnsignedByteType > view =
                Views.interval( image, new long[] { x1, y1 }, new long[]{ x2, y2 } );
		return view;
	}

	private static List<Point2D> getPointFromFile(String csv) {
		List<Point2D> list = new ArrayList<>();
		try (BufferedReader csvReader = new BufferedReader(new FileReader(csv))) {
			String row = csvReader.readLine();
			int i = 0;
			while ((row = csvReader.readLine()) != null) {
				System.out.println((i++) + ": " + row);
				String[] data = row.split(",");
				double x = Double.parseDouble(data[0]);
				double y = Double.parseDouble(data[1]);
				list.add(new Point2D(x, y));
			}
			csvReader.close();
		} catch (Exception e) {
			Log.error("Error: " + e.toString());
		}
		return list;
	}

}
