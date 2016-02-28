import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Scanner;


public class Model extends Observable implements Serializable{
    // the data in the model, just a counter
    private int counter;

    private ArrayList<MyStroke> allStrokes;
    private ArrayList<MyStroke> undidStrokes;

    private MyStroke currentStroke;
    private Color selectedColor;
    private int selectedThickness;
    private boolean inputEnabled;
    private JLabel drawedCanvas;

    private boolean isCanvasFit;

    public Model() {
        this.selectedColor = Color.BLACK; //The default value
        this.selectedThickness = 1;
        allStrokes = new ArrayList<MyStroke>();
        undidStrokes = new ArrayList<MyStroke>();
        inputEnabled = true;
        isCanvasFit = true;
        setChanged();
    }

    public boolean getIsCanvasFit() { return isCanvasFit; }
    public void setIsCanvasFit(boolean x) {
        this.isCanvasFit = x;
        setChanged();
        notifyObservers();
    }

    public boolean getInputEnabled() { return inputEnabled; }
    public Color getSelectedColor() { return selectedColor; }
    public void setSelectedColor(Color newColor) {
        selectedColor =  newColor;
        setChanged();
        notifyObservers();
    }

    public int getSelectedThickness() { return selectedThickness; }
    public void setSelectedThickness(int newThickness) {
        selectedThickness = newThickness;
        setChanged();
        notifyObservers();
    }

    public void startStroke(Point2D startPoint) {
        currentStroke = new MyStroke(selectedThickness, selectedColor);
        currentStroke.addToStroke(startPoint);
        undidStrokes.clear();
        setChanged();
        notifyObservers();
    }

    public void addToCurrentStroke(Point2D point) {
        if (currentStroke == null){
            return;
        }
        currentStroke.addToStroke(point);
        setChanged();
        notifyObservers();
    }

    public void endStroke(Point2D endPoint) {
        if (currentStroke != null) {
            currentStroke.addToStroke(endPoint);
            MyStroke airjgit = currentStroke.clone();
            allStrokes.add(airjgit);
            currentStroke = null;
        }
        setChanged();
        notifyObservers();
    }

    public int getNumDrawnStrokes(){ return allStrokes.size(); }
    public int getNumUndidStrokes(){ return undidStrokes.size(); }

    public boolean undoOne(){
        int numElements = allStrokes.size();
        if (numElements == 0){
            return false;
        }
        MyStroke removedStroke = allStrokes.get(numElements-1);
        allStrokes.remove(numElements-1);
        undidStrokes.add(removedStroke);
        setChanged();
        notifyObservers();
        return true;
    }

//    public boolean undoOne(){
//        int numElements = allStrokes.size();
//        if (numElements == 0){
//            return false;
//        }
//        this.inputEnabled = false;
//        MyStroke undidStroke = allStrokes.get(numElements-1).clone();
//
//        MyStroke removedStroke = allStrokes.get(numElements-1);
//        for (int i = 0; i < undidStroke.pointsList.size(); i++){
//            if (removedStroke.pointsList.size() > 0){
//                removedStroke.pointsList.remove(removedStroke.pointsList.size()-1);
//            }
//
//            setChanged();
//            notifyObservers();
//
//            try {
//                Thread.sleep(50);
//            } catch(InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        allStrokes.remove(numElements-1);
//        undidStrokes.add(undidStroke);
//        this.inputEnabled = true;
//        setChanged();
//        notifyObservers();
//        return true;
//    }

