import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class Julia extends JPanel{
    private BufferedImage img;
    private Complex constant;
    private int maxIterations, width, height;
    private double realAxisSize = 4, imaginaryAxisSize = 3.2;
    private JButton save;
    private File saves;
     
    class ButtonEar implements ActionListener{
        /*
         * for saving files as the specific coordinate at which they were possible and the number of iterations 
         * in the directory "saves"         * 
         */
    	@Override
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == save){
            	if (saves.canWrite()){
            		String [] a = constant.toString().split(" ");
                    String filename = ("saves/"+maxIterations+"iterations_"+a[0]+a[1]+a[2]+ ".png");
                     File juliaImage = new File(filename);
                     try {
                        ImageIO.write(img, "png", juliaImage);
                        System.out.println("Image saved: " + filename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            	}
            	
            }
        }
         
    }
     
    public Julia(Complex chosenPoint, int width, int height, int maxIterations){
        //creates the directory when this constructor is called
    	saves = new File("saves");
        saves.mkdir();
    	this.setLayout(new BorderLayout());
    	save = new JButton("Save image");
        ButtonEar ear = new ButtonEar();
        save.addActionListener(ear);
        this.width = width;
        this.height = height;
        constant = chosenPoint;
        this.maxIterations = maxIterations; 
        img = juliaImg();
        add(save, BorderLayout.SOUTH);

    }
    //basically here for a 'blank' screen until the user clicks on the Mandelbrot panel
    public Julia(int width, int height){
    	this.width = width;
        this.height = height;
    	img = new BufferedImage(width, height, BufferedImage.OPAQUE);
    	JLabel label = new JLabel("No Julia Set image available");
    	this.add(label, BorderLayout.CENTER);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
    /*
     * basically the same as the method in Mandelbrot(however using a different function) so I was wondering if I should have let this extend the Mandelbrot
     * class, but I was not sure
     */
    public BufferedImage juliaImg(){
    	double x1 = -realAxisSize/2,y1 = imaginaryAxisSize/2, yS = imaginaryAxisSize/2, yD = imaginaryAxisSize/height, xD =  realAxisSize/width;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int counterX = 0 ; counterX < width ; counterX++){
        	y1 = yS;
            for(int counterY = 0 ; counterY < height ; counterY++){
                Complex number = new Complex(x1,y1);
                int juliaValue = juliaValue(number);
                if(juliaValue == maxIterations){
                    img.setRGB(counterX, counterY, 0);
                }else{
                	Color color = new Color(Color.HSBtoRGB((float) juliaValue/150, 0.7f, 0.75f));
                    img.setRGB(counterX,counterY, color.getRGB());
                }
                y1 = y1 - yD;
                
            }
            x1 = x1 + xD;
        }
        return img;
    }
    //standard Julia function
    public int juliaValue(Complex number){
        int counter = 0;
        Complex temp = new Complex(number.getReal(), number.getImaginary());
        while (true){
            temp.square();
            temp.add(constant);
            if(temp.modulusSquare() > 4) {
                return counter;
            }else if( maxIterations <= counter){
                return maxIterations;
            }
            counter++;
        }
         
    }
    public void setIterations(int maxIterations){
    	this.maxIterations = maxIterations;
    	if(constant != null)img = juliaImg();
    }
    
     
}
