package FeeshTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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
    private static ArrayList<Feesh> myTransferList;

    private static Ball bouncy;
    private static int numBalls = 10    ;
    private static int UPDATE_RATE = 40;     //should evenly divide 1000  (number of updates a second)
    private int updateCount=0;

    private final Random random = new Random();
    public BallClient() {



        myFeeshList = new ArrayList<Feesh>();
        myTransferList = new ArrayList<Feesh>();

        for (int x = 0; x < numBalls; x+=1) {

            int ballSelector=random.nextInt(6);
            if(ballSelector<2)
               bouncy = new jumpBall(this);
            else if(ballSelector>4)
                bouncy=new wrapBall(this);
            else
                bouncy=new Ball(this);

        bouncy.startDisplaying();
            myFeeshList.add(bouncy);

        }

        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                    // Execute one update step
                    updateCount+=1;

                    if(updateCount%1000==0)
                    {   //check to see if a feesh should enter or leave


                    }


                    //all the feesh must update
                    Feesh curFeesh;
                    Iterator<Feesh> e = myFeeshList.iterator();
                    while (e.hasNext()) {
                        curFeesh =  e.next();
                       curFeesh.step();
                        if(!curFeesh.isDisplaying())
                        {
                            myTransferList.add(curFeesh);
                            e.remove();
                        }

                    }


                    // Delay for timing control and give other threads a chance
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        gameThread.start();  // Callback run()


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

