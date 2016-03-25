package squarewarrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHelloThread extends Thread implements Runnable {
	private static int myPort;
	private ServerSocket servSock;
	private SquareServer parent;
	
	/**
	 * Creates a client connection listener thread that will assign player number 
	 * and port number to incoming clients and register them with the servers main 
	 * thread. 
	 * @param port the port that the hello thread will be listening on
	 * @param myParent will be used to request a player number and port number when a new client is received 
	 */
	public ClientHelloThread(int port,SquareServer myParent){
		System.out.println("A Hello thread was initialized");
		myPort = port;
		parent = myParent;
	}
	
	/**
	 * Starts the Hello thread 
	 */
	public void run(){
		System.out.println("Opening port "+myPort+" for incoming clients");
		Socket link = null;
		String messageIn;
		int plyNum, portNum;
		
		//Attempts to listen for incoming connections
		try {
			servSock = new ServerSocket (myPort);
			
			do{
				try{
					//Receives a client connection
					link = servSock.accept();
					System.out.println("A connection was accepted!");
					
					BufferedReader in = 
							new BufferedReader(
									new InputStreamReader(
											link.getInputStream()));
					
					PrintWriter out = new PrintWriter(
							link.getOutputStream(), true);
					
					messageIn = in.readLine();
					System.out.println("a message was recieved: "+messageIn);
					
					if(messageIn.equalsIgnoreCase("Hello")){
						plyNum = parent.getNextPlayerNum();
						portNum = parent.getNextPortNum();
						String msgOut = "";
						
						System.out.println("from SqrServer: plyNum "+plyNum+" portNum "+portNum);
						//if there is room for another player
						//out.print(plyNum);
						//out.print(portNum);
						
						msgOut = ""+plyNum+" "+portNum;
						out.println(msgOut);
						parent.newPlayer();
						
						System.out.println("Hello thread replierd to a client and gave it # "+plyNum);
					}
					
					
					
					//closes the link and sets its socket equal to null
					link.close();
					link = null;
				}
				catch (IOException E){
					E.printStackTrace();
				}
			}while(true);
			
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client Hello thread Unable to attach port# "+myPort);
			e.printStackTrace();
		}
		finally{
			System.out.println("\n* Closing Hello connection...");
			//link.close();
		}
		
	}
}
