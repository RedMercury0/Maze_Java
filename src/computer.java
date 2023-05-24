

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.Timer;



public class computer extends Thread{

	
	private Game GM;
	private int x;
	private int y;
	private int toStop=0;
	private int[][] blocked;
	private MazeGenerator Maze;
	private Cell lastCell;
	private Cell thisCell;
	private int speed=250;
	private boolean ok=false;
	private ArrayList<Point> Path=new ArrayList<Point>();
	private int DIR;
	private int nextCell[];
	private Stack<Cell> c=new Stack<Cell>();
	private int behavior=1;
	private int score;
	private Point target=new Point(0,0);
	private PathFinder thePath;
	private PathFinder TargetPath;
	private int gIndex=-2;
	private int index=-1;
	private int movCount=0;
	private Timer t;
	private boolean suspended = false;
	
	computer(Game gm,Point Coords){
		this.GM=gm;
		this.x=Coords.x;
		this.y=Coords.y;
		Maze=this.GM.getMazeLayout();
		this.toStop=0;
		blocked=new int [Maze.getW()][Maze.getH()];
		for(int i=0;i<Maze.getW();i++){
			for(int j=0;j<Maze.getH();j++){
				blocked[i][j]=0;
			}
		}
		this.score=0;
		
		thePath=new PathFinder(this.GM);
		TargetPath=new PathFinder(this.GM);
		
		 this.t = new Timer(500 , new computer.AL());
		 this.t.start();
	}
	
	public void setScore(int Score){
		this.score+=Score;
	}
	public int getScore(){
		return this.score;
	}
	public void setSpeed(int Speed){
		this.speed=Speed;
	}
	public int getSpeed(){
		return this.speed;
	}
	
