package net.preibisch.ijannot.models;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.preibisch.ijannot.util.IOFunctions;
import net.preibisch.ijannot.util.Log;

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

	public String toString() {
		StringBuilder bld = new StringBuilder();
		bld.append(img);  
		for (Rectangle r : list) {
			bld.append(",");
			bld.append(r.x);
			bld.append(",");
			bld.append(r.y);
			bld.append(",");
			bld.append(r.width);
			bld.append(",");
			bld.append(r.height);	
		}
		bld.append("\n");
		String str = bld.toString();
		Log.print(str);
		return str;
	}

}
