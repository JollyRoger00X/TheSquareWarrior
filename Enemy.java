package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Colidable {
	private int 
	hp = 10,
	maxHp = 10,
	jump =18,
	goX = 0,
	goY = 0,
	baseHeight,
	baseWidth,
	red=20,green=20,blue=20,
	origX,origY;
	private boolean 
	jumped = false,
	dead = false,
	ducked = false;
	private  Platform contact = null,Rwall = null,Lwall=null;
//	private BulletList bts;
	private Level lv;
	private Player Player;
	private PlatformList platforms;
	private Color badGuy = Color.GRAY;
	
	
	public Enemy(Level l,Player r,int xloc,int yloc){
		height = 50;
		baseHeight = 50;
		width = 50;
		baseWidth = 50;
		origX = xloc;
		origY = yloc;
		setLeft(origX);
		setBottom(origY);
		lv = l;
		platforms = lv.getPlatforms();
		Player = r;
		zenith();
	}
	@Override
	public void draw(Graphics g) {
		//(x,y,width,height)
		g.setColor(badGuy);
		g.fillRect(x,y,width,height);
		g.drawString("("+hp+"/"+maxHp+")", x, y-20);
	}


	@Override
	public String name() {
		return "Enemy";
	}
	
	
	
	public void zenith(){
		jumped = true;
		goY+=1;
	}
	
	public void goX(int spd){
		goX=spd;
	}
	
	public void jump(){
		if(!jumped){
			goY-=jump;
			contact = null;
			jumped = true;
			height = baseHeight;
			width = baseWidth;
			y-=baseHeight-height;
		}
		ducked = false;
	}
	
	public void hit(Bullet blt){
		if(!dead){
			jump();
			hp-=blt.pow();
			red-=21;
			if(red<0) red = 255+red;
			blue-=21;
			if(blue<0) blue = 255+blue;
			green-=21;
			if(green<0) green = 255+green;
			//color = new color(red,green,blue)
			badGuy= new Color(red,green,blue);
			if(hp<=0){
				dead = true;
			}
		}
	}
	public boolean getjump()
	{
		return jumped;
	
	}

	public void touchdown(Platform p){
		jumped = false;
		speedY = p.getspeedY();
		setBottom(p.top());
		goY=0;
		contact = p;
		System.out.println("Enemy touchdown("+left()+","+bottom()+")");
	}
	
	public void moveLeft(){
		goX=-8;
	}
	
	public void moveRight(){
		goX=8;
	}
	
	public void stopX(){
		goX = 0;
	}
	
	public void duck(){
		if(!ducked){
			int h = baseHeight / 2;
			height = h;
			y+=h;
			ducked = true;
		}
	}
	
	public void unDuck(){ 
		height = baseHeight;
		y-=baseHeight/2;
		ducked = false;
	}
	
	
	public void update(){
		if(hp<=0)dead = true;
		
		speedX=-1*Player.scrollX();
		speedY=-1*Player.scrollY();
		if(!dead){
		Platform p;
			p = platforms.head();
			
			//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX X location Update
			if(goX>0){//the enemy is moving right 
				//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++moving right
				Lwall = null;
				if(Rwall==null) // if the enemy is not against a wall
					while(p!=null){
						if(hitsLeft(p,goX)){
							System.out.println("HIT left");
							Rwall = p;
							setRight(p.left());
							stopX();
							break;
						}
						p = p.getNext();
					}
				else{//if the enemy is against a wall
					if(touches(Rwall))
						goX = 0;
					
				}
				
				if(right()>=lv.right()){ 
					stopX();
					setRight(lv.right());
				}
			} else if (goX<0){//the enemy is moving left
				//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++moving left
				Rwall = null;
				if(Lwall == null)//the enemy is not against a wall
					while(p!=null){
						if(hitsRight(p,goX)){
							System.out.println("HIT right");
							setLeft(p.right());
							Lwall = p;
							stopX();
						}
						p = p.getNext();
					}
				else{//the enemy is against a wall
					if(touches(Lwall))
						goX = 0;
				}
				if(left()<=lv.left()) {
					setLeft(lv.left());
					stopX();
				}
				
			}
			
			//YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY Y location update
			p = platforms.head();
			if(goY>0){//---------------------------------------the enemy is falling
					goY++;
					while(p!= null){
						if(hitsTop(p,goY) || touches(p)){
							System.out.println("Enemy HIT Platform!");
							touchdown(p);
							break;
						}
						p = p.getNext();
					}
					if(bottom()>= lv.floor().top()){
						System.out.println("Enemy hit FLOOR");
						touchdown(lv.floor());
					}
			}else if(goY<0){//------------------------------the enemy is jumping up
				goY++;
				while(p!=null){
					if(hitsBottom(p,goY)){
						System.out.println("Enemy: HIT bottom");
						setTop(p.bottom());
						goY =0;
						zenith();
						break;
					}
					p = p.getNext();
				}
				
			}else{ //-------------------------------------the Enemy is not moving along Y
				if(contact!= null && !touches(contact))
						contact =null;
				if(contact == null)
					zenith();
			}
			
		}else {//------------------------------------------the enemy is dead
		//if dead the enemy will freeze in place and fade to black
		red--;
		green--;
		blue--;
		if(red<0) red=0;
		if(blue<0) blue=0;
		if(green<0) green=0;
		badGuy = new Color(red,green,blue);
		
		//once the RGB values reach 0s all variables are reset and the sprite is returned to the top of the level
		if(red == 0 && green == 0 && blue == 0){
				origX+=50;
				badGuy = Color.gray;
				setLeft(lv.left()+origX);
				setTop(lv.top()+origY);
				hp = maxHp;
				dead = false;
				contact = null;
				zenith();
			}
		
		}
		
		x+=(speedX+goX);
		y+=(speedY+goY);
	
	}
}
