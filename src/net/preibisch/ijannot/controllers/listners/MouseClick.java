package net.preibisch.ijannot.controllers.listners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import util.IOFunctions;

public class MouseClick implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		IOFunctions.println("Mouse Pressed: "+e.getX()+ " - "+e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		IOFunctions.println("Mouse Released: "+e.getX()+ " - "+e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
