package net.preibisch.ijannot.controllers.listners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManagerV2;
import net.preibisch.ijannot.util.IOFunctions;

public class KeyboardClickV2 implements KeyListener{
	private static final List<Integer> CATEGORIES = new ArrayList<>(Arrays.asList(0,1,2,3,4));
	private static final int TAB_BUTTON = 9;
//	private static final int Z_BUTTON = ;

	@Override
	public void keyTyped(KeyEvent e) {
		IOFunctions.println("Key Clicked ExtendedKeyCode:"+e.getExtendedKeyCode()+" "+e.getKeyChar());
		if(e.getExtendedKeyCode()==TAB_BUTTON) {
			IOFunctions.println("Next !" );
			CanvasAnnotationManagerV2.next();
		}
		// Z or z
		if((e.getExtendedKeyCode()==90)||(e.getExtendedKeyCode()==122)) {
			IOFunctions.println("Undo" );
			CanvasAnnotationManagerV2.undo();
		}
		// Q or q
		if((e.getExtendedKeyCode()==81)||(e.getExtendedKeyCode()==113)) {
			IOFunctions.println("Exit" );
			CanvasAnnotationManagerV2.exit();
		}
		//Selected category
		if(Character.isDigit(e.getKeyChar())){
			int digit = Integer.parseInt(String.valueOf(e.getKeyChar()));	
			if (CATEGORIES.contains(digit)){
				CanvasAnnotationManagerV2.add(digit);
				CanvasAnnotationManagerV2.next();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Nothing
	}

}
