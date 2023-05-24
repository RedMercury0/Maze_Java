import java.awt.Point;
import java.util.ArrayList;

public class PathFinder {

	private Game game;
	private MazeGenerator Maze;
	private int x;
	private int y;
	private ArrayList<Point> Path=new ArrayList<Point>();
	private Point target=new Point(0,0);
	
	PathFinder(Game GAME){
		this.game=GAME;
		this.Maze=this.game.getMazeLayout();
		
		
	}
	public ArrayList<Point> getPath(Point Start,Point Target){
		
		clearPath();
		this.target=Target;
		this.x=Start.x;
		this.y=Start.y;
		
		findPath(this.x,this.y,-1);
	    	 return this.Path;
	}
	public void clearPath(){
		this.Path.clear();
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
}
