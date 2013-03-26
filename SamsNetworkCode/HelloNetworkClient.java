package netTest;

import java.io.*;
import java.net.*;

public class HelloNetworkClient
{
	public static void main(String[] args)
	{
		Socket mySocket = null;
		BufferedReader instream = null;
		PrintWriter outstream = null;
		String destination = "192.168.2.8";
		int portNum = 5556;
		
		try
		{
			System.out.println("Attempting to connect...");
			mySocket = new Socket(destination, portNum);
			System.out.println("Client connected!");
			
			instream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outstream = new PrintWriter(mySocket.getOutputStream(), true);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			String inString = null;
			while(!((inString = instream.readLine()).equals("exit")))
			{
				System.out.println("Server: " + inString);
				outstream.println(stdIn.readLine());
			}

            System.out.println("Server has disconnected. Exiting.");
			instream.close();
			outstream.close();
			mySocket.close();
		}
		catch(UnknownHostException e)
		{
			System.out.println("Could not find host " + destination);
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
