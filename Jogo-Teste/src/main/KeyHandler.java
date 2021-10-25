package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	
	public boolean upPressed, downPressed;

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if( code == KeyEvent.VK_W ) {
			upPressed = true;
		}
		if( code == KeyEvent.VK_S ) {
			downPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if( code == KeyEvent.VK_W ) {
			upPressed = false;
		}
		if( code == KeyEvent.VK_S ) {
			downPressed = false;
		}
	}

}
