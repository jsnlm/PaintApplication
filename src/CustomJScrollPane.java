import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CustomJScrollPane extends JScrollPane implements Observer {

    Model model;
    public CustomJScrollPane(Component c, Model model_){
        super(c);
        model = model_;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (model.getIsCanvasFit()){
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
        else{
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        validate();
        repaint();
    }
}
