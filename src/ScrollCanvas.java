import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScrollCanvas extends Panel  implements Observer {


    private Model model;
    private int height;
    private int width;
    private DrawingCanvasView dcv;

    public ScrollCanvas(Model model_, int width_, int height_, DrawingCanvasView dcv_){
        super();
        model = model_;
        width = width_;
        height = height_;
        dcv = dcv_;

        this.setLayout(new BorderLayout());
        this.add("West", dcv);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
