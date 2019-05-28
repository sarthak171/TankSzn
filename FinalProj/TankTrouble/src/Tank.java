import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Tank {


	private double xLoc;
	private double yLoc;
	private double angle;
	private BufferedImage bimg;
	private Queue<Wall> wallStuff;
	private Queue<Bullet> ammo;
	private List<Bullet> fired;
	private Stack<PowerUp> powerups;
	private int bulletDmg;
	private int radius;
	private int maxVel = 25;
	private double xVel = maxVel;
	private double yVel = maxVel;
	public boolean isInv = false;
	public int score = 0;
	public boolean slow = false;
	private boolean DD = false;

	private Timer timer;
	private Timer timer2;

	public Tank(double x, double y, int type) {



		//URL url = cld.getResource("\\Images\\tank1.png");
		//img = new ImageIcon(url);
		if(type == 1){
			try {

				bimg = ImageIO.read(getClass().getResource("Images\\tank1.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(type == 2){
			try {

				bimg = ImageIO.read(getClass().getResource("Images\\tank2.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		xLoc = x;
		yLoc = y; 
		bulletDmg = 100;
		ammo = new LinkedList<Bullet>();
		fired = new ArrayList<Bullet>();
		wallStuff = new LinkedList<Wall>();
		angle = Math.random()* Math.PI * 2;
		for(int i =0; i < 25; i++){
			ammo.add(new Bullet());
		}
		for(int i =0; i < 3; i++){
			wallStuff.add(new Wall());
		}

	}

	public boolean buildWall(){
		if(ammo.size()>= 2){
			ammo.remove();
			ammo.remove();
			return true;
		}
		return false;
	}

	public void setAngle(double diff){

		angle += diff;
	}



	public void addScore(){
		score++;
	}

	public void getPowerUp(PowerUp p){
		powerups.add(p);
	}

	public void usePowerUp(){
		String p = powerups.pop().getName();
		switch(p){
		case("DD"):
			ammo.peek().doubleBulletDamage();

		}



	}

	public double getX(){
		return xLoc;
	}

	public double getY(){
		return yLoc;
	}

	public void decrementHealth(int amt){
		if(bulletDmg  > 0){
			bulletDmg -= amt;

		}
	}

	public void revive(){
		DD = false;
		slow = false;
		xLoc = 100 + (Math.random()*600);
		yLoc = 100 + (Math.random()*600);
		isInv =  false;
		slow = false;
		if(bulletDmg != 100){
			bulletDmg = 100;
		}
		while(!(ammo.isEmpty())){
			ammo.remove();
		}
		while(!(fired.isEmpty())){
			fired.remove(0);
		}
		for(int i =0; i < 25; i++){
			ammo.add(new Bullet());
		}

	}

	public void incrementHealth(int amt){
		bulletDmg += amt;
	}

	public int getHealth(){
		return bulletDmg;
	}

	public boolean checkAlive(){
		if(getHealth()<=0){
			return false;
		}
		return true;
	}

	public int getRadius(){
		return radius;
	}
	public ArrayList<Wall> checkSideColW(ArrayList<Wall> walls) {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		for(Wall wal : walls) {
			if(wal.getState().equals("vert")) {
				if((wal.getyPos()<=this.yLoc+this.getWidth() && wal.getyPos()+10>=this.yLoc+this.getWidth()
				&& wal.getxPos()-wal.getxLen()/2<=this.xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xLoc)){
					wall.add(wal);
				}
				else if(wal.getyPos()+wal.getyLen()>=this.yLoc && wal.getyPos()+wal.getyLen()-10<=this.yLoc
				&& wal.getxPos()-wal.getxLen()/2<=this.xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xLoc){
					wall.add(wal);
				}
			}
			else {
				if((wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc
				&& wal.getxPos()<=this.xLoc+this.getWidth() && wal.getxPos()+10>=this.xLoc+this.getWidth())){
					wall.add(wal);
				}
				else if(wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc
				&& wal.getxPos()+wal.getxLen()>=this.xLoc && wal.getxPos()+wal.getxLen()-10<=this.xLoc) {
					wall.add(wal);
				}
			}
		}
		return wall;
	}
	public ArrayList<Wall> checkSideColNW(ArrayList<Wall> walls,double xLoc, double yLoc) {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		for(Wall wal : walls) {
			if(wal.getState().equals("vert")) {
				if((wal.getyPos()<=yLoc+this.getWidth() && wal.getyPos()+10>=yLoc+this.getWidth()
				&& wal.getxPos()-wal.getxLen()/2<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=xLoc)){
					wall.add(wal);
				}
				else if(wal.getyPos()+wal.getyLen()>=yLoc && wal.getyPos()+wal.getyLen()-10<=yLoc
				&& wal.getxPos()-wal.getxLen()/2<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=xLoc){
					wall.add(wal);
				}
			}
			else {
				if((wal.getyPos()<=yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=yLoc
				&& wal.getxPos()<=xLoc+this.getWidth() && wal.getxPos()+10>=xLoc+this.getWidth())){
					wall.add(wal);
				}
				else if(wal.getyPos()<=yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=yLoc
				&& wal.getxPos()+wal.getxLen()>=xLoc && wal.getxPos()+wal.getxLen()-10<=xLoc) {
					wall.add(wal);
				}
			}
		}
		return wall;
	}
	public String checkSideCol(ArrayList<Wall> walls) {
		for(Wall wal : walls) {
			if(wal.getState().equals("vert")) {
				if((wal.getyPos()<=this.yLoc+this.getWidth() && wal.getyPos()+10>=this.yLoc+this.getWidth()
				&& wal.getxPos()-wal.getxLen()/2<=this.xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xLoc)){
					return "tvert";
				}
				else if(wal.getyPos()+wal.getyLen()>=this.yLoc && wal.getyPos()+wal.getyLen()-10<=this.yLoc
				&& wal.getxPos()-wal.getxLen()/2<=this.xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()/2>=this.xLoc){
					return "bvert";
				}
			}
			else {
				if((wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc
				&& wal.getxPos()<=this.xLoc+this.getWidth() && wal.getxPos()+10>=this.xLoc+this.getWidth())){
					return "fhoriz";
				}
				else if(wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc
				&& wal.getxPos()+wal.getxLen()>=this.xLoc && wal.getxPos()+wal.getxLen()-10<=this.xLoc) {
					return "bhoriz";
				}
			}
		}
		return null;
	}
	public ArrayList<Wall> checkColWallsW(ArrayList<Wall> walls) {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		for(Wall wal : walls){
			if(wal.getState().equals("vert")){
				if((wal.getxPos()<=this.xLoc+this.getWidth() && wal.getxLen()+wal.getxPos()>=this.xLoc+this.getWidth() && wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc)	
						|| (wal.getxPos()+wal.getxLen()>=this.xLoc && wal.getxPos()<=this.xLoc && wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc-this.getHeight())) {
						wall.add(wal);
				}
			}
			else{
				if((wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()<=yLoc+this.getWidth() && wal.getyPos()+wal.getyLen()>=yLoc+this.getWidth())
					|| wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()+wal.getyLen()>=yLoc && wal.getyPos()<=yLoc){
						wall.add(wal);
					
				}
			}
		}
		return wall;	
	}
	public String checkColWallsS(ArrayList<Wall> walls) {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		for(Wall wal : walls){
			if(wal.getState().equals("vert")){
				if((wal.getxPos()<=this.xLoc+this.getWidth() && wal.getxLen()+wal.getxPos()>=this.xLoc+this.getWidth() && wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc)){
					return "fvert";
				}
				else if(wal.getxPos()+wal.getxLen()>=this.xLoc && wal.getxPos()<=this.xLoc && wal.getyPos()<=this.yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=this.yLoc-this.getHeight()) {
					return "bvert";
				}
			}
			else{
				if((wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()<=yLoc+this.getWidth() && wal.getyPos()+wal.getyLen()>=yLoc+this.getWidth())){
					return "thoriz";
				}
				else if(wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()+wal.getyLen()>=yLoc && wal.getyPos()<=yLoc){
					return "bhoriz";
				}
			}
		}
		return null;	
	}
	public ArrayList<Wall> checkColWalls(ArrayList<Wall> walls, double xLoc, double yLoc) {
		ArrayList<Wall> wall = new ArrayList<Wall>();
		for(Wall wal : walls){
			if(wal.getState().equals("vert")){
				if((wal.getxPos()<=xLoc+this.getWidth() && wal.getxLen()+wal.getxPos()>=xLoc+this.getWidth() && wal.getyPos()<=yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=yLoc)
				|| (wal.getxPos()+wal.getxLen()>=xLoc && wal.getxPos()<=xLoc && wal.getyPos()<=yLoc+this.getHeight() && wal.getyPos()+wal.getyLen()>=yLoc-this.getHeight())) {
						wall.add(wal);
				}
			}
			else{
				if((wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()<=yLoc+this.getWidth() && wal.getyPos()+wal.getyLen()>=yLoc+this.getWidth())
				|| wal.getxPos()<=xLoc+this.getHeight() && wal.getxPos()+wal.getxLen()>=xLoc && wal.getyPos()+wal.getyLen()>=yLoc && wal.getyPos()<=yLoc){
						wall.add(wal);
					
				}
			}
		}
		return wall;	
	}
	public void move(ArrayList<Wall> walls){
		String swal = checkSideCol(walls);
		ArrayList<Wall> swals2 = checkSideColNW(walls,this.xLoc-(xVel/8 * Math.cos((Math.toRadians(angle)))),this.yLoc- (yVel/8 * Math.sin(((Math.toRadians(angle))))));
		ArrayList<Wall> swals = checkSideColW(walls);
		String walt = checkColWallsS(walls);
		ArrayList<Wall> wal = checkColWallsW(walls);
		ArrayList<Wall> wal2 = checkColWalls(walls,this.xLoc-(xVel/8 * Math.cos((Math.toRadians(angle)))),this.yLoc- (yVel/8 * Math.sin(((Math.toRadians(angle))))));
		if((wal2.size() == 0 || wal.size() == 0) && (swal == null || swals2.size()==0)){
			xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
			yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(swals.size()==1) {
			if(swal.equals("tvert")) {
				if((this.angle%360 <= -220 && this.angle%360>=-320)) {
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=176 && Math.abs(this.angle)%360<=184) {
					if(this.angle>=0)
						this.angle = 180;
					else
						this.angle = -180;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=354 || Math.abs(this.angle)%360<=5) {
					this.angle = 0;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=90 && Math.abs(this.angle%360)<=180)
						||(this.angle%360>=0 &&this.angle%360>=180 && this.angle%360<=270)){
					this.setAngle(-2*0.698131701);
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)<=90 && Math.abs(this.angle%360)>=0) 
						|| (this.angle%360>=0 && this.angle%360>=270 && this.angle%360<=359)){
					this.setAngle(2*0.698131701);
				}
			}
			else if(swal.equals("bvert")) {
				if((this.angle%360 <=-40 && this.angle%360 >= -140) || (this.angle%360>=220 && this.angle%360<=320)) {
	
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=176 && Math.abs(this.angle)%360<=184) {
					if(this.angle>=0)
						this.angle = 180;
					else
						this.angle = -180;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=354 || Math.abs(this.angle)%360<=5) {
					this.angle = 0;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=180 && Math.abs(this.angle%360)<=270)
						||(this.angle%360>=0 &&this.angle%360>=90 && this.angle%360<=180)) {
					this.setAngle(2*0.698131701);
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=270 && Math.abs(this.angle%360)<=359)
						|| (this.angle%360>=0 &&this.angle%360<=90)) {
					this.setAngle(-2*0.698131701);
				}
			}
			else if(swal.equals("fhoriz")) {
				if(this.angle%360 >=-50 && this.angle%369<=50) {
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				
				else if(Math.abs(this.angle%360)>=86 && Math.abs(this.angle)%360<=94) {
					if(this.angle>=0)
						this.angle = 90;
					else
						this.angle = -90;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=266 && Math.abs(this.angle)%360<=274) {
					if(this.angle>=0)
						this.angle = 270;
					else
						this.angle = -270;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=90 && Math.abs(this.angle%360)<=180)
						||(this.angle%360>=0 &&this.angle%360>=180 && this.angle%360<=270)) {
					this.setAngle(2*0.698131701);
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=180 && Math.abs(this.angle%360)<=270)
						|| (this.angle%360>=0 &&this.angle%360>=90 && this.angle%360<=180)) {
					this.setAngle(-2*0.698131701);
				}
			}
			else if(swal.equals("bhoriz")) {
				if(Math.abs(this.angle%360) >=130 && this.angle%360<=230){
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=86 && Math.abs(this.angle)%360<=94) {
					if(this.angle>=0)
						this.angle = 90;
					else
						this.angle = -90;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>=266 && Math.abs(this.angle)%360<=274) {
					if(this.angle>=0)
						this.angle = 270;
					else
						this.angle = -270;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)>=270 && Math.abs(this.angle%360)<=359)
						||(this.angle%360>=0 &&this.angle%360<=90)) {
					this.setAngle(2*0.698131701);
				}
				else if((this.angle%360<=0 && Math.abs(this.angle%360)<=90)
						|| (this.angle%360>=0 &&this.angle%360>=270 && this.angle%360<=359)) {
					this.setAngle(-2*0.698131701);
				}
			}
			//xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
			//yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(wal2.size()==0 && swals2.size()==0) {
			xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
			yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(wal2.size()>1 && wal.size()>=1 || swals2.size()>1) {
		}
		else{
			if(wal.get(0).getState().equals("vert")) {
				if((Math.abs(this.angle%360)>86 && Math.abs(this.angle%360)<94)) {
					if(walt.equals("bvert")) {
						if(this.angle>=0)
							this.angle = 93;
						else
							this.angle = -93;
					}
					if(walt.equals("fvert")) {
						if(this.angle>=0)
							this.angle = 87;
						else
							this.angle = -87;
					}
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				/*else if(Math.abs(this.angle%360)>176 && Math.abs(this.angle%360)<184) {
					if(this.angle>=0)
						this.angle = 180;
					else
						this.angle = -180;
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}*/
				else if(Math.abs(this.angle%360)>266 && Math.abs(this.angle%360)<274) {
					if(walt.equals("fvert")) {
						if(this.angle>=0)
							this.angle = 273;
						else
							this.angle = -273;
					}
					if(walt.equals("bvert")) {
						if(this.angle>=0)
							this.angle = 266;
						else
							this.angle = -266;
					}
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 <= 90)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 270)
						|| (this.angle%360 >=0 && this.angle%360 >= 180 && this.angle%360<=270)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 90) && Math.abs(this.angle%360)<=180) {
					this.setAngle(2*0.698131701);
					//xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
					//xLoc += (xVel/30 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/30 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 >= 270)
						|| (this.angle%360 <= 0 && Math.abs(this.angle%360) <=90)
						|| (this.angle%360>= 0 && this.angle%360 <= 180 && this.angle%360 >= 90)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) <= 270 && Math.abs(this.angle%360) >= 180)) {
					this.setAngle(-2*0.698131701);
					//xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
			}
			else {
				if(Math.abs(this.angle%360)>356 || Math.abs(this.angle%360)<4){
					if(walt.equals("thoriz")) {
						this.angle = 3;
					}
					if(walt.equals("bhoriz")) {
						this.angle = 357;
					}
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if (Math.abs(this.angle%360)>176 && Math.abs(this.angle%360)<184) {
					if(walt.equals("thoriz")) {
						if(this.angle>=0)
							this.angle = 177;
						else
							this.angle = -183;
					}
					if(walt.equals("bhoriz")) {
						if(this.angle>=0)
							this.angle = 183;
						else
							this.angle = -177;
					}
					xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
					yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 >= 90 && this.angle%360<=180)
					|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 180 && Math.abs(this.angle%360)<=270)
					|| (this.angle%360 >=0 && this.angle%360 >= 270 && this.angle%360<=360)
					|| ((this.angle%360<= 0 && Math.abs(this.angle%360) >= 0) && Math.abs(this.angle%360)<=90)){
					this.setAngle(2*0.698131701);
					//xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 <= 90)
					|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 270 && Math.abs(this.angle%360)<=360)
					|| (this.angle%360 >=0 && this.angle%360 >= 180 && this.angle%360<=270)
					|| (this.angle%360 <= 0 && Math.abs(this.angle%360) >= 90 && Math.abs(this.angle%360)<=180)){
					this.setAngle(-2*0.698131701);
					//xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
				}
				
			}
			
		}
		//System.out.println(xLoc +", "+ yLoc);
	}
	public void moveb(ArrayList<Wall> walls){
		String walt = checkColWallsS(walls);
		System.out.println(this.angle%360);
		System.out.println(walt);
		String swal = checkSideCol(walls);
		ArrayList<Wall> swals = checkSideColW(walls);
		ArrayList<Wall> wal = checkColWallsW(walls);
		ArrayList<Wall> wal2 = checkColWalls(walls,this.xLoc+(xVel/8 * Math.cos((Math.toRadians(angle)))),this.yLoc+(yVel/8 * Math.sin(((Math.toRadians(angle))))));
		if((wal.size() == 0 || wal2.size() == 0) && swal == null){
			xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
			yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(swal != null && swals.size()==1) {
			xLoc -= (xVel/8 * Math.cos((Math.toRadians(angle))));
			yLoc -= (yVel/8 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(wal2.size()==0 && wal.size()>=1) {
			xLoc += (xVel/8 * Math.cos((Math.toRadians(angle))));
			yLoc += (yVel/8 * Math.sin(((Math.toRadians(angle)))));
		}
		else if(wal2.size()>1 && wal.size()>=1) {
			
		}
		else{
			if(wal.get(0).getState().equals("vert")) {
				if(Math.abs(this.angle%360)>86 && Math.abs(this.angle%360)<94){
					if(walt.equals("fvert")) {
						if(this.angle>=0)
							this.angle = 93;
						else
							this.angle = -93;
					}
					else{
						if(this.angle>=0)
							this.angle = 87;
						else
							this.angle = -87;
					}
					xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
					yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				else if (Math.abs(this.angle%360)>176 && Math.abs(this.angle%360)<184) {
					/*if(this.angle>=0)
						this.angle = 180;
					else
						this.angle = -180;
					xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
					yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));*/
				}
				else if (Math.abs(this.angle%360)>266 && Math.abs(this.angle%360)<274) {
					if(walt.equals("fvert")) {
						if(this.angle>=0)
							this.angle = 273;
						else
							this.angle = -273;
					}
					else {
						if(this.angle>=0)
							this.angle = 277;
						else
							this.angle = -277;
					}
					xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
					yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 <= 90)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 270)
						|| (this.angle%360 >=0 && this.angle%360 >= 180 && this.angle%360<=270)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 90) && Math.abs(this.angle%360)<=180) {
					this.setAngle(2*0.698131701);
					//xLoc -= (xVel/12 * Math.cos((Math.toRadians(angle))));
					//yLoc -= (yVel/12 * Math.sin(((Math.toRadians(angle)))));
					//xLoc += (xVel/30 * Math.cos((Math.toRadians(angle))));
					//yLoc += (yVel/30 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 >= 270)
						|| (this.angle%360 <= 0 && Math.abs(this.angle%360) <=90)
						|| (this.angle%360>= 0 && this.angle%360 <= 180 && this.angle%360 >= 90)
						|| (this.angle%360<= 0 && Math.abs(this.angle%360) <= 270 && Math.abs(this.angle%360) >= 180)) {
					this.setAngle(-2*0.698131701);
					//xLoc -= (xVel/12 * Math.cos((Math.toRadians(angle))));
					//yLoc -= (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
			}
			else {
				if(Math.abs(this.angle%360)>356 || Math.abs(this.angle%360)<4) {
					if(walt.equals("thoriz")) {
						this.angle = 357;
					}
					else {
						this.angle = 3;
					}
					xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
					yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				else if(Math.abs(this.angle%360)>176 && Math.abs(this.angle%360)<184) {
					if(walt.equals("thoriz")) {
						if(this.angle>=0)
							this.angle = 183;
						else
							this.angle = -183;
					}
					else {
						if(this.angle>=0)
							this.angle = 177;
						else
							this.angle = -177;
						
					}
					xLoc += (xVel/12 * Math.cos((Math.toRadians(angle))));
					yLoc += (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 >= 90 && this.angle%360<=180)
					|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 180 && Math.abs(this.angle%360)<=270)
					|| (this.angle%360 >=0 && this.angle%360 >= 270 && this.angle%360<=360)
					|| ((this.angle%360<= 0 && Math.abs(this.angle%360) >= 0) && Math.abs(this.angle%360)<=90)){
					this.setAngle(2*0.698131701);
					//xLoc -= (xVel/12 * Math.cos((Math.toRadians(angle))));
					//yLoc -= (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				else if((this.angle%360 >=0 && this.angle%360 <= 90)
					|| (this.angle%360<= 0 && Math.abs(this.angle%360) >= 270 && Math.abs(this.angle%360)<=360)
					|| (this.angle%360 >=0 && this.angle%360 >= 180 && this.angle%360<=270)
					|| (this.angle%360 <= 0 && Math.abs(this.angle%360) >= 90 && Math.abs(this.angle%360)<=180)){
					this.setAngle(-2*0.698131701);
					//xLoc -= (xVel/12 * Math.cos((Math.toRadians(angle))));
					//yLoc -= (yVel/12 * Math.sin(((Math.toRadians(angle)))));
				}
				
			}
		}
		//System.out.println(xLoc +", "+ yLoc);
	}
	
	




	public int getHeight(){
		return bimg.getHeight();
	}

	public int getWidth(){
		return bimg.getWidth();
	}


	public void fire(){

		if(!ammo.isEmpty()){
			Bullet b = ammo.remove();

			if(slow && DD){
				b = new Bullet(this.getX() + (getHeight()/2), this.getY() - 7 +  (getWidth()/2), 1, 3, angle, 100);
				DD = false;

			}
			else if(slow){
				b = new Bullet(this.getX() + (getHeight()/2), this.getY() - 7 +  (getWidth()/2), 1, 3, angle, 50);
			}
			else if(DD){
				b = new Bullet(this.getX() + (getHeight()/2), this.getY() - 7 +  (getWidth()/2), 1, 6, angle, 100);
				DD = false;
			}
			else{
				b = new Bullet(this.getX() + (getHeight()/2), this.getY() - 7 +  (getWidth()/2), 1, 6, angle, 50);
			}
			fired.add(b);

		}

	}


	public void killBullet(){
		fired.remove(0);
	}

	public void incrementAmmo(){
		if(ammo.size() < 5){
			ammo.add(new Bullet());}
	}

	public Queue<Bullet> getAmmo(){
		return ammo;
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		//	ImageIcon i = new ImageIcon("tankTest.png");


		return bimg;


	}

	public List<Bullet> getFired(){
		return fired;
	}
	public double getAngle() {
		// TODO Auto-generated method stub
		return Math.toRadians(angle);
	}

	public void doubleDamage(){
		DD = true;
	}

	public void HalfSpeed(){
		
		xVel *= 0.5;
		yVel *= 0.5;
		slow = true;
		timer = new Timer();
		timer.schedule(new RemindTask(), 10*1000);
	}
	class RemindTask extends TimerTask{


		public void run() {
			// TODO Auto-generated method stub
			xVel *= (2);
			yVel *= (2);
			slow = false;
			timer.purge();
		}

	}

	public void addAmmo(){
		for(int i =0; i < 10; i++){
			ammo.add(new Bullet());
		}
	}
	public void addHealth(){
		bulletDmg += 50;
	}
	public void invicibile(){
		isInv = true;
		timer2 = new Timer();
		timer2.schedule(new Task(), 10*1000);
	}
	class Task extends TimerTask{


		public void run() {
			// TODO Auto-generated method stub
			isInv = false;
			timer2.purge();
		}

	}



}

