package net.preibisch.ijannot.controllers.tasks;

import ij.ImageJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.EDM;
import ij.plugin.filter.ParticleAnalyzer;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;
import net.preibisch.ijannot.controllers.managers.ImgPlusProc;
import net.preibisch.ijannot.util.Service;

public class AnalyzeTasks {
	//TODO abstract and extract vars
	// File result with total
	//  create interface with all or step by step
	// generate images out of this result
	public static void main(String[] args) {
		new ImageJ();

		// Service init
		OpService ops = Service.getOps();

		// File prepare
		String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/raw_Tiff/ND8_DIV0+4h_20x_noConfinment_3_ch_4.tif";
		ImagePlus imp = ImgPlusProc.getChannel(path, 1);
		Img<UnsignedByteType> image = ImageJFunctions.wrap(imp);
		final Object type = Util.getTypeFromInterval(image);
		System.out.println("Pixel Type: " + type.getClass());
		System.out.println("Img Type: " + image.getClass());
		imp.show();

		// Gauss
		image = (Img<UnsignedByteType>) ops.filter().gauss(image, 3.0);

		// Threshold
		IterableInterval<BitType> maskBitType = ops.threshold().apply(image, new UnsignedByteType(15));
		Img<BitType> target = image.factory().imgFactory(new BitType()).create(image, new BitType());
		copy(target, maskBitType);

		// Watershed
		ImagePlus water = ImageJFunctions.wrap(target, "target");
		EDM edm = new EDM();
		edm.toWatershed(water.getProcessor());
		water.show();

		//Invert
		
        final UnsignedByteType c = new UnsignedByteType();
        Img<UnsignedByteType> inv = ImageJFunctions.wrap(water);
//        if(true) return;
        for ( final UnsignedByteType t : inv )
        {
        	
            c.set( t );
            int x = c.getInteger();
            if(x==0) t.setInteger(255);
            else
            	t.setInteger(0);

        }
        ImagePlus inverted = ImageJFunctions.wrap(inv, "inverted");
//        ImageJFunctions.show(nn);
        
//		IJ.run(command);
//		ij.plugin.filter.Filters("invert")
		
		// Particle analyze
		double minSize = Math.PI * Math.pow((1.0 / 2), 2.0);
		double maxSize = Math.PI * Math.pow((1000.0 / 2), 2.0);
		System.out.println("Min: " + minSize + " | max:" + maxSize);
		int x = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;

		int opts = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES | ParticleAnalyzer.SHOW_PROGRESS
				| ParticleAnalyzer.CLEAR_WORKSHEET ;
		int meas = Measurements.AREA | Measurements.MEAN | Measurements.CENTER_OF_MASS;
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer analyzer = new ParticleAnalyzer(opts, meas, rt, minSize, maxSize);
		Analyzer.setRedirectImage(imp);

		analyzer.analyze(inverted);
		System.out.println("Length of result table: " + rt.size());
		if (rt.size() == 0)
			return;
		int i = 0;
		double dot_area = -1, dot_x = -1, dot_y = -1, dot_mean = -1;
		dot_area = rt.getValueAsDouble(rt.getColumnIndex("Area"), i);
		dot_mean = rt.getValueAsDouble(rt.getColumnIndex("Mean"), i);
		dot_x = rt.getValueAsDouble(rt.getColumnIndex("XM"), i);
		dot_y = rt.getValueAsDouble(rt.getColumnIndex("YM"), i);

		System.out.println("dot_area:" + dot_area + " |dot_mean:" + dot_mean + " |dot_x:" + dot_x + " |dot_y:" + dot_y);

	}

	public static <T extends Type<T>> void copy(final Img<BitType> target, final IterableInterval<BitType> source) {
		Cursor<BitType> c = source.cursor();
		RandomAccess<BitType> t = target.randomAccess();
		while (c.hasNext()) {
			c.fwd();
			t.setPosition(c);
			t.get().set(c.get());
		}
	}

}
