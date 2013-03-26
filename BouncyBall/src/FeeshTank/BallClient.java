package FeeshTank;

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


public class BallClient extends FeeshContainer {

    private static Ball bouncy;
    private static int numBalls = 3;

    private final Random random = new Random();

    public BallClient() {
        for (int x = 0; x < numBalls; x += 1) {

            int ballSelector = random.nextInt(6);
            if (ballSelector < 2)
                bouncy = new jumpBall();
            else if (ballSelector > 4)
                bouncy = new wrapBall();
            else
                bouncy = new Ball();

            bouncy.startDisplaying();
            myFeeshList.add(bouncy);

        }

        createThreads();


    }

    public void step() {
        // Execute one update step
        //           System.out.println(updateCount);


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



}



