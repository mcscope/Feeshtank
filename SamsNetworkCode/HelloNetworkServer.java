package netTest;

import java.io.*;
import java.net.*;

public class HelloNetworkServer
{	
	public static void main(String[] args)
	{
		ServerSocket myServerSocket = null;
		//String destination = "samuel-clamons-macbook-pro.local";
		int portNum = 5556;
		
		try
		{
			InetAddress myInet = InetAddress.getLocalHost();
			System.out.println("Canonical Host Name: " + myInet.getCanonicalHostName());
			System.out.println("Host Address: " + myInet.getHostAddress());
			System.out.println("Host Name: " + myInet.getHostName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		
		try
		{
			myServerSocket = new ServerSocket(portNum);
			System.out.println("Server Started!");
			
		}
		catch(UnknownHostException e)
		{
			System.out.println("Cannot listen on port" + portNum);
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Other error");
			e.printStackTrace();
			System.exit(0);
		}
		
		Socket clientSocket = null;
		
		try
		{
			clientSocket = myServerSocket.accept();
		}
		catch(IOException e)
		{
			System.out.println("Client socket failed to accept.");
			System.exit(0);
		}
		
		try
		{
			PrintWriter outstream = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader instream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			outstream.println("What's the password?");
			while(!instream.readLine().equals("Thunderbear"))
			{
                System.out.println("Client gave wrong password for 'Thunderbear'");
				outstream.println("Wrong! Try again. What's the password?");
			}

            System.out.println("Client correctly entered 'Thunderbear'");
			outstream.println("Correct! The secret number is 42. What's the new password?");

            while(!instream.readLine().equals("password"))
            {
                System.out.println("Client incorrectly entered password 'password'");
                outstream.println("Wrong! Try again!");
            }
            System.out.println("Client correctly entered 'password'\nShutting down server...");
            outstream.println("Correct! The secret letter is 'X'. Enter anything to exit.\nexit");
			outstream.println("exit");
			
			instream.close();
			outstream.close();
			myServerSocket.close();
			clientSocket.close();

            System.out.println("Server closed.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
