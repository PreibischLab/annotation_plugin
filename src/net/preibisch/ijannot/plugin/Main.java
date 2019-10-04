package net.preibisch.ijannot.plugin;

import java.io.IOException;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.preibisch.ijannot.controllers.managers.CanvasManager;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.view.FolderPickerView;

public class Main implements PlugIn {

	@Override
	public void run(String s) {
		try {
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			CanvasManager.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ImageJ();
		new Main().run("");
	}
}