    public boolean redoOne(){
        int numElements = undidStrokes.size();
        if (numElements == 0){
            return false;
        }
        MyStroke removedStroke = undidStrokes.get(numElements-1);
        undidStrokes.remove(numElements-1);
        allStrokes.add(removedStroke);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean redoAnimateOne(){
        int numElements = undidStrokes.size();
        if (numElements == 0){
            return false;
        }
        this.inputEnabled = false;
        MyStroke repaintedStroke = undidStrokes.get(numElements-1);
        undidStrokes.remove(numElements-1);

        MyStroke partialStroke = new MyStroke();
        partialStroke.strokeColor = repaintedStroke.strokeColor;
        partialStroke.strokeWidth = repaintedStroke.strokeWidth;
        allStrokes.add(partialStroke);
        for (int i = 0; i < repaintedStroke.pointsList.size(); i++){
            partialStroke.addToStroke(repaintedStroke.pointsList.get(i));

            setChanged();
            notifyObservers();

            try {
                Thread.sleep(50);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        //allStrokes.add(repaintedStroke);
        this.inputEnabled = true;
        setChanged();
        notifyObservers();
        return true;
    }

    public void undoAllStrokes(){
        while(undoOne()){}
//        Collections.reverse(allStrokes);
//        undidStrokes.addAll(allStrokes);
//        allStrokes.clear();
        setChanged();
        notifyObservers();
    }

    public void redoAllStrokes(){
        Collections.reverse(undidStrokes);
        allStrokes.addAll(undidStrokes);
        undidStrokes.clear();
        setChanged();
        notifyObservers();
    }

    public void redoAnimateAllStrokes(){
        while(redoAnimateOne()){}
        setChanged();
        notifyObservers();
    }

    public ArrayList<MyStroke> getAllStrokes(){ return allStrokes; }
    public MyStroke getCurrentStroke(){ return currentStroke; }

    public void loadFromModel(Model loadedModel, boolean isXML){

        if (isXML){
            fixXMLArrayLists(loadedModel.allStrokes);
            fixXMLArrayLists(loadedModel.undidStrokes);
            for (MyStroke someStroke : loadedModel.allStrokes  ){
                fixXMLArrayLists(someStroke.pointsList);
            }
            for (MyStroke someStroke : loadedModel.undidStrokes){
                fixXMLArrayLists(someStroke.pointsList);
            }
        }

        this.allStrokes = loadedModel.allStrokes;
        this.undidStrokes = loadedModel.undidStrokes;
        this.currentStroke = loadedModel.currentStroke;
        this.selectedColor = loadedModel.selectedColor;
        this.selectedThickness = loadedModel.selectedThickness;
        setChanged();
        notifyObservers();
    }

    private <someType> void fixXMLArrayLists(ArrayList<someType> input){
        if (input.size() < 2)
            return;

        someType temp = input.get(input.size()-1);
        input.remove(input.size()-1);
        input.add(0, temp);
    }

    public void writeToXML(String fullFilePath){
//        try{
//            System.out.println("File extension was .txt");
//            XMLEncoder encoder =
//                    new XMLEncoder(
//                            new BufferedOutputStream(
//                                    new FileOutputStream( fullFilePath )));
//            encoder.writeObject(m);
//            encoder.close();
//        }
//        catch(Exception err) {
//            System.out.println(err);
//        }
        try {
            PrintWriter writer = new PrintWriter(fullFilePath, "UTF-8");

            writer.println(counter); // int
//        writer.println("The first line"); // MyStroke
            writer.println(selectedColor.getRGB());// Color
            writer.println(selectedThickness); // int
            writer.println(inputEnabled); // boolean

            writer.println(allStrokes.size());
            for (int i = 0; i < allStrokes.size(); i++) {
                allStrokes.get(i).saveFileText(writer);
            }

            writer.println(undidStrokes.size());
            for (int i = 0; i < undidStrokes.size(); i++) {
                undidStrokes.get(i).saveFileText(writer);
            }
            writer.close();
        }
        catch(Exception err) {
            System.out.println(err);
        }
    }

    public static Model readFromTXT(String fullFilePath){
        try {
            Model returnedModel = new Model();
            Scanner sc = new Scanner(new File(fullFilePath), "UTF-8" );

            returnedModel.counter = Integer.parseInt(sc.next());
            returnedModel.selectedColor = new Color(Integer.parseInt(sc.next()));
            returnedModel.selectedThickness = Integer.parseInt(sc.next());
            returnedModel.inputEnabled = Boolean.parseBoolean(sc.next());
            int numAllStrokes = Integer.parseInt(sc.next());
            for (int i = 0; i < numAllStrokes; i++){
                returnedModel.allStrokes.add(MyStroke.loadFileText(sc));
            }
            int numUndidStrokes = Integer.parseInt(sc.next());
            for (int i = 0; i < numUndidStrokes; i++){
                returnedModel.undidStrokes.add(MyStroke.loadFileText(sc));
            }
            return returnedModel;
        } catch (Exception x) {
            System.out.println(x);
        }
        return null;
    }

    public void specifyCanvas(JLabel c){
        this.drawedCanvas = c;
    }

    public BufferedImage getScreenShot()throws AWTException{
        //BufferedImage bufImage = new BufferedImage(drawedCanvas.getSize().width, drawedCanvas.getSize().height,BufferedImage.TYPE_INT_RGB);

        Rectangle bounds = drawedCanvas.getBounds();
        bounds.x = drawedCanvas.getLocationOnScreen().x;
        bounds.y = drawedCanvas.getLocationOnScreen().y;
//        System.out.println("bounds.x : " + bounds.x);
//        System.out.println("bounds.y : " + bounds.y);
//        System.out.println("height : " + bounds.height);
//        System.out.println("width : " + bounds.width);
//        System.out.println("drawedCanvas.getLocationOnScreen() : " + drawedCanvas.getLocationOnScreen());
        //bounds.translate(drawedCanvas.getLocationOnScreen().x, drawedCanvas.getLocationOnScreen().y);
        BufferedImage bufImage = new Robot().createScreenCapture(
                bounds );
        return bufImage;
    }

    public int getCounterValue() {
        return counter;
    }

    public void incrementCounter() {
        if (counter < 5) {
            counter++;
            //System.out.println("Model: increment counter to " + counter);
            setChanged();
            notifyObservers();
        }
    }
}
