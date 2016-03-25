package squarewarrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientHandlerThread extends Thread implements Runnable {
	private static int myPort;
	private int playerNum;
	private ServerSocket servSock;
	private SquareServer parent;
	private UpdatePacket plyUpdate;
	private GameStatePacket gameUpdate;
	private boolean newUpdate = false;
	private boolean newGamestate = false;
	private boolean running = false;
	
	/**
	 * Creates a client connection listener thread that will assign player number 
	 * and port number to incoming clients and register them with the servers main 
	 * thread. 
	 * @param port the port that this client is told to connect on
	 * @param myParent will be used to request a player number and port number when a new client is received 
	 * @param plyrNum used to identify which player this thread belongs to
	 */
	public ClientHandlerThread(int port,int plyrNum,SquareServer myParent){
		System.out.println("A client Handler was initialized");
		myPort = port;
		parent = myParent;
		playerNum = plyrNum;
	}
	
	/**
	 * Starts the Client Handler thread 
	 */
	public void run(){
		running = true;
		System.out.println("Listening to port "+myPort+" for player number "+playerNum);
		Socket link = null;
		String messageIn,messageOut;
		boolean done = false;
		
		//Attempts to listen for incoming connection
		try {
			servSock = new ServerSocket (myPort);
			
			try{
				//Receives a client connection
				link = servSock.accept();
				
				BufferedReader in = 
						new BufferedReader(
								new InputStreamReader(
										link.getInputStream()));
				
				PrintWriter out = new PrintWriter(
						link.getOutputStream(), true);
				
				while(!done){
					//READ an update packet from the player/client
					messageIn = in.readLine();
					System.out.println("ClientHandlerThread read a message!");
					System.out.println("Message read from client "+messageIn);
					plyUpdate = deserialize(messageIn);
					System.out.println("Update "+plyUpdate.getPlayerNum()+" ("+plyUpdate.GetXlocation()+","+plyUpdate.GetYlocation()+")");
					
					
					//POST that update to the servers UpdatePacket Array
					newUpdate = true;
					parent.postUpdate(plyUpdate.getPlayerNum(),plyUpdate);
					
					
						
					//WAIT for the server to create a GameStatePacket
					//do something...
					System.out.println("ClientHandler waiting for update from server");
					while(!newGamestate){
					  	 //cout<<"waiting..."<<endl<<"but not patiently"<<endl;
						//System.out.println("waiting for an update from the server");
						Thread.sleep(1);
					}
					
					newGamestate = false;
					
					//now that a new update is ready we take it and turn it into a string for transmission
					gameUpdate = parent.nextUpdate();
					

					//REPLY to the player 
					messageOut = serialize();
					System.out.println("Client Handler got server responce! = "+messageOut);
					out.println(messageOut);
					//out.println("2 2 100 100 3 400 100");
					System.out.println("Message sent");
				}
				
				//procede to finally()
			}
			catch(SocketException e){
				
		//		running = false;
		//		link.close();
		//		link = null;
		//		parent.postPlayerLost(playerNum);				
			}
			catch (IOException E){
				E.printStackTrace();
			} catch (InterruptedException e) {//what happens if the thread is interrupted during sleep
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try{
					System.out.println("\n* Closing Client Handler Connection... ");
					link.close();
					link = null;
					running = false;
					parent.postPlayerLost(playerNum);
				}
				catch(IOException e){
					System.out.println("Unable to disconnect!");
					System.exit(1);
				}
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to attach port");
			e.printStackTrace();
		}
	}
	public int getPort(){
		return myPort;
	}
	public int getNum(){
		return playerNum;
	}
	public boolean running(){
		return running;
	}
	public void resetNewUpdate(){
		newUpdate = false;
	}
	
	public boolean newUpdate(){
		return newUpdate;
	}
	
	public void putGamestate(GameStatePacket gs){
		gameUpdate = gs;
		newGamestate = true;
	}
	
	
	/**
	 * Converts a received string into an UpdatePacket
	 * @param message the string that was received
	 * @return an object of type UpdatePacket
	 */
	private UpdatePacket deserialize(String message){
		if(message == null){
			System.out.println("UpdatePacket() tried to deserialize a null string");
			return new UpdatePacket(1,300,300);
		}
		System.out.println("deserialize("+message+")");
		UpdatePacket p;
		Scanner sc = new Scanner(message);
		
		int x,y,num;
		num = Integer.parseInt(sc.next());
		x = Integer.parseInt(sc.next());
		y = Integer.parseInt(sc.next());
		
		p = new UpdatePacket(num,x,y);
		sc.close();
		return p;
	}
	
	/**
	 * Used to convert the GameStatePacket held by the class into a
	 * string for transmission
	 * @return the packet as a single string
	 */
	private String serialize(){
		//gameUpdate class variable of type GameStatePacket
		//convert gameUpdate to a string
		return gameUpdate.toString();
	}
		
}