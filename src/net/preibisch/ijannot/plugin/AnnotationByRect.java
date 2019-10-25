package net.preibisch.ijannot.plugin;

import java.io.IOException;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManager;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.view.FolderPickerView;

@Plugin(type = Command.class, menuPath = "Plugins>CRG>AnnotationByRect")
public class AnnotationByRect implements PlugIn,Command {

	@Override
	public void run(String s) {
		run();
	}

	public static void main(String[] args) {
		new ImageJ();
		new AnnotationByRect().run("");
	}

	@Override
	public void run() {
		try {
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			CanvasAnnotationManager.start();
		} catch (IOException e) {
			IOFunctions.println(e.toString());
			e.printStackTrace();
		}
	}
}
