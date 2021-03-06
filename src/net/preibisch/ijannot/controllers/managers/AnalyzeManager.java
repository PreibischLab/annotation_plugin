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
import net.preibisch.ijannot.view.AnalyzeParamsView;

public class AnalyzeManager {
	private static final String CSVFolder = "CSV";
	private static List<String> log;
	private static List<String> total;
	private static File resultPath;

	public static void start() throws IOException {
		resultPath = new File(ImgManager.get().getFolder(), CSVFolder);
		IOFunctions.mkdir(resultPath);
		log = new ArrayList<>();
		total = new ArrayList<>();
		if (ImgManager.get().hasNext())
			next();
		else
			throw new IOException("Empty folder !");
	}

	public static void test() {
		String path = ImgManager.get().test();
		process(path, true);
	}

	private static void next() {
		while (ImgManager.get().hasNext()) {
			String path = ImgManager.get().next();
			ResultsTable rt = process(path);
			if (rt == null) {
				log.add("Empty: " + path);
			} else {
				save(rt, resultName(resultPath, path));
			}
		}
		File logFile = new File(ImgManager.get().getFolder(), "log.txt");
		IOFunctions.generateCSV(log, logFile,true);
		File totalFile = new File(ImgManager.get().getFolder(), "total.csv");
		IOFunctions.generateCSV(total, totalFile,true);
		Log.print("Done with all files");
	}

	private static void save(ResultsTable rt, File file) {
		List<String> list = new ArrayList<>();
		list.add("\"x\",\"y\"\n");

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
		total.add("\"" + file.getAbsolutePath() + "\"," + (list.size() - 1) + "\n");
		IOFunctions.generateCSV(list, file,true);
	}

	private static File resultName(File folder, String input) {
		return new File(folder, FilenameUtils.removeExtension(new File(input).getName()) + ".csv");
	}

	private static ResultsTable process(String path) {
		return process(path, false);
	}

	private static ResultsTable process(String path, Boolean testMode) {
		OpService ops = Service.getOps();
		ImagePlus imp = ImgPlusProc.getChannel(path, AnalyzeParamsView.get().getChannel());
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		if (testMode)
			imp.show();

		// Gauss
		image = (Img<UnsignedByteType>) ops.filter().gauss(image, AnalyzeParamsView.get().getGauss());
		if (testMode)
			ImageJFunctions.show(image, "Gauss");

		// Threshold
		IterableInterval<BitType> maskBitType = ops.threshold().apply(image,
				new UnsignedByteType(AnalyzeParamsView.get().getThreshold()));
		Img<BitType> target = image.factory().imgFactory(new BitType()).create(image, new BitType());
		AnalyzeTasks.copy(target, maskBitType);
		if (testMode)
			ImageJFunctions.show(target, "threshold");

		// Watershed
		ImagePlus water = ImageJFunctions.wrap(target, "target");
		EDM edm = new EDM();
		edm.toWatershed(water.getProcessor());
		if (testMode)
			water.show();

		// Invert
		Boolean invert = AnalyzeParamsView.get().getInverted();
		if (invert) {
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
			water = ImageJFunctions.wrap(inv, "inverted");
			if (testMode)
				water.show();
		}

		// Particle analyze
		double minSize = Math.PI * Math.pow((AnalyzeParamsView.get().getMin() / 2), 2.0);
		double maxSize = Math.PI * Math.pow((AnalyzeParamsView.get().getMax() / 2), 2.0);
		System.out.println("Min: " + minSize + " | max:" + maxSize);
		int x = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;

		int opts = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES | ParticleAnalyzer.SHOW_PROGRESS
				| ParticleAnalyzer.CLEAR_WORKSHEET;
		int meas = Measurements.AREA | Measurements.MEAN | Measurements.CENTER_OF_MASS;
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer analyzer = new ParticleAnalyzer(opts, meas, rt, minSize, maxSize);
		Analyzer.setRedirectImage(imp);

		analyzer.analyze(water);
		IOFunctions.println("Length of result table: " + rt.size());

		if (rt.size() == 0) {
			Log.print(path + ": is Empty !");
			return null;
		}
		int i = 0;
		double dot_area = rt.getValueAsDouble(rt.getColumnIndex("Area"), i);
		double dot_mean = rt.getValueAsDouble(rt.getColumnIndex("Mean"), i);
		double dot_x = rt.getValueAsDouble(rt.getColumnIndex("XM"), i);
		double dot_y = rt.getValueAsDouble(rt.getColumnIndex("YM"), i);

		IOFunctions
				.println("dot_area:" + dot_area + " |dot_mean:" + dot_mean + " |dot_x:" + dot_x + " |dot_y:" + dot_y);
		return rt;
	}

}
