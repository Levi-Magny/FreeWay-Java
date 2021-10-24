package tile;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.Graphics2D;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[10];
		geTileImage();
	}
	
	public void geTileImage() {
		
		try {
			
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
		g2.drawImage(tile[0].image, 48, 0, gp.tileSize, gp.tileSize, null);
		g2.drawImage(tile[0].image, 96, 0, gp.tileSize, gp.tileSize, null);
	}
}
