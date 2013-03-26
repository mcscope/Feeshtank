package FeeshTank;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class wrapBall extends Ball {
    public wrapBall( ) {

        super( );

    }


    public void createBall(GraphicsConfiguration translucencyCapableGC) {
        myFrame = new wrapBallFrame(translucencyCapableGC);
    }

}
class wrapBallFrame extends BallFrame
{
    public wrapBallFrame(GraphicsConfiguration gc)
    {super(gc);

    }

    public void step()
    {

        Rectangle screenDimensions = screen.getBounds();
        Rectangle bounds = getBounds();


        double speedLimit=10.0;
        double slowMultiple=.9;
        //if going too fast!
        if(xspeed>speedLimit || xspeed<-1*speedLimit)
        {
            xspeed*=slowMultiple;
        }
        if(yspeed>speedLimit || yspeed<-1*speedLimit)
        {
            yspeed*=slowMultiple ;
        }

        x += xspeed;
        y += yspeed;
        x=(x>screenDimensions.width+ballWidth)? x  % screenDimensions.width-ballWidth: x ;
        y=(y>screenDimensions.height+ballHeight)? y  % screenDimensions.height -ballHeight: y ;
//        y= y% screenDimensions.height;
        x=(x<-ballWidth)? x+ screenDimensions.width +ballWidth : x;
        y=(y<-ballHeight)? y+ screenDimensions.height +ballWidth: y;

        bounds.x = (int) x-ballHeight/2;
        bounds.y = (int) y-ballWidth/2;

        setBounds(bounds);


    }



}
