import java.applet.Applet;
import java.awt.Canvas;
import java.io.BufferedInputStream;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.omg.CORBA.portable.InputStream;

public class GameLauncher extends Canvas implements Runnable, KeyListener, ActionListener {
	//to keep it quiet:
	private static final long serialVersionUID = 1L;

	//private GameEngine engine = new GameEngine();

	public static final int xDimension=788;
	public static final int yDimension=788;//screen dimensions

	private boolean running=false;
	private Thread thread;
	private Arena arena;
	private List<Bullet> bList2;
	private ArrayList<Tank> tanks;

	//Images used:

	private Tank tank1;
	private Tank tank2;
	
	private BufferedImage tank1img;
	private BufferedImage tank2img;
	private String lastTick2 = "None";
	private String lastTick1 = "None";
	private Color greeny = new Color(0, 100, 0);
	private Color yuckY = new Color(59, 59, 59);
	public int state = 0;
	private BufferedImage credits;
	private BufferedImage howTo;
	private BufferedImage menu;
	private BufferedImage iTank1, iTank2;
	
	private boolean[] instructionsArray = new boolean[6]; //W,A,S,D,Q,UP,Left,Down,Right,Enter
	private boolean[] instructionsArray2 = new boolean[6]; //W,A,S,D,Q,UP,Left,Down,Right,Enter

	private synchronized void start(){
		if (running){
			return;
		}
		running=true;
		thread=new Thread(this);
		thread.start();
	}

