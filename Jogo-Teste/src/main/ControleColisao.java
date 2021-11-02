package main;

public class ControleColisao implements Runnable {

	GamePanel gp;
	Thread threadCC;
	public int matriz[][];
	public int colision = 0;
	
	public ControleColisao(GamePanel gp) {
		this.gp = gp;
		IniciaMatriz();
	}
	
	public void startThread() {
	    threadCC = new Thread(this);
		threadCC.start();
	}
	
	public void IniciaMatriz() {
		matriz = new int[14][20];
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

	public void checkColision() {
		try {
			gp.mutex.acquire();
			if(colision == 1) {
				gp.player1.resetPosition();
				colision = 0;
			} else if (colision == 2) {
				gp.player2.resetPosition();
				colision = 0;
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			gp.mutex.release();
		}
	}
	
	@Override
	public void run() {
		while(threadCC != null) {
			checkColision();
		}
	}
}
