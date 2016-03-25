package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public class PlatformMenu extends ButtonMenu {
	private boolean closed = true;
	private Color poiColor;
	private Platform selectedPt;
	private int buttonSize = 12;
	private SqrButton edit,delete;
	private int clickx,clicky;
	
	
	public PlatformMenu(Color c){
		numButtons = 12;
		poiColor = c;
		buttons = new SqrButton[numButtons];				
	}
	
	public void setClicks(int x,int y){
		clickx = x;
		clicky = y;
	}
	
	public void update(){
		int btn;
		btn = menuClick(clickx,clicky);
		switch(btn){
		case -1:
			return;
		case 0:
			System.out.println("Close Menu");
			closeMenu();
			return;
		case 1:
			System.out.println("Edit Platform");
			return;
		case 2:
			System.out.println("Delete Platform");
			return;
		}
		
	}
	
	public void selectPlatform(Platform p){
		closed = false;
//		selectedPt = p;
		int ptx,pty,ptwd,ptht,centerX,centerY;
		
		ptx = p.left();
		pty = p.top();
		ptwd = p.getWidth();
		ptht = p.getHeight();
		centerX = ptx+(ptwd/2);
		centerY = pty+(ptht/2);
		Color c = Color.green;
		
		
		closeMe = new SqrButton(centerX+(3*buttonSize),pty - (5*buttonSize),buttonSize,closeColor);
		buttons[0] = closeMe;
		
		edit = new SqrButton(centerX-(3*buttonSize),pty-(5*buttonSize),buttonSize,Color.cyan);
		buttons[1] = edit;
		
		delete = new SqrButton(centerX, pty-(5*buttonSize),buttonSize,Color.orange);
		buttons[2] = delete;
		
		//topleft
		buttons[3] = new SqrButton(ptx-buttonSize,pty-buttonSize,buttonSize,c);
		//topcenter		
		buttons[4] = new SqrButton(centerX-(buttonSize/2),pty-(3*buttonSize),buttonSize,c);
		//topRight
		buttons[5] = new SqrButton(ptx+ptwd,pty-buttonSize,buttonSize,c);
		//centerLeft
		buttons[6] = new SqrButton(ptx - (3*buttonSize),centerY-(buttonSize/2),buttonSize,c);
		//center
		buttons[7] = new SqrButton(centerX-(buttonSize/2),centerY-(buttonSize/2),buttonSize,c);
		//centerRight
		buttons[8] = new SqrButton(ptx+ptwd+(buttonSize*2),centerY-(buttonSize/2),buttonSize,c);
		//bottomLeft
		buttons[9] = new SqrButton(ptx-buttonSize,pty+ptht,buttonSize,c);
		//bottomCenter
		buttons[10] = new SqrButton(centerX-(buttonSize/2),pty+ptht+(buttonSize*2),buttonSize,c);
		//bottomRight
		buttons[11] = new SqrButton(ptx+ptwd,pty+ptht,buttonSize,c);
		
		
	}

	public boolean closed() {
		return closed;
	}

	@Override
	public void closeMenu() {
		closed = true;
		for(int i=0;i<numButtons;i++){
			buttons[i]=null;
		}
	}

	@Override
	public void draw(Graphics g) {
		if(!closed)
			for(int i=0;i<numButtons;i++)
				buttons[i].draw(g);
	}

}
