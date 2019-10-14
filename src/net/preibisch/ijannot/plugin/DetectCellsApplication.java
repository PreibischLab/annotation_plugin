package net.preibisch.ijannot.plugin;

import java.io.IOException;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.imagej.ops.OpService;
import net.preibisch.ijannot.controllers.managers.AnalyzeManager;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.Service;
import net.preibisch.ijannot.view.AnalyzeParams;
import net.preibisch.ijannot.view.FolderPickerView;

public class DetectCellsApplication implements PlugIn {

	private OpService ops;

	@Override
	public void run(String s) {
		try {
			this.ops = Service.getOps();
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			AnalyzeParams.init();
			AnalyzeManager.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ImageJ();
	
		new DetectCellsApplication().run("");
	}
}
