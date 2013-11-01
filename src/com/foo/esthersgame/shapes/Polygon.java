package com.foo.esthersgame.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.foo.esthersgame.MyGLRenderer;

import android.opengl.GLES20;

public class Polygon extends AbstractShape {
	
        private final FloatBuffer vertexBuffer;
        private final int mProgram;
        private int mPositionHandle;
        private int mColorHandle;
        private int mMVPMatrixHandle;

        static final int COORDS_PER_VERTEX = 3;

		private final float[] coords;
        private final int vertexCount;
        private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        public Polygon( int sides ) {
        	coords = new float[ sides * COORDS_PER_VERTEX ];
        	vertexCount = sides;
        	for ( int n = 0 ;n < sides ; n++ ){
        		double rad = ((Math.PI*2) / sides) * n;
        		coords[ n * COORDS_PER_VERTEX + 0 ] = (float) (/* x */ 0.2f * Math.sin( rad ));
        		coords[ n * COORDS_PER_VERTEX + 1 ] = (float) (/* y */ 0.2f * Math.cos( rad ));
        		coords[ n * COORDS_PER_VERTEX + 2 ] = /* z */ 0.0f;
        	}
        	
            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    coords.length * 4);
            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(coords);
            // set the buffer to read the first coordinate
            vertexBuffer.position(0);

            // prepare shaders and OpenGL program
            int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                       vertexShaderCode);
            int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                         fragmentShaderCode);

            mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
            GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
            GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        }

        public void draw(float[] mvpMatrix, int n) {
            // Add program to OpenGL environment
            GLES20.glUseProgram(mProgram);

            // get handle to vertex shader's vPosition member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                         GLES20.GL_FLOAT, false,
                                         vertexStride, vertexBuffer);

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

            // Set color for drawing the triangle
            GLES20.glUniform4fv(mColorHandle, 1, color, 4*(n%COLOURS));

            // get handle to shape's transformation matrix
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            MyGLRenderer.checkGlError("glGetUniformLocation");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            MyGLRenderer.checkGlError("glUniformMatrix4fv");

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(mPositionHandle);
        }

}