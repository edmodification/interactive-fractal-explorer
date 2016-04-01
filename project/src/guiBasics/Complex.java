package guiBasics;
 
public class Complex {
     
    private double realNumber;
    private double imaginaryNumber; 
     
    public Complex(double realNumber, double imaginaryNumber){
        this.realNumber = realNumber;
        this.imaginaryNumber = imaginaryNumber;
    }
     
    public void square(){
        double rnSquared = realNumber*realNumber;
        double inSquared = imaginaryNumber*imaginaryNumber;
        imaginaryNumber = realNumber * imaginaryNumber * 2;
        realNumber = rnSquared - inSquared;
    }
     
    public double modulusSquare(){
        return (realNumber*realNumber) + (imaginaryNumber*imaginaryNumber);
    }
     
    public void add(Complex d){
        realNumber += d.getReal();
        imaginaryNumber += d.getImaginary();
    }
 
    public double getReal(){
        return realNumber;
    }
     
    public double getImaginary(){
        return imaginaryNumber;
    }
     
    public Complex stringToComplex(String complex){
        String[] complexParts = complex.split(" ");
        realNumber = Double.parseDouble(complexParts[0]);
         
        imaginaryNumber = Double.parseDouble(complexParts[2].split("i")[0]);
        return new Complex(realNumber, imaginaryNumber);
    }
     
    public String toString(){
        String out = "";
        String rNFormat = String.format("%.3g%n", realNumber);
        String iNFormat = String.format("%.3g%n", imaginaryNumber);
        if(imaginaryNumber < 0) out = rNFormat + " - " + Math.abs(Double.parseDouble(iNFormat)) + "i";
        else out = rNFormat + " + " + iNFormat +"i";
        return out;
    }
     
}