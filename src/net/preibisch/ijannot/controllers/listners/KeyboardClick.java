package net.preibisch.ijannot.controllers.listners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.preibisch.ijannot.util.IOFunctions;

public class KeyboardClick implements KeyListener{
	private final int tab_code = 9;

	@Override
	public void keyTyped(KeyEvent e) {
		// Listner for tab button click code = 9
		IOFunctions.println("Key Clicked ExtendedKeyCode:"+e.getExtendedKeyCode());
		if(e.getExtendedKeyCode()==tab_code) {
			IOFunctions.println("Tab clicked !" );
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
