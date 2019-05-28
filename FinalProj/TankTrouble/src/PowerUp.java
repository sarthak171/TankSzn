import java.awt.image.BufferedImage;

public interface PowerUp 
{
	//Powerups/Ammo (with subclasses):
	//(ammo for walls, one shot bullet, health restore, tank speed )
	//Main Class instance var: xLoc, yLoc 
	//Methods: checkTankCol()

	public void checkTankCol();

	public String getName();

	public double getX();

	public double getY();
	
	public BufferedImage getImage();

}

