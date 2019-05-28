import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
public class Bullet {
	private double xPos;
	private double yPos;

	private double xVel;
	private double yVel;
	private double angle;
	private boolean isAlive;
	private Timer timer;
	private Timer timer2;
	private int bulletDmg;
	private BufferedImage bimg;
	private boolean bool = false;
	private boolean collide = true;
	public Bullet(){

	}
	public Bullet(double xp, double yp, double r, double Vel, double ang, int dmg){
		if(dmg == 100){
			try {

				bimg = ImageIO.read(getClass().getResource("Images/fireyone.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			try {

				bimg = ImageIO.read(getClass().getResource("Images/Bullet.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		xPos = xp;
		yPos = yp;
		xVel = Vel*Math.cos((Math.toRadians(ang)));
		yVel = Vel*Math.sin((Math.toRadians(ang)));
		//winc.print(xVel+" "+yVel);
		angle = ang;
		bulletDmg = dmg;
		isAlive = true;
		timer = new Timer();
		timer2 = new Timer();
		timer.schedule(new ReminderTask(), 125);
		timer.schedule(new RemindTask(),10*1000);
	}


	public double getAngle(){
		return angle;
	}
	public void setAngle(double d){
		angle = d;
	}

	class ReminderTask extends TimerTask{


		public void run() {
			// TODO Auto-generated method stub
			bool = true;
		}

	}


	class RemindTask extends TimerTask{

		public void run(){
			bulletDmg = 0;
			isAlive = false;

			timer.cancel();
		}
	}

	public void doubleDamage(){
		bulletDmg *= 2;
	}
	public Wall checkColWalls(ArrayList<Wall> walls){
		if(!collide)
			return null;
		for(Wall wal : walls){
			if(wal.checkAlive()){
				if(wal.getState().equals("vert")){
					if((wal.getxPos()<=this.xPos && wal.getxLen()+wal.getxPos()>=this.xPos && wal.getyPos()<=this.yPos && wal.getyPos()+wal.getyLen()>=this.yPos)
							|| (wal.getxPos()+wal.getxLen()>=this.xPos && wal.getxPos()<=this.xPos && wal.getyPos()<=this.yPos && wal.getyPos()+wal.getyLen()>=this.yPos)){
						collide = false;

						timer2.schedule(new RemTask(), 100);
						return wal;
					}
				}
				else{
					if((wal.getxPos()<=this.xPos && wal.getxPos()+wal.getxLen()>=this.xPos && wal.getyPos()<=this.yPos && wal.getyPos()+wal.getyLen()>=this.yPos)
							|| wal.getxPos()<=this.xPos && wal.getxPos()+wal.getxLen()>=this.xPos && wal.getyPos()+wal.getyLen()>=this.yPos && wal.getyPos()<=this.yPos){
						collide = false;

						timer2.schedule(new RemTask(), 100);
						return wal;
					}
				}
			}
		}
		return null;
	}
	class RemTask extends TimerTask{

		public void run(){

			collide = true;
		}
	}
	public Tank checkColTank(ArrayList<Tank> tanks){
		if(bool){
			for(Tank tan : tanks){
				if(tan.getHeight()>=
						Math.sqrt((Math.pow(this.xPos+this.getWidth()/2-(tan.getX()+tan.getWidth()/2), 2)
								+Math.pow(this.yPos+this.getHeight()/2-(tan.getY()+tan.getHeight()/2),2)))){
					if(!tan.isInv){
						tan.decrementHealth(bulletDmg);
					}
					isAlive = false;
					bulletDmg = 0;
					return tan;
				}
			}
		}
		return null;
	}
	public void colwithWall(Wall wal){
		if(wal.checkAlive()){
			if(wal.getState().equals("horiz")){
				yVel *= -1;
				wal.updateHealth(bulletDmg/2.5);
			}
			else if(wal.getState().equals("vert")){
				xVel *= -1;
				wal.updateHealth(bulletDmg/2.5);
			}
		}
	}
	public void colWithTank(Tank tan){
		tan.decrementHealth(bulletDmg);
		isAlive = false;
	}
	public Wall checkSideCol(ArrayList<Wall> walls) {
		for(Wall wal : walls) {
			if(wal.checkAlive()){
				if(wal.getState().equals("vert")) {
					if((wal.getyPos()<=this.yPos+this.getWidth() && wal.getyPos()+10>=this.yPos+this.getWidth()
					&& wal.getxPos()-wal.getxLen()/2<=this.xPos+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xPos)
							|| (wal.getyPos()+wal.getyLen()>=this.yPos && wal.getyPos()+wal.getyLen()-10<=this.yPos
							&& wal.getxPos()-wal.getxLen()/2<=this.xPos+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xPos)) {
						return wal;
					}
				}
				else {
					if((wal.getyPos()<=this.yPos+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yPos
							&& wal.getxPos()<=this.xPos+this.getWidth() && wal.getxPos()+30>=this.xPos+this.getWidth())
							|| (wal.getyPos()<=this.yPos+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yPos
							&& wal.getxPos()+wal.getxLen()>=this.xPos && wal.getxPos()+wal.getxLen()-10<=this.xPos)) {
						return wal;
					}
				}
			}
		}
		return null;
	}

	public void colWithSWall(Wall wal) {
		System.out.println("seart");
		if(wal.getState().equals("vert") && wal.checkAlive()){
			yVel *= -1;
			wal.updateHealth(bulletDmg/2.5);
		}
		else if(wal.getState().equals("horiz") && wal.checkAlive()){
			xVel *= -1;
			wal.updateHealth(bulletDmg/2.5);
		}
	}
	public void move(ArrayList<Wall> walls){
		Wall wal = checkColWalls(walls);
		Wall swal = checkSideCol(walls);
		if(wal != null){
			colwithWall(wal);
		}
		if(swal != null) {
			colWithSWall(swal);
		}
		xPos -= (xVel);
		yPos -= (yVel);

		//System.out.println(xLoc +", "+ yLoc);
	}

	public boolean getAlive(){
		return isAlive;
	}

	public double getX(){
		return xPos;
	}
	public double getY(){
		return yPos;
	}
	public double getVelX(){
		return xVel;
	}
	public double getVelY(){
		return yVel;
	}
	public void doubleBulletDamage() {
		// TODO Auto-generated method stub
		bulletDmg *= 2;
	}
	public Image getImage() {
		// TODO Auto-generated method stub


		return bimg;

	}
	public int getHeight(){
		return bimg.getHeight();
	}

	public int getWidth(){
		return bimg.getWidth();
	}


}





