package com.foo.esthersgame;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.foo.esthersgame.shapes.AbstractShape;
import com.foo.esthersgame.shapes.Polygon;
import com.foo.esthersgame.shapes.Star;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyGLRenderer";
    
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
	private AbstractGameState gameState;
	private IRenderTarget mInnerRenderTarget;
	
	private AbstractShape[] mShapes; 

    public MyGLRenderer(AbstractGameState gameState) {
		this.gameState = gameState;
	}

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable( GLES20.GL_BLEND );
        GLES20.glBlendFunc( GLES20.GL_SRC_ALPHA , GLES20.GL_ONE_MINUS_SRC_ALPHA );

        mShapes = new AbstractShape[11];
    	mShapes[0] = new Star( 4 );
    	mShapes[1] = new Star( 5 );
    	mShapes[2] = new Star( 6 );
    	mShapes[3] = new Star( 7 );
    	mShapes[4] = new Star( 8 );
    	mShapes[5] = new Polygon( 3 );
    	mShapes[6] = new Polygon( 4 );
    	mShapes[7] = new Polygon( 5 );
    	mShapes[8] = new Polygon( 6 );
    	mShapes[9] = new Polygon( 7 );
    	mShapes[10] = new Polygon( 8 );
        
        mInnerRenderTarget = new InnerRenderTarget();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        gameState.render( mInnerRenderTarget );
        gameState.tick();
    }

    public class InnerRenderTarget implements IRenderTarget {

		private float[] mModelMatrix = new float[16]; // 4x4 matrix
		private float[] mTempMatrix = new float[16]; // 4x4 matrix

		@Override
		public void drawShape(int shape, float colour, float x, float y,
				float rotation, float scale) {
			//Matrix.setRotateM(mModelMatrix, 0, mAngle, 0, 0, -1.0f);
			Matrix.setIdentityM(mModelMatrix , 0);
			Matrix.scaleM(mModelMatrix, 0 , scale, scale, 0.0f );
			Matrix.translateM( mModelMatrix, 0, (float)x, (float)y, 0);
			Matrix.rotateM(mModelMatrix, 0, rotation, 0.0f, 0.0f, -1.0f );
			//float[] scratch = new float[16];
			// Combine the rotation matrix with the projection and camera view
			Matrix.multiplyMM(mTempMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);			

	        // Draw triangle
	        //mSquare.draw(mDrawMatrix);
			int shapeId = (shape % mShapes.length);
			
			mShapes[shapeId].draw( mTempMatrix , colour );
		}

		@Override
		public void drawShape(int shape, float colour, float x, float y,
				float rotation) {
			this.drawShape(shape, colour, x, y , rotation , 1.0f );
		}

		@Override
		public void drawShape(int shape, float colour, float x, float y) {
			this.drawShape(shape, colour, x, y , 0.0f , 1.0f );
		}

	}
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}