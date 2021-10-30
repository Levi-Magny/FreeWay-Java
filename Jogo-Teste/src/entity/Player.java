package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity implements Runnable{
	GamePanel gp;
	KeyHandler keyH;
	int posicaoMatriz;
	int DefaultX, DefaultY;
	int NumJogador;
	
	Thread threadPlayer;
	
	public Player(GamePanel gp, KeyHandler keyH, int x, int y, int ij) {
		this.gp = gp;
		this.keyH = keyH;
		
		this.posicaoMatriz = x; 
		
		this.NumJogador = ij;
		
		setDefaultValues(x, y);
		getPlayerImage();
		
		startThread();
	}
	
	public void startThread() {
		threadPlayer = new Thread(this);
		threadPlayer.start();
	}
	
	public void setDefaultValues(int X, int Y) {
		
		x = DefaultX = X;
		y = DefaultY = Y;
		speed = 4;
	}
	
	public void posicaoInicial() {
		x = DefaultX;
		y = DefaultY;
	}
	
	public void getPlayerImage() {
		try {
			
			look = ImageIO.read(getClass().getResourceAsStream("/player/Player.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
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
	
	
	
	
	
	@Override
	public void run() {
		double drawInterval = 1000000000/60; // 0.01666 segundos
		double nextDrawTime = System.nanoTime() + drawInterval; // "nanoTime()" retorna o valor atual em nanossegundos.
		
		while(threadPlayer != null) {
			if(y != posicaoMatriz) {
				atualizaMatriz();
			}
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime /= 1000000;
				
				if(remainingTime < 0) remainingTime = 0;
				
				Thread.sleep((long) remainingTime);
				
				nextDrawTime += drawInterval;
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			}
		}
	}
	
	public void atualizaMatriz() {
		
		//bota mutex

		int Y = y;
		
		gp.matriz[posicaoMatriz][Y] = 0;
		Y++;
		if(gp.matriz[posicaoMatriz][Y] != 0) {
			gp.colision = gp.matriz[posicaoMatriz][Y];
		}
		gp.matriz[posicaoMatriz][Y] = NumJogador;
		
		//tira mutex
	}
}
