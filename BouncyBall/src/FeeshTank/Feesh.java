package FeeshTank;

import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class Feesh implements Serializable  {


    Feesh( ) {

    }

    abstract public void step();

   abstract public void die();

    public void collide(Feesh collidingFeesh)
    {}

   abstract public void stopDisplaying();
   abstract public void startDisplaying();
    abstract boolean isDisplaying();

}


