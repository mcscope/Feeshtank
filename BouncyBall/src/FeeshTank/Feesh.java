package FeeshTank;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class Feesh extends javax.swing.JFrame implements javax.swing.RootPaneContainer {
    FeeshContainer tank;

    Feesh(GraphicsConfiguration gc, FeeshContainer intank) {
            super(gc);
            tank=intank;
    }

    abstract public void step();

    public void die() {
         dispose();

    }

    public void collide(Feesh collidingFeesh)
    {}
}


