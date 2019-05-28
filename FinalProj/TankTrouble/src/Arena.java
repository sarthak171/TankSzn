import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Arena implements ActionListener{

	public ArrayList<Wall> walls = new ArrayList<Wall>();
	private Queue<PowerUp> total = new LinkedList<PowerUp>();
	private List<PowerUp>  powerups = new ArrayList<PowerUp>();
	public Tank tank1;
	public Tank tank2;
	private Tank[] tanks;
	private ScheduledExecutorService executor;

	public Arena(){
		tanks = new Tank[2];
		tank1 = new Tank(100+(int) (Math.random()*600), 100+(int)(Math.random() * 600),1);
		tank2 = new Tank(100+(int) (Math.random()*600), 100+(int)(Math.random() * 600),2);
		tanks[0] = tank1;
		tanks[1] = tank2;







		for(int i = 0; i < 7; i++){
			double k = (5*Math.random());
			if(k >= 4.6){
				total.add(new Invincibility(50,50));	
			}
			else if(k >= 2.5){
				total.add(new Ammo(50,50));
			}
			else if(k >= 1.5){
				total.add(new Health(50,50));
			}
			else if(k >= 0.5){
				total.add(new DoubleDamage(50, 50));
			}
			else if (k > 0.25){ 
				total.add(new OneFiveSpeed(50,50));
			}
		}

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(PURunnable, 5, 25, TimeUnit.SECONDS);
		int i = 0;
		walls.add(new Wall(1000000, "vert", 0, 
				0, 0.25, 1));
		walls.add(new Wall(1000000, "horiz", 0, 
				0, 0.55, 0.25));
		walls.add(new Wall(10000000, "horiz", 0, 
				785, 0.55, 1));

		walls.add(new Wall(10000000, "vert", 785, 
				0, 0.25, 1));

		while(i < 10){



			if(i%2 == 0){
				walls.add(new Wall(100, "vert", (int)(Math.random()*800), 
						(int)(Math.random() * 800), .1+Math.random()/4.0, .1+Math.random()/4.0));
				if(tank1.checkColWallsW(walls).size() !=0 || tank2.checkColWallsW(walls).size() != 0 || walls.get(walls.size()-1).checkColWalls(walls)) {
					walls.remove(walls.get(walls.size()-1));
				}

			}
			else{
				walls.add(new Wall(100, "horiz", (int)(Math.random()*800), 
						(int)(Math.random() * 800), .1+Math.random()/4.0, .1+Math.random()/4.0));
				if(tank1.checkColWallsW(walls).size() !=0 || tank2.checkColWallsW(walls).size() != 0 || walls.get(walls.size()-1).checkColWalls(walls)) {
					walls.remove(walls.get(walls.size()-1));
				}
			}

			i++;
		}

	}

	Runnable PURunnable = new Runnable() {
		public void run() {
			dropPU();
			
		}
	};

	public void roundOver(){

		while(powerups.isEmpty() == false){
			powerups.remove(0);
		}
		for(int i = 0; i < 7; i++){
			double k = (5*Math.random());
			if(k >= 4.6){
				total.add(new Invincibility(50,50));	
			}
			else if(k >= 2.5){
				total.add(new Ammo(50,50));
			}
			else if(k >= 1.5){
				total.add(new Health(50,50));
			}
			else if(k >= 0.5){
				total.add(new DoubleDamage(50, 50));
			}
			else if (k > 0.25){ 
				total.add(new OneFiveSpeed(50,50));
			}
		}
		tank1.revive();
		tank2.revive();
		int i  =0;
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(PURunnable, 25, 250, TimeUnit.SECONDS);
		while(!walls.isEmpty()){
			walls.remove(0);
		}
		generateWalls();

	}	

	public void generateWalls(){
		double temp = 6*Math.random();
		if(temp >= 4){
			int i = 0;
			walls.add(new Wall(1000000, "vert", 0, 
					0, 0.25, 1));
			walls.add(new Wall(1000000, "horiz", 0, 
					0, 0.55, 0.25));
			walls.add(new Wall(10000000, "horiz", 0, 
					785, 0.55, 1));

			walls.add(new Wall(10000000, "vert", 785, 
					0, 0.25, 1));

			 
			

			while(i < 10){
				int x = (int)(Math.random()*800);
				int y = (int)(Math.random() * 800);
				if(x != tank1.getX() && x!= tank2.getX() && y != tank1.getY() && y!= tank2.getAngle()){

					if(i%2 == 0){
						walls.add(new Wall(100, "vert", x, 
								y, .1+Math.random()/4.0, .1+Math.random()/4.0));
						if(tank1.checkColWallsW(walls).size() !=0 || tank2.checkColWallsW(walls).size() != 0 || walls.get(walls.size()-1).checkColWalls(walls)) {
							walls.remove(walls.get(walls.size()-1));

						}

					}
					else{
						walls.add(new Wall(100, "horiz", x, 
								y, .1+Math.random()/4.0, .1+Math.random()/4.0));
						if(tank1.checkColWallsW(walls).size() !=0 || tank2.checkColWallsW(walls).size() != 0 || walls.get(walls.size()-1).checkColWalls(walls)) {
							walls.remove(walls.get(walls.size()-1));
						}
					}
					

					i++;
				}
			}
		}
		else{
			
		}
	}
	
	public int checkPUCols(){
		if(powerups.size() > 0){
			for(int k = 0;k  < powerups.size(); k++){
				PowerUp p = powerups.get(k);
				for(int i = 0; i < tanks.length; i++){
					Tank tan = tanks[i];
					
					if(tan.getHeight()>=
							Math.sqrt((Math.pow(p.getX()+25-(tan.getX()+tan.getWidth()/2), 2)
									+Math.pow(p.getY()+25-(tan.getY()+tan.getHeight()/2),2)))){
						
						if(p.getName().equals("DD")){
							tan.doubleDamage();

						}
						else if(p.getName().equals("OFS")){
							if(i == 0){
								Tank t = tanks[1];
								t.HalfSpeed();
							}
							else{
								Tank t = tanks[0];
								t.HalfSpeed();
							}


						}
						else if(p.getName().equals("I")){
							tan.invicibile();

						}
						else if(p.getName().equals("H")){
							tan.addHealth();

						}
						else if(p.getName().equals("A")){
							tan.addAmmo();

						}
						returnPU(k);
					}
				}
			}
		}
		return -1;
	}

	public PowerUp dropPU(){
		
		if(!total.isEmpty() && powerups.size() < 2){
			PowerUp n = total.remove();
			
			powerups.add(n);
			return n;
		}
		return null;
	}

	public void returnPU(int k){
		if(k >= 0){
			powerups.remove(k);
		}
		if(powerups.size() == 0){
			for(int i = 0; i < 7; i++){
				double b = (5*Math.random());
				if(b >= 4.6){
					total.add(new Invincibility(50,50));	
				}
				else if(b >= 2.5){
					total.add(new DoubleDamage(50,50));
				}
				else if(b >= 1.5){
					total.add(new Health(50,50));
				}
				else if(b >= 0.5){
					total.add(new DoubleDamage(50, 50));
				}
				else{ 
					total.add(new OneFiveSpeed(50,50));
				}
			}
		}
	}

	public List<Wall> getWalls(){
		return this.walls;
	}


	public List<PowerUp> getPowerups(){
		return powerups;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//PowerUp ok = dropPU();

	}

	public void dropWall(Tank t){
		if(t.buildWall()){
			if((t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) < 0.25*Math.PI 
					||(t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) > 1.75*Math.PI ){
				walls.add(new Wall(100, "vert", t.getX()-30, 
						t.getY()-15, 0.1, 0.1));
			}
			else if((t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) >= 0.25*Math.PI 
					&&(t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) < 0.75*Math.PI ){
				walls.add(new Wall(100, "horiz", t.getX()-15, 
						t.getY()-35, 0.1, 0.1));
			}
			else if((t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) >= 0.75*Math.PI 
					&& (t.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI) < 1.25*Math.PI ){
				walls.add(new Wall(100, "vert", t.getX()+t.getWidth()+10, 
						t.getY()-15, 0.1, 0.1));
			}
			else{
				walls.add(new Wall(100, "horiz", t.getX()-15, 
						t.getY()+t.getHeight()+28, 0.1, 0.1));
			}
		}
	}
}