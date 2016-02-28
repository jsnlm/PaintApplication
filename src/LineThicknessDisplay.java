import javax.swing.*;
import java.awt.*;

public class LineThicknessDisplay extends JLabel {

    int thickness;
    Color lineColor;


    public LineThicknessDisplay(int deafultThickness, Color defaultColor) {
        thickness = deafultThickness;
        lineColor = defaultColor;
    }


    public void updateParams(int thickness, Color newColor) {
        this.thickness = thickness;
        lineColor = newColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = this.getHeight();

        g.setColor(lineColor);
        g.fillRect(0+20,(int)(0.5*height - 0.5*thickness), this.getWidth()-40, thickness);
    }
}
