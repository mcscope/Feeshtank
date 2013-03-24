package FeeshTank;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class jumpBall extends Ball {
    public jumpBall(GraphicsConfiguration gc, FeeshContainer inTank) {
        super(gc, inTank);
    }


                      public void step()
                      {
    Rectangle screenDimensions = screen.getBounds();
                          double slowMultiple=.8;
                          double bounceMultiplier = -8;

                          Rectangle bounds = getBounds();
    double speedLimit=10.0;
    //if going too fast!
    if(xspeed>speedLimit || xspeed<-1*speedLimit)
    {
        xspeed*=slowMultiple;
    }
    if(yspeed>speedLimit || yspeed<-1*speedLimit)
    {
        yspeed*=slowMultiple ;
    }


    if ((0 > x && xspeed<0 )||( screenDimensions.width < x + ballWidth  && xspeed>0 )) {
        xspeed *= bounceMultiplier;//reverse
    }

    if  ((0 > y && yspeed<0 )||( screenDimensions.height < y + ballHeight  && yspeed>0 )) {
        yspeed *= bounceMultiplier;//reverse
    }


    x += xspeed;
    y += yspeed;
//        x=x % screenDimensions.width;
//        y= y% screenDimensions.height;

    bounds.x = (int) x;
    bounds.y = (int) y;

    setBounds(bounds);
                      }
}

