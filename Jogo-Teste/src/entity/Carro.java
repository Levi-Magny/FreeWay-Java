package entity;

import java.util.Random;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Carro extends Entity implements Runnable{

	GamePanel gp;
	KeyHandler keyH;
	int direction, RelPosition, AbsPosition;
	
	Thread threadCarro;
	
	/**
	 * 
	 * @param gp GamePanel
	 * @param x x-axis initial position
	 * @param y y-axis initial position
	 * @param direction 0: from left to right; 1: from right to left
	 */
	public Carro(GamePanel gp, int position, int direction) {
		this.gp = gp;
		this.direction = direction;
		
		this.RelPosition = position;
		
		setAbsPos();
		setSpeed();
		setDefaultValues();
		getPlayerImage();
	}
	
	public void startThread() {
	    threadCarro = new Thread(this);
		threadCarro.start();
	}
	
	public void setSpeed() {
		int Speeds[] = {1, 2, 4, 6, 8};
		
		speed = Speeds[RelPosition];
	}
	
	public void setAbsPos() {
		AbsPosition = (direction == 0) ? (9-RelPosition) : RelPosition;
	}
	
	public void setDefaultValues() {
		if(direction == 0)
			x = 0;
		else
			x = 912;
		
		y = (AbsPosition+2)*48;
	}
	
	public void getPlayerImage() {
		
		String colors[] = {"Carro01.png", "Carro02.png"};
		String colors2[] = {"Carro01-180d.png", "Carro02-180d.png"};
		
		Random rand = new Random();
		
		int color = rand.nextInt(2);
		
		
		try {
			if(direction == 0) {
				look = ImageIO.read(getClass().getResourceAsStream("/cars/"+colors2[color]));
			} else {
				look = ImageIO.read(getClass().getResourceAsStream("/cars/"+colors[color]));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		int newPos = (direction == 0) ? x + speed : x - speed;
		x = newPos;
		
		if(x > gp.screenWidth || x < 0) {
			this.setDefaultValues();
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = look;
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	private void alteraMatrizBaixo() {
		try {
			gp.mutex.acquire();
			if(x % 48 == 0 && x < gp.screenWidth) {
				if(x / 48 > 0)
					gp.cc.matriz[y / 48][(x / 48) - 1] = 0;
				
				if(gp.cc.matriz[y / 48][x / 48] != 0) {
					gp.cc.colision = gp.cc.matriz[y / 48][x / 48];
				}
				gp.cc.matriz[y / 48][x / 48] = 3;
				//gp.cc.PrintMatriz();
			}
			//System.out.println(gp.matriz[y/48][0] == 3);
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}
	
	private void alteraMatrizAlto() {
		try {
			gp.mutex.acquire();
			if(x % 48 == 0 && x / 48 < 19) {
				gp.cc.matriz[y / 48][(x / 48) + 1] = 0;
				if(gp.cc.matriz[y / 48][x / 48] != 0) {
					gp.cc.colision = gp.cc.matriz[y / 48][x / 48];
				}
				gp.cc.matriz[y / 48][x / 48] = 3;
				//gp.cc.PrintMatriz();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}
	
	private void inicializaMatriz() {
		try {
			gp.mutex.acquire();
			gp.cc.matriz[y / 48][x / 48] = 3;
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}

	@Override
	public void run() {
		//double nextDrawTime = System.nanoTime() + gp.drawInterval; // "nanoTime()" retorna o valor atual em nanossegundos.
		
		inicializaMatriz();
		while(threadCarro != null) {
			if(this.direction == 1)
				this.alteraMatrizAlto();
			else
				this.alteraMatrizBaixo();
			
			//nextDrawTime = gp.pausarThread(nextDrawTime);
		}
	}
}
