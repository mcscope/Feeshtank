package netTest;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sclamons
 * Date: 5/1/12
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindowPassingClient
{
    static ArrayList myList ;
    public static void main(String[] args)
    {
        myList= new ArrayList(10000);

        //Constants
        String destination = "192.168.1.5";
        int portNum = 5556;

        //Build a frame to send across the net
        JFrame myFrame = new JFrame("I got passed!");

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel myPanel = new JPanel(new FlowLayout());
        myPanel.add(new JLabel("Contents shown here"));

        myFrame.getContentPane().add(myPanel);
        myFrame.pack();


        //Connect to the server
        try
        {
            System.out.println("Connecting to server...");
            Socket mySocket = new Socket(destination, portNum);
            System.out.println("Connection established. Sending JFrame...");

            BufferedReader instream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            ObjectOutputStream outstream = new ObjectOutputStream(mySocket.getOutputStream());


            //NewArrayList myList = new NewArrayList(100000);
            for(int x = 0; x < 100000; x++)
            {
                myList.add(new Integer(x));
            }

            outstream.writeObject(myList);

            if(!instream.readLine().equals("Frame received"))
            {
                System.out.println("Server receipt of Frame was not correct.");
            }
            else
            {
                System.out.println("Frame sent. Server receipt correct. Closing connection.");
            }

            instream.close();
            outstream.close();
            mySocket.close();
        }
        catch(IOException e)
        {
            System.out.println("Error connecting to server at destination " + destination + " with port " + portNum);
            e.printStackTrace();
            System.exit(0);
        }
    }

    private class NewArrayList extends ArrayList
    {
            public NewArrayList()
            {
                super();
            }
            public NewArrayList(int i)
            {
                super(i);
            }

            public int newMethod()
            {
                return 919;
            }
    }
}
