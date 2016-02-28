import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public class PlaybackView extends JPanel implements Observer {

    private Model model;
    JButton playButton, startButton, endButton;
    JSlider strokeSlider;
    private boolean enableSliderStateChange;

    public PlaybackView(Model model_) {
        model = model_;
        enableSliderStateChange = false;

        //this.setLayout(new FlowLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        playButton = new JButton("Play", new ImageIcon(getClass().getResource("play.png")));
        startButton = new JButton("Start", new ImageIcon(getClass().getResource("fastReverse.png")));
        endButton = new JButton("End", new ImageIcon(getClass().getResource("fastForeward.png")));

        playButton.setPreferredSize(new Dimension(100, 30));
        playButton.setPreferredSize(new Dimension(100, 30));
        playButton.setPreferredSize(new Dimension(100, 30));

        strokeSlider = new JSlider();
        strokeSlider.setMajorTickSpacing(1);
        strokeSlider.setPaintTicks(true);
        strokeSlider.setPaintLabels(true);


        registerControllers();

        playButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(playButton);
//        this.add(Box.createHorizontalGlue());
        this.add(strokeSlider);
//        this.add(Box.createHorizontalGlue());
        this.add(startButton);
        this.add(endButton);
    }

    @Override
    public void update(Observable o, Object arg) {

        if (model.getInputEnabled()){
            playButton.setEnabled(true);
            startButton.setEnabled(true);
            endButton.setEnabled(true);
            strokeSlider.setEnabled(true);
        }
        else{
            playButton.setEnabled(false);
            startButton.setEnabled(false);
            endButton.setEnabled(false);
            strokeSlider.setEnabled(false);
            repaint();
        }

        int totalTicks = model.getNumDrawnStrokes() + model.getNumUndidStrokes();
        if (totalTicks == 0){
            strokeSlider.setVisible(false);
        }
        else{
            strokeSlider.setVisible(true);
            //System.out.println("totalTicks : " + totalTicks);
            //System.out.println("model.getNumDrawnStrokes() : " + model.getNumDrawnStrokes());

//            strokeSlider = new JSlider(0, totalTicks, model.getNumDrawnStrokes());
//            strokeSlider.setMinimum(0);
            enableSliderStateChange = false;
            strokeSlider.setMaximum(totalTicks);
            enableSliderStateChange = true;
            strokeSlider.setValue(model.getNumDrawnStrokes());
        }

        //System.out.println("playbackView : " + this.getWidth());
    }


    private void registerControllers() {
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                model.redoAnimateAllStrokes();
            }
        });
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                model.undoAllStrokes();
            }
        });
        endButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                model.redoAllStrokes();
            }
        });
            strokeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!enableSliderStateChange) {
                    return;
                }
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int sliderVal = (int)source.getValue();
//                    System.out.println("sliderVal : " + sliderVal);
//                    System.out.println("model.getNumDrawnStrokes() : " + model.getNumDrawnStrokes());
                    int difference = sliderVal - model.getNumDrawnStrokes();
                    if ( difference < 0 ) {
                        for (int i = 0; i < (-1)*difference; i++) {
                            model.undoOne();
                        }
                    }
                    else if ( difference > 0 ) {
                        for (int i = 0; i < difference; i++) {
                            model.redoOne();
                        }
                    }
                }
            }
        });
    }
}
