package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Platform extends Colidable{

	private Platform next;
	private Color c;
	
	/**
	 * Creates a new platform of width and height 
	 * at specified location
	 * @param width platforms width
	 * @param height platforms height
	 * @param xloc location of the left of the platform
	 * @param yloc location of the top of the platform
	 */
	public Platform(int wd,int ht,int xloc,int yloc){
		width = wd;
		height = ht;
		x = xloc;
		y = yloc;
		next = null;
		c = Color.WHITE;
	}
	
	/**
	 * Sets the platforms new location using its speeds along x and y  
	 */
	public void move(){
		setTop(this.top()+speedY);
		setLeft(this.left()+speedX);
	}
	
	public void makeBlue(){
		c = Color.BLUE;
	}
	
	/**
	 * Sets a color to the platform
	 * @param cin the color 
	 */
	public void setColor(Color cin){
		c = cin;
	}

	/**
	 * Changes the platforms x value
	 * @param dist the distance to add
	 */
	public void moveX(int dist){
		x+= dist;
	}
	
	/**
	 * Changes the platforms y value 
	 * @param dist the distance to add
	 */
	public void moveY(int dist){
		y+=dist;
	}
	
	/**
	 * Draws the platform
	 */
	public void draw(Graphics g){
		g.setColor(c);
		g.fillRect(x, y,width,height);
	}
	
	/**
	 * Name of the platform
	 */
	public String name(){
		return "Platform";
	}
	
	/**
	 * Adds a platform to this platform's next pointer
	 * @param m the platform to add
	 */
	public void setNext(Platform m){
		next = m;
	}
	
	/**
	 * Gets the next platform in the list
	 * @return the next platform
	 */
	public Platform getNext(){
		return next;
	}
	
	/**
	 * Platforms red value
	 * @return red value
	 */
	public int getR(){
		return c.getRed();
	}
	/**
	 * Platforms green value
	 * @return green value
	 */
	public int getG(){
		return c.getGreen();
	}
	/**
	 * Platforms blue value
	 * @return blue value
	 */
	public int getB(){
		return c.getBlue();
	}

}
