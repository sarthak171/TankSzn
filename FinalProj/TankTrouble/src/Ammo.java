import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ammo implements PowerUp {

	private double xLoc;
	private double yLoc;
	private BufferedImage image;
	
	
	public Ammo(int width, int height){
		
		xLoc = 500*  Math.random();
		yLoc = 500 * Math.random();
		try {

			image = ImageIO.read(getClass().getResource("Images/ammoplus.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void checkTankCol() {
		// TODO Auto-generated method stub
		
	}

	public BufferedImage getImageIcon(){
		return image;
	}

	
	public BufferedImage getImage(){
		return image;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "A";
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