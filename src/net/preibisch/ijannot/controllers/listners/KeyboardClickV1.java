package net.preibisch.ijannot.controllers.listners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.preibisch.ijannot.controllers.managers.TaskManager;
import net.preibisch.ijannot.util.IOFunctions;

public class KeyboardClickV1 implements KeyListener{
	private static final int TAB_BUTTON = 9;
	private static final int Z_BUTTON = 90;

	@Override
	public void keyTyped(KeyEvent e) {
		IOFunctions.println("Key Clicked ExtendedKeyCode:"+e.getExtendedKeyCode());
		if(e.getExtendedKeyCode()==TAB_BUTTON) {
			IOFunctions.println("Next !" );
			TaskManager.get().next();
		}
		if(e.getExtendedKeyCode()==Z_BUTTON) {
			IOFunctions.println("Next !" );
			TaskManager.get().undo();
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
