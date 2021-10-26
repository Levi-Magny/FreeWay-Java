package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	GamePanel gp;
	KeyHandler keyH;
	
	public Player(GamePanel gp, KeyHandler keyH, int x, int y) {
		this.gp = gp;
		this.keyH = keyH;
		
		setDefaultValues(x, y);
		getPlayerImage();
	}
	
	public void setDefaultValues(int X, int Y) {
		x = X;
		y = Y;
		speed = 4;
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
}
