package net.preibisch.ijannot.models;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.preibisch.ijannot.util.IOFunctions;

public class Annot {
	String img;
	List<Rectangle> list;

	public Annot(String img) {
		this.img = img;
		list = new ArrayList<>();
	}

	public void addRect(Rectangle r) {
		list.add(r);
	}

	public void undo() {
		if (list.isEmpty())
			IOFunctions.println("List Empty ! no undo");
		else
			list.remove(list.size() - 1);
	}

}
