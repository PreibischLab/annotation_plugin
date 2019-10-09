package net.preibisch.ijannot.controllers.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.EDM;
import ij.plugin.filter.ParticleAnalyzer;
import net.imagej.ops.OpService;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;
import net.preibisch.ijannot.controllers.tasks.AnalyzeTasks;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Log;
import net.preibisch.ijannot.util.Service;

public class AnalyzeManager {
	private static List<String> log;

	public static void start() throws IOException {
		log = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	private static void next() {
		while (ImgManager.get().hasNext()) {
			String path = ImgManager.get().next();
			ResultsTable rt = process(path);
			if (rt == null) {
				log.add("Empty: "+path);
			} else {
				save(rt, resultName(path));
			}
		}
		File logFile = new File(ImgManager.get().getFolder(),"log.txt");
		IOFunctions.generateCSV(log, logFile);
		Log.print("Done with all files");
	}

	private static void save(ResultsTable rt, File file) {
		List<String> list = new ArrayList<>();
		list.add("\"x\",\"y\"");

		for (int i = 0; i < rt.size(); i++) {
			double x = rt.getValueAsDouble(rt.getColumnIndex("XM"), i);
			double y = rt.getValueAsDouble(rt.getColumnIndex("YM"), i);

			StringBuilder bld = new StringBuilder();
			bld.append(x);
			bld.append(",");
			bld.append(y);
			bld.append("\n");
			list.add(bld.toString());
		}
		IOFunctions.generateCSV(list, file);
	}

	private static File resultName(String path) {
		File f = new File(path);
		return new File(f.getParent(), FilenameUtils.removeExtension(f.getName()) + ".csv");

	}

	private static ResultsTable process(String path) {
		OpService ops = Service.getOps();
		ImagePlus imp = ImgPlusProc.getChannel(path, 1);
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		// imp.show();

		// Gauss
		image = (Img<UnsignedByteType>) ops.filter().gauss(image, 3.0);

		// Threshold
		IterableInterval<BitType> maskBitType = ops.threshold().apply(image, new UnsignedByteType(15));
		Img<BitType> target = image.factory().imgFactory(new BitType()).create(image, new BitType());
		AnalyzeTasks.copy(target, maskBitType);

		// Watershed
		ImagePlus water = ImageJFunctions.wrap(target, "target");
		EDM edm = new EDM();
		edm.toWatershed(water.getProcessor());
		// water.show();

		// Invert
		final UnsignedByteType c = new UnsignedByteType();
		Img<UnsignedByteType> inv = ImageJFunctions.wrap(water);

		for (final UnsignedByteType t : inv) {

			c.set(t);
			int x = c.getInteger();
			if (x == 0)
				t.setInteger(255);
			else
				t.setInteger(0);

		}
		ImagePlus inverted = ImageJFunctions.wrap(inv, "inverted");
		// ImageJFunctions.show(nn);

		// Particle analyze
		double minSize = Math.PI * Math.pow((1.0 / 2), 2.0);
		double maxSize = Math.PI * Math.pow((1000.0 / 2), 2.0);
		System.out.println("Min: " + minSize + " | max:" + maxSize);
		int x = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;

		int opts = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES | ParticleAnalyzer.SHOW_PROGRESS
				| ParticleAnalyzer.CLEAR_WORKSHEET;
		int meas = Measurements.AREA | Measurements.MEAN | Measurements.CENTER_OF_MASS;
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer analyzer = new ParticleAnalyzer(opts, meas, rt, minSize, maxSize);
		Analyzer.setRedirectImage(imp);

		analyzer.analyze(inverted);
		System.out.println("Length of result table: " + rt.size());
		if (rt.size() == 0) {
			Log.print(path + ": is Empty !");
			return null;
		}
		int i = 0;
		double dot_area = -1, dot_x = -1, dot_y = -1, dot_mean = -1;
		dot_area = rt.getValueAsDouble(rt.getColumnIndex("Area"), i);
		dot_mean = rt.getValueAsDouble(rt.getColumnIndex("Mean"), i);
		dot_x = rt.getValueAsDouble(rt.getColumnIndex("XM"), i);
		dot_y = rt.getValueAsDouble(rt.getColumnIndex("YM"), i);

		System.out.println("dot_area:" + dot_area + " |dot_mean:" + dot_mean + " |dot_x:" + dot_x + " |dot_y:" + dot_y);
		return rt;
	}

}
