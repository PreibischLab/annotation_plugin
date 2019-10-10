package net.preibisch.ijannot.plugin;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.imagej.ops.OpService;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.controllers.managers.TaskManager;
import net.preibisch.ijannot.controllers.tasks.TrainDataGenerator;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.Service;
import net.preibisch.ijannot.view.FolderPickerView;

public class TrainingDataGeneratorApplication implements PlugIn {

	private OpService ops;

	@Override
	public void run(String s) {
		try {
			this.ops = Service.getOps();
			TaskManager.init(TaskManager.GENERATE_TRAIN_IMAGE_TASK);
			FolderPickerView f = new FolderPickerView();
			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.CSV);
			TrainDataGenerator.start();
			System.out.println("Finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ImageJ();
	
		new TrainingDataGeneratorApplication().run("");
	}
}
