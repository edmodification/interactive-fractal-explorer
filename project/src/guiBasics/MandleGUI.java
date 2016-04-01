package guiBasics;
 
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
public class MandleGUI extends JFrame {
 
    private Complex userSelectedPoint;
    private MandleBrot mandleBrot;
    private JFrame julia;
 
    private File juliaFavourites;
    
    private int startX, startY, currentX, currentY;
     
     
    private Rectangle rectangle;
     
    private boolean isZoom = false;
 
    private int xPixelNumber = 500;
    private int yPixelNumber = 500;
     
    private double upperXComplex = 2;
    private double lowerXComplex = -2;
    private double upperYComplex = 1.6;
    private double lowerYComplex = -1.6;
 
    private int maxIterate = 100;
 
    private JComboBox<String> favourites;
 
    private BufferedImage ioMandle, ioJulia, ioZoom;
    private Complex constant;
 
    public static void main(String[] args) {
        new MandleGUI();
    }
 
    public MandleGUI() {
 
        /*
         * PANEL FOR FAVOURITES AND RECENT SCROLLBAR
         */
 
        JPanel favouritesAndRecent = new JPanel();
 
        favourites = new JComboBox<String>();
        favourites.addItem("Favourites");
        favourites.setMaximumRowCount(5);
        favourites.setPreferredSize(new Dimension(90, 25));
 
        JButton displayFavourite = new JButton("Display Favourite");
 
        JComboBox<String> recent = new JComboBox<String>();
        recent.addItem("Recent");
        recent.setMaximumRowCount(5);
        recent.setPreferredSize(new Dimension(90, 25));
 
        JButton displayRecent = new JButton("Display Recent");
 
        class comboListener extends MouseAdapter {
            public void mouseClicked(MouseEvent me) {
                try {
                    if (me.getSource() == displayRecent)
                        userSelectedPoint = userSelectedPoint
                                .stringToComplex((String) recent
                                        .getSelectedItem());
                    else
                        userSelectedPoint = userSelectedPoint
                                .stringToComplex((String) favourites
                                        .getSelectedItem());
                    if (julia != null) julia.dispose();
                    new JuliaGUI().repaint();
                } catch (Exception e) {
                }
                ;
            }
        }
 
        displayFavourite.addMouseListener(new comboListener());
        displayRecent.addMouseListener(new comboListener());
 
        favouritesAndRecent.add(recent);
        favouritesAndRecent.add(displayRecent);
        favouritesAndRecent.add(favourites);
        favouritesAndRecent.add(displayFavourite);
 
        /*
         * PANEL FOR DISPLAYING CURRENT POINT, SETTING ITERATION AND ZOOM
         */
 
        JPanel iterateDisplayZoom = new JPanel();
         
        JButton zoomOut = new JButton("Zoom out");
         
        zoomOut.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                mandleBrot.repaint();
            }
        });
 
        JTextField complexDisplay = new JTextField(10);
        complexDisplay.setEditable(false);
 
        JTextField iterateSet = new JTextField(10);
        iterateSet.setEditable(true);
 
        JButton iterateButton = new JButton();
        iterateButton.setText("Set Iterate");
 
        iterateButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent Me) {
                try {
                    maxIterate = Integer.parseInt(iterateSet.getText());
                    mandleBrot.draw(false);
                    repaint();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), "Not a number, please try again");
                }
            }
        });
         
        iterateDisplayZoom.add(zoomOut);
        iterateDisplayZoom.add(iterateSet);
        iterateDisplayZoom.add(iterateButton);
        iterateDisplayZoom.add(complexDisplay);
 
        /*
         * Panel for MANDLEBROT
         */

        mandleBrot = new MandleBrot();
        mandleBrot.draw(false);
        mandleBrot.setVisible(true);
        mandleBrot.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e) {
                userSelectedPoint = new Complex(e.getX() * (4.0 / xPixelNumber)
                        - 2, e.getY() * -1 * (3.2 / (yPixelNumber)) + 1.6);
                complexDisplay.setText(userSelectedPoint.toString());
                recent.insertItemAt(userSelectedPoint.toString(), 1);
                if (julia != null) julia.dispose();
                new JuliaGUI();
                mandleBrot.draw(false);
                mandleBrot.repaint();
            }
        });
 
        /*
         * MAIN PANEL CONNECTING PREVIOUS PANELS
         */
 
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(mandleBrot, BorderLayout.CENTER);
        pane.add(iterateDisplayZoom, BorderLayout.SOUTH);
        pane.add(favouritesAndRecent, BorderLayout.NORTH);
 
        // Creating a folder directory for favourites images
        juliaFavourites = new File("Julia Favourites");
        juliaFavourites.mkdir();
 
        this.add(pane);
        this.setVisible(true);
        this.setTitle("MandleBrot Set");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(xPixelNumber + 5, yPixelNumber + 100);
        this.setResizable(false);       
 
 
    }
 
    private class MandleBrot extends JPanel {
    	
    	public MandleBrot(){
    		 addMouseListener(new MouseAdapter(){
                 public void mousePressed(MouseEvent e){
                     startX = e.getX();
                     startY = e.getY();
                     rectangle = new Rectangle(startX, startY, currentX, currentY);
                 }
              
                 public void mouseReleased(MouseEvent e){
                     currentX = e.getX() - startX;
                     currentY = e.getY() - startY;
                     if(currentX > 0 && currentY > 0){ 
                         rectangle = new Rectangle(startX, startY, currentX, currentY);
                         isZoom=true;
                         repaint();
                     }else return;
                 }   
             });
              
             addMouseMotionListener(new MouseMotionAdapter(){
                 public void mouseDragged(MouseEvent e){
                     currentX = e.getX() - startX;
                     currentY = e.getY() - startY;
                     repaint();
                 }
             });
         }   
    	
     
        public void draw(boolean isJulia) {
            ioMandle = new BufferedImage(xPixelNumber, yPixelNumber,
                    BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < xPixelNumber; x++) {
                double xC = x * ((upperXComplex - lowerXComplex) / xPixelNumber) + lowerXComplex;
                for (int y = 0; y < yPixelNumber; y++) {
                    double yC = -1 * y * ((upperYComplex - lowerYComplex) / yPixelNumber) + upperYComplex;
                    if (!isJulia)
                        constant = new Complex(xC, yC);
                    int counter = zFunction(new Complex(xC, yC));
                    if (counter == maxIterate) {
                        if (!isJulia)
                            ioMandle.setRGB(x, y, 0);
                        else
                            ioJulia.setRGB(x, y, 0);
                    } else {
                        if (!isJulia)
                            ioMandle.setRGB(x, y, counter | (counter << 42));
                        else
                            ioJulia.setRGB(x, y, counter | (counter << 42));
                    }
                }
            }
 
        }
 
        public int zFunction(Complex c) {
            int counter = 0;
            while (c.modulusSquare() <= 4) {
                if (counter == maxIterate) {
                    break;
                }
                c.square();
                c.add(constant);
                counter++;
            }
            return counter;
        }
 
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(ioMandle, 0, 0, this);
            if(rectangle != null){ 
            	g.setColor(Color.blue);
            	g.draw3DRect(startX, startY, currentX, currentY, true);
            	if(isZoom){
            	ListenForDrag s = new ListenForDrag();
            	s.draw();
            	g.drawImage(ioZoom, 0, 0, this);
            	isZoom=false;
            	}
            }
                  
    }
    }
 
    private class JuliaGUI extends JPanel {
 
        public JuliaGUI() {
 
            JButton save = new JButton("Save");
 
            save.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    favourites.insertItemAt(userSelectedPoint.toString(), 1);
                    File savedImage = new File("Julia Favourites/.PNG");
                    try {
                        ImageIO.write(ioJulia, "PNG", savedImage);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
 
            });
 
            constant = userSelectedPoint;
            ioJulia = new BufferedImage(xPixelNumber, yPixelNumber,
                    BufferedImage.TYPE_INT_RGB);
            mandleBrot.draw(true);
 
            JPanel panel = new JPanel(new BorderLayout());
 
            panel.add(this, BorderLayout.CENTER);
            panel.add(save, BorderLayout.SOUTH);
 
            julia = new JFrame("Julia Set");
            julia = new JFrame("julia set");
            julia.add(panel);
            julia.setVisible(true);
            julia.setLocation(xPixelNumber, 0);
            julia.setSize(xPixelNumber, yPixelNumber);
        }
 
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(ioJulia, 0, 0, this);
        }
    }
     
   class ListenForDrag{
   
        public void draw(){
            ioZoom = new BufferedImage(xPixelNumber, yPixelNumber, BufferedImage.TYPE_INT_RGB);
             
            double currentXComplex = currentX * ((upperXComplex - lowerXComplex) / xPixelNumber) + lowerXComplex;
            double startXComplex = startX * ((upperXComplex - lowerXComplex) / xPixelNumber) + lowerXComplex;
            double currentYComplex = -1 * currentY * ((upperYComplex - lowerYComplex) / yPixelNumber) + upperYComplex;
            double startYComplex = -1 * startY * ((upperYComplex - lowerYComplex) / yPixelNumber) + upperYComplex;
             
            for (int x = 0; x < xPixelNumber; x++) {
                double xC = (x * ((currentXComplex - startXComplex) / xPixelNumber) + startXComplex);
                for (int y = 0; y < yPixelNumber; y++) {
                    double yC = (-1 * y * ((currentYComplex - startYComplex) / yPixelNumber) + startYComplex);
                    constant = new Complex(xC, yC);
                    int counter = mandleBrot.zFunction(new Complex(xC, yC));
                    if (counter == maxIterate) ioZoom.setRGB(x, y, 0);
                    else ioZoom.setRGB(x, y, counter | (counter << 42));
                }
            }
 
        }
       /* public void paint(Graphics g){
            super.paint(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.draw3DRect(startX, startY, currentX, currentY, true);
            if(notZoom){
                g.drawImage(ioMandle, 0, 0, this);
                notZoom = false;
                return;
            }
            if(rectangle != null){ 
                listenForDrag.draw();
                g.drawImage(ioZoom, 0, 0, this);
                rectangle = null;
            }*/
             
                     
            }
        }
    