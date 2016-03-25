package squarewarrior;
//package squarewarrior;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class GameController extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private Player myRobot;//,enemy;
	private Level lv;
	private String lvlName =null;
	private Image image;//,character;
	public Graphics second;
	private boolean mouseOnScreen = false;
	private int screenWidth = 800,screenHeight=480, mx=0 ,my=0;//,frameCounter=0;
//	private Color color;
	private BulletList bullets = null;
	private boolean alreadyShot = false;
	private Boundary bnd;
	private AudioClip pop,ouch;
	private int numPlayers = 8;
	private Enemy[] enemy;
//	private Buff buff;
	public boolean jumped = false;
	private boolean inSinglePlayer = true;
	
	//Server connection variables
	private ServerConnectionThread svConn;
	private GameStatePacket nextGamestate;
	private UpdatePacket myUpdate;
	private int playerNum;
	private int svPort=8190;

	//private Server sv;

	
//level makers
	private int 
	mclickx=0,
	mclicky=0,
	mdrgx=0, 
	mdrgy=0,
	drgStrtx = 0,
	drgStrty = 0;
	private boolean 
	making = false,
	platformMake = false,
//	ptMakeStrt = false,
	poiSetup = false;
	private Platform selectedPlatform;
	private SqrButton btn;
	private SqrButton save;	

	PlatformMenu pltMenu;
	FirstMenu ff = new FirstMenu("Squaremagon");
	
	private static int frameDelay = 20;
	//set to 17 for normal play speed
	
	
	/*
	 * This is the first code executed when the applet runs
	 */
	@Override
	public void init(){
		
		pltMenu = new PlatformMenu(Color.darkGray);
		pop = getAudioClip(getCodeBase(),"pop.wav");
		ouch = getAudioClip(getCodeBase(),"ouch.wav");
//		ff.setVisible(true);
		
		//This block sets up the properties of the applet
		bnd = new Boundary(0,0,screenWidth,screenHeight);
		setSize(bnd.getWidth(),bnd.getHeight());
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame f = (Frame) this.getParent().getParent();
		f.setTitle("Squaremagon The Square Warrior");
		addMouseListener(this);
		addMouseMotionListener(this);	
		
		//sets up the 2 edit mode buttons
		btn = new SqrButton(100,100,50,Color.cyan);
		save = new SqrButton(200,100,50,Color.red);
		
		//creates a list to hold the players bullets
		bullets = new BulletList();
		
		//Sets up the player variable
		myRobot = new Player();
		myRobot.setspeedY(1);
		myRobot.setColor(Color.RED);
		myRobot.setScreen(bnd);
		myRobot.addShootSound(pop);
		myRobot.addOuchSound(ouch);
		
		//sets up the level
		lvlName = "LevelOne.txt";
		lv = new Level(myRobot,lvlName,numPlayers);
		myRobot.setLevel(lv); //the player needs the level to get access to its list of platforms
		enemy = lv.getEnemies();
		for(int i=0;i<(numPlayers-1);i++){
			System.out.println("Getting enemy["+i+"]");
			enemy[i] = lv.getEnemy(i);
		}
//		buff = lv.getBuff();
	}
	
	
	@Override
	public void start(){			
		Thread thread = new Thread(this);
		// sv = new Thread(this);
		//sv = new Server(12345);
		//sv.start();
		svConn = new ServerConnectionThread(svPort);
		svConn.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("sleep interuption");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerNum = svConn.getPlayerNum();
		thread.start();
	}
	
	
	@Override
	public void run() {
		//get info for creating a client thread to contact the server
		System.out.println("You must tell me where to find A SERVER! > 127.0.0.1");
		System.out.println("You must also tell me a... PORT NUMBER!> "+svPort);
		
		
		
		//create a new server handler thread 
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
			while(!myRobot.gameOver()){
				
			//Server will get to update first
				if(svConn.getPlayerNum()>0){
					int upx = myRobot.left(),upy = myRobot.top();
					myUpdate = new UpdatePacket(playerNum,upx,upy);
					svConn.postUpdate(myUpdate); 
					
					if(svConn.updateIn()){//if the server has provided a new update 
						nextGamestate = svConn.getNextGameState();
						int nmPlayers = nextGamestate.getNumPlayers();
						UpdatePacket[] newUpdates = nextGamestate.getState();
						//int[]  playerLocs = new int[numPlayers];
						int i=0,xx,yy;
						
						
						for(i=0;i<nmPlayers;i++){
							xx = newUpdates[i].GetXlocation();
							yy =newUpdates[i].GetYlocation();
							enemy[newUpdates[i].getPlayerNum()].setLeft(xx);
							enemy[newUpdates[i].getPlayerNum()].setTop(yy);
							System.out.println("Player "+newUpdates[i].getPlayerNum()+" set to ("+xx+","+yy+")");
						}
						
						//then put in my update
					}
				}

				//this will pause the game.
				if(!ff.isVisible() && (inSinglePlayer))
					{
						myRobot.update();
						lv.update();	

						repaint();
					}
				
			
			/*
			if(sv.getJump()){
				enemy[0].jump();
				sv.resetJump();
			}*/
			
			if(making){// level maker update
				levelMakerUpdate();
			}
			
			//update the bullets list if there are any bullets in play
			if (!bullets.empty()) {
				bulletListUpdate();
			}
			
			//finally the level gets updated
			
			try{
				Thread.sleep(frameDelay);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
	
		lv.save();
		System.out.println("Level Saved!\nAnd then death happened!");
	
	}
	
	/**
	 * This function redraws the contents of the applet
	 */
	public void update(Graphics g){
		
		if(ff.isVisible())
		{
			return;
		}
		if(image == null){
			image = createImage(this.getWidth(),this.getHeight());
			second = image.getGraphics();
		}
			second.setColor(getBackground());
			second.fillRect(0, 0, getWidth(), getHeight());
			second.setColor(getForeground());
			paint(second);
			g.drawImage(image,0,0,this);
	}
	
	private void levelMakerUpdate(){
		if(ff.isVisible())return;
		if(selectedPlatform!=null){
			if(!poiSetup){
				poiSetup = true;
				pltMenu.selectPlatform(selectedPlatform);
			}
			
			pltMenu.move(myRobot.scrollX(), myRobot.scrollY());
			pltMenu.update();
			
			if(pltMenu.closed()){
				selectedPlatform = null;
				pltMenu.closeMenu();
			}
			
			if(btn.checkMouseCollision(mclickx, mclicky))
					selectedPlatform.makeBlue();		

		}
		
		// The save button also ends the game
		if(save.checkMouseCollision(mclickx, mclicky)){
			myRobot.kill();
			
		}
		mclickx =0;
		mclicky =0;
	}
	
	/**
	 * Moves each bullet thats in play
	 */
	private void bulletListUpdate(){
		if(ff.isVisible())return;
		Platform p = null;
		BulletList blts = new BulletList();
		Enemy[] e = lv.getEnemies();
		Bullet ths = bullets.pop();
		int i,numEnemies = numPlayers-1;
		

		
	//	double bleh;
		
	
		
		while(ths!=null){//checks collision against every object in the game
							
				p = lv.getListHead();
				while(p!=null){//does it reflect off a platform?
					//if the bullet hits the bottom or top of a platform the distance it moves along y must be inverted so it will bounce vertically
					if(ths.hitsBottom(p, (int)ths.getRise())){
						ths.invertRise();
						ths.bounce();
					}else if(ths.hitsTop(p,(int)ths.getRise())){
						ths.invertRise();
						ths.bounce();
					}
					//if the bullet hits the left or right of a platform it will bounce horizontally
					if( ths.hitsLeft(p, (int)ths.getRun())){
						ths.invertRun();
						ths.bounce();
					}else if(ths.hitsRight(p,(int)ths.getRun())){
						ths.invertRun();
						ths.bounce();
					}
					p=p.getNext();
					/*
					 * Because a bullet is immediately reflected when its GOING to hit a platform
					 * and they have a speed of 20 they can bounce off up to 19 pixels before they
					 * actually touch it. Its kind of hard to see during runtime but it should still be fixed.
					 */
				}
				
				for(i=0;i<numEnemies;i++)
					if(ths.checkCollision(e[i])){//does the bullet hit the enemy?
						e[i].hit(ths);
						ths.done();
					}
				
				//if the bullet is still onscreen it will move whether or not it hits a platform
				ths.move();
				ths.scroll(lv.getspeedX(), lv.getspeedY());
				
				//bullets will only be pushed into the new bullet list if they are onscreen and have not hit the enemy
				if(ths.cont())
					blts.push(ths);
			
			//gets the next bullet from the list to be checked
			ths = bullets.pop();
		}
		
		/*
		 * Once the original list of bullets is empty only the 
		 * bullets left in play will have been pushed into the blts list
		 * so I reassign the levels bullets pointer to the head of the new list
		 *
		 * This list is turned upside down each time this loop passes
		 */
		bullets = blts;		
	}	

	public void paint(Graphics g){
//		frameCounter++;
		if(ff.isVisible())return;
		bnd.draw(g);
		myRobot.draw(g);
		lv.draw(g);

		if(making){//I am in level making mode
			btn.draw(g); //this button makes a platform blue
			save.draw(g); //this button saves the level and ends the game
			pltMenu.draw(g); //this menu doesn't work yet but it will be used for editing the properties of a platform
		}
		
		//paint cross-hair
		if(mouseOnScreen){			
			g.setColor(Color.PINK);
		//	g.drawString("hello mouse ", 100,100);
			g.drawLine(mx-8, my, mx+8, my);//(x1,y1,x2,y2)
			g.drawLine(mx, my-8, mx, my+8);//(x1,y1,x2,y2)
			g.drawOval(mx-13, my-13, 26, 26);
		}
		
		//paint bullets
		Bullet ths;
		ths = bullets.getHead();
		if(ths != null){
			while(true){
				ths.draw(g);
				ths = ths.getNext();
				if(ths == null)
					break;
				
			}
		}
	}
	
	
	@Override
	public void stop(){
	}
	@Override
	public void destroy(){
	}
	
	public void setFrameDelay(int time){
		frameDelay= time;
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_J: //move enemy left
			enemy[0].goX(-8);
		break;
		case KeyEvent.VK_T: //start making mode
			making = true;
			break;			
		case KeyEvent.VK_I: //duck enemy to jump
			enemy[0].duck();
			break;
		
		case KeyEvent.VK_K: //duck enemy
			enemy[0].duck();
			break;
			
		case KeyEvent.VK_L: //move enemy right
			enemy[0].goX(8);
		break;
		
		case KeyEvent.VK_W:
			System.out.println("move up");
		break;
		
		case KeyEvent.VK_S://duck player
			myRobot.duck();
		break;
		
		case KeyEvent.VK_A:////move player left
			myRobot.moveLeft();
		break;
		
		case KeyEvent.VK_D://move player right
			myRobot.moveRight();
		break;
		
		case KeyEvent.VK_SPACE://begin jumpcharge
			myRobot.jumpcharge();
		break;		
		
		case KeyEvent.VK_M://menu
			ff.setVisible(true);
			break;
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_J:
			enemy[0].stopX();
			break;
			
		case KeyEvent.VK_Y://leave making mode
			selectedPlatform =null;
			poiSetup = false;
			making = false;
			break;
		case KeyEvent.VK_I://jump enemy
			enemy[0].jump();
			break;
			
		case KeyEvent.VK_K:
			enemy[0].unDuck();
			break;
			
		case KeyEvent.VK_L://stop enemy movement along x
			enemy[0].stopX();
			break;
			
		case KeyEvent.VK_W:
			System.out.println("STOP up");
		break;
		
		case KeyEvent.VK_S: //unduck robot
			myRobot.unduck();
		break;
		
		case KeyEvent.VK_A: //stop players movement along x
			myRobot.stopX();
		break;
		
		case KeyEvent.VK_D://also stops the players movement along x
			myRobot.stopX();
		break; 
		
		case KeyEvent.VK_SPACE://JUMP!
			myRobot.jump();
		break;		
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
	
		
	}

	

	@Override
	public void mouseDragged(MouseEvent e) {
		if(ff.isVisible())return;
		platformMake=true;
		if(making){
			mdrgx = e.getX();
			mdrgy = e.getY();			
		}
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		if(ff.isVisible())return;
		mx = e.getX();
		my = e.getY();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(ff.isVisible())return;
		if(making){ 
			Platform p =null;
			mclickx = e.getX();
			mclicky = e.getY();
			drgStrtx = mclickx;
			drgStrty = mclicky;
			p = lv.clickPlatform(mclickx,mclicky);
			if(p!=null)
				selectedPlatform = p;
			
			pltMenu.setClicks(e.getX(), e.getY());
			
		}
		else{//shoot if not in level making mode
			System.out.println("shoot()");
	//		pop.play();
			Point p = new Point(e.getX(),e.getY());
			myRobot.shoot(p,bullets);
			alreadyShot = true;
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOnScreen = true;
	}


	@Override
	public void mouseExited(MouseEvent e) {
		mouseOnScreen = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(ff.isVisible())return;
		if(making){//if in making mode the starting point of the new platform is tracked
			mx = e.getX();
			my = e.getY();
			drgStrtx = mx;
			drgStrty = my;
		}
		if(!alreadyShot){
			System.out.println("shoot()");
			pop.play();
			Point p = new Point(e.getX(),e.getY());
			myRobot.shoot(p,bullets);
			alreadyShot = true;
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {	
		if(ff.isValid())return;
		if(making){
			int width,height;
			if(platformMake)
			{
				int top,left;
				width = drgStrtx - mdrgx;
				if(width<0) width*=-1;
				height = drgStrty - mdrgy;
				if(height<0) height*=-1;
				left=drgStrtx;
				top=drgStrty;
				if(mdrgx<drgStrtx) left-=width;
				if(mdrgy<drgStrty) top-=height;
				
				//platform(width,height,left,top)
				System.out.println("new Platform(wd "+width+",ht "+height+",left "+left+",top "+top+")");
				lv.addPlatform(new Platform(width,height,left,top));
			}
			drgStrtx = 0;
			drgStrty = 0;
			mdrgx=0;
			mdrgy=0;
			//ptMakeStrt = false;
		}
		platformMake = false;
		alreadyShot = false;
	}	
}


/*
	private void drawEffect(Graphics g){
		int col=0;
		col = (frameCounter%256);
		//Color c = new Color(red,green,blue)
		
		
		for(int i=0;i<25;i++){
			Color c = new Color( 0, 0,((col+(10*i))%256));
			g.setColor(c);
			g.drawLine(200, 200+i*4, 456, 200);
			c = new Color( ((col+(10*i))%256), 0,0);I 
			g.setColor(c);
			//g.drawLine(x1, y1, x2, y2)
			g.drawLine(200,300,0,0);
		}
	}
	
	private void paintBoarder(Graphics g){
		g.setColor(Color.WHITE);
		g.drawRect(2, 2, lv.width()-2,lv.height()-12);
	}
*/
