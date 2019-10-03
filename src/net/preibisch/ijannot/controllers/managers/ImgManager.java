package net.preibisch.ijannot.controllers.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

public class ImgManager {
	private static ImgManager instance;

	private String folder;
	private String ext;
	private List<String> toProcess;
	private List<String> processed;
	private String current_img;

	public static void init(String folder, String ext) throws IOException {
		instance = new ImgManager(folder, ext);
	}

	public static ImgManager get() throws Exception {
		if (instance == null)
			throw new Exception("Img Manager is not initialized yet \n use init(folder,ext) !");
		return instance;
	}

	private ImgManager(String folder, String ext) throws IOException {
		this.folder = folder;
		this.ext = ext;
		this.toProcess = new ArrayList<String>();
		this.processed = new ArrayList<String>();
		prepareList();
	}

	private void prepareList() throws IOException {
		File f = new File(this.folder);
		if (!f.exists())
			throw new IOException("Folder not found ! ");
		for (File tmp : f.listFiles()) {
			if (ext.equals(Files.getFileExtension(tmp.getName()))) {
				this.toProcess.add(tmp.getAbsolutePath());
			}
		}
		if (this.toProcess.isEmpty())
			throw new IOException("Empty folder ! \n No file found with extension: " + this.ext);
	}

	public String next() {
		if (!this.current_img.isEmpty())
			this.processed.add(this.current_img);
		if (this.toProcess.isEmpty())
			return null;
		this.current_img = this.toProcess.remove(0);
		return this.current_img;
	}

}
