package net.preibisch.ijannot.controllers.listners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.PlugIn;
import util.IOFunctions;

public class MouseClick implements PlugIn, MouseListener {

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

	@Override
	public void run(String arg0) {
//		String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/C1-ND8_DIV0+4h_20x_noConfinment_8_ch_4.tif";
		String path = "/Users/Marwan/Desktop/Irimia Project/New_data_lif/raw_Tiff/ND8_DIV0+4h_20x_noConfinment_8_ch_4.tif";
		 final ImagePlus imp = new Opener().openImage( path );
		 imp.show();
		 imp.getCanvas().addMouseListener(new MouseClick());
        
    }
 
    public static void main( String[] args )
    {
        // open an ImageJ window
        new ImageJ();
 
        // run the example
        new MouseClick().run("");
        
    }

}
