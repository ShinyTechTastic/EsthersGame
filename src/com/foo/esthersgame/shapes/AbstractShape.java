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
        


    // Set color with red, green, blue and alpha (opacity) values
    protected static final int COLOURS = 4;
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.3f ,
    		 0.76953125f, 0.22265625f, 0.63671875f, 0.3f ,
    		0.22265625f, 0.63671875f, 0.76953125f, 0.3f ,
    		0.63671875f, 0.22265625f,0.76953125f,  0.3f };
    
	abstract public void draw(float[] mTempMatrix, int n);

}
