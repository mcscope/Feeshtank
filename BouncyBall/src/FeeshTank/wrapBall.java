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


    public void createFeeshFrame(GraphicsConfiguration translucencyCapableGC) {
        myFrame = new wrapBallFrame(translucencyCapableGC,this, feeshSavedDataExists);
    }

}
class wrapBallFrame extends BallFrame
{
    public wrapBallFrame(GraphicsConfiguration gc, Ball parentBall, boolean restoreFromSaved)
    {super(gc,parentBall,restoreFromSaved);

    }

    public void step()
    {

        Rectangle screenDimensions = screen.getBounds();


        double speedLimit=10.0;
        double slowMultiple=.9;
        //if going too fast!
        if(myBall.xspeed>speedLimit || myBall.xspeed<-1*speedLimit)
        {
            myBall.xspeed*=slowMultiple;
        }
        if(myBall.yspeed>speedLimit || myBall.yspeed<-1*speedLimit)
        {
            myBall.yspeed*=slowMultiple ;
        }

        myBall.x += myBall.xspeed;
        myBall.y+= myBall.yspeed;
        myBall.x=(myBall.x>screenDimensions.width+myBall.ballWidth)? myBall.x  % screenDimensions.width-myBall.ballWidth: myBall.x ;
        myBall.y=(myBall.y>screenDimensions.height+myBall.ballHeight)? myBall.y % screenDimensions.height -myBall.ballHeight: myBall.y;
//        y= y% screenDimensions.height;
        myBall.x=(myBall.x<-myBall.ballWidth)? myBall.x+ screenDimensions.width +myBall.ballWidth : myBall.x;
        myBall.y=(myBall.y<-myBall.ballHeight)? myBall.y+ screenDimensions.height +myBall.ballWidth: myBall.y;

        updateLocation();

    }



}
