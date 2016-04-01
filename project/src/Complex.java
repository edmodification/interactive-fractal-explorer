public class Complex{
	
	private double real;
	private double imaginary;
	
	//constructor assigns real and imaginary values
	public Complex(double real, double imaginary){
		this.real = real;
		this.imaginary = imaginary;
	}
	
	//returns real number
	public double getReal(){
		return real;
	}
	//returns imaginary number
	public double getImaginary(){
		return imaginary;
	}
	
	//squares this complex number, doesn't return the value
	public void square(){
		double real = this.real;
		double imaginary = this.imaginary;
		this.real = (real*real) - (imaginary*imaginary);
		this.imaginary = 2*real*imaginary;
	}
	
	//returns the sqaure modulus of this complex number
	public double modulusSquare(){
		return (real*real + imaginary*imaginary);
	}
	//adds another complex number to this one
	public void add(Complex d){
		real = getReal()+d.getReal();
		imaginary = getImaginary()+d.getImaginary();
	}
	//for displaying purposes and saving images
	//takes this complex number and makes it readable 
	public String toString(){
		if(getImaginary()<0) return getReal() +" - "+ -getImaginary() +"i";
		else return getReal() +" + "+ getImaginary() +"i";
	}
	
}
