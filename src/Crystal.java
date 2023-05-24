import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Crystal extends Thread {
	
	
	private Game GM;
	private int x;
	private int y;
	private int toStop=0;
	private int[][] blocked;
	private MazeGenerator Maze;
	private Cell lastCell;
	private Cell thisCell;
	private int speed=550;
	private boolean ok=false;
	private ArrayList<Point> Path=new ArrayList<Point>();
	private int DIR;
	private int nextCell[];
	private Stack<Cell> c=new Stack<Cell>();
	private int behavior=0;
	private Point dest;
	private Point start;
	private int worth;
	private boolean suspended = false;
	
	Crystal(Game gm, Point Start,Point Destination){
		
		this.GM=gm;
		this.x=Start.x;
		this.y=Start.y;
		this.start=Start;
		this.dest=Destination;
		Maze=this.GM.getMazeLayout();
		this.toStop=0;
		blocked=new int [Maze.getW()][Maze.getH()];
		for(int i=0;i<Maze.getW();i++){
			for(int j=0;j<Maze.getH();j++){
				blocked[i][j]=0;
			}
		}
		this.worth=10;
		
	}
	
	public void setScosetWorth(int Worth){
		this.worth+=Worth;
	}
	public int getWorth(){
		return this.worth;
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
	
	
	public Point getDestination(){
		return this.dest;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		while(true){
			if(this.toStop==0){
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
			
			behavior(this.behavior);
			//if Crystal reached Destionation change Path
			if( this.x==dest.x && this.y==dest.y){
				this.dest.setLocation(this.start);
				this.start.x=this.x;
				this.start.y=this.y;
				for(int i=0;i<Maze.getW();i++){
					for(int j=0;j<Maze.getH();j++){
						blocked[i][j]=0;
					}
				}
				this.ok=false;
			}
		
			
			}
		
				
			
		}
	}
	public void Suspend() {
	      suspended = true;
	   }
	   
	public synchronized void Resume() {
	      suspended = false;
	      notify();
	   }
	
	
	public int checkCollision(){
		// 1 - PLAYER , 2 - COMPUTER , 0 - No Collision
		if(this.x==this.GM.getPLAYERcoords().x&&this.y==this.GM.getPLAYERcoords().y){
			this.stop();
			this.stopThread();
			return 1;
		}
		else if(this.x==this.GM.getCOMPUTERcoords().x&&this.y==this.GM.getCOMPUTERcoords().y){
			this.stop();
			this.stopThread();
			return 2;
		}
		return 0;
	}
	public void showPath(){
		for(int i=0;i<this.Path.size();i++){
			System.out.print(this.Path.get(i).x+ " , "+this.Path.get(i).y+" ");
		}
		System.out.println();
	}
	
	public void setBehavior(int bhv){this.behavior=bhv;}
	
	private void behavior(int bhv){
	
		 c.push(Maze.getTiles()[x][y]);
		switch (bhv)
		{
		
		case 0:
			if(this.ok){
				this.thisCell=Maze.getTiles()[this.Path.get(this.Path.size()-1).x][this.Path.get(this.Path.size()-1).y];
			c.push(this.thisCell);
			this.Path.clear();
			 this.ok=false;
			}
			while(!c.isEmpty()){
				if(this.toStop==1)
					break;
					if(behavior==1)
				  break;
			    	this.GM.repaint();
			    	this.x=c.peek().x;
			    	this.y=c.peek().y;
			    	this.thisCell=c.pop();
			    	if(!c.isEmpty()){
			    		this.lastCell=c.pop();
			    		c.push(this.lastCell);
			    		c.push(this.thisCell);
			    	}
			    	else{
			    		c.push(this.thisCell);
			    		this.lastCell=this.thisCell;
			    	}
			   	try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			   		 
			         DIR=this.chooseDIR(x,y);
			         if(DIR!=-1){
			             
			             nextCell=moveCell(DIR);
			             c.push(Maze.getTiles()[nextCell[0]][nextCell[1]]);
			         }else{ 
			        	 blocked[c.peek().x][c.peek().y]=1;
			        	 try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	 c.pop();
			         
			       }  
			     }
			break;
		case 1:
			
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
			    	 c.push(Maze.getTiles()[x][y]);
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
			break;
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
					if (x == this.dest.x  &&  y == this.dest.y)
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
	public void startThread(){
		this.toStop=0;
	}
	public int getToStop(){
		
		return this.toStop;
	}
	
	
	
	
	
	
	
	
}
