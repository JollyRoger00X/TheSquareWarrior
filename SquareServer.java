package squarewarrior;

public class SquareServer {
	private boolean newPlayerRcvd = false;
	private final int maxPlayers;
	private final int startingPort;
	private UpdatePacket[] updates; 
	private ClientHandlerThread[] clients;
	private boolean[] occupied;
	private GameStatePacket nextState =null;
	private boolean updateReady = false;
	private ClientHelloThread helo;
	
	private int numPlayers = 0;
	
	public SquareServer(int mxPlayers,int port){
		maxPlayers = mxPlayers;
		startingPort = port;
	}
	
	public static void main(String[] args){
		SquareServer s = new SquareServer(8,8190);
		s.run();
	}
	
	public void run(){
		int i,numUpdates=0;
		System.out.println("There has been a disturbance in the force!");
		updates = new UpdatePacket[maxPlayers];
		clients = new ClientHandlerThread[maxPlayers];
		occupied = new boolean[maxPlayers];
		helo = new ClientHelloThread(startingPort,this);
		helo.start();
		System.out.println("but main continued to run");
		int duh=1;
		
		//create initial values in all array slots as empty or null
		for(i=0;i<maxPlayers;i++){
			clients[i] = new ClientHandlerThread((startingPort+duh),duh,this);
			clients[i].start();
			duh++;
			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			occupied[i] = false;
			updates[i] = null;
		}

		//At this point all the threads are started none of them are occupied 
		while(true){//the server will continue to run forever
			//System.out.println("SquareServer running");
			//
			//updateReady = false;
			numUpdates = 0;
			for(i=0;i<maxPlayers;i++){//if a player is in a slot we need to get that player's update packet
				if(clients[i].running())
					if(occupied[i])//if there is a player in this slot
						if(clients[i].newUpdate()){//if that player has a new update
						//	System.out.println("Server found a new update in client thread "+i);
							numUpdates++;
						}
			}
			//System.out.println("running!");
			//if we have the same number of new updates as players than a new server state can be made
			if(numPlayers>0){
				//System.out.println("numPlayers now greater than 0!");
				//System.out.println("there are now "+numPlayers+" players");
				if(numUpdates == numPlayers){
					System.out.println("All Players put in an update!");
					int j=0;
					UpdatePacket[] state = new UpdatePacket[numPlayers];
					
					for(i=0;i<maxPlayers;i++){
						if(occupied[i]){
							System.out.println("in squserver updates["+i+"]= "+updates[i]);
							state[j] = updates[i];
							System.out.println("in squserver state["+j+"]= "+state[j]);
							j++;
						}
					}
				
				nextState = new GameStatePacket(numPlayers,state.clone());
				System.out.println("GameState Produced!\n********************************\n"+nextState.toString());
			//	updateReady = true;
				//we're resetting all the clients newUpdate() status to false
				for(i=0;i<numPlayers;i++) 
					if(occupied[i]){
						clients[i].putGamestate(nextState);
						clients[i].resetNewUpdate();
				}
				
				System.out.println("An update was posted");
			}
				else{
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		}
		}
	}
	
	
	public int getNextPlayerNum(){
		int i=0;
		for(i = 0; i < maxPlayers; i++)
		{
			if(occupied[i] == false)
				return i+1;
		}
		return -1;
	}
	
	public int getNextPortNum(){
		int i=0;
		for(i = 0; i < maxPlayers; i++)
		{
			if(occupied[i] == false)
			{
				occupied[i] = true;
				return startingPort + (i + 1);
			}
		}
		return 8191;
	}
	public void newPlayer(){
		numPlayers++;
		newPlayerRcvd = true;
	}
	
	/**
	 * 
	 * @param location
	 * @param update
	 */
	public void postUpdate(int location,UpdatePacket update){
		updates[(location-1)] = update;
	}
	
	public void postPlayerLost(int pyNum){
		//pyNum decremented because every player number is 1 more than its array slot
		pyNum--;
		occupied[pyNum] = false;
		System.out.println("Thread "+pyNum+" posted a player lost!");
		int port = clients[pyNum].getPort(),num = clients[pyNum].getNum();
		clients[pyNum].stop();
//		clients[i].destroy();
		clients[pyNum] = new ClientHandlerThread(port,num,this);
		clients[pyNum].start();
		numPlayers--;
		
	}
	
	public boolean gameStateReady(){
		return updateReady;
	}
	
	public GameStatePacket nextUpdate(){
		return nextState;
	}
}
