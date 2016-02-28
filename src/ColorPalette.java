import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class ColorPalette extends JPanel implements Observer {

    private Model model;
    JTextField SatTextField;
    JTextField BriTextField;
    JTextField HueTextField;
    JTextField RedTextField;
    JTextField GreTextField;
    JTextField BluTextField;
    JButton colorChooser;
    JLabel outputColor;
    LineThicknessDisplay lineDisplay;
    JLabel emptyLabel;
    JSpinner chooseThickness;

    public ColorPalette (Model model_){
        model = model_;

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //this.setMinimumSize(new Dimension(150, 200));

        JLabel HueLabel = new JLabel("Hue(0.0-1.0)", SwingConstants.RIGHT);
        JLabel SatLabel = new JLabel("Sat(0.0-1.0)", SwingConstants.RIGHT);
        JLabel BriLabel = new JLabel("Bri(0.0-1.0)", SwingConstants.RIGHT);
        JLabel RedLabel = new JLabel("Red(0-255)", SwingConstants.RIGHT);
        JLabel GreLabel = new JLabel("Gre(0-255)", SwingConstants.RIGHT);
        JLabel BluLabel = new JLabel("Blu(0-255)", SwingConstants.RIGHT);
        JLabel LineThickness = new JLabel("Line thickness", SwingConstants.RIGHT);
        JLabel emptyLabel = new JLabel();

        Integer[] thicknessList = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SpinnerListModel thicknessOptions = new SpinnerListModel(thicknessList);
        chooseThickness = new JSpinner(thicknessOptions);
        lineDisplay = new LineThicknessDisplay(
                (int)chooseThickness.getModel().getValue(),
                model.getSelectedColor()
        );

        colorChooser = new JButton("More colors");
        HueTextField = new JTextField();
        SatTextField = new JTextField();
        BriTextField = new JTextField();
        RedTextField = new JTextField();
        GreTextField = new JTextField();
        BluTextField = new JTextField();

        registerControllers();
        
//        JLabel outputColorLabel = new JLabel("Color Selected", SwingConstants.RIGHT);
//        outputColor = new JLabel();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        this.add(LineThickness, c);
        c.gridy = 1;
        this.add(HueLabel, c);
        c.gridy = 2;
        this.add(SatLabel, c);
        c.gridy = 3;
        this.add(BriLabel, c);
        c.gridy = 4;
        this.add(RedLabel, c);
        c.gridy = 5;
        this.add(GreLabel, c);
        c.gridy = 6;
        this.add(BluLabel, c);
        c.gridy = 7;
        this.add(colorChooser, c);

        c.gridx = 1;
        c.gridy = 0;
        this.add(chooseThickness, c);
        c.gridy = 1;
        this.add(HueTextField, c);
        c.gridy = 2;
        this.add(SatTextField, c);
        c.gridy = 3;
        this.add(BriTextField, c);
        c.gridy = 4;
        this.add(RedTextField, c);
        c.gridy = 5;
        this.add(GreTextField, c);
        c.gridy = 6;
        this.add(BluTextField, c);
        c.gridy = 7;
        this.add(lineDisplay, c);

//        this.add(emptyLabel);
//        this.add(outputColorLabel);
//        this.add(outputColor);
    }

    private void registerControllers(){
        HueTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Hue Controller was run");
                Color currentColor = model.getSelectedColor();
                float parsedColor;
                try {
                    parsedColor = Float.parseFloat(HueTextField.getText());
                }
                catch(Exception e){
                    System.out.println("Error trying to parse hue : ");
                    parsedColor = 0.0f;
                }
                float currentHSB[] = Color.RGBtoHSB( currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
                model.setSelectedColor(Color.getHSBColor(parsedColor, currentHSB[1], currentHSB[2]));
            }
        });

        SatTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color currentColor = model.getSelectedColor();
                float currentHSB[] = Color.RGBtoHSB( currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
                model.setSelectedColor(Color.getHSBColor(currentHSB[0], parseToNormalized(SatTextField.getText()), currentHSB[2]));
            }
        });

        BriTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color currentColor = model.getSelectedColor();
                float currentHSB[] = Color.RGBtoHSB( currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
                model.setSelectedColor(Color.getHSBColor(currentHSB[0], currentHSB[1], parseToNormalized(BriTextField.getText())));
            }
        });

        RedTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color currentColor = model.getSelectedColor();
                model.setSelectedColor(new Color(parseToInt255(RedTextField.getText()), currentColor.getGreen(), currentColor.getBlue()));
            }
        });

        GreTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color currentColor = model.getSelectedColor();
                model.setSelectedColor(new Color( currentColor.getRed(), parseToInt255(GreTextField.getText()), currentColor.getBlue()));
            }
        });

        BluTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color currentColor = model.getSelectedColor();
                model.setSelectedColor(new Color(currentColor.getRed(), currentColor.getGreen(), parseToInt255(BluTextField.getText())));
            }
        });

        colorChooser.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                Color newColor = JColorChooser.showDialog(
                        (JButton)evt.getSource(),
                        "Choose Color",
                        model.getSelectedColor());
                model.setSelectedColor(newColor);
            }
        });

        chooseThickness.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent evt) {
                SpinnerModel thicknessModel = chooseThickness.getModel();
                model.setSelectedThickness( (int)thicknessModel.getValue() );
            }
        });
    }

    private int parseToInt255(String input){
        int returnedInt;
        try{
            returnedInt = Integer.parseInt(input);
        }
        catch(Exception e){
            System.out.println("Error from parsing input -->" + e);
            returnedInt = 0;
        }
        if (returnedInt < 0)
            return 0;
        if (returnedInt > 255)
            return 255;
        return returnedInt;
    }

    private float parseToNormalized(String input){
        float returnedFloat;
        try{
            returnedFloat = Float.parseFloat(input);
        }
        catch(Exception e){
            System.out.println("Error from parsing input -->" + e);
            return 0.0f;
        }
        if (returnedFloat < 0.0f)
            return 0;
        if (returnedFloat > 1.0f)
            return 1.0f;
        return returnedFloat;
    }

    @Override
    public void update(Observable o, Object arg) {

        Color currentRGB = model.getSelectedColor();
        colorChooser.setEnabled(model.getInputEnabled());

        lineDisplay.updateParams((int)chooseThickness.getModel().getValue(), currentRGB);
        lineDisplay.repaint();
        float currentHSB[] = Color.RGBtoHSB( currentRGB.getRed(), currentRGB.getGreen(), currentRGB.getBlue(), null);

        HueTextField.setText(Float.toString(currentHSB[0]));
        SatTextField.setText(Float.toString(currentHSB[1]));
        BriTextField.setText(Float.toString(currentHSB[2]));
        RedTextField.setText(Integer.toString(currentRGB.getRed()));
        GreTextField.setText(Integer.toString(currentRGB.getGreen()));
        BluTextField.setText(Integer.toString(currentRGB.getBlue()));

//        outputColor.setOpaque(true);
//        outputColor.setBackground(currentRGB);

    }
}
