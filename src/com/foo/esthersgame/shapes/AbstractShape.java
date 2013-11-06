package com.foo.esthersgame.shapes;

public abstract class AbstractShape {
	
	protected final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

        protected final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";
    
    private float[] mfAcolour = new float[4];
    
    public float[] getColour( float colour , float brightness ){
    	double rad = colour * 2 * Math.PI;
    	mfAcolour[0] = (float)((Math.sin( rad ) * 0.5 ) + 0.5);
    	rad += (Math.PI * 2.0 / 3.0);
    	mfAcolour[1] = (float)((Math.sin( rad ) * 0.5 ) + 0.5);
    	rad += (Math.PI * 2.0 / 3.0);
    	mfAcolour[2] = (float)((Math.sin( rad ) * 0.5 ) + 0.5);
    	mfAcolour[3] = brightness * 0.8f; // opacity is constant
    	return mfAcolour;
    }
    
	abstract public void draw(float[] mTempMatrix, float colour, float brightness);

}
