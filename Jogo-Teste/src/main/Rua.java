package main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Carro;

public class Rua extends Thread {
	Carro carros_rua[];
	Thread ruaThread;
	
	public Rua(int tamanho) {
		carros_rua = new Carro[tamanho];
	}
	
	public void startRuaThread() {
		ruaThread = new Thread(this);
		ruaThread.start();
	}
	
	@Override
	public void run() {
		
	}
	
	public void update() {
		
	}
	
	public void paintCarro(Graphics g) {
		
	}
	
}
