package net.preibisch.ijannot.plugin;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.imagej.ops.OpService;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.TaskManager;
import net.preibisch.ijannot.controllers.tasks.TIFFDataGenerator;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Service;
import net.preibisch.ijannot.view.FolderPickerView;

@Plugin(type = Command.class, menuPath = "Plugins>CRG>TrainingDataGenerator")
public class TiffGenerator implements Command, PlugIn {

	private OpService ops;

	@Override
	public void run(String s) {
		run();
	}

	@Override
	public void run() {
		try {
			this.ops = Service.getOps();
			TaskManager.init(TaskManager.GENERATE_TRAIN_IMAGE_TASK);
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			TIFFDataGenerator.start();
			IOFunctions.println("Finish!");
		} catch (Exception e) {
			IOFunctions.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ImageJ();
		new TiffGenerator().run("");
	}
}
