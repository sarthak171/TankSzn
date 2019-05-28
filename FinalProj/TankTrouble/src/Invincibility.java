import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Invincibility implements PowerUp {
	private double xLoc;
	private double yLoc;
	private BufferedImage image;
	
	
	public Invincibility(int width, int height){
		
		xLoc =600*  Math.random();
		yLoc = 600 * Math.random();
		try {

			image = ImageIO.read(getClass().getResource("Images/Invincibility.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkTankCol() {
		// TODO Auto-generated method stub
		
	}

	

	
	public BufferedImage getImage(){
		return image;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "I";
	}

	public double getX() {
		// TODO Auto-generated method stub
		return xLoc;
	}

	public double getY() {
		// TODO Auto-generated method stub
		return yLoc;
	}
}