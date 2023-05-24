import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.sound.sampled.*;
import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class Game extends JPanel implements KeyListener {
	// Maze generator declaration
	private MazeGenerator MG;
	
	// Maze sizes declaration
	private int MazeW = 0;
	private int MazeH = 0;
	private int sizeX;
	private int sizeY;
	private int move = 0;
	private int moveY = 0;
	private int moveX = 0;
	private Dimension d;
	
	// Actors declaration
	private player PLAYER;
	private computer COMPUTER; 
	
	// Game mode
	private int mode = 0;
	
	// Game mode selection tab
	private int MMtab = 0;
	private int MMtabSelected = 0;
	
	// Game state declaration for each state
	private boolean win = false;
	private boolean Test = false;
	private boolean Cheats = false;
	
	// Game stats declaration
	private long startTime;
	private long [] deathTIME = new long [5];
	private int x1,x2,y1,y2;
	private int timeBetweenDeath = 5;
	private long TIME;
	private ArrayList<Crystal> CRYSTALS=new ArrayList<Crystal>();
	private int Csize=5;
	private boolean isPausing = false;
    private boolean isGameDone = false;
	private long elapsedTime ;
	private long elapsedSeconds ;
	private long secondsDisplay ;
	private long elapsedMinutes ;
	private long timeWhilePaused;
	
	// Some flags
	private boolean cheatsSOUND=false;
	private boolean playState=true;
	
	// Audio declaration for audio manager of the game 
	AudioManager audioManager = new AudioManager();
	
	// Cheat code declaration ,the code is: "m;kfa"
	private ArrayList<Integer> keys=new ArrayList<Integer>();
	private int[] cheatCode=new int[]{KeyEvent.VK_M,KeyEvent.VK_SEMICOLON,KeyEvent.VK_K,KeyEvent.VK_F,KeyEvent.VK_A};
	
	///
	private int counter=0;
	Game(int w,int h){
		this.audioManager.playMainMenuSound();
		this.MazeW=w;
		this.MazeH=h;
		
		this.d=this.getSize();
		addKeyListener(this);
		this.mode=0;
		
	}
	private void BuildMaze(int w,int h,int move){
		this.audioManager.playGameSound();
	    this.startTime = System.currentTimeMillis();

	    this.playState=true;
	    this.cheatsSOUND=false;
	    elapsedTime =0;
		elapsedSeconds =0;
		secondsDisplay =0;
		elapsedMinutes =0;
		timeWhilePaused =0;
		
		this.move=move;
		this.MazeW=w;
		this.MazeH=h;

		this.sizeX=(int)d.getWidth()/MazeW;
		this.sizeY=(int)d.getHeight()/MazeH;
		if(d.getWidth()>d.getHeight()){
			
		
			sizeX=sizeY;
			this.move=sizeX/25;
		}
		else {
			
		
			sizeY=sizeX;
			this.move=sizeY/25;
		}
		
		this.MG=new MazeGenerator(w,h);
		this.win=false;
		
		deathTIME=new long[]{-1,-1,-1,-1,-1};
		
		PLAYER=new player(this,MG.getPlayerBase());
		COMPUTER=new computer(this,MG.getComputerBase());
		this.buildCrystals();
		this.initCrystals();
		COMPUTER.start();
		
		
		
	}
	private void buildCrystals(){
		
		   if(!CRYSTALS.isEmpty()){
			   CRYSTALS.removeAll(CRYSTALS);
		   }
	

		for(int i=0;i<this.Csize;i++){
			x1 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
			y1 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
			x2 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
			y2 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
			
			
			CRYSTALS.add(new Crystal(this,new Point(x1,y1),new Point(x2,y2)));
		}
		
		
	}
	private void initCrystals(){
		for(int i=0;i<this.CRYSTALS.size();i++){
			
			CRYSTALS.get(i).setBehavior(1);
			
			CRYSTALS.get(i).start();
		}
	}
	private void stopCrystals(){
		for(int i=0;i<this.CRYSTALS.size();i++){
			CRYSTALS.get(i).stopThread();
			CRYSTALS.get(i).stop();
			CRYSTALS.remove(i);
		}
	}
	public ArrayList<Crystal> getCrystals(){
		return this.CRYSTALS;
	}
	
	public Point getPLAYERcoords(){
		return this.PLAYER.getCoordinates();
	}
	public Point getCOMPUTERcoords(){
		return this.COMPUTER.getCoordinates();
	}
	
	public void changeScore(int Score,int Who){
		// PLAYER = 0 , COMPUTER = 1
		if(Who == 0)
			this.PLAYER.setScore(Score);
		else if(Who == 1)
			this.COMPUTER.setScore(Score);
	}
	public MazeGenerator getMazeLayout(){
		return MG;
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.setBackground(Color.white);
		
		if(!isPausing){
		 elapsedTime = startTime+TIME-System.currentTimeMillis()+this.timeWhilePaused;
		 elapsedSeconds = elapsedTime / 1000;
		 secondsDisplay = elapsedSeconds % 60;
		 elapsedMinutes = elapsedSeconds / 60;
		}
	
		
		
		this.d=this.getSize();
		this.sizeX=(int)d.getWidth()/MazeW;
		this.sizeY=(int)d.getHeight()/MazeH;
		if(d.getWidth()>d.getHeight()){
			
			
			sizeX=sizeY;
		}
		else {
			
		
			sizeY=sizeX;
		}
	switch (this.mode){
	case 0:{
		int MiddleX=(int)d.getWidth()/40*17;
		
		//Main Menu Title
	    g.setFont(new Font("TimesRoman", Font.BOLD, 65));
		g.setColor(Color.BLUE);
		g.drawString("Main Menu", MiddleX, (int)d.getHeight()/10);
		
		//Choose Game Mode
		g.setFont(new Font("TimesRoman", Font.BOLD, 35));
		g.setColor(Color.BLACK);
		g.drawString("Choose Board Size", MiddleX, (int)d.getHeight()/100*25);
		
		//Size 10x10
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(Color.BLACK);
		g.drawString("15x15", MiddleX, (int)d.getHeight()/100*35);
		
		//Size 25x25
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(Color.BLACK);
		g.drawString("25x25", MiddleX, (int)d.getHeight()/100*40);
		
		//Size 50x50
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(Color.BLACK);
		g.drawString("50x50", MiddleX, (int)d.getHeight()/100*45);
		
		//Selected Tab
		if(MMtab==1)
			g.drawRect(MiddleX-5, (int)d.getHeight()/100*35-((int)d.getHeight()/100*35)/100*9, (int)d.getWidth()/1000*90, (int)d.getWidth()/1000*30);
		else if(this.MMtab==2)
			g.drawRect(MiddleX-5, (int)d.getHeight()/100*40-((int)d.getHeight()/100*40)/100*7, (int)d.getWidth()/1000*90, (int)d.getWidth()/1000*30);
		else if(this.MMtab==3)
			g.drawRect(MiddleX-5, (int)d.getHeight()/100*45-((int)d.getHeight()/100*45)/100*7, (int)d.getWidth()/1000*90, (int)d.getWidth()/1000*30);
		
	
		
		break;
		
	}
	case 1:{
		
		break;
		
	}
	case 2:{
		//Test mode ACTIVATED!
		if(this.Test)
		{
			g.setFont(new Font("TimesRoman", Font.BOLD, 25));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("TEST MODE ACTIVATED!", (int)d.getWidth()/40*15, (int)d.getHeight()/100*95);
		}
		//CHEATS MOD ACTIVATED!
		if(this.Cheats)
		{	
			
			PLAYER.Cheats();
			g.setFont(new Font("TimesRoman", Font.BOLD, 25));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("CHEAT MODE ACTIVATED!", (int)d.getWidth()/40*15, (int)d.getHeight()/100*95);
			
			for(int i=0;i<this.PLAYER.getPath().size();i++)
			{
							
				g.setColor(Color.BLACK);
				g.fillRect(moveX+move+this.PLAYER.getPath().get(i).x*sizeX+5, moveY+move+this.PLAYER.getPath().get(i).y*sizeY+5, sizeX-10, sizeY-10);
				
			}
		}
		//draw board
		g.setColor(Color.black);
		for (int i = 0; i < MazeH; i++) {
			
			for (int j = 0; j < MazeW; j++) {
				//System.out.print((MG.getTiles()[j][i]).NorthWall == 1 ? "+---" : "+   ");
				if((MG.getTiles()[j][i]).NorthWall == 1){
					g.drawLine(moveX+move+j*sizeX, moveY+move+i*sizeY, moveX+move+(j+1)*sizeX, moveY+move+(i)*sizeY);
				}
			}
			
			
			for (int j = 0; j < MazeW; j++) {
				//System.out.print((MG.getTiles()[j][i]).WestWall == 1 ? "|   " : "    ");
				if((MG.getTiles()[j][i]).WestWall == 1){
					g.drawLine(moveX+move+j*sizeX, moveY+move+i*sizeY, moveX+move+(j)*sizeX, moveY+move+(i+1)*sizeY);
				}
			}
			if(MG.getTiles()[MazeW-1][i].EastWall==1)
			//System.out.println("|");
				g.drawLine(moveX+move+(MazeW)*sizeX, moveY+move+i*sizeY, moveX+move+(MazeW)*sizeX, moveY+move+(i+1)*sizeY);
			
		}
			
		for (int j = 0; j < MazeW; j++) {
			if(MG.getTiles()[j][MazeH-1].SouthWall==1)
			//System.out.print("+---");

				g.drawLine(moveX+move+(j)*sizeX, moveY+move+(MazeH)*sizeY, moveX+move+(j+1)*sizeX, moveY+move+(MazeH)*sizeY);
			
		}
		//drawBases
		g.setColor(Color.BLUE);
		g.fillRect(moveX+move+MG.getPlayerBase().x*sizeX+sizeX/10, moveY+move+MG.getPlayerBase().y*sizeY+sizeX/10, sizeX-(sizeX/10)*2, sizeY-(sizeX/10)*2);
		g.setColor(Color.RED);
		g.fillRect(moveX+move+MG.getComputerBase().x*sizeX+sizeX/10, moveY+move+MG.getComputerBase().y*sizeY+sizeX/10, sizeX-(sizeX/10)*2, sizeY-(sizeX/10)*2);
		
	
		
		//draw crystals
		BufferedImage imageC = null;
		   try {                
		          imageC = ImageIO.read(new File("Crystal.png"));
		       } catch (IOException ex) {
		            // handle exception...
		       }
		   for(int i=0;i<CRYSTALS.size();i++){
			   if(!this.CRYSTALS.isEmpty()&&this.CRYSTALS.get(i).getToStop()==0)
					   {
				g.drawImage(imageC, moveX+move+CRYSTALS.get(i).getCoordinates().x*sizeX+sizeX/10*2, moveY+move+CRYSTALS.get(i).getCoordinates().y*sizeY+sizeY/10*2, sizeX/3*2, sizeY/3*2, this);
				
				//if (this.CRYSTALS[i].isAlive()&&this.CRYSTALS[i].checkCollision()!=0)
				if (this.CRYSTALS.get(i).getToStop()==0 && this.CRYSTALS.get(i).checkCollision()!=0)
				{
					counter++;
					//System.out.println("how many died : "+counter);
					this.changeScore(this.CRYSTALS.get(i).getWorth(), this.CRYSTALS.get(i).checkCollision()-1);
					
					//this.CRYSTALS[i].stop();
					this.CRYSTALS.get(i).stopThread();
					this.deathTIME[i]=elapsedSeconds;
				}
				
			   }
			}	
		    for(int i=0;i<this.CRYSTALS.size();i++){
			   if(deathTIME[i]!=-1 && deathTIME[i]>= elapsedSeconds+3 && !CRYSTALS.isEmpty()){
				   
				x1 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
				y1 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
				x2 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
				y2 = ThreadLocalRandom.current().nextInt(1, MazeW-2 + 1);
				CRYSTALS.remove(i);
				CRYSTALS.add(new Crystal(this,new Point(x1,y1),new Point(x2,y2)));
				CRYSTALS.get(CRYSTALS.size()-1).setBehavior(1);
				CRYSTALS.get(CRYSTALS.size()-1).startThread();
				CRYSTALS.get(CRYSTALS.size()-1).start();
				
				deathTIME[i]=-1;
			   }
			}
		   
		
		
		   
	//draw player	
	g.setColor(Color.RED);
	g.fillOval(moveX+move+PLAYER.getCoordinates().x*sizeX+sizeX/10*2, moveY+move+PLAYER.getCoordinates().y*sizeY+sizeY/10*2, sizeX/3*2, sizeY/3*2);
	g.setColor(Color.BLACK);
	g.drawOval(moveX+move+PLAYER.getCoordinates().x*sizeX+sizeX/10*2, moveY+move+PLAYER.getCoordinates().y*sizeY+sizeY/10*2, sizeX/3*2, sizeY/3*2);
	
	//draw computer
	g.setColor(Color.BLUE);
	g.fillOval(moveX+move+COMPUTER.getCoordinates().x*sizeX+sizeX/10*2, moveY+move+COMPUTER.getCoordinates().y*sizeY+sizeY/10*2, sizeX/3*2, sizeY/3*2);
	g.setColor(Color.BLACK);
	g.drawOval(moveX+move+COMPUTER.getCoordinates().x*sizeX+sizeX/10*2, moveY+move+COMPUTER.getCoordinates().y*sizeY+sizeY/10*2, sizeX/3*2, sizeY/3*2);
	

	
	//draw score board
	   //PLAYER SCORE
	    g.setFont(new Font("TimesRoman", Font.BOLD, (int)d.getWidth()/50));
		g.setColor(Color.RED);
		g.drawString(Integer.toString(this.PLAYER.getScore()), (int)d.getWidth()/10*8, (int)d.getWidth()/10);
		g.setColor(Color.BLACK);
		g.drawRect((int)d.getWidth()/100*80,(int)d.getWidth()/1000*158, (int)d.getWidth()/50*4, (int)d.getWidth()/50);
	
		//COMPUTER SCORE
		g.setFont(new Font("TimesRoman", Font.BOLD, (int)d.getWidth()/50));
		g.setColor(Color.BLUE);
		g.drawString(Integer.toString(this.COMPUTER.getScore()), (int)d.getWidth()/10*9,(int)d.getWidth()/10);
		g.setColor(Color.BLACK);
		g.drawRect((int)d.getWidth()/100*90,(int)d.getWidth()/1000*158, (int)d.getWidth()/50*4, (int)d.getWidth()/50);
		//g.drawRect(sizeX*MazeW/35*40-sizeX*MazeW/200,sizeX*MazeW/10-sizeX*MazeW/200*4, sizeX*MazeW/50*4, sizeX*MazeW/50);
		
	//draw timer
		g.setFont(new Font("TimesRoman", Font.BOLD, (int)d.getWidth()/50));
		g.setColor(Color.BLACK);
		g.drawString(Long.toString(elapsedSeconds), (int)d.getWidth()/10*9, (int)d.getWidth()/10*2);
		g.setColor(Color.BLACK);
		g.drawRect((int)d.getWidth()/100*90,(int)d.getWidth()/1000*350, (int)d.getWidth()/50*4, (int)d.getWidth()/50);
		//g.drawRect(sizeX*MazeW/35*45-sizeX*MazeW/200*10,sizeX*MazeW/10*2-sizeX*MazeW/200*4, sizeX*MazeW/50*5, sizeX*MazeW/50);
		
	//if player won
	if(PLAYER.getScore()>COMPUTER.getScore()&& elapsedSeconds==0){
		if (!this.cheatsSOUND && this.playState) { 
			 this.audioManager.playWinSound();
			 this.playState=false;
		 	}
		this.win=true;
		g.setFont(new Font("TimesRoman", Font.BOLD, 65));
		g.setColor(Color.RED);
		g.clearRect(0, 0, 10000, 10000);
		g.drawString("YOU WON !", (int)d.getWidth()/40*17, (int)d.getHeight()/10);
		COMPUTER.stopThread();
		if(!CRYSTALS.isEmpty())
		stopCrystals();
			
			}
	//if computer won
	else if(PLAYER.getScore()<COMPUTER.getScore()&& elapsedSeconds==0){
		if (!this.cheatsSOUND && this.playState) { 
			 this.audioManager.playLoseSound();
			 this.playState=false;
		 	}
		this.win=false;
		g.setFont(new Font("TimesRoman", Font.BOLD, 65));
		g.setColor(Color.BLUE);
		g.clearRect(0, 0, 10000, 10000);
		g.drawString("YOU LOSE !", (int)d.getWidth()/40*17, (int)d.getHeight()/10);
		COMPUTER.stopThread();
		if(!CRYSTALS.isEmpty())
		stopCrystals();
		elapsedSeconds=-1;
		
	}
	//if player and computer got a draw
	else if (elapsedSeconds==0){
		if (!this.cheatsSOUND && this.playState) { 
			 this.audioManager.playDrawSound();
			 this.playState=false;
		 	}
		g.setFont(new Font("TimesRoman", Font.BOLD, 65));
		g.setColor(Color.BLACK);
		g.clearRect(0, 0, 10000, 10000);
		g.drawString("DRAW !", (int)d.getWidth()/40*17, (int)d.getHeight()/10);
		COMPUTER.stopThread();
		if(!CRYSTALS.isEmpty())
		stopCrystals();
		
		
		}
	if (elapsedSeconds<=-1 &&!this.isGameDone) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 65));
		g.setColor(Color.BLACK);
		g.clearRect(0, 0, 10000, 10000);
		g.drawString("Press escape to return to menu", (int)d.getWidth()/100*25, (int)d.getHeight()/10);
		g.drawString("to quit press x", (int)d.getWidth()/100*35, (int)d.getHeight()/10*2);
		
	}
	else if(elapsedSeconds<=-1 && this.isGameDone) {
		System.exit(0);
	}
	
	//if game is paused
	if(isPausing){
		g.setColor(new Color(255, 255, 255,127));
		g.fillRect(0,0, (int)d.getWidth(), (int)d.getWidth());
		g.setFont(new Font("TimesRoman", Font.BOLD, (int)d.getWidth()/50));
		g.setColor(Color.BLACK);
		g.drawString("PAUSE", (int)d.getWidth()/2, (int)d.getHeight()/3);
		
	}
	
	break;
		
	}
	default:{
		
		break;
		
	   }
	 }
		
	}
	 public boolean Pause(boolean isPausing){
		 if(!isPausing){
			 //pause threads
			 	if (!this.cheatsSOUND) { 
				 this.audioManager.playPauseSound();
			 	}
			 	for(int i=0;i<this.CRYSTALS.size();i++){
				this.CRYSTALS.get(i).Suspend();
			 	}
				this.COMPUTER.Suspend();
				this.timeWhilePaused=this.elapsedTime;
			 return true;
		 }else{
			 //resume threads
			 	if (!this.cheatsSOUND) { 
				 this.audioManager.resumePreviousSound();
			 	}
			 
				for(int i=0;i<this.CRYSTALS.size();i++){
					this.CRYSTALS.get(i).Resume();
				}
				this.COMPUTER.Resume();
				this.timeWhilePaused-=startTime+TIME-System.currentTimeMillis();
			 return false;
		 }
	 }
	 
	 public void changeGameSpeed(boolean minusPlus){
		 for(int i=0;i<this.CRYSTALS.size();i++){
			 //System.out.println("minusPlus");
			 if(minusPlus){
				 if(this.CRYSTALS.get(i).getSpeed()<1100)
				 this.CRYSTALS.get(i).setSpeed(this.CRYSTALS.get(i).getSpeed()+25);
			 }else {
				 if(this.CRYSTALS.get(i).getSpeed()>275)
					 this.CRYSTALS.get(i).setSpeed(this.CRYSTALS.get(i).getSpeed()-25);
			 }
		 }
		 if(minusPlus){
			 if(this.COMPUTER.getSpeed()<500)
			 this.COMPUTER.setSpeed(this.COMPUTER.getSpeed()+25);
		 }else {
			 if(this.COMPUTER.getSpeed()>50)
				 this.COMPUTER.setSpeed(this.COMPUTER.getSpeed()-25);
		 }
	 }
	public void addKeyToBuffer(Integer key){
		this.keys.add(key);
		if(checkBuffer()){
			if (this.cheatsSOUND==false) {
				this.audioManager.playCheatsOnSound();
				this.cheatsSOUND=true;
			}
			cheat();
			
		}
	}
	
	public boolean checkBuffer(){
		if(this.keys.size()>=5){
			if(this.keys.get(this.keys.size()-5)==this.cheatCode[0]&&
					this.keys.get(this.keys.size()-4)==this.cheatCode[1]&&
					this.keys.get(this.keys.size()-3)==this.cheatCode[2]&&
					this.keys.get(this.keys.size()-2)==this.cheatCode[3]&&
					this.keys.get(this.keys.size()-1)==this.cheatCode[4]){
				
				return true;
			}
		}
		return false;
	}
	public void cheat(){
		this.PLAYER.setScore(1000);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode=e.getKeyCode();
		switch (this.mode){
		case 0:{
			this.MainMenuKeys(e.getKeyCode());
			break;
			
		}
		case 1:{
			
			break;
			
		}
		case 2:{
			this.addKeyToBuffer(keyCode);
			if(this.win==false&&isPausing==false)
			PLAYER.move(e.getKeyCode());
		//press esc to exit
			if(keyCode==KeyEvent.VK_ESCAPE&&this.isPausing==false){
				
				//this.stopCrystals();
				
				this.audioManager.playMainMenuSound();
				this.cheatsSOUND=false;
				this.isGameDone=false;
				this.playState=true;
				this.MMtab=0;
				this.MMtabSelected=0;
				this.mode=0;
				COMPUTER.stopThread();
				
			}
		//press P to pause the game
			if(keyCode==KeyEvent.VK_P){
				this.isPausing=Pause(this.isPausing);
			}
		//press - to make game slower to an extent
			if(keyCode==KeyEvent.VK_SUBTRACT){
				System.out.println("minusPlus");
				changeGameSpeed(true);
			}
		//press + to make game faster to an extent
			if(keyCode==KeyEvent.VK_ADD){
				System.out.println("minusPlus");
				changeGameSpeed(false);
			}
			//press x to quit the game \
			if(keyCode==KeyEvent.VK_X){
				System.out.println("x");
				this.isGameDone = true;
			}
		}
		default:{
			
			break;
			
		 	}
		}
		   
	        e.consume();
		}
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		 repaint();
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		e.consume();
	}
	
	public void MainMenuKeys(int keyCode){
		
		switch (keyCode){
			case KeyEvent.VK_DOWN:{
			if(this.MMtab<3)
				this.MMtab++;
			else 
				this.MMtab=1;
			this.repaint();
				break;
			}
			case KeyEvent.VK_UP:{
				if(this.MMtab>1)
					this.MMtab--;
				else
					this.MMtab=3;
				this.repaint();
				break;
			}
			case KeyEvent.VK_ENTER:{
				this.MMtabSelected=1;
				if(MMtab==1){
					
					this.BuildMaze(15, 15, 5);
					this.COMPUTER.setSpeed(250);
					timeBetweenDeath=5;
					this.TIME=30000;
					
				}
				else if(this.MMtab==2){
					
					this.BuildMaze(25, 25, 10);
					this.COMPUTER.setSpeed(210);
					timeBetweenDeath=3;
					this.TIME=30000*2;
					
				}
				else if(this.MMtab==3)
				{
					
					this.BuildMaze(50, 50, 20);
					this.COMPUTER.setSpeed(150);
					timeBetweenDeath=1;
					this.TIME=30000*3;
					
					
				}
				else {
					break;
				}
				//skiping mode 1 for now
				this.mode=2;
				this.repaint();
				
				break;
			}
			case KeyEvent.VK_ESCAPE:{
				System.exit(0);
				break;
			}
		}
	}
	
	  public static void main(String [] args) {
		  
	    //JFrame.setDefaultLookAndFeelDecorated(true);
		
		AudioManager.initializeJavaFXToolkit();
		
	    JFrame frame = new JFrame("Maze");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBackground(Color.white);
	    frame.setSize(800, 800);
	    frame.setResizable(false);
	    Game panel = new Game(25,25);
	    panel.setFocusable(true);
	    panel.requestFocusInWindow();
	   
	    frame.add(panel);
	    
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    frame.setUndecorated(true);
	    frame.setVisible(true);
	    
	  }



}
