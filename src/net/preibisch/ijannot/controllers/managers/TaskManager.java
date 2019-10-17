package net.preibisch.ijannot.controllers.managers;

import java.awt.Rectangle;

import net.preibisch.ijannot.util.Log;

public class TaskManager {
	private static TaskManager instance;
	public static final int ANNOTATION_TASK = 0;
	public static final int ANALYZE_TASK = 1;
	public static final int GENERATE_TRAIN_IMAGE_TASK = 2;
	public static final int ANNOTATE_TRAIN_IMAGE_TASK = 3;

	private int currentTask;

	private TaskManager(int task) {
		currentTask = task;
	}

	public static TaskManager get() throws RuntimeException {
		if (instance == null)
			throw new RuntimeException("Img Manager is not initialized yet \n use init(folder,ext) !");
		return instance;
	}

	public static void init(int task) throws Exception {
		switch (task) {
		case ANNOTATION_TASK:
		case ANALYZE_TASK:
		case GENERATE_TRAIN_IMAGE_TASK:
		case ANNOTATE_TRAIN_IMAGE_TASK:
			instance = new TaskManager(task);
			break;
		default:
			throw new Exception("invalid Task id: " + task);
		}
	}

	public void next() {

		switch (currentTask) {
		case ANNOTATION_TASK:
			CanvasAnnotationManager.next();
			break;
		case ANNOTATE_TRAIN_IMAGE_TASK:
			CanvasAnnotationManagerV2.next();
			break;

		default:
			Log.error("Not implimented Next in " + currentTask);
			break;
		}
	}

	public void undo() {
		switch (currentTask) {
		case ANNOTATION_TASK:
			CanvasAnnotationManager.undo();
			break;

		default:
			Log.error("Not implimented Next in " + currentTask);
			break;
		}
	}

	public void add(Object obj) {
		switch (currentTask) {
		case ANNOTATION_TASK:
			if (obj instanceof Rectangle)
				CanvasAnnotationManager.addRect((Rectangle) obj);

			else
				Log.error("invalid object to cast");
			break;

		default:
			Log.error("Not implimented add in " + currentTask);
			break;
		}
	}

}
