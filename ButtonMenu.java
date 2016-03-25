package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public abstract class ButtonMenu {
	protected int numButtons,x,y,width,hight,selected;
	protected boolean closed = true;
	protected SqrButton[] buttons;
	protected SqrButton closeMe;
	public static Color closeColor = Color.red;
	public abstract void draw(Graphics g);
	public abstract void closeMenu();
	public abstract void update();
	
	/**
	 * Checkes which button in the menu was clicked
	 * @param mx mouse X
	 * @param my mouse Y
	 * @return -1 if no button was clicked int+ otherwise
	 */
	public int menuClick(int mx,int my){
		
		if(!closed)
			for(int i=0;i<numButtons;i++)
				if(buttons[i].checkMouseCollision(mx, my))
					return i;
		
		return -1;
	}
	
	/**
	 * Adds a button to the end of the array
	 * @param b the button to be added
	 */
	public void addButton(SqrButton b){
		SqrButton[] temp = new SqrButton[numButtons+1];
		int i;
		for(i=0;i<numButtons;i++)
			temp[i] = buttons[i];
		
		temp[numButtons] = b;
		
		buttons = temp;
		
		numButtons++;
	}
	
	public void move(int x,int y){
		if(!closed)
			for(int i=0;i<numButtons;i++){
				buttons[i].setLeft((buttons[i].left()+x)*-1);
				buttons[i].setTop((buttons[i].top()+y)*-1);
			}
	}
	
	/**
	 * Reassignes a button
	 * @param b button to be put in
	 * @param loc which slot to add the button
	 */
	public void setButton(SqrButton b,int loc){
		buttons[loc] = b;
	}

	public int getNumButtons() {
		return numButtons;
	}

	public void setNumButtons(int numButtons) {
		this.numButtons = numButtons;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}

	public SqrButton[] getButtons() {
		return buttons;
	}

	public void setButtons(SqrButton[] buttons) {
		this.buttons = buttons;
	}
}
