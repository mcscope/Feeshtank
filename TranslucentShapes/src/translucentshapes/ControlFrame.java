package translucentshapes;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author  Anthony Petrov
 */
public class ControlFrame extends javax.swing.JFrame {

    private boolean isShapingSupported;
    private boolean isOpacityControlSupported;
    private boolean isTranslucencySupported;
    private GraphicsConfiguration translucencyCapableGC;
    private FancyFrame fancyFrame = null;
    private ComponentListener shapeListener = null;

    /** Creates new form ControlFrame */
    public ControlFrame() {
        initComponents();

        isShapingSupported = AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT);
        isOpacityControlSupported = AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.TRANSLUCENT);
        isTranslucencySupported = AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSLUCENT);

        translucencyCapableGC = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
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
            if (translucencyCapableGC == null) {
                isTranslucencySupported = false;
            }
        }

        if (!isShapingSupported) {
            plainRectangularShapeRadioButton.setSelected(true);
        } else {
            roundedCornersShapeRadioButton.setSelected(true);
        }
        plainRectangularShapeRadioButton.setEnabled(isShapingSupported);
        roundedCornersShapeRadioButton.setEnabled(isShapingSupported);
        ovalShapeRadioButton.setEnabled(isShapingSupported);

        if (!isOpacityControlSupported) {
            opacitySlider.setValue(100);
        } else {
            opacitySlider.setValue(75);
        }
        opacitySlider.setEnabled(isOpacityControlSupported);

        translucencyCheckBox.setSelected(isTranslucencySupported);
        translucencyCheckBox.setEnabled(isTranslucencySupported);
    }

    private synchronized void applyOpacity() {
        if (!isOpacityControlSupported) {
            return;
        }
        if (opacitySlider.getValueIsAdjusting()) {
            return;
        }
        if (fancyFrame == null) {
            return;
        }
        AWTUtilitiesWrapper.setWindowOpacity(fancyFrame, ((float) opacitySlider.getValue()) / 100.0f);
    }

    private synchronized void applyShape() {
        if (!isShapingSupported) {
            return;
        }
        if (fancyFrame == null) {
            return;
        }
        if (shapeListener != null) {
            shapeListener.componentResized(null);
            return;
        }

        final FancyFrame fd = fancyFrame;

        fd.addComponentListener(shapeListener = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent evt) {
                Shape shape = null;

                if (roundedCornersShapeRadioButton.isSelected()) {
                    shape = new RoundRectangle2D.Float(0, 0, fd.getWidth(), fd.getHeight(), 30, 30);
                } else if (ovalShapeRadioButton.isSelected()) {
                    shape = new Ellipse2D.Float(0, 0, fd.getWidth(), fd.getHeight());
                }

                AWTUtilitiesWrapper.setWindowShape(fd, shape);
            }
            });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shapeButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        opacitySlider = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        plainRectangularShapeRadioButton = new javax.swing.JRadioButton();
        roundedCornersShapeRadioButton = new javax.swing.JRadioButton();
        ovalShapeRadioButton = new javax.swing.JRadioButton();
        translucencyCheckBox = new javax.swing.JCheckBox();
        showFancyFrameButton = new javax.swing.JButton();
        closeApplicationButton = new javax.swing.JButton();
        paintGradientCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Translucent and shaped windows demo");
        setLocationByPlatform(true);
        setResizable(false);

        jLabel1.setText("Use controls below to define the appearance of the frame.");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Constant opacity level"));

        opacitySlider.setMajorTickSpacing(10);
        opacitySlider.setMinorTickSpacing(1);
        opacitySlider.setPaintLabels(true);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setSnapToTicks(true);
        opacitySlider.setValue(75);
        opacitySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                opacitySliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opacitySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opacitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Shape of the frame"));

        shapeButtonGroup.add(plainRectangularShapeRadioButton);
        plainRectangularShapeRadioButton.setText("Plain rectangular");
        plainRectangularShapeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                shapeRadioButtonStateChanged(evt);
            }
        });

        shapeButtonGroup.add(roundedCornersShapeRadioButton);
        roundedCornersShapeRadioButton.setSelected(true);
        roundedCornersShapeRadioButton.setText("Rounded corners");
        roundedCornersShapeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                shapeRadioButtonStateChanged(evt);
            }
        });

        shapeButtonGroup.add(ovalShapeRadioButton);
        ovalShapeRadioButton.setText("Oval");
        ovalShapeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                shapeRadioButtonStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(plainRectangularShapeRadioButton)
                    .addComponent(roundedCornersShapeRadioButton)
                    .addComponent(ovalShapeRadioButton))
                .addContainerGap(342, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(plainRectangularShapeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedCornersShapeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ovalShapeRadioButton))
        );

        translucencyCheckBox.setSelected(true);
        translucencyCheckBox.setText("Enable the per-pixel translucency effect");

        showFancyFrameButton.setText("Display the frame");
        showFancyFrameButton.setActionCommand("Display the frame");
        showFancyFrameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFancyFrameButtonActionPerformed(evt);
            }
        });

        closeApplicationButton.setText("Close the application");
        closeApplicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeApplicationButtonActionPerformed(evt);
            }
        });

        paintGradientCheckBox.setSelected(true);
        paintGradientCheckBox.setText("Paint gradient");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(closeApplicationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                        .addComponent(showFancyFrameButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(translucencyCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                        .addComponent(paintGradientCheckBox)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(translucencyCheckBox)
                    .addComponent(paintGradientCheckBox))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeApplicationButton)
                    .addComponent(showFancyFrameButton))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void showFancyFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFancyFrameButtonActionPerformed
        synchronized (this) {
            if (fancyFrame == null) {
                fancyFrame = new FancyFrame(
                        translucencyCheckBox.isSelected() ? translucencyCapableGC : null,
                        paintGradientCheckBox.isSelected());
                if (translucencyCheckBox.isSelected()) {
                    AWTUtilitiesWrapper.setWindowOpaque(fancyFrame, false);
                }

                translucencyCheckBox.setEnabled(false);

                applyOpacity();
                applyShape();

                fancyFrame.setVisible(true);
                showFancyFrameButton.setText("Hide the frame");
            } else {
                fancyFrame.setVisible(false);
                fancyFrame.dispose();
                fancyFrame = null;
                shapeListener = null;
                translucencyCheckBox.setEnabled(isTranslucencySupported);
                showFancyFrameButton.setText("Display the frame");
            }

        }
}//GEN-LAST:event_showFancyFrameButtonActionPerformed

    private void closeApplicationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeApplicationButtonActionPerformed
        setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_closeApplicationButtonActionPerformed

    private void opacitySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_opacitySliderStateChanged
        applyOpacity();
    }//GEN-LAST:event_opacitySliderStateChanged

    private void shapeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_shapeRadioButtonStateChanged
        applyShape();

}//GEN-LAST:event_shapeRadioButtonStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeApplicationButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider opacitySlider;
    private javax.swing.JRadioButton ovalShapeRadioButton;
    private javax.swing.JCheckBox paintGradientCheckBox;
    private javax.swing.JRadioButton plainRectangularShapeRadioButton;
    private javax.swing.JRadioButton roundedCornersShapeRadioButton;
    private javax.swing.ButtonGroup shapeButtonGroup;
    private javax.swing.JButton showFancyFrameButton;
    private javax.swing.JCheckBox translucencyCheckBox;
    // End of variables declaration//GEN-END:variables
}
