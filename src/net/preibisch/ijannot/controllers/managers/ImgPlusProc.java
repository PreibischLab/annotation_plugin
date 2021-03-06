package net.preibisch.ijannot.controllers.managers;

import ij.CompositeImage;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.plugin.ChannelSplitter;

public class ImgPlusProc {

	public static ImagePlus getChannel(String path, int ch) {
		ImagePlus imp = new Opener().openImage(path);
		return getChanel(imp, ch);
	}

	public static ImagePlus getChanel(ImagePlus imp, int ch) {
		ImageStack channelStack = ChannelSplitter.getChannel(imp, ch);
		return new ImagePlus("ch" + ch, channelStack);
	}
	
	public static ImagePlus getComposite(String path) {
		ImagePlus imp = new Opener().openImage(path);
		CompositeImage comp = new CompositeImage(imp,CompositeImage.COMPOSITE);
		return comp.flatten();
	}

	private ImgPlusProc() {
		throw new IllegalStateException("Utility class");
	}

}
