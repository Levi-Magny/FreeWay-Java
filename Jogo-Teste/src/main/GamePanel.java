package main;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import entity.Carro;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile size
	final int scale = 3;
	
	public final int tileSize = originalTileSize*scale; // 48x48 scale
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 14;
	public final int screenWidth = tileSize*maxScreenCol; // 960 pixels
	public final int screenHeight = tileSize*maxScreenRow; // 672 pixels

	// FPS
	public final int FPS = 60;
	public final double drawInterval = 1000000000/FPS; // 0.01666 segundos
	
	public int matriz[][];
	
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH_arrow = new KeyHandler(true);
	KeyHandler keyH_ws = new KeyHandler(false);
	Thread gameThread;
	public Semaphore mutex;
	Player player1;
 	Player player2;
 	// public ControlStreets controleRuas;
 	Carro carro1;
 	/*Carro carro2;
 	Carro carro3;
 	Carro carro4;
 	Carro carro5;
 	Carro carro6;
 	Carro carro7;
 	Carro carro8;
 	Carro carro9;
 	Carro carro10;*/
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH_arrow); // Event listener (Parece com Javascript +/-)
		this.addKeyListener(keyH_ws);
		this.setFocusable(true); // com isso o painel do jogo vai estar focado para receber inputs de teclado a

		mutex = new Semaphore(1, true);
		matriz = new int[14][20];
		IniciaMatriz();
		player1 = new Player(this, keyH_ws, 192, 576, 1);
		player2 = new Player(this, keyH_arrow, 720, 576, 2);
		carro1 = new Carro(this, 0, 0);
		/*carro2 = new Carro(this, 1, 0);
		carro3 = new Carro(this, 2, 0);
		carro4 = new Carro(this, 3, 0);
		carro5 = new Carro(this, 4, 0);
		carro6 = new Carro(this, 0, 1);
		carro7 = new Carro(this, 1, 1);
		carro8 = new Carro(this, 2, 1);
		carro9 = new Carro(this, 3, 1);
		carro10 = new Carro(this, 4, 1);*/
	}
	
	public void IniciaMatriz() {
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 20; j++) {
				matriz[i][j] = 0;
			}
		}
	}
	
	public void PrintMatriz() {
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 20; j++) {
				System.out.print(matriz[i][j]);
			}
			System.out.println();
		}
		System.out.println("-------------------");
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		player1.startThread();
		player2.startThread();
		carro1.startThread();
		/*carro2.startThread();
		carro3.startThread();
		carro4.startThread();
		carro5.startThread();
		carro6.startThread();
		carro7.startThread();
		carro8.startThread();
		carro9.startThread();
		carro10.startThread();*/
	}

	@Override
	public void run() {
		double nextDrawTime = System.nanoTime() + drawInterval; // "nanoTime()" retorna o valor atual em nanossegundos.
		
		while(gameThread != null) {
			
			// 1 UPDATE: Atualizar informações, como a posicao do usuario.
			update();
			
			// 2 DRAW: Desenhar a tela com as informacoes atualizadas.
			repaint();
			
			nextDrawTime = pausarThread(nextDrawTime);
		}
		
	}
	
	public double pausarThread(double nextDrawTime) {
		try {
			double remainingTime = nextDrawTime - System.nanoTime();
			remainingTime /= 1000000;
			
			if(remainingTime < 0) remainingTime = 0;
			
			Thread.sleep((long) remainingTime);
			
			nextDrawTime += drawInterval;
			
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
		}
		return nextDrawTime;
	}
	
	public void update() {
		player1.update();
		player2.update();
		carro1.update();
		/*carro2.update();
		carro3.update();
		carro4.update();
		carro5.update();
		carro6.update();
		carro7.update();
		carro8.update();
		carro9.update();
		carro10.update();*/
	}
	
	public void paintComponent(Graphics g) { // graphics é uma classe que implementa varias formas de desenhar objetos na tela.
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		player1.draw(g2);
		player2.draw(g2);
		carro1.draw(g2);
		/*carro2.draw(g2);
		carro3.draw(g2);
		carro4.draw(g2);
		carro5.draw(g2);
		carro6.draw(g2);
		carro7.draw(g2);
		carro8.draw(g2);
		carro9.draw(g2);
		carro10.draw(g2);*/
		
		g2.dispose(); // descarta este contexto gráfico e libera quisquer recursos do sistema que estao usando ele.
		
	}
}
