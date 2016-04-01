import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class Mandelbrot extends JPanel{
    private double x1=-2,y1=1.6;
    private double x2,y2;
	private BufferedImage img;
    private Rectangle tangle;
    private JLabel txt = new JLabel("...", JLabel.CENTER);
    private int maxIterations, width, height;
    private double realAxisSize = 4;
    private double imaginaryAxisSize = 3.2;
    
    //created purely to create a rectangle while dragging the mouse across this panel
    public class Rectangle {
    	private Point start, end;
    	private Rectangle2D tangle;
    	//constructor takes the point where the mouse is first clicked and the point where the mouse is release
    	public Rectangle(Point start, Point end){
    		this.start = start;
    		this.end = end;
    		tangle = new Line2D.Double(start, end).getBounds2D();
    	}
    	public Rectangle2D getRectangle(){
    		return tangle;
    	}
    	/*
    	 * used for changing the rectangle's size when the mouse is being dragged so it takes the point where the mouse
    	 * is at during the dragging event
    	 */
    	public void increaseSize(Point newEnd){
    		end = newEnd;
    		tangle = new Line2D.Double(start, end).getBounds2D();
    	}
    	public double getWidth(){
    		return tangle.getWidth();
    	}
    	public double getHeight(){
    		return tangle.getHeight();
    	}
    }
    
    //class responsible for zooming into the Mandelbrot image
    class ZoomListener extends MouseAdapter{
    	//used so panel doesn't zoom on one point and also for the rectangle
    	private Point start, end;
    	public void mousePressed(MouseEvent press){
    		start = press.getPoint();
    		end = press.getPoint();
    		tangle = new Rectangle(start,end);
    		
    		/*
    		 * both equations are used to translate the x2,y2 values to the point where the rectangle began
    		 * relative to the real and imaginary axis
    		 */
            x2 = (tangle.getRectangle().getMinX()*realAxisSize/width) + x1;
            y2 = y1 - (tangle.getRectangle().getMinY()*imaginaryAxisSize/height);
            
    	}
    	//end point used to change size of rectangle
    	public void mouseDragged(MouseEvent drag){
    			end = drag.getPoint();
    			tangle.increaseSize(end);
    			repaint();
    	}
    
    	public void mouseReleased(MouseEvent release){
    			if(end.equals(start)){
        			//if it's just a click then it doesn't draw the rectangle which is essentially a point
    				tangle = null;
            		repaint();
        		}else{
        			/*
            		 * both equations are used to translate the x1,y1 values to the upper left point of the rectangle
            		 * relative to the real and imaginary axis
            		 */
        			x1 = (tangle.getRectangle().getMinX()*realAxisSize/width) + x1;;
        			y1 =  y1 - (tangle.getRectangle().getMinY()*imaginaryAxisSize/height);
        			/*
        			 * uses the dimensions of the rectangle to average a scale since I didn't want it to 
        			 * scale in strangely
        			 */
        			double scaleReal = (tangle.getRectangle().getWidth()/width);
        			double scaleImaginary = (tangle.getRectangle().getHeight()/height);
                    imaginaryAxisSize *= (scaleReal+scaleImaginary)/2;
                    realAxisSize *= (scaleReal+scaleImaginary)/2;
                    img = MandelImg();
                    tangle = null;
            		repaint();
        		}
    	}
    }
   
     
    public Mandelbrot(int maxIterations, int width, int height){
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        img = MandelImg();
        this.setLayout(new BorderLayout());
        this.add(txt, BorderLayout.PAGE_START);
        /*
         * set to be transparent, uses x2 and y2 for the coordinates since they are accurately translated from 
         * the most recent click
         */
        txt.setBackground(new Color(0, 0, 0, .5f));
        this.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent click){
        		String content = "User's selected point is: " + new Complex(x2,y2);
        		txt.setText(content);
        	}
        });
        
        //restores values needed for calculating the zoomed in Mandlebrot image and the normal one
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				x1 = -2;
				y1 = 1.6;
				imaginaryAxisSize = 3.2;
				realAxisSize = 4;
				img = MandelImg();
				repaint();
			}
        });
        add(reset, BorderLayout.SOUTH);
        ZoomListener zoom = new ZoomListener();
        this.addMouseListener(zoom);
        this.addMouseMotionListener(zoom);
        this.setSize(width, (height+txt.getHeight())+reset.getHeight());
        this.setVisible(true);
        
        
    }
    public Julia setJulia(){
		return new Julia(new Complex(x2,y2), width, height, maxIterations);
    }
      
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0,0, this);
        if(tangle != null) {
        	Graphics2D g2 = (Graphics2D) g;
        	//used to draw the rectangle on the screen
        	g2.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(),Color.BLUE.getBlue(), 100));
        	g2.fill(tangle.getRectangle());
        }
    }
     
    public BufferedImage MandelImg(){
    	/*
    	 * sets what the first coordinate is supposed to be using x1 and x2(upper left hand corner of the screen
    	 * relative to the axis) and also what each of those those values are supposed to be incremented/decremented by
    	 * 
    	 * it then uses two for loops to go through each point and then returns an image at the ends of the process
    	 */
    	double x1 = this.x1,y1 = this.y1, yD = imaginaryAxisSize/height, xD = realAxisSize/width;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int counterX = 0 ; counterX < width ; counterX++){
        	y1 = this.y1;
            for(int counterY = 0 ; counterY < height ; counterY++){
                Complex number = new Complex(x1,y1);
                int mandelbrotValue = mandelbrotValue(number);
                if(mandelbrotValue == maxIterations){
                    img.setRGB(counterX, counterY, 0);
                }else{
                    Color color = new Color(Color.HSBtoRGB((float) mandelbrotValue/150, 0.7f, 0.75f));
                    img.setRGB(counterX,counterY, color.getRGB());
                }
                y1 = y1 - yD;
            }
            x1 = x1 + xD;
        }
        return img;
    }
    public double getMinReal(){
    	return x1;
    }
    public double getMinImaginary(){
    	return y1;
    }
    
    //used to calculate the formula
    public int mandelbrotValue(Complex number){
        int counter = 0;
        Complex temp = new Complex(number.getReal(), number.getImaginary());
        while(true){
            temp.square();
            temp.add(number);
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
    	img = MandelImg();
    }
    public int getMaxIterations(){
    	return maxIterations;
    }
    
    public double getRealXSize(){
    	return realAxisSize;
    }
    public double getImaginaryXSize(){
    	return imaginaryAxisSize;
    }
}
