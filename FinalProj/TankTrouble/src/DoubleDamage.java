import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DoubleDamage implements PowerUp {

	private double xLoc;
	private double yLoc;
	private BufferedImage image;
	
	
	public DoubleDamage(int width, int height){
		
		xLoc = 600*  Math.random();
		yLoc = 600 * Math.random();
		try {

			image = ImageIO.read(getClass().getResource("Images/doubledamage.png"));
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
		return "DD";
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