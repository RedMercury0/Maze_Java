import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class player extends Thread{

	
	private Game GM;
	private int x;
	private int y;
	private int STEP;
	private ArrayList<Point> Path=new ArrayList<Point>();
	private int score;
	player(Game gm,Point Coords){
		this.GM=gm;
		this.x=Coords.x;
		this.y=Coords.y;
		this.score=0;
		
	}
	public void setScore(int Score){
		this.score+=Score;
	}
	public int getScore(){
		return this.score;
	}
	public Point getCoordinates(){
	
		return new Point(x,y);
		
	}
	public void move(int keyCode){
		MazeGenerator maze=GM.getMazeLayout();
	
		//LEFT KEY
			if(keyCode==KeyEvent.VK_LEFT&&x>0&&maze.getTiles()[x][y].WestWall==0){
				x--;
				
				Cheats();
		
			}
		//UP KEY
	      if(keyCode==KeyEvent.VK_UP&&y>0&&maze.getTiles()[x][y].NorthWall==0){
	    	  y--;
	    	  
	    	  Cheats();
	      
	      }
	    //RIGHT KEY
	       if(keyCode==KeyEvent.VK_RIGHT&&x<GM.getWidth()&&maze.getTiles()[x][y].EastWall==0){
	    	   x++;
	    	   
	    	   Cheats();
	       
	       }
	    //DOWN KEY
	        if(keyCode==KeyEvent.VK_DOWN&&y<GM.getHeight()&&maze.getTiles()[x][y].SouthWall==0){
	        	y++;
	        	
	        	Cheats();
	       
	        }
	}
	
	public boolean findPath (int x, int y, int d)
	{
			boolean ok = false;
				for (int i = 0  ;  i < 4  &&  !ok  ;  i++)
						if (i != d)
							switch (i)
							{
							// 0 = NorthWall, 1 = EastWall, 2 = SouthWall, 3 = WestWall
							case 0:
								if (GM.getMazeLayout().getTiles()[x][y].NorthWall==0&&y!=0)
									ok = findPath ( x, y - 1, 2);
								break;
							case 1:
								if (GM.getMazeLayout().getTiles()[x][y].EastWall==0&&x<this.GM.getMazeLayout().getW()-1)
									ok = findPath ( x + 1, y, 3);
								break;
							case 2:
								if (GM.getMazeLayout().getTiles()[x][y].SouthWall==0&&y<this.GM.getMazeLayout().getH()-1)
									ok = findPath ( x, y + 1, 0);
								break;
							case 3:
								if (GM.getMazeLayout().getTiles()[x][y].WestWall==0&&x!=0)
									ok = findPath ( x - 1, y, 1);
								break;
							}
				
					if (x == this.GM.getMazeLayout().getComputerBase().x  &&  y == this.GM.getMazeLayout().getComputerBase().y)
					{ 
						ok = true;
					}
					if (ok)
						{
						
							this.Path.add(new Point(x,y));
		            }
				  return ok;
	}
	public ArrayList<Point> getPath()
	{
		return this.Path;
	}
	public void Cheats(){
		// TODO Auto-generated method stub
		//while(true){
		this.Path.clear();
		this.findPath(this.x, this.y, -1);
		GM.repaint();
		
		//}
	}
	


}
