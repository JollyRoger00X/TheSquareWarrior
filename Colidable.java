package squarewarrior;

import java.awt.Graphics;



public abstract class Colidable {
	protected int
		x, 
		y, 
		width, 
		height,
		speedX,
		speedY;
	
	public abstract void draw(Graphics g);
	//public abstract void move();
	public abstract String name();
	
	
	public void stop(){
		speedX=0;
		speedY=0;
	}
	
	/**
	 * this is moving
	 * @param c object to be checked against, assumed still
	 * @return true if a collision has occurred 
	 */
	public boolean checkCollision(Colidable c){
		if((right()>=c.left() && left()<=c.right()) || (right()<c.left() && (right()+speedX)>=c.left()) || (left()>c.right() && (left()+speedX)<=c.right()))
			if((bottom()>= c.top() && top()<=c.bottom()) || (top() > c.bottom() && (top()+speedY) <= c.bottom()) || (bottom()<c.top() && (bottom()+speedY) >= c.top()))
				return true;
			
		return false;
	}
	
	public boolean checkMouseCollision(int mx,int my){
		if(mx>=left() && mx<=right())
			if(my>=top() &&my<=bottom())
				return true;
		return false;
	}
	
	/**
	 * Do I hit the top of c?
	 * @param c
	 * @return true if I do
	 */
	public boolean hitsTop(Colidable c){
		if(bottom()<= c.top() && (bottom()+speedY)>=c.top()) 
			if(right()>c.left() && left()<c.right()){
//				System.out.println("Squaremagon("+x+","+(y+height)+")\nPlatform("+c.left()+","+c.top()+") w="+c.getWidth()+" h="+c.getHeight());
				return true;
			}
		
		return false;
	}
	
	public boolean hitsBottom(Colidable c){
		if(top()> c.bottom() && (top()+speedY)<=c.bottom())
			if(right()>c.left() && left()< c.right())
				return true;
		
		return false;
	}
	
	public boolean hitsLeft(Colidable c){
		if(right() < c.left() && (right()+speedX)>=c.left())
			if(top()< c.bottom() && bottom()>c.top())
				return true;
		
		return false;
	}
	
	public boolean hitsRight(Colidable c){
		if(left() > c.right() && (left()+speedX)<=c.right())
			if(top()< c.bottom() && bottom()>c.top())
				return true;
		
		return false;
	}
	
	public boolean hitsTop(Colidable c,int spy){
		if(right()> c.left() && left()< c.right())
			if((bottom()< c.top()) && (bottom()+(speedY+spy)>=c.top()))
				return true;
		
		return false;
	}
	
	public boolean hitsBottom(Colidable c,int spy){
		if(top()> c.bottom() && (top()+(speedY+spy))<=c.bottom())
			if(right()> c.left() && left()<c.right())
				return true;
		
		return false;
	}
	
	public boolean hitsLeft(Colidable c,int spx){
		if(right() < c.left() && (right()+(speedX+spx))>=c.left())
			if(top()< c.bottom() && bottom()>c.top())
				return true;
		
		return false;
	}
	
	public boolean hitsRight(Colidable c,int spx){
		if(left() > c.right() && (left()+(speedX+spx))<=c.right())
			if(top()< c.bottom() && bottom()>c.top())
				return true;
		
		return false;
	}
	
	public boolean touches(Colidable c){
		if((right() >= c.left()) && (left() <= c.right()))
			if((top()<=c.bottom())&&(bottom()>=c.top()))
				return true;
		return false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int left(){
		return x;
	}
	public int right(){
		return x+width;
	}
	public int top(){
		return y;
	}
	public int bottom(){
		return y+height;
	}
	public void setLeft(int in){
		x = in;
	}
	public void setRight(int in){
		x = (in-width);
	}
	public void setTop(int in){
		y = in;
	}
	public void setBottom(int in){
		y = (in-height);
	}
	public int getspeedX(){
		return speedX;
	}
	public int getspeedY(){
		return speedY;
	}
	public void setspeedX(int in){
		speedX=in;
	}
	public void setspeedY(int in){
		speedY=in;
	}
}
