package net.preibisch.ijannot.view;

import java.io.File;

import javax.swing.JFileChooser;

import net.preibisch.ijannot.util.Default;

public class FolderPickerView extends JFileChooser {

	private static final String TITLE = "Folder to Process";

	public FolderPickerView() {
		super();
		this.setCurrentDirectory(new File(Default.PATH));
		this.setDialogTitle(TITLE);
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		this.setAcceptAllFileFilterUsed(false);
		//
		if (this.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + this.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + this.getSelectedFile());
		} else {
			System.out.println("No Selection ");
		}
	}

	public static void main(String[] args) {

		new FolderPickerView();

	}
}