package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Boundary extends Colidable {
	private int 
	leftBound = 50,
	rightBound = 750,
	topBound = 50,
	bottomBound =430 ;
	
	public Boundary(int xx,int yy,int wid,int ht){
		x = xx;
		y = yy;
		width = wid;
		height = ht;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		//x,y,width,height
		g.drawRect(leftBound,topBound,rightBound -leftBound,bottomBound-topBound);
	}

	@Override
	public String name() {
		return "Screen";
	}

	public int leftBound() {
		return leftBound;
	}

	public int rightBound() {
		return rightBound;
	}

	public int topBound() {
		return topBound;
	}

	public int bottomBound() {
		return bottomBound;
	}
	
	

}
