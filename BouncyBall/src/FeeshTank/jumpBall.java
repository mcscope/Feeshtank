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
    public jumpBall() {
        super( );
    }


    public void createFeeshFrame(GraphicsConfiguration translucencyCapableGC) {
        myFrame = new jumpBallFrame(translucencyCapableGC,this, feeshSavedDataExists);
    }

}
class jumpBallFrame extends BallFrame
{
    public jumpBallFrame(GraphicsConfiguration gc, Ball parentBall, boolean restoreFromSaved)
    {
        super(gc,parentBall,restoreFromSaved);
    }


    public void step()
    {
        Rectangle screenDimensions = screen.getBounds();
        double slowMultiple=.8;
        double bounceMultiplier = -8;

        double speedLimit=10.0;
        //if going too fast!
        if(myBall.xspeed>speedLimit || myBall.xspeed<-1*speedLimit)
        {
            myBall.xspeed*=slowMultiple;
        }
        if(myBall.yspeed>speedLimit || myBall.yspeed<-1*speedLimit)
        {
            myBall.yspeed*=slowMultiple ;
        }


        if ((0 > myBall.x&& myBall.xspeed<0 )||( screenDimensions.width < myBall.x+ myBall.ballWidth  && myBall.xspeed>0 )) {
            myBall.xspeed *= bounceMultiplier;//reverse
        }

        if  ((0 > myBall.y&& myBall.yspeed<0 )||( screenDimensions.height < myBall.y+ myBall.ballHeight  && myBall.yspeed>0 )) {
            myBall.yspeed *= bounceMultiplier;//reverse
        }


        myBall.x+= myBall.xspeed;
        myBall.y+= myBall.yspeed;

        updateLocation();
    }


}
