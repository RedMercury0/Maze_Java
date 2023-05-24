import java.awt.Point;
import java.util.Random;
import java.util.Stack;

import javax.swing.JPanel;

public class MazeGenerator {

private int w;
private int h;
private Cell Tiles[][];
private Point pBase=new Point(0,0);
private Point cBase=new Point(0,0);
private Random rand = new Random();



MazeGenerator(int w,int h){
    this.w=w;
    this.h=h;
    Tiles=new Cell[w][h];
    for(int i=0;i<w;i++){
        for(int j=0;j<h;j++){
            this.Tiles[i][j]=new Cell(i,j);
        }
    }
  
    this.Build();
}
private void Build(){
    int sX,sY;
    int DIR;
    int nextCell[];
     sX= rand.nextInt(w);
     sY= rand.nextInt(h);
     Stack<Cell> c=new Stack<Cell>();
     c.push(Tiles[sX][sY]);

     while(!c.isEmpty()){
    	 
         DIR=chooseDIR(c.peek().x,c.peek().y);
         if(DIR!=-1){
             Tiles[c.peek().x][c.peek().y].openWall(DIR);
             nextCell=openNear(DIR,c.peek().x,c.peek().y);
             c.push(Tiles[nextCell[0]][nextCell[1]]);
         }else c.pop();
       
     }  
         if(rand.nextInt(2)==0){
        	 pBase.x=0;pBase.y=rand.nextInt(h-1);
        	 cBase.x=w-1;cBase.y=rand.nextInt(h-1);
        	 Tiles[pBase.x][pBase.y].WestWall=0;
        	 Tiles[cBase.x][cBase.y].EastWall=0;
         }
         else {
        	 pBase.x=rand.nextInt(w-1);pBase.y=0;
        	 cBase.x=rand.nextInt(w-1);cBase.y=h-1;
        	 Tiles[pBase.x][pBase.y].NorthWall=0;
        	 Tiles[cBase.x][cBase.y].SouthWall=0;
         }
     
}
 int[] openNear(int DIR,int x,int y){
	int nextCell[]={0,0};
	switch(DIR){
	 case 0:
		 Tiles[x][y-1].SouthWall=0;		
		 nextCell[0]=x;
		 nextCell[1]=y-1;
		 break;
	 case 1:
		 Tiles[x][y+1].NorthWall=0;
		 nextCell[0]=x;
		 nextCell[1]=y+1;
	 	 break;
	 case 2:
		 Tiles[x+1][y].WestWall=0;
		 nextCell[0]=x+1;
		 nextCell[1]=y;
		 break;
	 case 3:
		 Tiles[x-1][y].EastWall=0; 
		 nextCell[0]=x-1;
		 nextCell[1]=y;
		 break;
	 	}
	return nextCell;
	 
	}
 int chooseDIR(int x,int y){
	int opend=0;
	int rndWall=0;
	if(!((y-1>=0&&Tiles[x][y-1].isClosed()) || (y+1<h&&Tiles[x][y+1].isClosed()) || (x+1<w&&Tiles[x+1][y].isClosed()) || (x-1>=0&&Tiles[x-1][y].isClosed())))
	 {return -1;}
     while(opend==0){
    	 
    		 rndWall = rand.nextInt(4);
		 
		 switch(rndWall){
		 case 0:
			 if( y-1>=0&&Tiles[x][y-1].isClosed()){
				 //Tiles[x][y].NorthWall=0;
			 opend=1;
			 }
			 break;
		 case 1:
			 if(y+1<h&&Tiles[x][y+1].isClosed()){
				// Tiles[x][y].SouthWall=0;
			 opend=1;
			 }
		 	 break;
		 case 2:
			 if(x+1<w&&Tiles[x+1][y].isClosed()){
				// Tiles[x][y].EastWall=0;
			 opend=1;
			 }
			 break;
		 case 3:
			 if(x-1>=0&&Tiles[x-1][y].isClosed()){
				// Tiles[x][y].WestWall=0;
			 opend=1;
			 }
			 break;
		 
		 	}
		 
     	}
     return rndWall;
}
public Cell[][] getTiles(){
	return this.Tiles;
}
public Point getPlayerBase(){
	return this.pBase;
}
public Point  getComputerBase(){
	return this.cBase;
}
public void ShowMaze(){
	System.out.println("This is the Maze");
	for (int i = 0; i < h; i++) {
		
		for (int j = 0; j < w; j++) {
			System.out.print((Tiles[j][i]).NorthWall == 1 ? "+---" : "+   ");
		}
		System.out.println("+");
		
		for (int j = 0; j < w; j++) {
			System.out.print((Tiles[j][i]).WestWall == 1 ? "|   " : "    ");
		}
		if(Tiles[w-1][i].EastWall==1)
		System.out.println("|");
		else 
			System.out.println(" ");
	}
	
	for (int j = 0; j < w; j++) {
		if(Tiles[j][h-1].SouthWall==1)
		System.out.print("+---");
		else 
			System.out.print("+   ");
	}
	System.out.println("+");
}
public int getH(){
	return this.h;
}
public int getW(){
	return this.w;
}
public static void main(String[] args) {
	
	/*MazeGenerator MG = new MazeGenerator(10,10);
	MG.ShowMaze();*/
}

}
