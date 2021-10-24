package main;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile size
	final int scale = 3;
	
	public final int tileSize = originalTileSize*scale; // 48x48 scale
	final int maxScreenCol = 24;
	final int maxScreenRow = 14;
	final int screenWidth = tileSize*maxScreenCol; // 768 pixels
	final int screenHeight = tileSize*maxScreenRow; // 576 pixels
	
	// FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	Player player = new Player(this, keyH);
	
	
	// Define a posicao inicial do jogador
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH); // Event listener (Parece com Javascript +/-)
		this.setFocusable(true); // com isso o painel do jogo vai estar focado para receber inputs de teclado
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/FPS; // 0.01666 segundos
		double nextDrawTime = System.nanoTime() + drawInterval; // "nanoTime()" retorna o valor atual em nanossegundos.
		
		while(gameThread != null) {
			
			// 1 UPDATE: Atualizar informações, como a posicao do usuario.
			update();
			
			// 2 DRAW: Desenhar a tela com as informacoes atualizadas.
			repaint();
			
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
	
	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) { // graphics é uma classe que implementa varias formas de desenhar objetos na tela.
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		player.draw(g2);
		
		g2.dispose(); // descarta este contexto gráfico e libera quisquer recursos do sistema que estao usando ele.
		
	}
}
