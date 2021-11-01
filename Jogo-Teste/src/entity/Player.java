package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity implements Runnable {
	GamePanel gp;
	KeyHandler keyH;
	Thread threadPlayer;
	int idPlayer;
	int yAntigo; // Gambiarra talvez ???
	
	public Player(GamePanel gp, KeyHandler keyH, int x, int y, int idPlayer) {
		this.gp = gp;
		this.keyH = keyH;
		this.idPlayer = idPlayer;
		
		setDefaultValues(x, y);
		getPlayerImage();
	}
	
	public void startThread() {
	    threadPlayer = new Thread(this);
	    threadPlayer.start();
	}
	
	public void setDefaultValues(int X, int Y) {
		x = X;
		y = Y;
		speed = 4;
		yAntigo = y;
	}
	
	public void getPlayerImage() {
		try {
			
			look = ImageIO.read(getClass().getResourceAsStream("/player/Player.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() { // Talvez colocar mais uma condição aqui para update na tela quando atingir o outro lado da rodovia
		if(keyH.upPressed == true) {
			y -= speed;
		}
		else if(keyH.downPressed == true) {
			y += speed;
		}
	}
	public void draw(Graphics2D g2) {
		//g2.setColor(Color.white);
		//g2.fillRect(x, y, gp.tileSize, gp.tileSize); // desenhando um retangulo
		
		BufferedImage image = look;
		
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	private void inicializaMatriz() {
		try {
			gp.mutex.acquire();
			gp.matriz[y / 48][x / 48] = idPlayer;
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}
	
	private void atualizaMatriz() { // Juntamente com atualizar na matriz o retorno ao inicio ao atingir o outro lado da rodovia
		try {
			gp.mutex.acquire();
			if(y % 48 == 0) {
				gp.matriz[yAntigo / 48][x / 48] = 0;
				gp.matriz[y / 48][x / 48] = idPlayer;
				yAntigo = y;
				gp.PrintMatriz();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}

	@Override
	public void run() {

		inicializaMatriz();
		while(threadPlayer != null) {
			
			atualizaMatriz();
			
		}
		
	}
}
