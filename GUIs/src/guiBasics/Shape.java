package guiBasics;
 
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
 
public abstract class Shape{
	private ArrayList <Color> color;
	private double x, y, width, height;
	private int colourValue = 0;
	
	public Shape(double x, double y, double width, double height) {
		color = new ArrayList <Color>();
		color.add(Color.BLACK);
		color.add(Color.BLUE);
		color.add(Color.ORANGE);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void changeColor(){
		colourValue = (colourValue+1)%(color.size());
	}

	public Color getColor() {
		return color.get(colourValue);
	}
	
	
	
     
}