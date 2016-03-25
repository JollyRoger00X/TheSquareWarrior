package squarewarrior;

import java.io.*;
import java.net.*;

public class ClientClass {
	private static InetAddress host;
	private static final int PORT =12345;
	
	public static void main(String[] args){
		try{
			host = InetAddress.getLocalHost();
		}catch(UnknownHostException e){
			System.out.println("Host ID not found!");
			System.exit(1);
		}
		run();
		
	}
	
	private static void run(){
		Socket link = null;
		try{
			link = new Socket(host,PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			
			PrintWriter out = new PrintWriter(link.getOutputStream(),true);
			
			BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
			
			String message, response;
			do{
				System.out.print("Enter message: ");
				message = userEntry.readLine();
				out.println(message);
				response = in.readLine();
				System.out.println("\nSERVER> "+response);
			}while(!message.equals("***CLOSE**"));
			
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				System.out.println("\nClosing Connection... *");
				link.close();
			}catch(IOException e){
				System.out.println("UNABLE to disconnect from myself!");
				System.exit(1);
				
			}
		}
	}
}
