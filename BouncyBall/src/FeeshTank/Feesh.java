package FeeshTank;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class Feesh implements Serializable {


    public FeeshFrame myFrame;
    protected boolean displaying;
    boolean feeshSavedDataExists = false;

    Feesh() {
        displaying = false;
    }

    abstract public void step();

    public void die() {
        stopDisplaying();
    }

    public void collide(Feesh collidingFeesh) {
    }

    public void stopDisplaying() {
        displaying = false;
        if (myFrame != null) {
            myFrame.dispose();
            myFrame = null;
        }

    }

    public void startDisplaying() {
        stopDisplaying();

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
        createFeeshFrame(translucencyCapableGC);
        feeshSavedDataExists = true;
        displaying = true;
    }

    boolean isDisplaying() {
        return displaying;
    }

    abstract void createFeeshFrame(GraphicsConfiguration translucencyCapableGC);
}

abstract class FeeshFrame extends javax.swing.JFrame implements javax.swing.RootPaneContainer {
    FeeshFrame(GraphicsConfiguration gc)
    {super(gc);}
    abstract void step();


}