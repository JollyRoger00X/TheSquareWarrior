package squarewarrior;

import java.util.Scanner;

public class GameStatePacket {
	private int numPlayers;
	//private int[] playerList;
	private UpdatePacket[] servState;
	
	//private BulletList bts;
//	int numBullets;
//	Bullet[] bulletin;
	

	public GameStatePacket(int numplyrs,UpdatePacket[] svST){
		numPlayers = numplyrs;
		//playerList = plyList;
		servState = svST.clone();
	}
	
	public GameStatePacket(String in){
		
		//System.out.println("New gameState recieved\n"+in);
		Scanner sc = new Scanner(in);
		int i;
		
		numPlayers = Integer.parseInt(sc.next());
		
		System.out.println("parsed");
		
		int x,y,num;
		servState = new UpdatePacket[numPlayers];
		for(i=0;i<numPlayers;i++){
			num = Integer.parseInt(sc.next());
			x= Integer.parseInt(sc.next());
			y= Integer.parseInt(sc.next());
			
			servState[i] = new UpdatePacket(num,x,y);
		}
		
		sc.close();		
	}
	
	public String toString(){
		int i;
		String out=numPlayers+" ";
		System.out.println("numPlayers "+out);
		
		
		for(i=0;i<numPlayers;i++){
			System.out.println("setting player["+i+"] to "+servState[i].toString());
			out+=servState[i].toString();
			
			//out = ""+(i+1)+"  "+(i+1)*100+" "+(i+1)*100+" ";
			//System.out.println("out = "+out);
		}
		
//		System.out.println("GameStatePacket.toString() = "+out);
		return out;
	}
	
	public UpdatePacket[] getState(){
		return servState;
	}
	
	public int getNumPlayers(){
		return numPlayers;
	}		
}
