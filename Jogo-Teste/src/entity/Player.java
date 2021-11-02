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
	int defaultX, defaultY;
	
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
		x = defaultX = X;
		y = defaultY = Y;
		speed = 3;
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
		//System.out.println("y: " + y);
		
		if(keyH.upPressed == true)
			y -= speed;
		else if(keyH.downPressed == true)
			y += y == 576 ? 0 : speed;
		
	}
	public void draw(Graphics2D g2) {
		//g2.setColor(Color.white);
		//g2.fillRect(x, y, gp.tileSize, gp.tileSize); // desenhando um retangulo
		
		BufferedImage image = look;
		
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	/**
	 * quando o Player e criado, ele chama este metodo para setar seus valores na matriz de controle.
	 */
	private void inicializaPosicaoMatriz() {
		try {
			gp.mutex.acquire();
			gp.cc.matriz[y / 48][x / 48] = idPlayer;
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}
	
	/**
	 * Este metodo atualiza a matriz de controle do jogo, implementando um mutex para tratar acesso a
	 * regiao critica.
	 */
	private void atualizaMatriz() { // Juntamente com atualizar na matriz o retorno ao inicio ao atingir o outro lado da rodovia
		if(y > 48) {
			try {
				gp.mutex.acquire();
				if(y % 48 == 0 && yAntigo != y / 48) {
					gp.cc.matriz[yAntigo / 48][x / 48] = 0;
					if(gp.cc.matriz[y / 48][x / 48] == 0) {
						gp.cc.matriz[y / 48][x / 48] = idPlayer;
						yAntigo = y;
					} else {
						gp.cc.colision = idPlayer;
					}
					//gp.cc.PrintMatriz();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			} finally {
				gp.mutex.release();
			}			
		} else {
			inicializaPosicaoMatriz();
			x = defaultX;
			y = defaultY;
			yAntigo = y;
		}
	}

	/**
	 * Quando há colisao, este metodo reseta as posicoes do player.
	 */
	public void resetPosition() {
		if(gp.cc.matriz[y / 48][x / 48] == idPlayer) {
			gp.cc.matriz[y / 48][x / 48] = 0;			
		}
		x = defaultX;
		y = defaultY;
		yAntigo = y;
		gp.cc.matriz[defaultY / 48][defaultX / 48] = idPlayer;
	}
	
	@Override
	public void run() {

		inicializaPosicaoMatriz();
		while(threadPlayer != null) {
			
			atualizaMatriz();
			
		}
	}
}
