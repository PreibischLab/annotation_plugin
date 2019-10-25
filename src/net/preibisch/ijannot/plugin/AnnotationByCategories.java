package net.preibisch.ijannot.plugin;

import java.util.ArrayList;
import java.util.Arrays;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManagerV2;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.TaskManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.view.AnnotationCategoriesView;
import net.preibisch.ijannot.view.FolderPickerView;

@Plugin(type = Command.class, menuPath = "Plugins>CRG>AnnotationByCategories")
public class AnnotationByCategories implements PlugIn, Command {

	public static void main(String[] args) {
		new ImageJ();
		new AnnotationByCategories().run("");
	}

	@Override
	public void run(String s) {
		run();
	}

	@Override
	public void run() {
		IOFunctions.println("Hello");
		try {

			TaskManager.init(TaskManager.GENERATE_TRAIN_IMAGE_TASK);
			FolderPickerView f = new FolderPickerView();
			AnnotationCategoriesView.init(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)));
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			CanvasAnnotationManagerV2.start();
		} catch (Exception e) {
			IOFunctions.println(e.toString());
			e.printStackTrace();
		}
	}
}
