 package squarewarrior;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;


public class Player extends Colidable{
	private boolean gameOver = false;	
	private Color myColor;
	private boolean
	platformed = false,
	ducked = false,
	gotHit = false;
	private int 
	maxhp = 7,
	hp=7,
	healthblock = 5,
	flashTime = 20,
	flashTimeUsed = 0;
	private Enemy[] e;
	
	public boolean buffOn = false;
	
	private AudioClip shoot,ouch;
	
	//Jumping Reference
	private boolean jumped = false;
	private int jumpCharge = 0;
	private int baseJump = 20;
	private int maxJump = 28;
	private int baseWidth,baseHeight;
	
	//movement refrences
	private int moveSpeed=8;

	//Environment Variables
	private Level lv;
	private Platform contact = null,Lwall = null, Rwall = null;
	private Boundary bnd;
	private int scrollX;
	private int scrollY;
	//attack
	int bltnum=0;

	
	public Player(){
		width = 50;
		height = 50;
		y = 190;
		x = 375;
		baseWidth = width;
		baseHeight= height;
	}

	
	
	/*
	 *    X++ ==>
	 *  Y
	 *  +
	 *  +
	 *  
	 *  x get larger as a sprite moves right 
	 *  y gets larger as a sprite moves down
	 */
	public void update(){	
		Platform p;
		//Updates X position
		//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		if(speedX<0){//the character is trying to move left==========================================
			Rwall = null;
			p = lv.getListHead();
			
			if (Lwall == null) //the character is not against a wall to his left
				while(p!=null){
					if(hitsRight(p)){//have I hit the right side of any platform?
						System.out.println("i hit a wall!");
						Lwall = p;
						setLeft(Lwall.right());
						speedX=0;
						break;
					}
					p = p.getNext();
				}
			else{ //the character is against a wall to his left
				
				if(touches(Lwall))//am i still against the wall to my left?
					speedX = 0;//if so then i cant go left
				else
					Lwall = null;//if not then I can go left
			}
			
			//moveleft
			//moves the player left whether or not he will move off the play area or the edge of the level
			x += speedX;
			if(left() <= lv.left()){//has the character has hit the left side of the level?
				scrollX=x - lv.left();
				x = lv.left();//if so then his speed is now 0 and is left side is on the level's left side
				speedX=0;
			}
			
			//check collision with the edges of the boundary
			if(left() < bnd.leftBound()){//the character has touched the left side of the boundary
				scrollX = x - bnd.leftBound();
				x = bnd.leftBound();
			}
		}
		else if(speedX>0){//the character is trying to move right====================================
			Lwall = null;
			p = lv.getListHead();
			
			if (Rwall == null) //the character is not against a wall to his right
				while(p!=null){
					if(hitsLeft(p)){
						System.out.println("i hit a wall");
						Rwall = p;
						setRight(Rwall.left());
						speedX=0;
					}
					p = p.getNext();
				}
			else{ //am I still against a wall to my right?
				if(touches(Rwall))
					speedX = 0; //if so I don't move right
				else
					Rwall = null; //if not, then i can move right
			}
			
			//moves the character right whether or not he will move off the level or off the play area
			x += speedX;
			
			if(right()>= lv.right()){//the character cannot move right any further
				setRight(lv.right());
				scrollX = right()-lv.right();
				speedX=0;
			}
			if(right() > bnd.rightBound()){//the character is at boundary right so scroll the background
				scrollX = right() - bnd.rightBound();
				setRight(bnd.rightBound());
			} 
		
			
		} else {//the character is not moving along X================================================
			scrollX = 0;
		}//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
		
		
		//Updates Y Position
		//YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
		boolean accell = true;//assume by default that the player is accelerating	
//		boolean talk =false;
		if(speedY>0){//if the character is falling====================================================
			p = lv.getListHead();
			while(p!=null){
				if(hitsTop(p)){
					System.out.println("i landed on a platform top("+(p.top()-lv.top())+") bottom("+((p.top()-lv.top())+p.getHeight())+")" +
							"  left("+(p.left()-lv.left())+")  right("+((p.left()-lv.left())+p.getWidth())+")");
					platform(p);//be platformed on platform p
					accell = false;
					break;
				}
				p = p.getNext();
			}
			
			if(accell){
				y += speedY;
				speedY++;
			}
			
		
			if(bottom() >= lv.floor.top()){//the character has struck the bottom of the level
				setBottom(lv.floor().top());
				touchdown();
			}
			if(bottom()>= bnd.bottomBound()){//the character has struck the bottom of the boundary
				scrollY = (bottom() - bnd.bottomBound());
				setBottom(bnd.bottomBound());
			}			
		}
		else if(speedY<0){// if the character is moving up===========================================

			p = lv.getListHead();
			boolean stopped = false;
			
			//check for collision with top of the level
			if(y<=lv.top()){
				System.out.println("hit the top of the level!");
				setTop(lv.top());
				stopped = true;
			}
			
			while(p!= null){//check to see if I hit the bottom of a platform
				if(p != contact)
					if(hitsBottom(p)){
						System.out.println("hit the bottom of a platform! Robot("+(left()-lv.left())+","+(top()-lv.top())+")");
						//platform(p);
						stopped = true;
						scrollY=0;
						//speedY=0;
						//zenith();
						setTop(p.bottom());
						//lv.setspeedY(0);
						//System.out.println("I executed this code");
						break;
					}
				p = p.getNext();
			}
		
			
			
			if (stopped){
				speedY =0;
				zenith();
			}
			else {
				y+=speedY;
				speedY++;
			}
			 
			
			if(!stopped)
				if(y <= bnd.topBound()){//check for collision with the top of the boundary
					scrollY = y - bnd.topBound();
					y = bnd.topBound();
				}            
			
			
			
			 
						
		}else{//the character is not moving along Y=================================================
			if(contact!=null){
				if(!hitsTop(contact)) {
					contact = null;
					zenith();//fall
				}
			}
			
			if(jumped)zenith();
			
			scrollY = 0;
		}
		//yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
		
		
		/*
		 * This block checks to see if the player has touched the enemy
		 * 
		 * If he did then the player looses an hp and gets a number
		 * of frames when he can't take more damage		
		 */
		e = lv.getEnemies();
		int enems = lv.getNumPlayers();
		for(int i=0;i<(enems-1);i++)
			if(checkCollision(e[i]) && flashTimeUsed == 0 && hp>0){
				hit();
			}
		
