import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class main{

    public static void main(String[] args){
        JFrame frame = new JFrame("HelloMVC4");

        // create Model and initialize it
        Model model = new Model();

        View view = new View(model);
        MenuBarView menuBar = new MenuBarView(model);
        ColorPalette colorPanel = new ColorPalette(model);
        PlaybackView playbackPanel = new PlaybackView(model);

        DrawingCanvasView drawingPart = new DrawingCanvasView(model);
        CustomJScrollPane scrollingPane = new CustomJScrollPane(drawingPart, model);
//        JPanel scrollPanePanel = new JPanel();
//        scrollPanePanel.setLayout(new BorderLayout());
//        scrollPanePanel.setPreferredSize(new Dimension(90, 90));
//        scrollPanePanel.setMaximumSize(new Dimension(91, 91));
//        scrollPanePanel.setMinimumSize(new Dimension(89, 89));
//        scrollPanePanel.add(scrollingPane, BorderLayout.CENTER);

        model.addObserver(view);
        model.addObserver(menuBar);
        model.addObserver(colorPanel);
        model.addObserver(playbackPanel);
        model.addObserver(scrollingPane);
        model.addObserver(drawingPart);
        model.specifyCanvas(drawingPart);

        model.notifyObservers();

        // create the window

        frame.setJMenuBar(menuBar);

//        JPanel p = new JPanel(new GridBagLayout());

//        p.add(view, BorderLayout.CENTER);
//        p.add(drawingPart, BorderLayout.CENTER);
//        p.add(playbackPanel, BorderLayout.PAGE_END);
//        p.add(colorPanel, BorderLayout.LINE_START);

//        GridBagConstraints c = new GridBagConstraints();
//        c.fill = GridBagConstraints.BOTH;
//
//        c.fill = GridBagConstraints.BOTH ;
//        c.gridx = 1;
//        c.gridy = 0;
//        //c.weightx = 4;
//        //c.weighty = 1;
//        //p.add(drawingPart, c);
//
//        c.weightx = 1;
//        c.weighty = 0;
//        p.add(scrollPanePanel, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 0;
//        c.gridy = 0;
////        c.weightx = 1;
////        c.weighty = 1;
//        p.add(colorPanel, c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridwidth = 3;
//        c.weightx = 1.0;
//        p.add(playbackPanel, c);

        JPanel allContent = new JPanel(new GridBagLayout());
        JPanel drawingControls = new JPanel(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        GridBagConstraints b = new GridBagConstraints();

        allContent.setBackground(new Color(255, 0, 0));
        drawingControls.setBackground(new Color(0, 0, 255));

        b.fill = GridBagConstraints.VERTICAL;
        b.weightx = 0;
        b.weighty = 1;
        drawingControls.add(colorPanel, b);

        b.fill = GridBagConstraints.BOTH;
        b.weightx = 1;
        b.weighty = 1;
        drawingControls.add(scrollingPane, b);

//        scrollingPane.setPreferredSize(new Dimension(200, 200));
//        scrollingPane.setMaximumSize(new Dimension(200, 200));
//        scrollingPane.setMinimumSize(new Dimension(200, 200));
        frame.getContentPane().add(allContent);

        a.fill = GridBagConstraints.BOTH;
        a.gridy = 0;
        a.weightx = 1;
        a.weighty = 1;
        allContent.add(drawingControls, a);
        a.fill = GridBagConstraints.BOTH;
        a.gridy = 1;
        a.weightx = 1;
        a.weighty = 0;
        allContent.add(playbackPanel, a);

        frame.setMinimumSize(new Dimension(400, 350) );
        frame.setPreferredSize(new Dimension(600,300));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
