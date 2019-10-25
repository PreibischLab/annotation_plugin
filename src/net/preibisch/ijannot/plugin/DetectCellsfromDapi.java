package net.preibisch.ijannot.plugin;

import java.io.IOException;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import ij.ImageJ;
import ij.plugin.PlugIn;
import net.imagej.ops.OpService;
import net.preibisch.ijannot.controllers.managers.ImgManager;
import net.preibisch.ijannot.models.Ext;
import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Service;
import net.preibisch.ijannot.view.AnalyzeParamsView;
import net.preibisch.ijannot.view.FolderPickerView;

@Plugin(type = Command.class, menuPath = "Plugins>CRG>DetectCellsfromDapi")
public class DetectCellsfromDapi implements PlugIn, Command {

	private OpService ops;

	@Override
	public void run(String s) {
		run();
	}

	public static void main(String[] args) {
		new ImageJ();
		new DetectCellsfromDapi().run("");
	}

	@Override
	public void run() {
		try {
			this.ops = Service.getOps();
			FolderPickerView f = new FolderPickerView();

			ImgManager.init(f.getSelectedFile().getAbsolutePath(), Ext.TIFF);
			AnalyzeParamsView.init();

		} catch (IOException e) {
			IOFunctions.println(e.toString());
			e.printStackTrace();
		}
	}
}
