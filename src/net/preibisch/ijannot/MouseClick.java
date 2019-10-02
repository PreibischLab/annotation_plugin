package net.preibisch.ijannot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;


public class MouseClick implements PlugIn, MouseListener
{

    @Override
    public void mouseClicked(MouseEvent e) {
        IJ.showMessage("Mouse clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void run(String arg0) 
    {
        IJ.open();
        ImagePlus imp = WindowManager.getCurrentImage();
        imp.getCanvas().addMouseListener(this);
    }
    
    public static void main(String[] args) {
		new MouseClick().run("");
	}
    
}