	public Point getCoordinates(){
		
		return new Point(x,y);
		
	}

	
	 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		index=-1;
		while(true)
		{
			if(this.toStop==1)
	    		 break;
			synchronized(this) {
	               while(suspended) {
	                  try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	               }
	            }
		
			//index=this.checkTarget(); 
			
			//////////
				System.out.println(index);
				//	System.out.println(gIndex);
			
		/*	if(gIndex!=-2 && this.GM.getCrystals()[gIndex].isAlive() && movCount<2)
			{
				movCount++;
				
				index = gIndex;
				this.target.x=this.GM.getCrystals()[index].getCoordinates().x;
				this.target.y=this.target.x=this.GM.getCrystals()[index].getCoordinates().y;
				
				////////
				System.out.println("target "+this.target.x+" : "+this.target.y);
				
				this.Path=this.thePath.getPath(new Point(this.x,this.y), this.target);
				
				////////
				System.out.println("Path Size : " +this.Path.size());
				System.out.println("Path "+this.Path.get(this.Path.size()-1).x+" : "+this.Path.get(this.Path.size()-1).y);
				
				this.GM.repaint();
				if(this.Path.size()>1)
				{	
				this.x=this.Path.get(this.Path.size()-2).x;
		    	this.y=this.Path.get(this.Path.size()-2).y;
				}
				else
				{
					this.x=this.Path.get(this.Path.size()-1).x;
			    	this.y=this.Path.get(this.Path.size()-1).y;
				}
				
				////////
		    	System.out.println("this "+this.x+" : "+this.y);
		    	
		    	
		    	
		    	
		    	
		    	 try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			else*/ 
				
				if(index!=-1&& this.GM.getCrystals().get(index).isAlive())
			{
				
				
				this.target.x=this.GM.getCrystals().get(index).getCoordinates().x;
				this.target.y=this.GM.getCrystals().get(index).getCoordinates().y;
				
				////////
				//System.out.println("target "+this.target.x+" : "+this.target.y);
				
				this.Path=this.thePath.getPath(new Point(this.x,this.y), this.target);
				
				////////
				//System.out.println("Path Size : " +this.Path.size());
				//System.out.println("Path "+this.Path.get(this.Path.size()-1).x+" : "+this.Path.get(this.Path.size()-1).y);
				
				
				if(this.Path.size()>1)
				{	
				this.x=this.Path.get(this.Path.size()-2).x;
		    	this.y=this.Path.get(this.Path.size()-2).y;
				}
				else
				{
					this.x=this.Path.get(this.Path.size()-1).x;
			    	this.y=this.Path.get(this.Path.size()-1).y;
				}
				this.GM.repaint();
				////////
		    //	System.out.println("this "+this.x+" : "+this.y);
		    	
		    	
		    	
		    	
		    	
		    	 try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		     
			}
			else
			{
				//index=-1;
				this.target.x=this.x;
				this.target.y=this.y;
				
				
			}
			
				/*if(index!=-1 && this.GM.getCrystals()[index].isAlive())
					this.t.start();*/
			//behavior(this.behavior);
		
			//System.out.println("TEST000");
		}
	}
	public void Suspend() {
	      suspended = true;
	   }
	   
	public synchronized void Resume() {
	      suspended = false;
	      notify();
	   }
	
	private int checkTarget(){
		//System.out.println("TEST1");
		ArrayList<Point> path=new ArrayList<Point>();
		
		int Distance=Integer.MAX_VALUE;
		int index=-1;
		Point START = new Point(0,0);
		START.x=this.x;
		START.y=this.y;
		
		
		//System.out.println("Befor FOR");
		for(int i=0;i<this.GM.getCrystals().size() ;i++){
			
			//System.out.println("i = "+ i);
		
			path=this.TargetPath.getPath(START, this.GM.getCrystals().get(i).getCoordinates());
			
			//System.out.println("in FOR");
			
					if(path.size()<=Distance && this.GM.getCrystals().get(i).isAlive()){
						//system.out.println("in IF");
						index=i;
						Distance=path.size();
	    		
					}
					
	    	 
				}
		 
		
		return index;
	}
	public void showPath(){
		for(int i=0;i<this.Path.size();i++){
			System.out.print(this.Path.get(i).x+ " , "+this.Path.get(i).y+" ");
		}
		System.out.println();
	}
	
	public void setBehavior(int bhv){this.behavior=bhv;}
	
	private void behavior(int bhv){
	//	System.out.println("TEST2");
		// c.push(Maze.getTiles()[x][y]);
		
			
			  if(this.ok==false)
			     {
			  // 	 System.out.println("OK...");
			      this.ok=findPath(this.x,this.y,-1);
			     }
			     else if(this.ok==true&&!this.Path.isEmpty())
			     {
			   // 	 System.out.println("Path Found!");
			    	 //this.showPath();
			    	 this.GM.repaint();
			    	 //c.push(Maze.getTiles()[x][y]);
			    	this.x=this.Path.get(this.Path.size()-1).x;
			    	this.y=this.Path.get(this.Path.size()-1).y;
			    	 
			    	this.Path.remove(this.Path.size()-1);
			    	 try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			     }
		
	}

	
	private boolean findPath (int x, int y, int d)
	{
	
			boolean ok = false;
				for (int i = 0  ;  i < 4  &&  !ok  ;  i++)
						if (i != d)
							switch (i)
							{
							// 0 = NorthWall, 1 = EastWall, 2 = SouthWall, 3 = WestWall
							case 0:
								if (Maze.getTiles()[x][y].NorthWall==0&&y!=0)
									ok = findPath ( x, y - 1, 2);
								break;
							case 1:
								if (Maze.getTiles()[x][y].EastWall==0&&x<this.Maze.getW()-1)
									ok = findPath ( x + 1, y, 3);
								break;
							case 2:
								if (Maze.getTiles()[x][y].SouthWall==0&&y<this.Maze.getH()-1)
									ok = findPath ( x, y + 1, 0);
								break;
							case 3:
								if (Maze.getTiles()[x][y].WestWall==0&&x!=0)
									ok = findPath ( x - 1, y, 1);
								break;
							}
					if (x == this.target.x  &&  y == this.target.y)
					{ 
						ok = true;
					}
					if (ok)
						{
							this.Path.add(new Point(x,y));
		            }
				  return ok;
	}
	private int chooseDIR(int x,int y){
		int countChecks=0;
		int DIR;
		int CanMoveBack=0;
		int max=0;
		//System.out.println("DIR0_IF_3WallsBlock");
		if(Maze.getTiles()[x][y].NorthWall==1)
			countChecks++;
		if(Maze.getTiles()[x][y].SouthWall==1)
			countChecks++;
		if(Maze.getTiles()[x][y].EastWall==1)
			countChecks++;
		if(Maze.getTiles()[x][y].WestWall==1)
			countChecks++;
		if(countChecks==3){
			
			return -1;
		}

		//System.out.println("DIR1_IF_CanMoveBack");
	

		if((Maze.getTiles()[x][y].NorthWall==1||(this.y-1>0&&blocked[x][y-1]==1)))
		{
			CanMoveBack++;
		}
		if((Maze.getTiles()[x][y].SouthWall==1||(this.y<Maze.getH()-1&&blocked[x][y+1]==1)))
		{
			CanMoveBack++;
		}
		if((Maze.getTiles()[x][y].EastWall==1||(this.x<Maze.getW()-1&&blocked[x+1][y]==1)))
		{
			CanMoveBack++;
		}
		if((Maze.getTiles()[x][y].WestWall==1||(this.x-1>0&&blocked[x-1][y]==1)))
		{
			CanMoveBack++;
		}
		//System.out.println("DIR_CanMoveBack : CanMoveBack = "+CanMoveBack);
		
		if(CanMoveBack>=3)
			return -1;
	//	System.out.println("DIR2_EnterWHILE");
	
		while (true){
		if(++max>=100)
			return-1;
		//System.out.println("DIR_WHILE");
		
		//DIR=rand.nextInt(4);
		
/*		System.out.println("DIR_WHILE : DIR = "+DIR);
		
		System.out.println("This Cell"+this.thisCell.x+" , "+this.thisCell.y+"  "+"Previous Cell"+this.lastCell.x+" , "+this.lastCell.y);
		System.out.println(blocked[moveCell(DIR)[0]][moveCell(DIR)[1]]);*/
		
		DIR=0;
		if(
				
				DIR==0&&
				Maze.getTiles()[x][y].NorthWall==0&&
				blocked[moveCell(DIR)[0]][moveCell(DIR)[1]]==0&&
				!(this.lastCell.x==moveCell(DIR)[0] && this.lastCell.y==moveCell(DIR)[1]))
			
			return DIR;
		DIR=1;
		if(
				
				DIR==1&&
				Maze.getTiles()[x][y].SouthWall==0&&
				blocked[moveCell(DIR)[0]][moveCell(DIR)[1]]==0&&
				!(this.lastCell.x==moveCell(DIR)[0]&&this.lastCell.y==moveCell(DIR)[1]))
			
			return DIR;
		DIR=2;
		if(
				DIR==2&&
				Maze.getTiles()[x][y].EastWall==0&&
				blocked[moveCell(DIR)[0]][moveCell(DIR)[1]]==0&&
				!(this.lastCell.x==moveCell(DIR)[0]&&this.lastCell.y==moveCell(DIR)[1]))
			
			return DIR;
		DIR=3;
		if(
				DIR==3&&
				Maze.getTiles()[x][y].WestWall==0&&
				blocked[moveCell(DIR)[0]][moveCell(DIR)[1]]==0&&
				!(this.lastCell.x==moveCell(DIR)[0]&&this.lastCell.y==moveCell(DIR)[1]))
			
			
			return DIR;
		
		}
		
		
	}
	private int [] moveCell(int DIR){
		int Dirx=this.x;
		int Diry=this.y;
		switch (DIR){
		case 0:{
			if(this.y-1>=0)
			Diry-=1;
			break;
		}
		case 1:{
			if(this.y+1<Maze.getH())
			Diry+=1;
			break;
		}
		case 2:{
			if(this.x+1<Maze.getW())
			Dirx+=1;
			break;
		}
		case 3:{
			if(this.x-1>=0)
			Dirx-=1;
			break;
		}
		
		default:
			{
			break;
			}
		}
		return new int[] {Dirx,Diry};
		
	}
	public void stopThread() {
		// TODO Auto-generated method stub
	 this.toStop=1;
		
	}

	 class AL implements ActionListener
	  {
	    AL() {}
	    
	    public void actionPerformed(ActionEvent arg0)
	    {
	    	//if(index==-1)
	    	index=checkTarget(); 
	    	
	    	
	    }
	  }

}
