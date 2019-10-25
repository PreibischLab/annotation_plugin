package net.preibisch.ijannot.controllers.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

import net.preibisch.ijannot.util.IOFunctions;

public class ImgManager {
	private static ImgManager instance;

	private String folder;
	private String ext;
	private List<String> toProcess;
	private List<String> processed;
	private String current;

	public static void init(String folder, String ext) throws IOException {
		instance = new ImgManager(folder, ext);
	}
	
	public String getFolder() {
		return folder;
	}

	public static ImgManager get() throws RuntimeException {
		if (instance == null)
			throw new RuntimeException("Img Manager is not initialized yet \n use init(folder,ext) !");
		return instance;
	}

	private ImgManager(String folder, String ext) throws IOException {
		IOFunctions.println("Prepare Img Manager..");
		this.folder = folder;
		this.ext = ext;
		this.toProcess = new ArrayList<>();
		this.processed = new ArrayList<>();
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
		IOFunctions.println("Found " + this.toProcess.size() + " File found with extension: " + this.ext);
	}
	
	public boolean hasNext() {
		return !this.toProcess.isEmpty();
	}

	public String test() {
		if (this.toProcess.isEmpty())
			return null;
		return this.toProcess.get(0);
	}
	public String next() {
		if (this.current != null)
			this.processed.add(this.current);
		if (this.toProcess.isEmpty())
			return null;
		this.current = this.toProcess.remove(0);
		IOFunctions.println("Processed:" + this.processed.size() + " |Rest:" + this.toProcess.size() + " |Current: "
				+ this.current);
		return this.current;
	}

	public void undo() {
		if(this.processed.isEmpty()) {
			IOFunctions.println("Processed is empty !");
			return;
		}
		this.toProcess.add(0, this.current);
		this.toProcess.add(0, this.processed.get(this.processed.size()-1));
		this.current = null;
	}

}
