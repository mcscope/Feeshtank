package FeeshTank;
/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */


import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Random;


public class Ball extends Feesh {
    public BallFrame myFrame;
    private boolean displaying;

    boolean ballSavedDataExists=false;
    double x;
    double y;
    double xspeed;
    double yspeed;
    int ballWidth;
    int ballHeight;
    Color ballColorTrans, ballColorDark;


    public Ball() {
        super();
        displaying = false;


    }


    public void step() {
        if (displaying) {
            if (myFrame != null) {
                myFrame.step();
           if(!myFrame.isDisplayable())
                stopDisplaying();
            }

        }
    }

    public void die() {
        stopDisplaying();
    }

    public void stopDisplaying() {
        displaying = false;
        if (myFrame != null) {
            myFrame.dispose();
            myFrame=null;
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
        createBall(translucencyCapableGC);
        ballSavedDataExists=true;
        displaying = true;
    }

    public void createBall(GraphicsConfiguration translucencyCapableGC) {

            myFrame = new BallFrame(translucencyCapableGC,this,ballSavedDataExists);

    }

    boolean isDisplaying() {
        return displaying;
    }

}

class BallFrame extends javax.swing.JFrame implements javax.swing.RootPaneContainer {
    //global vars

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;

    GraphicsConfiguration screen;
    private final Random random = new Random();
    private static final double RANGE = 10;
    Ball myBall;

    public BallFrame(GraphicsConfiguration gc, Ball parentBall, boolean restoreFromSaved) {
        super(gc);
        screen = gc;
        myBall=parentBall;
        initComponents();
        jPanel1.setOpaque(false);

        if(!restoreFromSaved)
        {
        myBall.xspeed = random.nextDouble() * (RANGE) - (RANGE / 2);
        myBall.yspeed = random.nextDouble() * (RANGE) - (RANGE / 2);
        setColor();
        myBall.x= screen.getBounds().getWidth() / 2;
        myBall.y= screen.getBounds().getHeight() / 2;
        }
        initWindow();
        }

    private void initWindow() {
        this.setVisible(true);

        AWTUtilitiesWrapper.setWindowOpaque(this, false);

        AWTUtilitiesWrapper.setWindowOpacity(this, .80f);
        Shape shape = new Ellipse2D.Float(0, 0, this.getWidth(), this.getHeight());
        AWTUtilitiesWrapper.setWindowShape(this, shape);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setTitle("Feesh");
        //HACK to change ClassName to make Feesh stack in windows and be handled by xmonad
     try{
        Toolkit xToolkit = Toolkit.getDefaultToolkit();
        java.lang.reflect.Field awtAppClassNameField =
                xToolkit.getClass().getDeclaredField("awtAppClassName");
        awtAppClassNameField.setAccessible(true);
        awtAppClassNameField.set(xToolkit, "Feesh");
     }
     catch (Exception e)
     {}

        MouseInputAdapter ml = new MouseInputAdapter() {
            public void mouseDragged(MouseEvent event) {
                myBall.xspeed = event.getX() - myBall.ballHeight / 2;
                myBall.yspeed = event.getY() - myBall.ballWidth / 2;

            }

            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON3) {

                    dispose();
                }


            }

        };
        this.addMouseMotionListener(ml);
        jPanel1.addMouseMotionListener(ml);
        jPanel2.addMouseMotionListener(ml);

        this.addMouseListener(ml);
        jPanel1.addMouseListener(ml);
        jPanel2.addMouseListener(ml);

    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myBall.ballHeight = 100;
        myBall.ballWidth = 100;

        jPanel1 = new javax.swing.JPanel() {
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D && true) {

                    Paint p =
                            new GradientPaint(0.0f, 0.0f, myBall.ballColorTrans,
                                    getWidth(), getHeight(), myBall.ballColorDark, true);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(p);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    super.paintComponent(g);
                }
            }
        };

        jPanel2 = new javax.swing.JPanel();


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(myBall.ballHeight, myBall.ballWidth));
        setUndecorated(true);

        jPanel1.setDoubleBuffered(false);
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());


        jPanel2.setDoubleBuffered(false);
        jPanel2.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, myBall.ballWidth, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, myBall.ballHeight, Short.MAX_VALUE)
        );


        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public void setColor() {

        int R = random.nextInt(155) + 100;
        int G = random.nextInt(200) + 55;
        int B = random.nextInt(200) + 55;
        myBall.ballColorTrans = new Color(R, G, B, 0);
        myBall.ballColorDark = new Color(R, G, B, 255);
    }

    public void step() {
        Rectangle screenDimensions = screen.getBounds();
        Rectangle bounds = getBounds();
        double speedLimit = 15.0;
        double slowMultiple = .9;
        double bounceMultiplier = -1;

        //if going too fast!
        if (myBall.xspeed > speedLimit || myBall.xspeed < -1 * speedLimit) {
            myBall.xspeed *= slowMultiple;
        }
        if (myBall.yspeed > speedLimit || myBall.yspeed < -1 * speedLimit) {
            myBall.yspeed *= slowMultiple;
        }

        //add a little random walk to prevent them getting stuck in the long term
        if (myBall.xspeed < 1.0)
            myBall.xspeed += 0.05 - 0.1 * random.nextDouble();
        if (myBall.yspeed < 1.0)
            myBall.yspeed += 0.05 - 0.1 * random.nextDouble();


        if ((0 > myBall.x&& myBall.xspeed < 0) || (screenDimensions.width < myBall.x+myBall. ballWidth && myBall.xspeed > 0)) {
            myBall.xspeed *= bounceMultiplier;//reverse
        }

        if ((0 > myBall.y&& myBall.yspeed < 0) || (screenDimensions.height < myBall.y+ myBall.ballHeight && myBall.yspeed > 0)) {
            myBall.yspeed *= bounceMultiplier;//reverse
        }


        myBall.x+= myBall.xspeed;
        myBall.y+= myBall.yspeed;
//        x=x % screenDimensions.width;
//        y= y% screenDimensions.height;
        bounds.x= (int) myBall.x;
        bounds.y = (int) myBall.y;

        setBounds(bounds);
    }


}