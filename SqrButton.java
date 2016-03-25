package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public class SqrButton extends Colidable {

	private Color cl;
	
	public SqrButton(int btx, int bty,int size,Color r){
		x=btx;
		y=bty;
		width=size;
		height =size;
		cl = r;
	}
	
	public boolean checkMouseCollision(int mX,int mY){
		if(mX>=x && mX<=(x+width))
			if(mY>=y && mY<=(y+height))
				return true;
		
		return false;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(cl);
		g.fillRect(this.x, this.y, this.width, this.height);
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "A perfectly square button";
	}
	
}
