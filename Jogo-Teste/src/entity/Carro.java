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
	 * @param speed Speed of the car
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
		startThread();
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
		if(direction == 0) {
			x = 0;
			y = (AbsPosition+2)*48;
		} else {
			x = 936;
			y = (AbsPosition+2)*48;
		}
		gp.matriz[x][y] = 1;
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
		if(x > gp.screenWidth) {
			setDefaultValues();
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
		while(threadCarro != null) {
			
		}
	}
}
