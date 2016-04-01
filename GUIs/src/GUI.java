import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
 
@SuppressWarnings("serial")
public class GUI extends JFrame{
	Container main,secondary;
	Julia jul;
	Mandelbrot brot;
	JTextField input = new JTextField("Input your preferred number of iterations");
	//i put this class in here because I thought that the main initiator class should handle the other panel appearing
	//it creates the Julia using the mandlebrot class
	class MandelEar extends MouseAdapter{
		//private int width, height;
        
        public MandelEar(int width, int height, int maxIterations, double realAxisSize, double imaginaryAxisSize){
            ////this.width = width;
           // this.height = height;
        }
        
        @Override
        public void mouseClicked(MouseEvent click) {
            main.remove(jul);
            jul = brot.setJulia();
            main.add(jul);
            main.validate();
            main.repaint();
            repaint();
            }
  
        }
   
    
    public static void main (String[] args){
    	new GUI();
    }
    public GUI (){
    	this.setLayout(new BorderLayout());
    	this.setTitle("Mandelbrot | Julia");
       	main = new Container();
        main.setLayout(new GridLayout(1,2));
        brot = new Mandelbrot(100, 600,600);
        brot.addMouseListener(new MandelEar(brot.getWidth(), brot.getHeight(), brot.getMaxIterations(), brot.getRealXSize(), brot.getImaginaryXSize()));
        main.add(brot);
        jul = new Julia(brot.getWidth(), brot.getHeight());
        main.add(jul);
        add(main);
        
        secondary = new Container();
        secondary.setLayout(new FlowLayout());
        secondary.add(new JLabel("Maximum number of iterations: "));
        input = new JTextField("100", 6);
        secondary.add(input);
        add(secondary, BorderLayout.PAGE_START);
        //takes the input from the textfield and sets that to the iteration number, it then repaints
        input.addActionListener(new ActionListener(){
        	String inputText;
			@Override
			public void actionPerformed(ActionEvent enter) {
				try{
					inputText = input.getText().trim();
					brot.setIterations(Integer.parseInt(inputText));
					jul.setIterations(Integer.parseInt(inputText));
					repaint();
				}catch (Exception e){
					JOptionPane.showMessageDialog(new JFrame(), ("What you have enterd is invalid!\nPlease try again"));
				}
			}      
        });
        
        this.setSize((brot.getWidth()*2), brot.getHeight()+50+secondary.getHeight());
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    
    
    
     
     
}

