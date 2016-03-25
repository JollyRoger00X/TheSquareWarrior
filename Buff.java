package squarewarrior;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Buff extends Colidable {
	private int 
	type = generateSeed();
	private int goX = 0;
	private int red=20;
	private int green=20;
	private int blue=20;
	private int goY = 0;
	private int origX=((generateSeed()*160));
	private int origY =100;
	public boolean 
	touched = false;
	private  Platform contact = null;
	private Level lv;
	private Player Player = new Player();
	private PlatformList platforms;
	private Color buffcolor;
	public int count =0;
	public int timer =0;
	public int currentbuff;

	
	public Buff(Level l,Player r){
		height = 35;
		width = 35;
		setLeft(origX);
		setBottom(origY);
		lv = l;
		platforms = lv.getPlatforms();
		Player = r;
		zenith();
	}
	@Override
	public void draw(Graphics g) {
		switch (type) {
		
		case 1: buffcolor = Color.red;
		break;
		
		case 2: buffcolor = Color.yellow;
		break;
		
		case 3: buffcolor = Color.blue;
		break;
		
		case 4: buffcolor = Color.green;
		break;
		
		case 5: buffcolor = Color.white;
		break;
		
		case 6: buffcolor = Color.orange;
		break;
		
		case 7: buffcolor = Color.gray;
		break;
		
		case 8: buffcolor = Color.magenta;
		break;
		
		case 9: buffcolor = Color.pink;
		break;
		
		case 10: buffcolor = Color.CYAN;
		break;
		}
		g.setColor(buffcolor);
		g.fillRect(x,y,width,height);
		
	}


	@Override
	public String name() {
		return "Bname";
	}
	
	public  void touched(int b){
		if(b == 1){
			touched = true;
		}
		else{touched = false;}
	}
	
	public int generateSeed(){
		Random rn = new Random();
		int seed = rn.nextInt(10)+1;
		return seed;
	}
	
	public void award(Player x)
	{
		touched(1);
		switch (type) {
		
		case 1: x.setSize(100, 100);
		x.buffOn = false;
		
		break;
		
		case 2: x.setSize(15, 15);
		x.buffOn = false;
		
		break;
		
		case 3: x.setMoveSpeed(20);
		x.buffOn = false;
	
		break;
		
		case 4: x.setMoveSpeed(5);
		x.buffOn = false;
		
		break;
		
		case 5: x.setJumpHeight(50);
		x.buffOn = false;
		
		break;
		
		case 6: x.setJumpHeight(10);
		x.buffOn = false;
	
		break;
		
		case 7: x.addHP(5);
		x.buffOn = false;
		
		break;
		
		case 8: x.maxHP();
		x.buffOn = false;
		
		break;
		
		case 9: //GameController.setFrameDelay(31);
		x.buffOn = false;
		
		break;
		
		case 10: x.setSize(100, 100);
		x.buffOn = false;
		
		break;
		
		}
		
		System.out.println("Player Awarded");
		
	}
	
	public void debuff(Player x){
	
	switch (currentbuff) {
	
	case 1: x.setSize(50, 50);
	x.buffOn = false;
	
	break;
	
	case 2: x.setSize(50, 50);
	x.buffOn = false;
	break;
	
	case 3: x.setMoveSpeed(10);
	x.buffOn = false;
	break;
	
	case 4: x.setMoveSpeed(10);
	x.buffOn = false;
	break;
	
	case 5: x.setJumpHeight(28);
	x.buffOn = false;
	break;
	
	case 6: x.setJumpHeight(28);
	x.buffOn = false;
	break;
	
	case 7: 
	
	break;
	
	case 8: 
	
	break;
	
	case 9: //GameController.setFrameDelay(17);
	
	break;
	
	case 10: x.setSize(50, 50);
	x.buffOn = false;
	
	break;
	}
	}
	public void zenith(){
		goY+=1;
	}
	
	public void goX(int spd){
		goX=spd;
	}
	



	public void touchdown(Platform p){
		speedY = 0;
		setBottom(p.top());
		goY=0;
		contact = p;
		System.out.println("Buff touchdown("+left()+","+bottom()+")");
	}
	
	
	
	public void stopX(){
		goX = 0;
	}

	
	
	public void update(){
		count++;
	
		
		if(touched == false ){
			speedX=-1*Player.scrollX();
			speedY=-1*Player.scrollY();
		Platform p;
		p = platforms.head();
					
			
			//YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY Y location update
			p = platforms.head();
			if(goY>0){//---------------------------------------the enemy is falling
				
					while(p!= null){
						if(hitsTop(p,goY) || touches(p)){
							System.out.println("Buff HIT Platform!");
							touchdown(p);
							break;
						}
						p = p.getNext();
					}
					if(bottom()>= lv.floor().top()){
						System.out.println("Buff hit FLOOR");
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
		}	
		
			
	  if(touched == true)
	  {
		speedX=-1*Player.scrollX();
		speedY=-1*Player.scrollY();
		
		red--;
		green--;
		blue--;
		if(red<=0) red=0;
		if(blue<=0) blue=0;
		if(green<=0) green=0;
		buffcolor = new Color(red,blue,green);
		
			
		//once the RGB values reach 0s all variables are reset and the sprite is returned to the top of the level
		if(red == 0 && green == 0 && blue == 0){
			
				type = generateSeed();
				x= ((generateSeed()*160));
				y= 120;
				switch (type) {
				
				case 1: buffcolor = Color.red;
				break;
				
				case 2: buffcolor = Color.yellow;
				break;
				
				case 3: buffcolor = Color.blue;
				break;
				
				case 4: buffcolor = Color.green;
				break;
				
				case 5: buffcolor = Color.white;
				break;
				
				case 6: buffcolor = Color.orange;
				break;
				
				case 7: buffcolor = Color.gray;
				break;
				
				case 8: buffcolor = Color.magenta;
				break;
				
				case 9: buffcolor = Color.pink;
				break;
				
				case 10: buffcolor = Color.CYAN;
				break;
				}
				
				touched = false;
				contact = null;
				zenith();
			}	
		
		}
		Player = lv.getPlayer();
		if(checkCollision(Player))
		{  
			if(Player.buffOn == false){
				
			award(this.Player);
			currentbuff = type;
			  timer = count+420 ;
			
		}
			
			
		}
		
	if( currentbuff ==  9){
		if(count> timer-230 ){
			debuff(Player);
	}
	}
	if(count> timer ){
		debuff(Player);
		//System.out.println("debuffed");
	}
	
		
	
	
		x+=(speedX+goX);
		y+=(speedY+goY);
		
	
	}
}
