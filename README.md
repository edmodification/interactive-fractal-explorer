# interactive-fractal-explorer

I built Java application which creates a Graphical User Interface which is capable of rendering a depiction of the well-known fractal, the Mandelbrot Set. In addition to this, the user is able to a select a point from the Mandelbrot set display using a mouse click. For this selected point, the Julia Set corresponding to that point will be visualised in an adjacent window or panel.

The Mandelbrot Set is actually a set of complex numbers. We can calculate (an approximation to it) using an iterated formula over complex numbers: given any complex number, c, we calculate the sequence of values:

Z(0), Z(1), Z(2), Z(3), ...      where Z(i+1) = (Z(i) * Z(i)) + c and Z(0) = c
Now, point c is said to be in the Mandelbrot Set if the sequence of Z(i) values does not diverge, that is, if the values of Z(i) do not just keep getting bigger and bigger. In practical terms, we are not going to be able to calculate all values of Z(i) for each point c. Thus we must limit ourselves to some finite subsequence of the Z(i) values. Let's say we calculate up to value Z(n) only (for some fixed n). We could approximate the Mandelbrot Set if we include point c whenever the value of Z(i) up to Z(n) has not yet diverged. To test for non-divergence, it is sufficient to check that the absolute value (or modulus) of Z(i) (usually written |Z(i)|) is less than 2 for each Z(i) up to Z(n).

Every point c in the Mandelbrot set can be used to generate a different Julia Set.
