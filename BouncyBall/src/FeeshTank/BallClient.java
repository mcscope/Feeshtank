package FeeshTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */


public class BallClient implements FeeshContainer {
    private static ArrayList<Feesh> myFeeshList;
    private static Ball bouncy;
    private static int numBalls = 10    ;
    private static int UPDATE_RATE = 40;     //should evenly divide 1000  (number of updates a second)


    public BallClient() {

        GraphicsConfiguration translucencyCapableGC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (!AWTUtilitiesWrapper.isTranslucencyCapable(translucencyCapableGC)) {
            translucencyCapableGC = null;

            GraphicsEnvironment env =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] devices = env.getScreenDevices();

            for (int i = 0; i < devices.length && translucencyCapableGC == null; i++) {
                GraphicsConfiguration[] configs = devices[i].getConfigurations();
                for (int j = 0; j < configs.length && translucencyCapableGC == null; j++) {
                    if (AWTUtilitiesWrapper.isTranslucencyCapable(configs[j])) {
                        translucencyCapableGC = configs[j];
                    }
                }
            }
        }

        myFeeshList = new ArrayList<Feesh>();

        for (int x = 0; x < numBalls; x+=1) {


//            bouncy = new wrapBall(translucencyCapableGC, this);
//           myFeeshList.add(bouncy);
//            bouncy = new jumpBall(translucencyCapableGC, this);
//            myFeeshList.add(bouncy);
//

            bouncy = new Ball(translucencyCapableGC, this);
            myFeeshList.add(bouncy);

        }

        Thread gameThread = new Thread() {
            public void run() {
                while (true) {
                    // Execute one update step
                  Feesh curFeesh;
                    Iterator<Feesh> e = myFeeshList.iterator();
                    while (e.hasNext()) {
                        curFeesh =  e.next();
                       curFeesh.step();
                        if(!curFeesh.isDisplayable())
                        {
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