	private void init(){
		arena = new Arena();
		//Loads images
		this.tank1 = arena.tank1;
		this.tank2 = arena.tank2;
		
		bList2 = tank2.getFired();
		tank1img = tank1.getImage();
		tank2img = tank2.getImage();
		tanks = new ArrayList<Tank>();
		tanks.add(tank1);
		tanks.add(tank2);

		addKeyListener(this);

		try {

			credits = ImageIO.read(getClass().getResource("Images/credits.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			iTank1 = ImageIO.read(getClass().getResource("Images/invincitank1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			iTank2 = ImageIO.read(getClass().getResource("Images/invincitank2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			howTo = ImageIO.read(getClass().getResource("Images/howTO.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			menu = ImageIO.read(getClass().getResource("Images/loading.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String soundName = "Images/BLACK_PANTHER_Trailer_2_Version_Music_Proper_Official_Movie_Soundtrack_Complete_Theme_Song_MOKU-s75eUxUxjZw.wav";    
	
		java.io.InputStream is= getClass().getResourceAsStream("Images/BLACK_PANTHER_Trailer_2_Version_Music_Proper_Official_Movie_Soundtrack_Complete_Theme_Song_MOKU-s75eUxUxjZw.wav"); 
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//AudioInputStream audioInputStream = null;
		/*
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();
		
	}

	private synchronized void stop(){
		if (!running){
			return;
		}

		running=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}


	public void run(){
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks=60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		while(running){
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime=now;
			if(delta>=1){
				tick();

				if(!(tank1.checkAlive())){

					arena.roundOver();
					tank2.addScore();

				}
				if(!(tank2.checkAlive())){

					arena.roundOver();
					tank1.addScore();

				}

				for(Bullet b: tank1.getFired()){
					if(b.getAlive()){
						b.checkColTank(tanks);

					}

				}

				for(Bullet b1: bList2){
					if(b1.getAlive()){
						b1.checkColTank(tanks);						


					}

				}
				arena.checkPUCols();
				render();
				delta--;
			}
		}
		stop();
	}


	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Image offImage = createImage(800, 800);
		
		// Creates an off-screen drawable image to be used for
		// double buffering; XSIZE, YSIZE are each of type ‘int’;
		// represents size of JFrame or JPanel, etc
		Graphics buffer = offImage.getGraphics();
		// Creates a graphics context for drawing to an 
		// off-screen image
		paintOffScreen(buffer);		// your own method
		g.drawImage(offImage, 0, 0, null);	
		// draws the image with upper left corner at 0,0
		//Draw the objects{

		//Draw the background:

		//Draw the walls:
		/*for (int x = 0; x<7;x++){
			for (int y = 0; y<7; y++){
				if (engine.maze.isWallBelow(x, y)){
					g.drawImage(hWall,(GameEngine.squareWidth+GameEngine.wallWidth)*x,(GameEngine.squareWidth+GameEngine.wallWidth)*(y+1),this);
				}
				if (engine.maze.isWallRight(x, y)){
					g.drawImage(vWall,(GameEngine.squareWidth+GameEngine.wallWidth)*(x+1),(GameEngine.squareWidth+GameEngine.wallWidth)*y,this);
				}
			}
		}*/


		//Draw the rotated tanks:

		//Draw the bullets:

		/*for (int i = 0 ;i<GameEngine.bulletList.size();i++){
			g.drawImage(bullet,(int)(GameEngine.bulletList.get(i).getPosition().getxCoord()-GameEngine.bulletWidth/2),(int)(GameEngine.bulletList.get(i).getPosition().getyCoord()-GameEngine.bulletWidth/2),this);
		}*/

		//Draw the objects}
		g.dispose();
		bs.show();
	}



	private void paintOffScreen(Graphics buffer) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D)buffer;
		if(state == 0){
			g2.drawImage(menu, 0, 0, this);
		}
		if(state == 2){
			g2.setColor(yuckY);
			g2.fillRect(100, 100, 1000, 1000);
			g2.drawImage(credits, 0, 0, this);
		}
		if(state == 3){
			g2.drawImage(howTo, 0, 0, this);
		}
		if(state == 1){
			
			Font font = new Font("Calibri", Font.BOLD, 15);
			g2.setFont(font);

			g2.setColor(Color.BLUE);
			g2.drawString("Tank 1: " + tank1.score, 700, 700);

			g2.drawString("Ammo remaining: " + tank1.getAmmo().size(), 650, 750);
			g2.setColor(Color.RED);
			g2.drawString("Tank 2: " + tank2.score, 50, 700);
			g2.drawString("Ammo remaining: " + tank2.getAmmo().size(), 50, 750);
			g2.setColor(Color.BLACK);

			// draw a red line; 2 blue rectangles, one filled and one
			// not; and 2 magenta ovals, one filled and one not


			// location of fish changes each time the timer goes off
			//	g2.rotate(Math.toRadians(90));

			for(int i = 0; i < arena.getPowerups().size(); i++){
				g2.drawImage(arena.getPowerups().get(i).getImage(), (int)arena.getPowerups().get(i).getX(),
						(int)arena.getPowerups().get(i).getY(), this);
			}

			for(int i = 0;i<arena.walls.size();i++){
				Wall w = arena.walls.get(i);
				if(w.checkAlive()){
					g2.drawImage(w.getImage(), (int)w.getxPos(), 
							(int)w.getyPos(),
							(int)(w.getxLen()),(int)(w.getyLen()), this);
					if(w.getHealth() > 60){

						g2.setColor(greeny);
					}
					else if(w.getHealth() > 35){
						g2.setColor(Color.orange);
					}
					else{
						g2.setColor(Color.RED);
					}
					g2.drawString(""+ w.getHealth() , (int)w.getxPos(), (int)(w.getyPos()));
				}
				else {
					arena.walls.remove(i);
				}

			}

			g2.setColor(Color.black);

			for(int i = 0; i < arena.getPowerups().size(); i++){
				PowerUp p = arena.getPowerups().get(i);
				//g2.drawRect((int)p.getX(), (int)p.getY(), 50, 50);

			}

			if(tank1.checkAlive()){
				AffineTransform at = new AffineTransform();
				at.setToRotation(tank1.getAngle(), 
						tank1.getX()+(tank1.getWidth()/2), tank1.getY()+(tank1.getHeight()/2));
				g2.transform(at);


				if(tank1.isInv){
					g2.drawImage(iTank1, (int)tank1.getX(), (int)tank1.getY(), this);
				}
				else{
					g2.drawImage(tank1img, (int)tank1.getX(), (int)tank1.getY(), this);
				}

				try {
					g2.transform(at.createInverse());
				} catch (NoninvertibleTransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String n = String.valueOf(tank1.getHealth());
				g2.drawString(n, (int)tank1.getX(), (int)tank1.getY());

				String n1 = String.valueOf(tank2.getHealth());
				g2.drawString(n1, (int)tank2.getX(), (int)tank2.getY());

			}


			if(tank2.checkAlive()){
				AffineTransform at1 = new AffineTransform();
				at1.setToRotation(tank2.getAngle(), 
						tank2.getX()+(tank2.getWidth()/2), tank2.getY()+(tank2.getHeight()/2));
				g2.transform(at1);

				if(tank2.isInv){
					g2.drawImage(iTank2, (int)tank2.getX(), (int) tank2.getY(), this);
				}
				else{
					g2.drawImage(tank2img, (int)tank2.getX(), (int) tank2.getY(), this);
				}



				try {
					g2.transform(at1.createInverse());
				} catch (NoninvertibleTransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			for(Bullet b: tank1.getFired()){
				if(b.getAlive()){
					g2.drawImage(b.getImage(), (int) b.getX(), (int) b.getY(), this);
				}

			}

			for(Bullet b1: bList2){
				if(b1.getAlive()){
					g2.drawImage(b1.getImage(), (int) b1.getX(), (int) b1.getY(), this);
				}

			}
		}
	}

	private void tick(){

		//	winc.println((tank1.getAngle() % (2*Math.PI) + (2*Math.PI)) % (2*Math.PI));

		if(state == 1){
			if (instructionsArray[0]){
				tank1.move(arena.walls);
				lastTick1 = "move";
			} else if (instructionsArray[2]){
				tank1.moveb(arena.walls);
				lastTick1 = "moveb";
			}
			if (instructionsArray[1]){

				tank1.setAngle(-5*0.698131701);

			}else if (instructionsArray[3]){


				tank1.setAngle(5*0.698131701);
			}



			if (instructionsArray2[0]){
				tank2.move(arena.walls);
				lastTick2 = "move";
			} else if (instructionsArray2[2]){
				tank2.moveb(arena.walls);
				lastTick2 = "moveb";
			}
			if (instructionsArray2[1]){
				tank2.setAngle(-5*0.698131701);


			}else if (instructionsArray2[3]){
				tank2.setAngle(5*0.698131701);

			}
			if(instructionsArray[5]){
				arena.dropWall(tank1);
				instructionsArray[5] = false;

			}
			if(instructionsArray2[5]){
				arena.dropWall(tank2);
				instructionsArray2[5] = false;

			}
			//	winc.println(tank1.getHealth());


			if (instructionsArray[4]){
				tank1.fire();
				instructionsArray[4] = false;


			}
			if (instructionsArray2[4]){
				tank2.fire();
				instructionsArray2[4] = false;

			}

			for(int i = 0; i < tank2.getFired().size(); i++){

				tank2.getFired().get(i).move(arena.walls);

			}

			for(int i = 0; i < tank1.getFired().size(); i++){

				tank1.getFired().get(i).move(arena.walls);


			}
		}

	}

	//TODO: set a delay in firing
	//fire if necessary}

	//For each bullet...
	/*	for (int i = 0 ; i< GameEngine.bulletList.size();i++){
			GameEngine.bulletList.get(i).moveBullet();//move it
			boolean removed = GameEngine.bulletList.get(i).reduceTimer();//count down it's timer
			if (!removed){//if the bullet hasn't run out...
				GameEngine.bulletList.get(i).tankCollision();//check for a collision
			}
		}*/


	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_B){
			state = 0;
		}
		else if(key == KeyEvent.VK_X){
			System.exit(0);	
		}
		if(state == 0){
			if(key == KeyEvent.VK_P){
				state = 1;

			}
			else if(key == KeyEvent.VK_C){
				state = 2;
			}

			else if(key == KeyEvent.VK_H){
				state = 3;
			}
		}
		if(state == 1){
			if(key == KeyEvent.VK_R){
				tank1.revive();
				tank2.revive();
				arena.roundOver();
			}
			if (key==KeyEvent.VK_W){
				instructionsArray[0]=true;
			} if (key==KeyEvent.VK_A){
				instructionsArray[1]=true;
			} if (key==KeyEvent.VK_S){
				instructionsArray[2]=true;
			} if (key==KeyEvent.VK_D){
				instructionsArray[3]=true;
			} if (key==KeyEvent.VK_1){
				instructionsArray[4]=true;
			} 
			if(key == KeyEvent.VK_2){
				instructionsArray[5] = true;
				//instructionsArray[5] = false;
			}
			if (key==KeyEvent.VK_UP){
				instructionsArray2[0]=true;
			} if (key==KeyEvent.VK_LEFT){
				instructionsArray2[1]=true;
			} if (key==KeyEvent.VK_DOWN){
				instructionsArray2[2]=true;
			} if (key==KeyEvent.VK_RIGHT){
				instructionsArray2[3]=true;
			} if (key==KeyEvent.VK_CONTROL){
				instructionsArray2[4]=true;
			} if (key==KeyEvent.VK_SHIFT){
				instructionsArray2[5] = true;
				//instructionsArray2[5] = false;
			}
		}
	}
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(state == 1){
			if (key==KeyEvent.VK_W){
				instructionsArray[0]=false;
			}else if (key==KeyEvent.VK_A){
				instructionsArray[1]=false;
			}else if (key==KeyEvent.VK_S){
				instructionsArray[2]=false;
			}else if (key==KeyEvent.VK_D){
				instructionsArray[3]=false;
			}else if (key==KeyEvent.VK_Q){
				//instructionsArray[4]=true;

			}
			if(key == KeyEvent.VK_F){
				instructionsArray[5] = true;
			}else if (key==KeyEvent.VK_UP){
				instructionsArray2[0]=false;
			}else if (key==KeyEvent.VK_LEFT){
				instructionsArray2[1]=false;
			}else if (key==KeyEvent.VK_DOWN){
				instructionsArray2[2]=false;
			}else if (key==KeyEvent.VK_RIGHT){
				instructionsArray2[3]=false;
			} if (key==KeyEvent.VK_SHIFT){
				instructionsArray2[5] = false;
			}
		}
	}

	public static void main(String args[]){
		GameLauncher game = new GameLauncher();

		//set window size:
		game.setPreferredSize(new Dimension(GameLauncher.xDimension,GameLauncher.yDimension));
		game.setMaximumSize(new Dimension(GameLauncher.xDimension,GameLauncher.yDimension));
		game.setMinimumSize(new Dimension(GameLauncher.xDimension,GameLauncher.yDimension));

		JFrame frame = new JFrame("Tank Trouble");

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);

		JMenuItem item2 = new JMenuItem("How to Play");
		item2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(null, "Tank 1: Movement with WASD, Q to Shoot, F to build wall. \nTank 2: Movement with Arrow Keys, CTRL to Shoot, SHIFT to build wall. "
						+ "\nR to restart game, X to quit.", "Instructions", JOptionPane.INFORMATION_MESSAGE);			}
		});
		menu.add(item2);




		JMenuItem item3 = new JMenuItem("Developers");
		item3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Designed by Michael Grendahl, Arvind Kalyan, Sarthak Madan, Seth Siegel.\n All code and graphics developed and created by the above.", 
						"Developers", JOptionPane.INFORMATION_MESSAGE);			
			}
		});
		menu.add(item3);





		JMenuItem item = new JMenuItem("Exit");
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showMessageDialog(null, "Developers: Michael Grendahl, Arvind Kalyan, Sarthak Madan, Seth Siegel \nAll graphics and code"
						+ " made by the above","Developers", JOptionPane.INFORMATION_MESSAGE);		
				System.exit(0);
			}
		});
		menu.add(item);
		frame.setJMenuBar(menuBar);


		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
		game.requestFocus();
		game.start();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		//arena.dropPU();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}





}