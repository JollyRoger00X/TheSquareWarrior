package squarewarrior;

import java.util.Scanner;

public class UpdatePacket{
	private int speedx;
	private int speedy;
	private int height;
	private int width;
	private int xlocation;
	private int ylocation;
	private int health;
	private BulletList blts;
	private int playerNum;
	
	public UpdatePacket(int num,int x,int y){
		xlocation = x;
		ylocation = y;
		playerNum = num;
	}
	
	public UpdatePacket(String in){
		Scanner sc = new Scanner(in);
		int x,y,num;
		
		num = Integer.parseInt(sc.next());
		x = Integer.parseInt(sc.next());
		y = Integer.parseInt(sc.next());
		playerNum = num;
		xlocation = x;
		ylocation = y;
		sc.close();
	}
	
	public String toString(){
		return playerNum+" "+xlocation+" "+ylocation+" ";
	}

	public UpdatePacket()
	{
		speedx=0;
		speedy=0;
		height=0;
		width=0;
		xlocation=0;
		ylocation=0;
		health=0;
		blts = null;
	}
	public UpdatePacket(int speedx1, int speedy1, int height1, int width1, int xlocation1, int ylocation1, int h,BulletList b)
	{
		speedx=speedx1;
		speedy=speedy1;
		height=height1;
		width=width1;
		xlocation=xlocation1;
		ylocation=ylocation1;
		health = h;
		blts = b;
	}
	
	
	
	
	public void SetXlocation (int x)
	{
		xlocation=x;
	}
	public void SetHealth (int h)
	{
		health=h;
	}
	public void SetYlocation (int y)
	{
		ylocation=y;
	}
	public void SetWidth (int w)
	{
		width=w;
	}
	public void SetHeight (int h)
	{
		height=h;
	}
	public void SetXspeed (int xs)
	{
		speedx=xs;
	}
	public void SetYspeed (int ys)
	{
		speedy=ys;
	}
	public int GetXlocation ()
	{
		return xlocation;
	}
	public int GetYlocation ()
	{
		return ylocation;
	}
	public int GetWidth ()
	{
		return width;
	}
	public int GetHeight ()
	{
		return height;
	}
	public int GetXspeed ()
	{
		return speedx;
	}
	public int GetYspeed ()
	{
		return speedy;
	}
	public int GetHealth ()
	{
		return health;
	}
	public int getPlayerNum(){
		return playerNum;
	}
	
	/**
	 * gives the bullets added to 
	 * @return
	 */
	public BulletList getBullets(){
		return blts;
	}
}

