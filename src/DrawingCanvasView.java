import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Observable;

public class DrawingCanvasView extends JLabel implements Observer, MouseListener, MouseMotionListener {

    private Model model;

    public DrawingCanvasView(Model model_){

        this.setPreferredSize(new Dimension(100, 100));
        //this.setMinimumSize(new Dimension(100, 200) );
        model = model_;
        setBackground(Color.white);
        addMouseMotionListener(this);
        addMouseListener(this);

        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
//                System.out.println("Component was Resized");
            }
        });
    }

//    @Override
//    public void update(Graphics startg){
//        Graphics2D g2 = (Graphics2D) startg;
//    }

    @Override
    public void update(Observable o, Object arg) {

        if (model.getIsCanvasFit()){
            this.setPreferredSize(new Dimension(100, 100));
            System.out.println("a");
        }
        else{
            this.setPreferredSize(new Dimension(1024, 1024));
            this.setMinimumSize(new Dimension(1024, 1024));
            this.setMaximumSize(new Dimension(1024, 1024));
            this.setSize(new Dimension(1024, 1024));
            System.out.println("b");
        }

//        System.out.println("canvas.getWidth()" + this.getWidth());
        otherPaint(this.getGraphics());
        repaint(1);

//        otherPaint(this.getGraphics());
//        repaint(1);
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        if (model.getInputEnabled())
            model.addToCurrentStroke(e.getPoint());
    }
    public void mouseReleased(MouseEvent e) {
        if (model.getInputEnabled())
            model.endStroke(e.getPoint());
    }
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        if (model.getInputEnabled())
            model.startStroke(e.getPoint());
    }
    public void mouseExited(MouseEvent e) {
        if (model.getInputEnabled())
            model.endStroke(e.getPoint());
    }
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void paint(Graphics g){
        super.paint(g);
        otherPaint(g);
    }

    public void otherPaint(Graphics g){
        if (model.getCurrentStroke() != null) {
            drawStroke(model.getCurrentStroke(), g);
        }
        ArrayList<MyStroke> allStrokes = model.getAllStrokes();
        if (allStrokes != null){
            for (int i = 0; i < allStrokes.size(); i++ ){
                drawStroke(allStrokes.get(i), g);
            }
        }
    }

    public void drawStroke(MyStroke stroke, Graphics g){


        Graphics2D g2 = (Graphics2D)g;
        if (stroke.pointsList.size() == 0)
            return;
        g2.setStroke(new BasicStroke(stroke.strokeWidth));
        g2.setColor(stroke.strokeColor);
        if (stroke.pointsList.size() == 1){
            g2.drawLine(
                    (int)stroke.pointsList.get(0).getX(),
                    (int)stroke.pointsList.get(0).getY(),
                    (int)stroke.pointsList.get(0).getX(),
                    (int)stroke.pointsList.get(0).getY()
            );
            return;
        }
        ArrayList<Point2D> drawnStroke = stroke.pointsList;
        for (int j = 0; j < drawnStroke.size()-1; j++ ){
            g2.drawLine(
                    (int)drawnStroke.get(j).getX(),
                    (int)drawnStroke.get(j).getY(),
                    (int)drawnStroke.get(j+1).getX(),
                    (int)drawnStroke.get(j+1).getY()
            );
        }
    }
}