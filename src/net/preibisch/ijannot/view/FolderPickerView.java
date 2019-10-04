package net.preibisch.ijannot.view;

import javax.swing.JFileChooser;

import net.preibisch.ijannot.util.Default;
import net.preibisch.ijannot.util.Log;

public class FolderPickerView extends JFileChooser {
	/**
	 * Folder Picker view to get for folder to process
	 */
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Folder to Process";

	public FolderPickerView() {
		super(Default.PATH);
		this.setDialogTitle(TITLE);
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		this.setAcceptAllFileFilterUsed(false);
		//
		if (this.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			Log.print("CurrentDirectory: " + this.getCurrentDirectory());
			Log.print("SelectedFile : " + this.getSelectedFile());
		} else {
			Log.error("No Selection ");
		}
	}

	public static void main(String[] args) {
		new FolderPickerView();
	}
}