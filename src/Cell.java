import java.util.Random;

public class Cell {

	 int x=0;
	 int y=0;
	 int NorthWall=1;
	 int SouthWall=1;
	 int EastWall=1;
	 int WestWall=1;
	 boolean visited=false;
	
	Cell(int x,int y){
		this.x=x;
		this.y=y;
	}
	 boolean isClosed(){
		if (this.EastWall*this.NorthWall*this.WestWall*this.SouthWall==1)
					return true;
		else return false;
		}
	 void openWall(int DIR){
	
     
		
		 
		 switch(DIR){
		 case 0:
			 
			 this.NorthWall=0;
			 
			 
			 break;
		 case 1:
			 
			 this.SouthWall=0;
			 
			 
		 	 break;
		 case 2:
			
			 this.EastWall=0;
			 
			 
			 break;
		 case 3:
			 
			 this.WestWall=0;
			 
			 
			 break;
		 	}
		 
     	
     
	 }
}