		/*
		 * If the player is currently flashing the flashTimeUsed is
		 * incremented every frame until it becomes greater than the 
		 * flash time alloted then the vars are reset
		 */
		if(gotHit){
			flashTimeUsed++;
			if(flashTimeUsed > flashTime){
				flashTimeUsed = 0;
				gotHit = false;
			}
		}
		
		
		
	}
	
	public void draw(Graphics g){
		g.setColor(myColor);
		g.fillRect(left(), top(), getWidth(),getHeight());
		
		//(x,y,width,height)
		for(int i=0;i<maxhp;i++)
			if(hp>i)
				g.fillRect((left()+i*(healthblock+2)),top()-8,healthblock,healthblock);
			else
				g.drawRect((left()+i*(healthblock+2)),top()-8,healthblock,healthblock);
		
		if(jumpCharge>0){
			g.setColor(Color.WHITE);
			g.drawRect(left(), (top()-21), getWidth(), 5);
			g.setColor(Color.CYAN);
			g.fillRect((left()+1), (top()-20), getJumpBarWidth(), 3);
		}

	}
	
	
	public void stopX(){
		speedX =0;
	}
	public void moveRight(){
		speedX = moveSpeed;
	}
	public void moveLeft(){
		speedX =-1*moveSpeed;
	}

	
	/**
	 * Handles what happens when the character stops falling
	 */
	public void touchdown(){
		speedY = 0;
		jumped = false;
		jumpCharge = 0;
		System.out.println("touchdown("+(left()-lv.left())+","+(top()-lv.top())+")");
	}
	
	public void platform(Platform p){
		platformed = true;
		contact = p;
		if(this.y>=p.bottom())
			setTop(p.bottom());
		else{
			setBottom(p.top());
			touchdown();
		}
		
	}
	
	/**
	 * Handles what happens when the character reaches the top of his jump
	 */
	private void zenith(){
		System.out.println("Player zenith("+(top()-lv.top())+")");
		speedY=1;
		jumped = true;
	}
	
	public void jump(){
		if(!jumped){
			platformed = false;
			speedY = -1*(baseJump+jumpCharge);
			jumped = true;
			jumpCharge = 0; 
			width = baseWidth;
			height = baseHeight;
		}
	}
	
	public void jumpcharge(){
		if((baseJump + jumpCharge)<maxJump){
			jumpCharge++;
			if(height > baseHeight/2){
				height--;
				y++;
			}
		}
	}	
	
	public int getJumpBarWidth(){
		double percentage=0;
		double barwidth =0;
		if(width>=3){
			percentage = ((double)jumpCharge / (double)(maxJump - baseJump));
			barwidth = (percentage * (width-2));
			return (int) barwidth;
		}
		else
			return 1;
	}
	public boolean jumped() {
		return jumped;
	}

	public void duck(){
		if(!ducked){
			int h = baseHeight / 2;
			height = h;
			y+=h;
			ducked = true;
		}
	}
	
	public void unduck(){
		int h = baseHeight;
		height = h;
		y -= h/2;
		ducked = false;
	}
	
	public void hit(){
		//jump();
		ouch.play(); //plays the "ouch.wav" audio clip
		gotHit = true;
		hp--;
	}

public void shoot(Point tgt,BulletList bullets){	
	shoot.play();//plays the "pop.wav" audio clip
	Point start = new Point(x+(width/2),y+(height/2));
	Bullet b = new Bullet(start,tgt);
	bltnum++;
	b.setNum(bltnum);
	bullets.push(b);
}

public void addShootSound(AudioClip sht){
	shoot = sht;
}

public void addOuchSound(AudioClip och){
	ouch = och;
}

public void setScreen(Boundary sc){
	bnd = sc;
}

public int scrollX(){
	return scrollX;
}

public int scrollY(){
	return scrollY;
}

	public void setLevel(Level l){
		lv = l;
	}	

	public boolean gameOver() {
		return gameOver;
	}
	
	public void setColor(Color c){
		myColor = c;
	}
	public Color getColor(){
		return myColor;
	}
	
	public String name(){
		return "robot";
	}
	
	public boolean platformed(){
		return platformed;
	}
	
	public void kill(){
		gameOver = true;
	}
	public void setSize(int x, int y){
		width = x;
		height= y;
		
	}
	
	public void setMoveSpeed(int s){
		moveSpeed = s;
	}
	
	public void setJumpHeight(int s){
		maxJump = s;
	}
	public void addHP(int s){
		hp = hp + s;
	}
	
	public void maxHP(){
		hp = maxhp;
	}
}

