import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
public class Wall {
	private double health;
	private String state;
	private double xPos;
	private double yPos;
	private double xLen;

	private BufferedImage walimg;
	private double yLen;
	public Wall(){

	}
	public Wall(double hel, String stat, double xP, double yP, double xL, double yL){

		if(stat.equals("horiz")){
			try {

				walimg = ImageIO.read(getClass().getResource("Images/HorWall.png"));
				health = hel;
				state = stat;
				xPos = xP;
				yPos = yP;
				xLen = xL*walimg.getWidth();
				yLen = yL*walimg.getHeight() + 10;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		else{
			try {

				walimg = ImageIO.read(getClass().getResource("Images/Wall.png"));
				health = hel;
				state = stat;
				xPos = xP;
				yPos = yP;
				xLen = xL*walimg.getWidth() + 10;
				yLen = yL*walimg.getHeight();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}
	public boolean checkColWalls(ArrayList<Wall> walls){
		for(Wall wal : walls) {
			if(checkAlive()){
				if(wal != this) {
					if(this.getState().equals("vert") && wal.getState().equals("vert")) {
						if((this.xPos<=wal.getxPos() && this.xPos+this.xLen>=wal.getxPos()) 
								|| (this.xPos<=wal.getxPos()+wal.getxLen() && this.xPos+this.xLen>=wal.getxPos()+wal.getxLen())) {
						
							return true;
						}
					}
					else if(this.getState().equals("horiz") && wal.getState().equals("horiz")) {
						if((this.yPos<=wal.getyPos() && this.yPos+this.yLen>=wal.getyPos())
								|| (this.yPos<=wal.getyPos()+wal.getyLen() && this.yPos+this.yLen>=wal.getyPos()+wal.getyLen())) {
							
							return true;
						}
					}
					else {
						if(this.getState().equals("vert")) {
							if(this.xPos>=wal.getxPos() && this.xPos+this.xLen<=wal.getxPos()+wal.getxLen() && this.yPos<=wal.getyPos() && this.yPos+this.yLen>=wal.getyPos()+wal.getyLen()) {
								
								return true;
							}
						}
						else {
							if(this.xPos<=wal.getxPos() && this.xPos+this.xLen>=wal.getxLen()+wal.getxPos() && this.yPos>=wal.getyPos() && this.yPos+this.yLen<=wal.getyPos()+wal.getyLen()) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;

	}
	public boolean checkAlive(){
		if(health<=0){
			return false;
		}
		return true;
	}
	public void updateHealth(double subHealth){
		health -= subHealth;
	}
	public String getState(){
		return state;
	}
	public double getxPos(){
		return xPos;
	}
	public double getyPos(){
		return yPos;
	}
	public double getxLen(){
		return xLen;
	}
	public double getyLen(){
		return yLen;
	}
	public double getHealth(){
		return health;
	}
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		//	ImageIcon i = new ImageIcon("tankTest.png");
		return walimg;
	}
}


