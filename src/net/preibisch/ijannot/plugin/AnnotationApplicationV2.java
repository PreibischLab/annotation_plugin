package net.preibisch.ijannot.plugin;

import java.util.ArrayList;
import java.util.Arrays;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManagerV2;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.TaskManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.view.AnnotationCategoriesView;
import net.preibisch.ijannot.view.FolderPickerView;

public class AnnotationApplicationV2 implements PlugIn {

	@Override
	public void run(String s) {
		try {
		
			TaskManager.init(TaskManager.GENERATE_TRAIN_IMAGE_TASK);
			FolderPickerView f = new FolderPickerView();
			AnnotationCategoriesView.init(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)));
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			CanvasAnnotationManagerV2.start();
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ImageJ();
		new AnnotationApplicationV2().run("");
	}
}
