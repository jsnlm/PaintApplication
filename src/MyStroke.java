import java.awt.*;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MyStroke implements Serializable {
    public int strokeWidth;
    public Color strokeColor;
    public ArrayList<Point2D> pointsList;

    public MyStroke(){
        pointsList = new ArrayList();
        strokeWidth = 1;
        strokeColor = Color.BLACK;
    }

    public MyStroke(int width, Color color){
        pointsList = new ArrayList();
        strokeWidth = width;
        strokeColor = color;
    }

    public void addToStroke(Point2D startPoint) {
        pointsList.add(startPoint);
    }

    public MyStroke clone(){
        MyStroke clonedStroke = new MyStroke(this.strokeWidth, this.strokeColor);
        clonedStroke.pointsList = (ArrayList<Point2D>)this.pointsList.clone();
        return clonedStroke;
    }

    public void loadFileText(PrintWriter pw){
        pw.println(strokeWidth);
        pw.println(strokeColor.getRGB());
        pw.println(pointsList.size());
        for ( int i = 0; i < pointsList.size(); i++){
            pw.println(pointsList.get(i).getX());
            pw.println(pointsList.get(i).getY());
        }
    }

    public void saveFileText(PrintWriter pw){
        pw.println(strokeWidth);
        pw.println(strokeColor.getRGB());
        pw.println(pointsList.size());
        for ( int i = 0; i < pointsList.size(); i++){
            pw.println(pointsList.get(i).getX());
            pw.println(pointsList.get(i).getY());
        }
    }

    public static MyStroke loadFileText(Scanner sc){
        String s1 = sc.next();
        String s2 = sc.next();
        MyStroke returnedStroke = new MyStroke(
                (int)Integer.parseInt(s1),
                new Color(Integer.parseInt(s2))
        );
        int numElements = Integer.parseInt(sc.next());
        for ( int i = 0; i < numElements; i++){
            s1 = sc.next();
            s2 = sc.next();
                returnedStroke.addToStroke(new Point2D.Double(
                    Float.parseFloat(s1),
                    Float.parseFloat(s2)
            ));
        }
        return returnedStroke;
    }
}
