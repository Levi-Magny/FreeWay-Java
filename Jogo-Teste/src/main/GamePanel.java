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
	final int originalTileSize = 16; // 16x16 tamanho do bloco
	final int scale = 3;
	
	public final int tileSize = originalTileSize*scale; // 48x48 escala
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 14;
	public final int screenWidth = tileSize*maxScreenCol; // 960 pixels
	public final int screenHeight = tileSize*maxScreenRow; // 672 pixels

	// FPS
	public final int FPS = 90;
	public final double drawInterval = 1000000000/FPS; // 0.01666 segundos
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH_arrow = new KeyHandler(true);
	KeyHandler keyH_ws = new KeyHandler(false);
	Thread gameThread;
	
	public Semaphore mutex;
	public ControleColisao cc;
	
	public Score score = new Score(this);
	Player player1;
 	Player player2;
 	Ruas ruas;
 
	
	/**
	 * Construtor do Game Panel
	 */
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH_arrow); // Event listener do teclado
		this.addKeyListener(keyH_ws);
		this.setFocusable(true); // com isso o painel do jogo vai estar focado para receber inputs de teclado a

		cc = new ControleColisao(this);
		mutex = new Semaphore(1, true);
		player1 = new Player(this, keyH_ws, 192, 576, 1);
		player2 = new Player(this, keyH_arrow, 720, 576, 2);
		ruas = new Ruas(this);
		
	}
	
	/**
	 * Inicializar as Threads do Jogo
	 */
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		player1.startThread();
		player2.startThread();
		cc.startThread();
		ruas.iniciaThreadsDosCarros();
		
	}

	@Override
	public void run() {
		double nextDrawTime = System.nanoTime() + drawInterval; // "nanoTime()" retorna o valor atual em nanossegundos.
		
		while(gameThread != null) {
			
			// 1 UPDATE: Atualizar informa??es, como a posicao do usuario.
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
		ruas.updateCarros();
		
	}
	
	public void paintComponent(Graphics g) { // graphics ? uma classe que implementa varias formas de desenhar objetos na tela.
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		score.draw(g2);
		player1.draw(g2);
		player2.draw(g2);
		ruas.paintComponentCarros(g2);

		
		g2.dispose(); // descarta este contexto gr?fico e libera quisquer recursos do sistema que estao usando ele.
		
	}
}
