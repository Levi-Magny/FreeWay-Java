package main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Carro;

public class Rua extends Thread {
	Carro carros_rua[];
	Thread ruaThread;
	
	public Rua() { // Recebe 'int tamanho'
		carros_rua = new Carro[1];
	}
	
	public void startRuaThread() {
		ruaThread = new Thread(this);
		ruaThread.start();
	}
	
	public void CreateCars() {
		
	}
	
	@Override
	public void run() {
		
	}
	
	public void update() {
		
	}
	
	public void drawCarros(Graphics2D g2) {
		
	}
	
}
