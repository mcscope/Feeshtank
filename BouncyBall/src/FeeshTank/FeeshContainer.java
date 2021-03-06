package FeeshTank;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class FeeshContainer {
    protected static int UPDATE_RATE = 40;     //should evenly divide 1000  (number of updates a second)
    ArrayList<Feesh> myFeeshList = new ArrayList<Feesh>();
    ArrayList<Feesh> myOutgoingTransferList = new ArrayList<Feesh>();
    ArrayList<Feesh> myIncomingTransferList = new ArrayList<Feesh>();
    ArrayList<String> otherFeeshContainerIPs = new ArrayList<String>();
    public final Random random = new Random();
    public boolean headless=false;
    public boolean debug = true;
    public boolean prune= false;
    public boolean masturbate = false; //do I connect to myself or not?
    public int port = 2002;
    private int updateCount = 0;
    private int spawnInterval=0;
    private int migrateInterval=0;
    private int sendInterval=200;

    FeeshContainer() {
        Properties defaultProps = new Properties();
                                                //todo create Readme for configuration options
        otherFeeshContainerIPs.add("192.168.2.4");
        try {
            FileInputStream in = new FileInputStream("settings.env");
            defaultProps.load(in);

            if(debug)System.out.println(".env properties file found "+ defaultProps);
            in.close();
            if(defaultProps.containsKey("firstIP")) otherFeeshContainerIPs.add(defaultProps.getProperty("firstIP"));

            if(defaultProps.containsKey("headless")) headless = Boolean.parseBoolean(defaultProps.getProperty("headless"));
            if(defaultProps.containsKey("masturbate")) masturbate = Boolean.parseBoolean(defaultProps.getProperty("masturbate"));
            if(defaultProps.containsKey("prune")) prune = Boolean.parseBoolean(defaultProps.getProperty("prune"));

            if(defaultProps.containsKey("port")) port = Integer.parseInt(defaultProps.getProperty("port"));
            if(defaultProps.containsKey("spawnInterval")) spawnInterval =   Integer.parseInt(defaultProps.getProperty("spawnInterval"));
            if(defaultProps.containsKey("migrateInterval")) migrateInterval = Integer.parseInt(defaultProps.getProperty("migrateInterval"));

            if(defaultProps.containsKey("sendInterval")) migrateInterval = Integer.parseInt(defaultProps.getProperty("SendInterval"));

        } catch (IOException e) {
          if(debug)  System.out.println(".env file not found");
        }


    }

    abstract public void createFeesh() ;

    abstract public ArrayList<Feesh> getFeeshList();

    abstract public ArrayList<Feesh> getFeeshListExcluding(Feesh a);

    abstract public boolean removeFeesh(Feesh toRemove);

    public void step() {
        // Execute one update step
        //           System.out.println(updateCount);


        //all the feesh must update
        synchronized (myIncomingTransferList) {
            for (Feesh curFeesh : myIncomingTransferList) {
                if(!headless) curFeesh.startDisplaying();
            }
            myFeeshList.addAll(myIncomingTransferList);
            myIncomingTransferList.clear();
        }
        Feesh curFeesh;

        if(spawnInterval !=0 && random.nextDouble()> (1.0-(1.0/spawnInterval)))
        {
            createFeesh();
        }


        synchronized (myOutgoingTransferList )
        {
       //sometimes fish leave.  The frequency is defined by migrateinterval
            if(migrateInterval !=0 && random.nextDouble()> (1.0-(1.0/migrateInterval)) &&myFeeshList.size()>0)
            {  curFeesh=myFeeshList.get(random.nextInt(myFeeshList.size()));
                curFeesh.stopDisplaying();
                myOutgoingTransferList.add(curFeesh);
                myFeeshList.remove(curFeesh);
            }

        Iterator<Feesh> e = myFeeshList.iterator();
        while (e.hasNext()) {
            curFeesh = e.next();
           curFeesh.step();
            //todo : call Collide on colliding Feesh
            if (!curFeesh.isDisplaying()) {
                 myOutgoingTransferList.add(curFeesh);
                e.remove();
            }
        }
        }


        // Delay for timing control and give other threads a chance
        try {
            Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
        } catch (InterruptedException ex) {
        }

    }

    public void sendList() {
        if (!otherFeeshContainerIPs.isEmpty()) {
            String targetIP = otherFeeshContainerIPs.get(random.nextInt(otherFeeshContainerIPs.size()));
            try {

                if (debug) System.out.println("outgoing: contacting " + targetIP + ":" + port);
                Socket s = new Socket(targetIP, port);
                if (debug) System.out.println("outgoing: response from " + targetIP + ":" + port);
                OutputStream os = s.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);

                //send the set of IPs that you have
                oos.writeObject(otherFeeshContainerIPs);

                if (debug) System.out.println("outgoing: " + myOutgoingTransferList.size() + " feesh to transfer");
                for (Feesh cur : myOutgoingTransferList) {
                    cur.stopDisplaying();
                }
                oos.writeObject(myOutgoingTransferList);
                myOutgoingTransferList.clear();

                if (debug) System.out.println("outgoing: Feesh successfully transfered");


                oos.close();
                os.close();
                s.close();
            } catch (SocketTimeoutException e) {
                if(prune) otherFeeshContainerIPs.remove(targetIP);
               System.out.println(e);
            }
            catch (IOException e) {

                System.out.println(e);
            }

        }
        else
        {
            if(debug) System.out.println("Outgoing: IP list empty. Can not attempt to connect ");
        }
    }

    public void receiveList(Socket s) {
        try {
            InputStream is = s.getInputStream();

            ObjectInputStream ois = new ObjectInputStream(is);
            //read a list of addresses from the socket
            ArrayList<String> receivedIPSet = (ArrayList<String>) ois.readObject();
          receivedIPSet.add(s.getInetAddress().getHostAddress());

            synchronized (otherFeeshContainerIPs)
           {
               //add all IPs that we don't already have to our list.
            for (String IP : receivedIPSet) {
                if (!otherFeeshContainerIPs.contains(IP))
                    otherFeeshContainerIPs.add(IP);
            }
               if(!masturbate) otherFeeshContainerIPs.remove(s.getLocalAddress().getHostAddress());
               if(debug) System.out.println("List of IPs: "+ otherFeeshContainerIPs);
           }
            //      read a list of fish from the socket
            ArrayList<Feesh> receivedList = (ArrayList<Feesh>) ois.readObject();
            if (debug) System.out.println("server:" + receivedList.size() + " feesh recieved: " + receivedList + " from " + s.getInetAddress().getHostAddress());
            synchronized (myIncomingTransferList) {
                myIncomingTransferList.addAll(receivedList);
            }
            is.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createThreads() {
        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                    updateCount += 1;
                    step();
                }
            }
        };

        Thread receiveThread = new Thread() {
            public void run() {
                ServerSocket ss = null;
                //we start one server
                try {
                    ss = new ServerSocket(port);
                    if (debug) System.out.println("server: spawned");
                } catch (IOException e) {
                    if (debug) System.out.println("server: socket failed (IO Exception)");
                    e.printStackTrace();
                }

                while (true) {
                    try {
                        Socket s = ss.accept();
                        //server spawns a new thread for each client connection
                        Thread spawnedRecieveThread = new ServerSpawnedThread(s);
                        spawnedRecieveThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread sendThread = new Thread() {
            public void run() {
                while (true) {
                    if (updateCount % sendInterval == 0) {
                        //attempt to contact server once every while

                        sendList();
                    }
                    try {
                        Thread.sleep(1000 / BallClient.UPDATE_RATE);  // milliseconds
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        receiveThread.start();  // Callback run()
        sendThread.start();  // Callback run()
        gameThread.start();  // Callback run()
    }

    protected class ServerSpawnedThread extends Thread {
        private Socket socket = null;

        public ServerSpawnedThread(Socket s) {
            super("ServerSpawnedThread");
            this.socket = s;
        }

        public void run() {
            if (debug) System.out.println("server: connection made");
            receiveList(socket);
        }
    }
}
