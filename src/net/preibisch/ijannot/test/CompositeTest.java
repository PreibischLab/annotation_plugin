package net.preibisch.ijannot.test;

import ij.CompositeImage;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;

public class CompositeTest {
public static void main(String[] args) {
	new ImageJ();
	String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/raw_Tiff/ND8_DIV0+4h_20x_noConfinment_3_ch_4.tif";
	ImagePlus imp = new Opener().openImage(path);
	imp.show();
	CompositeImage comp = new CompositeImage(imp);
	comp.show();
	CompositeImage comp2 = new CompositeImage(imp,CompositeImage.COMPOSITE);
	comp2.show();
}
}
