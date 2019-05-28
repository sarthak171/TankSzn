import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Health implements PowerUp {
	private double xLoc;
	private double yLoc;
	private BufferedImage image;
	
	
	public Health(int width, int height){
		
		xLoc = 700*  Math.random();
		yLoc = 700 * Math.random();
		try {

			image = ImageIO.read(getClass().getResource("Images/health.png"));
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
		return "H";
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