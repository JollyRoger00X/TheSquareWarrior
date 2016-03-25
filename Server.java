package squarewarrior;
import java.io.*;
import java.net.*;

public class Server extends Thread
{
	
	private ServerSocket servSock;
	private int PORT = 12345;
	boolean makeJump = false;
	UpdatePacket [] WriteArray = new UpdatePacket[9];
	UpdatePacket [] ReadArray = new UpdatePacket[9];
	
	public Server(int port){
		PORT = port;
	}
	
	
	public void run(){
		System.out.println("Opening port...\n");
		
		try{
			servSock = new ServerSocket(PORT);
		}
		
		catch(IOException e){
			System.out.println("Unable to attach to port!");
			System.exit(1);
		}
		
		do
		
		{
			Socket link = null;
			try{
				link = servSock.accept();
				
				BufferedReader in = 
						new BufferedReader(
								new InputStreamReader(
										link.getInputStream()));
				PrintWriter out = new PrintWriter(
						link.getOutputStream(), true);
				
				int numMessages = 0;
				String message = in.readLine();
				
				while (!message.equals("***CLOSE***")){
					if(message.equals("jump")) makeJump = true;
					System.out.println("Message recieved.");
					numMessages++;
					out.println("Message " + numMessages + ": " + message);
					message = in.readLine();
				}
				out.println(numMessages + " messages recieved.");
			}
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				try{
					System.out.println("\n* Closing connection... *");
					link.close();
				}
				catch(IOException e){
					System.out.println("Unable to disconnect!");
					System.exit(1);
				}
			}
			
		}while (true);
		
	}
	
	public boolean getJump(){
		return makeJump;
	}
	
	public void resetJump(){
		makeJump = false;
	}
}
