package translucentshapes;

import javax.swing.UIManager;

/**
 *
 * @author Anthony Petrov
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlFrame().setVisible(true);
            }
        });
    }

}
