package net.preibisch.ijannot.plugin;

import java.io.IOException;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.imagej.ops.OpService;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.Service;
import net.preibisch.ijannot.view.FolderPickerView;

public class TrainingDataGeneratorApplication implements PlugIn {

	private OpService ops;

	@Override
	public void run(String s) {
		try {
			Service.init();
			this.ops = Service.opService;
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
//			CanvasManager.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ImageJ();
	
		new TrainingDataGeneratorApplication().run("");
	}
}
