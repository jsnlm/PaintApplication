// HelloMVC: a simple MVC example
// the model is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;

class View2 extends JPanel implements Observer {

    // the model that this view is showing
    private Model model;

    View2(Model model_) {
        // create UI
        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // set the model
        model = model_;

        // setup the event to go to the "controller"
        // (this anonymous class is essentially the controller)
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                model.incrementCounter();
            }
        });
    }

    // Observer interface
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("View2: updateView");
        // just displays an 'X' for each counter value
        if (model.getCounterValue() > 0)
            this.add(new JLabel("X"));
    }
}
