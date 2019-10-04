package net.preibisch.ijannot.controllers.listners;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import net.preibisch.ijannot.controllers.managers.CanvasManager;
import net.preibisch.ijannot.util.IOFunctions;

public class MouseClick implements MouseListener {
	private static Point point;
	private static final int THRESHOLD = 4;

	@Override
	public void mousePressed(MouseEvent e) {
		IOFunctions.println("Mouse Pressed: "+e.getX()+ " - "+e.getY());
		point = new Point(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		IOFunctions.println("Mouse Released: "+e.getX()+ " - "+e.getY());
		Dimension d = new Dimension(e.getX()-point.x, e.getY()-point.y);
		if((d.height<=THRESHOLD)||(d.width<=THRESHOLD))
			return;
		Rectangle r = new Rectangle(point, d );
		CanvasManager.addRect(r);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//Nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Nothing
	}

}
