package FeeshTank;

import java.util.ArrayList;
import java.util.Iterator;

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


    public BallClient() {
        super(); //initalizes env file values
        for (int x = 0; x < numBalls; x += 1) {

            createFeesh();

        }

        createThreads();


    }

    public void createFeesh() {
        int ballSelector = random.nextInt(6);
        if (ballSelector < 2)
            bouncy = new jumpBall();
        else if (ballSelector > 4)
            bouncy = new wrapBall();
        else
            bouncy = new Ball();

        if(!headless)   bouncy.startDisplaying();
        myFeeshList.add(bouncy);
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



