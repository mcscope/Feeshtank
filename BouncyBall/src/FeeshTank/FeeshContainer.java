package FeeshTank;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    public int port = 2002;
    public String targetIP = "192.168.2.4";
    public boolean debug = true;
    private int updateCount = 0;

    abstract public ArrayList<Feesh> getFeeshList();

    abstract public ArrayList<Feesh> getFeeshListExcluding(Feesh a);

    abstract public boolean removeFeesh(Feesh toRemove);

    abstract public void step();

    public void sendList() {
        try {

            if (debug) System.out.println("outgoing: contacting " + targetIP + ":" + port);
            Socket s = new Socket(targetIP, port);
            if (debug) System.out.println("outgoing: response from" + targetIP + ":" + port);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //if (in.readLine().equals("Feesh?")) {
            //  out.println("Feesh!");
            if (debug) System.out.println("outgoing: " + myOutgoingTransferList.size() + "feesh to transfer");
            for (Feesh cur : myOutgoingTransferList) {
                cur.stopDisplaying();
            }
            oos.writeObject(myOutgoingTransferList);
            myOutgoingTransferList.clear();

            //if (in.readLine().equals("Feesh Recieved")) {
            if (debug) System.out.println("outgoing: Feesh successfully transfered");
            //}

            //}

            oos.close();
            in.close();
            out.close();
            os.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void receiveList(Socket s) {
        try {

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            InputStream is = s.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            //   out.println("Feesh?");
            //  if (in.readLine().equals("Feesh!")) {
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList<Feesh> receivedList = (ArrayList<Feesh>) ois.readObject();
            if (debug) System.out.println("server:" + receivedList.size() + " feesh recieved: " + receivedList);
            for (Feesh curFeesh : receivedList) {
                curFeesh.startDisplaying();
                    }
                myIncomingTransferList.addAll(receivedList);
                out.println("Feesh Recieved");

            is.close();
            out.close();
            in.close();
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
                    if (updateCount % 200 == 0) {
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
