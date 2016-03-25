package squarewarrior;


import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Colidable {
	private int 
	speed =25,
	num=0,
	maxBounces=10,
	bounces=0;
	double exactX,exactY,rise,run;
	
	private Point loc = null;
	
	private Bullet next;
	
	/**
	 * Creates a new bullet which will fly along a straight line
	 * designated by a mouse click and the characters center point
	 * @param strt the location of the bullets origin 
	 * @param tgt t the location of the mouse click
	 */
	public Bullet(Point strt,Point tgt){
		height = 5;
		width = 5;
		double xdif,ydif,distC,scale;
		
		//places the bullet at the location passed in
		loc = strt;
		exactX = loc.getX();
		exactY = loc.getY();

		
		/**
		 * This block finds the distance between the starting point and the target point 
		 * distance = sqrt((x2-x1)^2 + (y2-y1)^2)
		 */
		xdif = (tgt.getX() - loc.getX());//(x2-x1) this is the total change in X
		ydif = (tgt.getY() - loc.getY());//(y2-y1) this is the total change in y
		distC = Math.sqrt((xdif*xdif)+(ydif*ydif));//finds the distance from start to target 
		
		/**
		 * To make the bullet travel the correct distance along its slope I 
		 * divide its speed by the distance between the two points to get 
		 * a ratio that I call scale. That ratio is applied to the 
		 * change in x and y components to make the rise and run that the 
		 * bullet needs to add so that it moves a total distance of 20 units
		 * with every update
		 */
		scale = speed/distC;
		rise = scale * ydif;
		run = scale * xdif;	
	}
	
	/**
	 * Gets the hit power of the bullet
	 * @return the hit power
	 */
	public int pow(){
		return 1;
	}
	
	/**
	 * Draws the bullet on the screen
	 */
	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Changes the bullets (x,y) location vars
	 */
	public void move(){
		//The slope the bullet is traveling will go wrong if only integers are used
		//exact location has to be kept in doubles
		exactX += run;
		exactY += rise;
		//then type cast into integers
		x=(int)exactX;
		y=(int)exactY;		
	}
	
	public void setNum(int in){
		num = in;
	}
	
	public int getNum(){
		return num;
	}

	/**
	 * Reports the bullets current (x,y) location
	 */
	public String toString(){
		return ("("+loc.getX()+"/"+loc.getY()+")");
	}

	/**
	 * Finds the current travel speed of the bullet
	 * @return the value of its speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Reassigns the bullets travel speed
	 * @param speed the new speed of the bullet
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Gets the next bullet in the list  
	 * @return the bullet after this one
	 */
	public Bullet getNext() {
		return next;
	}

	/**
	 * Adds a bullet to this bullets next pointer
	 * @param next the bullet to add
	 */
	public void setNext(Bullet next) {
		this.next = next;
	}
	
	/**
	 * Gets the bullets change in y
	 * @return the y change
	 */
	public double getRise(){
		return rise;
	}
	
	/**
	 * Gets the bullets change in x
	 * @return the x change
	 */
	public double getRun(){
		return run;
	}
	
	/**
	 * Multiplies the value of run by -1
	 */
	public void invertRun(){
		run = run*-1;
	}
	
	/**
	 * Multiplies the value of rise by -1
	 */
	public void invertRise(){
		rise = rise*-1;
	}
	
	public String name(){
		return "Bullet";
	}
	
	/**
	 * Increments the number of times the bullet has bounced
	 */
	public void bounce(){
		bounces++;
	}
	
	/**
	 * Tells if the bullet has exceeded its max number of bounces
	 * @return true if the bullet has bounces remaining
	 */
	public boolean cont(){
		if(bounces > maxBounces)
			return false;
		return true;
	}
	
	public void done(){
		bounces = maxBounces;
	}
	
	/**
	 * Adds the scroll values of the level to the bullet so that it will move relative to the level.
	 * @param sx distance to scroll along x
	 * @param sy distance to scroll along y
	 */
	public void scroll(int sx,int sy){
		exactX-=sx;
		exactY-=sy;
		//System.out.println("I scrolled! ("+sx+","+sy+")");
	}
	
	/**
	 * Allows the bullet's exact x to be set
	 * @param in the value to set x to
	 */
	public void setX(double in){
		exactX = in;
	}
	
	/**
	 * Allows the bullet's exact y to be set
	 * @param in the value to set its y to
	 */
	public void setY(double in){
		exactY = in;
	}
	
	/**
	 * Provides the bullets exact x 
	 * @return the x value
	 */
	public double getX(){
		return exactX;
	}

	/**
	 * Provides the bullets exact y
	 * @return the y value
	 */
	public double getY(){
		return exactY;
	}
}
