package net.preibisch.ijannot.controllers.tasks;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import ij.IJ;
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
import net.preibisch.ijannot.util.Default;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Log;

public class TrainDataGenerator {
	private static List<String> log;
	private static List<String> total;
	private static File folder;
	private static List<String> positions;
	

	public static void start() throws IOException {
		folder = new File(new File(ImgManager.get().getFolder()).getParent(), "blocks_25");
		IOFunctions.mkdir(folder);

		log = new ArrayList<>();
		total = new ArrayList<>();
		positions = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	private static void next() {
		while (ImgManager.get().hasNext()) {
			String path = ImgManager.get().next();
			IOFunctions.println("File to process: "+path);
			process(new File(path));
		}
		 IOFunctions.generateCSV(log, new File(folder,"log.txt"),true);
		 IOFunctions.generateCSV(positions, new File(folder,Default.DOT_POSITIONS_CSV_FILE),true);
		 Log.print("Done with all files");
	}

	private static void process(File csv) {
		File tif = new File(csv.getParentFile().getParent(),FilenameUtils.removeExtension(csv.getName()) + "." + Ext.TIFF);
		IOFunctions.println("Tif to process: "+tif.getAbsolutePath());
		ImagePlus imp = ImgPlusProc.getComposite(tif.getAbsolutePath());
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		List<Point2D> list = getPointFromFile(csv);
		int i = 0;
		for (Point2D p : list) {
			try{RandomAccessibleInterval<UnsignedByteType> block = block(image,p,Default.blockSize );
			String fileName = FilenameUtils.removeExtension(csv.getName()) +"_"+i +"." + Ext.TIFF;
			String imPath = new File(folder,fileName).getAbsolutePath();
			ImagePlus im = ImageJFunctions.wrap(block,imPath );
			Point dot = getPoint(p,Default.blockSize,image);
			positions.add(IOFunctions.toAnnotString(fileName, dot));
			IJ.save(im, imPath);
			i++;
			}catch(Exception exc) {
				String l = "Error: "+p.toString() +"  "+i + tif.getName();
				log.add(l);
			}
		}
	}

	private static Point getPoint(Point2D p, int marge, Img<UnsignedByteType> image) {
		int posX = getCenter(p.getX(),marge,image.dimension(0));
		int posY = getCenter(p.getY(),marge,image.dimension(1));
		return new Point(posX, posY);
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
	

	private static int getCenter(double p, int marge, long size) {
		int min = (int) (p- marge);
		int max = (int) ((p + marge)-size);
		return (min<0)?marge + min:((max>0)?max+marge:marge);
	}

	private static List<Point2D> getPointFromFile(File csv) {
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
