package squarewarrior;


import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class Level extends Colidable {
	
	private PlatformList platforms;
	private Scanner scan;
	private Player player;
	private Enemy enemy[];
	Platform floor;
	private File f;
	private FileWriter fr;
	private int numPlatforms;
	private int wOriginal = 3000;
	private int hOriginal = 1000;
	private int numEnemies;
	private int numPlayers;
	Buff buff;
	
	
	public Level(Player r,String name,int numPlayrs){
		
		player = r;
		platforms = new PlatformList();
		numPlatforms = 12;
		numEnemies = numPlayrs-1;
		numPlayers = numPlayrs;
		enemy = new Enemy[numEnemies];
		setupLv(name);
	}
	
	/**
	 * Loads a level from a file
	 * @param num number of the level to load
	 */
	private void setupLv(String name){

		f = new File(name);
		
		try{
			Platform t;
			int enmyXloc = 250;
			for(int i=0;i<numEnemies;i++){
				System.out.println("creating enemy["+i+"]");
				enemy[i] = new Enemy(this,player,enmyXloc*(i+1),200);
				System.out.println("Enemy["+i+"] has x location:"+enemy[i].left());
			}
			
			buff = new Buff(this,player);
			scan = new Scanner(f);
			int i;
			int dats=7;
			
			//reads the first int in the file to find how many platforms there are to read
			numPlatforms = scan.nextInt();
			//the next 2 ints tell the levels width and height
			width = scan.nextInt();
			height = scan.nextInt();
			//the number of reads left to be done is the number of platforms in the level
			//multiplied by number of data bits per platform
			int numReads=numPlatforms*dats;
			//creates an array to hold the data from the files
			int[] inputs = new int[numReads];
			
			//fills the inputs array with data from the file
			for(i=0;i<numReads;i++)
				inputs[i] = scan.nextInt();
			
			//the floor is the first platform stored in the file
			floor = new Platform(inputs[0],inputs[1],inputs[2],inputs[3]);
			floor.setColor(new Color(inputs[4],inputs[5],inputs[6]));
			platforms.push(floor);
			
			//reads the data from the inputs[] array into each platform in the level
			for(i=1;i<numPlatforms;i++){
				t= new Platform(inputs[dats*i],inputs[dats*i+1],inputs[dats*i+2],inputs[dats*i+3]);
				t.setColor(new Color(inputs[(dats*i)+4],inputs[(dats*i)+5],inputs[(dats*i)+6]));
				platforms.push(t);
			}
			
			x=0;
			y=0; 
			speedX=0;
			speedY=0;
			wOriginal = width;
			hOriginal = height;
			numPlatforms = platforms.getLength();
				

		}
		catch(FileNotFoundException e){
			//if the file read does not work I setup a default level 1
			setupLV1();
			System.out.println("I couldn't open the file");
		}
		
	}
	
	/**
	 * This setup function was used to load a default LV1 when I was
	 * originally making the level and when making the level loading tool and
	 * is still called by default if setupLV() cannot open the level file
	 */
	private void setupLV1(){
		
		int enmyXloc = 200;
		for(int i=0;i<numEnemies;i++)
			enemy[i] = new Enemy(this,player,enmyXloc*(i+1),200);
		buff = new Buff(this,player);

		x=0;
		y=0; 
		speedX=0;
		speedY=0;
		width = 3000;
		height = 1000;

		
		//remembers the original values for the level
		//so it can be saved
		wOriginal = width;
		hOriginal = height;

		
		//platform(width,height,left,top)
		//Ibeam landing
		platforms.push(new Platform(300,10,370,250)); //landing
		platforms.push(new Platform(10,100,670,200)); // landing Right wall
		platforms.push(new Platform(100,10,550,180)); //landing Roof
		platforms.push(new Platform (10,100,360,215)); //landing left wall
		
		//Staircase
		platforms.push(new Platform(300,10,0,500));  //step1
		platforms.push(new Platform(200,10,400,650));//step2
		platforms.push(new Platform(200,10,700,800)); //step3 
		platforms.push(new Platform(200,10,1100,920)); //step4
		
		//pyramid
		//*******************
		//********___********
		//*******|   |*******
		//****|         |****
		//*******************
		int left = 1500;
		int bottom = 900;
		int widh = 350;
		int blkht = 90;
		int blkwd = 113;
		platforms.push(new Platform(10,blkht,left,bottom - blkht));  //Left block 1
		platforms.push(new Platform(blkwd,10,left+10,bottom-blkht));//step1
		platforms.push(new Platform(10,blkht,left+blkwd,bottom - (blkht*2))); //left block 2
		platforms.push(new Platform(widh,10,left+blkwd,bottom - (blkht*2))); //top
		platforms.push(new Platform(10,blkht,left+blkwd+widh,bottom - (blkht*2))); //right block 2
		platforms.push(new Platform(blkwd,10,left+blkwd+widh,bottom - blkht)); //right step 1
		platforms.push(new Platform(10,blkht,left+(blkwd*2)+widh,bottom - blkht)); //right block 1	
		
		//Stage Walls
		platforms.push(floor = new Platform(width,10,0,height-10));//bottom
		platforms.push(new Platform(10,height-10,0,0));//left
		platforms.push(new Platform(10,height-10,width-10,0));//right
		platforms.push(new Platform(width-80,10,70,0));//top
		 
	}
	 
	/**
	 * Makes changes to the level with each iteration of the game loop
	 */
	public void update(){ 
		
		for(int i=0;i<numEnemies;i++)
			enemy[i].update();
		
		buff.update();
		speedX = player.scrollX();
		speedY = player.scrollY();
	
		
		//when the level scrolls all the platforms in it are scrolled with it		 
		Platform pt = platforms.head();
		while(pt!=null){
			pt.setLeft(pt.left()+(-1*speedX));
			pt.setTop(pt.top()+(-1*speedY));
			pt = pt.getNext();
		}
	
		//the (x,y) location of the level is also updated in the scrolling
		x+= -1*speedX;
		y+= -1*speedY;
		
		
	}
	

	
	/**
	 * Saves the level that is currently running
	 */
	public void save(){
		
		f = new File("LevelOne.txt");
		
		try {
			fr = new FileWriter(f);
			
			fr.write(numPlatforms+" "+wOriginal+" "+hOriginal);
			
			Platform p = platforms.head();
			platformWriter(p);
			
			fr.close();
		}
		catch (IOException e) {
			System.out.println("Cannot write to file!");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Calls itself recursively and writes each platform to the file
	 * so the bottom platform in the list is the top platform in the file
	 * @param p platform I'm on
	 * @throws IOException cannot write
	 */
	private void platformWriter(Platform p) throws IOException{
		
		if(p.getNext()!=null) platformWriter(p.getNext());
		
			fr.write("\n");
			fr.write(p.getWidth()+" ");
			fr.write(p.getHeight()+" ");
			fr.write((p.left() - x)+" ");
			fr.write((p.top() - y)+" ");
			fr.write(p.getR()+" "+p.getG()+" "+p.getB());
	}
	
	/**
	 * Adds a platform to the level
	 * @param p the platform to be added
	 */
	public void addPlatform(Platform p){
		Platform hd = platforms.head();
		p.setNext(hd.getNext());
		hd.setNext(p);
		numPlatforms++;
	}
	
	/**
	 * Figures out if a platform has been clicked by the mouse
	 * @param x x location of the click
	 * @param y y location of the click
	 * @return the platform clicked; or null if no platform was clicked
	 */
	public Platform clickPlatform(int x,int y){
		Platform p;
		p = platforms.head();
		while(p!=null){
			if(p.checkMouseCollision(x,y))
				return p;
			p=p.getNext();
		}
		return null;
	}
	
	/**
	 * Adds a list of platforms to the level
	 * @param platforms the list to add
	 */
	public void setPlatforms(PlatformList platforms) {
		this.platforms = platforms;
	}

	public Enemy getEnemy(int which){
		if(which<0) which*=-1;
		
		which%=numEnemies;
		
		System.out.println("getting enemy "+which);
		System.out.println("enemy["+which+"] has an x loc of "+enemy[which].left());
		return enemy[which];
	}
	
	public Enemy[] getEnemies(){
		return enemy;
	}
	
	public PlatformList getPlatforms(){
		return platforms;
	}
	public Buff getBuff() {
		return buff;
	}
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public void draw(Graphics g) {
		Platform pt = platforms.head();
		while(pt!=null){
			pt.draw(g);
			pt = pt.getNext();
		}
		
		for(int i=0;i<numEnemies;i++)
			enemy[i].draw(g);
		buff.draw(g);
	}
	@Override
	public String name() {
		return "Level";
	}
	
	public Platform getListHead(){
		return platforms.head();
	}
	public Platform floor(){
		return floor;
	}
	
	public int getNumPlayers(){
		return numPlayers;
	}
	
}




/*
 * 	Old SetupLv1() from lv constructor
  		int ptx, pty, ptwd, ptht;
  		String lineIn;
	try {
	//	fr = new FileWriter(f);
		
	} catch (IOException e) {

		e.printStackTrace();
	}
	Platform hd = platforms.head();
	while (hd != null){
		ptx = hd.x;
		pty = hd.y;
		ptwd = hd.getWidth();
		ptht = hd.getHeight();
		tofile = ptx+" "+pty+" "+ptwd+" "+ptht+"\n";
		try {
			fr.write(tofile);
			System.out.println("wrote to the file");
		} catch (IOException e) {

			e.printStackTrace();
		}
		hd = hd.getNext(); 
	}*/

/*
public Platform checkPtCollisions(int x,int y){
	Platform p;
	p = platforms.head();
	while(p != null){
		if(x >= p.left() && x <= p.right())
			if(y >= p.top() && y<= p.bottom())
				return p;
		p = p.getNext();
	}
	return null;
}*/
