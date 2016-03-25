package squarewarrior;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
//import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerConnectionThread extends Thread implements Runnable {
	private InetAddress host;
	private int port;
	private int playerNum = -1;
	private GameStatePacket serverUpdate;
	private UpdatePacket updateOut;
	private boolean newUpdate = false,updateIn = false;

	
	public ServerConnectionThread(int prt){
		port = prt;
	}
	
	public void run(){
		Socket link = null;
		try{
			Scanner sc;
			host=InetAddress.getLocalHost();
			
			link = new Socket(host,port);
			BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			
			PrintWriter out = new PrintWriter(link.getOutputStream(),true);
			
			//BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
		
			String message, response;
			int newPort;
			
			message = "Hello";
			
			out.println(message);
			
			response  = in.readLine();
			sc = new Scanner(response);
			
			playerNum = Integer.parseInt(sc.next());
			newPort = Integer.parseInt(sc.next());
			
			sc.close();
			
			System.out.println("The server gave me player number "+playerNum+" port num "+newPort);
			link.close();
			
			host = null;
			link = null;
			in = null;
			out = null;
			
			
			host=InetAddress.getLocalHost();
			link = new Socket(host,newPort);
			Thread.sleep(200);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(),true);
			response = "";
			String msg;
			
			do{
				//System.out.println("Waiting for a new UPDATE!");
				if(newUpdate){
					msg = updateOut.toString();
					System.out.println("Got one! "+msg);
					out.println(msg);
					System.out.println("In between write and read");
					response = in.readLine();
					System.out.println("Message from server "+response);
					serverUpdate = new GameStatePacket(response);
					System.out.println("Got a reply from the server!\n"+serverUpdate.toString());
					updateIn = true;
					newUpdate = false;
				}
				else{
					Thread.sleep(5);
				}
				
			}while(true);
			/*
			do{ 
				System.out.print("Enter message: ");
				message = userEntry.readLine();
				out.println(message);
				response = in.readLine();
				System.out.println("\nSERVER> "+response);
			}while(!message.equals("***CLOSE**"));
			*/
			
			
			
		}catch(IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Thrown because the thread was interupted");
			e.printStackTrace();
		}
		finally{
			try{
				System.out.println("\nClosing ServerHandlerThread Connection... ");
				link.close();
			}catch(IOException e){
				System.out.println("UNABLE to disconnect from myself!");
				System.exit(1);
				
			}
		}
	
	}
	
	public void postUpdate(UpdatePacket p){
		updateOut =p;
		newUpdate = true;
	}
	
	public GameStatePacket getNextGameState(){
		updateIn = false;
		return serverUpdate;
	}
	
	public boolean updateIn(){
		return updateIn;
	}
	
	public int getPlayerNum(){
		return playerNum;
	}
}
