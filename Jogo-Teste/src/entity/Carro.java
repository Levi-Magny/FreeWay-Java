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
	int posicaoMatriz;
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
	
	/**
	 * Metodo que define a velocidade do carro
	 */
	public void setSpeed() {
		int Speeds[] = {1, 2, 4, 6, 8};
		
		speed = Speeds[RelPosition];
	}
	
	/**
	 * Metodo que define a posicao absoluta do carro
	 */
	public void setAbsPos() {
		AbsPosition = (direction == 0) ? (9-RelPosition) : RelPosition;
	}
	
	/**
	 * Define a posicao inicial do carro
	 */
	public void setDefaultValues() {
		if(direction == 0) {
			posicaoMatriz = 0;
			x = 0;
			y = (AbsPosition+2)*48;
		} else {
			posicaoMatriz = 20;
			x = 936;
			y = (AbsPosition+2)*48;
		}
		gp.matriz[x][y] = 1;
	}
	
	/**
	 * Define a aparência do carro
	 */
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
	
	/**
	 * Atualiza o carro na tela
	 */
	public void update() {
		int newPos = (direction == 0) ? x + speed : x - speed;
		
		x = newPos;
	}
	
	/**
	 * Desenha o carro na tela
	 * @param g2 Os graficos da janela do jogo.
	 */
	public void draw(Graphics2D g2) {
		//g2.setColor(Color.white);
		//g2.fillRect(x, y, gp.tileSize, gp.tileSize); // desenhando um retangulo
		
		BufferedImage image = look;
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	
	
	
	@Override
	public void run() {
		
		while(threadCarro != null) {
			if(x > posicaoMatriz) {
				atualizaMatriz();
			}
		}
	}
	
	public void atualizaMatriz() {
		
		//bota mutex
		gp.matriz[posicaoMatriz][AbsPosition] = 0;
		AbsPosition++;
		if(gp.matriz[posicaoMatriz][AbsPosition] != 0) {
			gp.colision = gp.matriz[posicaoMatriz][AbsPosition];
		}
		gp.matriz[posicaoMatriz][AbsPosition] = 3;
		//tira mutex
	}
}
