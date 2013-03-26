package FeeshTank;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */


public class BallClient implements FeeshContainer {
    private static ArrayList<Feesh> myFeeshList;
    private static ArrayList<Feesh> myOutgoingTransferList;
    private static ArrayList<Feesh> myIncomingTransferList;

    private static Ball bouncy;
    private static int numBalls = 10;
    private static int UPDATE_RATE = 40;     //should evenly divide 1000  (number of updates a second)
    private int updateCount = 0;

    private final Random random = new Random();

    public BallClient() {


        myFeeshList = new ArrayList<Feesh>();
        myOutgoingTransferList = new ArrayList<Feesh>();
        myIncomingTransferList= new ArrayList<Feesh>();

        for (int x = 0; x < numBalls; x += 1) {

            int ballSelector = random.nextInt(6);
            if (ballSelector < 2)
                bouncy = new jumpBall(this);
            else if (ballSelector > 4)
                bouncy = new wrapBall(this);
            else
                bouncy = new Ball(this);

            bouncy.startDisplaying();
            myFeeshList.add(bouncy);

        }

        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                step();
                }
            }
        };

        Thread receiveThread = new Thread() {
            public void run() {
                while (true) {
                    if (updateCount % 100 == 0) {
                        //try to connect to server

                        System.out.println("CHECK FOR SERVER");
                        receiveList();

                    }

                    }
            }
        };

        Thread sendThread = new Thread() {
            public void run() {
                if (updateCount % 100 == 0) {
                    //spawn server

                    System.out.println("SPAWN SERVER");
                    sendList();
                }

            }
        };
        receiveThread.start();  // Callback run()
        gameThread.start();  // Callback run()
        sendThread.start();  // Callback run()


    }

    private void step() {
        // Execute one update step
        updateCount += 1;
                        System.out.println(updateCount);


        //all the feesh must update
        myFeeshList.addAll(myIncomingTransferList);
        myIncomingTransferList.clear();
        Feesh curFeesh;
        Iterator<Feesh> e = myFeeshList.iterator();
        while (e.hasNext()) {
            curFeesh = e.next();
            curFeesh.step();
            if (!curFeesh.isDisplaying()) {
                myOutgoingTransferList.add(curFeesh);
                e.remove();
            }

        }


        // Delay for timing control and give other threads a chance
        try {
            Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
        } catch (InterruptedException ex) {
        }

    }


    public ArrayList<Feesh> getFeeshList() {
        return myFeeshList;
    }

    public ArrayList<Feesh> getFeeshListExcluding(Feesh a) {
        ArrayList<Feesh> toReturn = (ArrayList<Feesh>) myFeeshList.clone();
        toReturn.remove(a);
        return toReturn;

    }

    public boolean removeFeesh(Feesh toRemove) {
        return myFeeshList.remove(toRemove);
    }

    @Override
    public void sendList() {
        try {
            Socket s = new Socket("localhost", 2002);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(myOutgoingTransferList);
            myOutgoingTransferList.clear();
            oos.close();
            os.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void receiveList() {
        int port = 2002;
        try {
            System.out.println("Hello");
            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();
            System.out.println("Hello 2");
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList<Feesh> receivedList = (ArrayList<Feesh>) ois.readObject();
            System.out.println(receivedList);
            for(Feesh curFeesh :receivedList)
            {
                curFeesh.startDisplaying();
            }
            myIncomingTransferList.addAll(receivedList);
            is.close();
            s.close();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



