package guiBasics;
 
import java.awt.geom.Ellipse2D;
 
public class Circle extends Shape{
    Ellipse2D circle;
	
    public Circle(double x, double y, double radius){
        super(x,y, radius, radius);
		circle = new Ellipse2D.Double(x, y, radius, radius);
        }
	
	public Ellipse2D getShape(){
		return circle;
	}
	
}