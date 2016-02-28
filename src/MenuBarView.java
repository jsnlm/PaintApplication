import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;


import java.awt.datatransfer.*;
import java.awt.image.*;

public class MenuBarView extends JMenuBar implements Observer {

    private Model model;
    JMenuItem save, open, takeScreenShot;

    JRadioButtonMenuItem fit, oneToOne;
    ButtonGroup group;
    FileFilter serFilter;
    FileFilter txtFilter;

    public MenuBarView (Model model_){
        model = model_;
        serFilter = new FileNameExtensionFilter("Serialized","ser");
        txtFilter = new FileNameExtensionFilter("Text","txt");

        JMenu fileMenu = new JMenu("File");
        save = new JMenuItem("Save File");
        open = new JMenuItem("Open File");
        takeScreenShot = new JMenuItem("Save To Clipboard");

        JMenu viewMenu = new JMenu("View");
        fit = new JRadioButtonMenuItem("Fit");
        oneToOne = new JRadioButtonMenuItem("1:1");

        this.add(fileMenu);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(takeScreenShot);

        this.add(viewMenu);
        viewMenu.add(fit);
        viewMenu.add(oneToOne);

        group = new ButtonGroup();
        group.add(fit);
        group.add(oneToOne);

        if (model.getIsCanvasFit())
            fit.setSelected(true);
        else
            oneToOne.setSelected(true);

        open.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter() );
                fileChooser.setFileFilter(serFilter);
                fileChooser.addChoosableFileFilter(txtFilter);
                int someVal = fileChooser.showDialog((JMenuItem)e.getSource(), "Choose file to Open");

                if (someVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("Filename : " + fileChooser.getSelectedFile().getName());
                    System.out.println("Path : " + fileChooser.getCurrentDirectory().toString());

                    String fileName = fileChooser.getSelectedFile().getName();
                    String filePath = fileChooser.getCurrentDirectory().toString().replace("\\", "/");
                    String fullFilePath = filePath + "/" + fileName;
                    System.out.println("fullFilePath : " + fullFilePath);


                    if (fileChooser.getSelectedFile().getName().endsWith(".txt")) {
                        try {
//                            XMLDecoder decoder =
//                                    new XMLDecoder(new BufferedInputStream(
//                                            new FileInputStream(fullFilePath)));
//                            Model loadedModel = (Model) decoder.readObject();
//                            decoder.close();
//                            model.loadFromModel(loadedModel, true);

                            Model loadedModel = Model.readFromTXT(fullFilePath);
                            model.loadFromModel(loadedModel, false);

                        } catch (Exception i) {
                            i.printStackTrace();
                        }

                    }
                    else if (fileChooser.getSelectedFile().getName().endsWith(".ser")){
                        try
                        {
                            FileInputStream fileIn = new FileInputStream(fullFilePath);
                            ObjectInputStream in = new ObjectInputStream(fileIn);
                            Model loadedModel = (Model) in.readObject();
                            in.close();
                            fileIn.close();
                            model.loadFromModel(loadedModel, false);
                        }catch(IOException i)
                        {
                            i.printStackTrace();
                            return;
                        }catch(ClassNotFoundException i)
                        {
                            System.out.println("Employee class not found");
                            i.printStackTrace();
                            return;
                        }
                    }
                    else{
                        System.out.println("File extension wasn't .txt or .ser");
                        JOptionPane.showMessageDialog(null,
                                "Loaded file must end with .txt or .ser  . \n File was not opened",
                                "Load Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (someVal == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No file was selected");
                }
            }
        });

        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter() );
                fileChooser.setFileFilter(serFilter);
                fileChooser.addChoosableFileFilter(txtFilter);

                int someVal = fileChooser.showDialog((JMenuItem)e.getSource(), "Choose save file");

                if (someVal == JFileChooser.APPROVE_OPTION) {

                    System.out.println("Filename : " + fileChooser.getSelectedFile().getName());
                    System.out.println("Path : " + fileChooser.getCurrentDirectory().toString());

                    String fileName = fileChooser.getSelectedFile().getName();
                    String filePath = fileChooser.getCurrentDirectory().toString().replace("\\", "/");
                    String fullFilePath = filePath + "/" + fileName;
                    System.out.println("fullFilePath : " + fullFilePath);

                    if (fileChooser.getSelectedFile().getName().endsWith(".txt")){
                        //model.writeToXML("D:/Jason/toBeDeleted/toBeDeleted.txt");
                        model.writeToXML(fullFilePath);
                    }
                    else if (fileChooser.getSelectedFile().getName().endsWith(".ser")){
                        System.out.println("File extension was .ser");
                        try {
                            FileOutputStream fileOut = new FileOutputStream(fullFilePath);
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(model);
                            System.out.printf("Serialized data is saved in " + fullFilePath);
                            out.close();
                            fileOut.close();
                        } catch(IOException i) {
                            i.printStackTrace();
                        }
                        catch(Exception err){
                            System.out.println(err);
                        }
                    }
                    else{
                        System.out.println("File extension wasn't .txt or .ser");
                        JOptionPane.showMessageDialog(null,
                                "Saved file must end with .txt or .ser  . \n File was not saved",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE);
                    }


                }
                if (someVal == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No file was selected");
                }
            }
        });

        takeScreenShot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try{
//                    JFileChooser fileChooser = new JFileChooser();
//                    fileChooser.setCurrentDirectory(new java.io.File("."));
//                    fileChooser.setDialogTitle("Choose directory to save Screen Shot");
//                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    fileChooser.setAcceptAllFileFilterUsed(false);
//
//                    int someVal = fileChooser.showDialog((JMenuItem)e.getSource(), "Choose save folder");
//
//                    System.out.println(fileChooser.getCurrentDirectory().toString());
//                    System.out.println(fileChooser.getSelectedFile().getName());
//                    String filePath = fileChooser.getCurrentDirectory().toString().replace("\\", "/") + "/" +
//                            fileChooser.getSelectedFile().getName() +
//                            "/Screen_Shot.jpeg" ;
//                    File imageFile = new File(filePath);


                    BufferedImage screenShot = model.getScreenShot();



                    TransferableImage trans = new TransferableImage( screenShot );
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

                    SomeClipboardOwner someClipboardOwner = new SomeClipboardOwner();
                    c.setContents(trans, someClipboardOwner);
//
//                    imageFile.createNewFile();
//                    ImageIO.write(screenShot, "jpeg", imageFile);
                }catch(Exception ex){
                    System.out.println(ex);
                }
            }
        });

        fit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("fit was selected");
                model.setIsCanvasFit(true);
            }
        });

        oneToOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("oneToOne was selected");
                model.setIsCanvasFit(false);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (model.getInputEnabled()){
            save.setEnabled(true);
            open.setEnabled(true);
        }
        else{
            save.setEnabled(false);
            open.setEnabled(false);
        }
        //System.out.println("model.getIsCanvasFit() : " + model.getIsCanvasFit());
        if (model.getIsCanvasFit())
            fit.setSelected(true);
        else
            oneToOne.setSelected(true);
    }

    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
                throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }
}