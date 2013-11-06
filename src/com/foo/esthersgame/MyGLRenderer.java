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

import com.foo.esthersgame.particle.IPartcleSystem;
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

    private float screenW = 0.0f;
    private float screenH = 0.0f;
    
    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
	private AbstractGameState gameState;
	private IRenderTarget mInnerRenderTarget;
	private InnerParticleSystem mInnerParticleSystem;

	private AbstractShape[] mShapes; 

    public MyGLRenderer(AbstractGameState gameState) {
		this.gameState = gameState;
	}

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);
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
        mInnerParticleSystem = new InnerParticleSystem();
        
        gameState.setParticleSystem(mInnerParticleSystem);
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
        double time = gameState.tick();
        mInnerParticleSystem.tick( time );
        mInnerParticleSystem.render();
    }
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        screenW = width;
        screenH = height;

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

    private float[] worldPos = new float[2];
    
	public float[] getWorldPosition(float touch_x, float touch_y) {
				       
       // Auxiliary matrix and vectors
       // to deal with ogl.
       float[] invertedMatrix,
           normalizedInPoint, outPoint;
       
       invertedMatrix = new float[16];
       normalizedInPoint = new float[4];
       outPoint = new float[4];

       // Invert y coordinate, as android uses
       // top-left, and ogl bottom-left.
       int oglTouchY = (int) (screenH - touch_y);

       /* Transform the screen point to clip
       space in ogl (-1,1) */       
       normalizedInPoint[0] =
        (float) ((touch_x) * 2.0f / screenW - 1.0);
       normalizedInPoint[1] =
        (float) ((oglTouchY) * 2.0f / screenH - 1.0);
       normalizedInPoint[2] = - 1.0f;
       normalizedInPoint[3] = 1.0f;

       /* Obtain the transform matrix and
       then the inverse. */
       Matrix.invertM(invertedMatrix, 0,
    		   mProjMatrix, 0);       

       /* Apply the inverse to the point
       in clip space */
       Matrix.multiplyMV(
           outPoint, 0,
           invertedMatrix, 0,
           normalizedInPoint, 0);

       if (outPoint[3] == 0.0)
       {
           // Avoid /0 error.
           Log.e("World coords", "ERROR!");
           return worldPos;
       }

       // Divide by the 3rd component to find
       // out the real position.
       worldPos[0] = -(outPoint[0] / outPoint[3]);
       worldPos[1] = outPoint[1] / outPoint[3];

       return worldPos;
	}
	

    public class InnerRenderTarget implements IRenderTarget {

		private float[] mModelMatrix = new float[16]; // 4x4 matrix
		private float[] mTempMatrix = new float[16]; // 4x4 matrix


		@Override
		public void drawShape(AbstractShape shape, float colour,
				float brightness, float x, float y, float rotation , float scale ) {

			//Matrix.setRotateM(mModelMatrix, 0, mAngle, 0, 0, -1.0f);
			Matrix.setIdentityM(mModelMatrix , 0);
			Matrix.scaleM(mModelMatrix, 0 , scale, scale, 0.0f );
			Matrix.translateM( mModelMatrix, 0, (float)x, (float)y, 0);
			Matrix.rotateM(mModelMatrix, 0, rotation, 0.0f, 0.0f, -1.0f );
			//float[] scratch = new float[16];
			// Combine the rotation matrix with the projection and camera view
			Matrix.multiplyMM(mTempMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);			

			shape.draw( mTempMatrix , colour , brightness );
		}
			
		@Override
		public void drawShape(int shape, float colour, float x, float y,
				float rotation, float scale) {
			int shapeId = (shape % mShapes.length);
			this.drawShape( mShapes[shapeId] , colour , 1.0f , x , y , rotation , scale );
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

	public class InnerParticleSystem implements IPartcleSystem {

		private static final int MAX_PARTICLES = 200; // max onscreen at once
		private static final int PARTICLE_SIZE = 6; // floats in the data structure
		private static final float PARTICLE_LIFE = 5.0f; // seconds to live
		
		private float[] data = new float[ PARTICLE_SIZE * MAX_PARTICLES ];
		private int dataHead = 0;
		private int dataTail = 0;
		
		private Polygon mSquare = new Polygon( 8 , 0.04f );
		
		@Override
		public void create(float x, float y, float vx, float vy, float colour) {
			int offset = dataHead * PARTICLE_SIZE;
			data[ offset + 0 ] = x;
			data[ offset + 1 ] = y;
			data[ offset + 2 ] = vx;
			data[ offset + 3 ] = vy;
			data[ offset + 4 ] = colour;
			data[ offset + 5 ] = PARTICLE_LIFE; // TTL

			dataHead = (dataHead +1) % MAX_PARTICLES;
			if ( dataHead == dataTail ){
				// no space...
				dataTail = (dataTail +1) % MAX_PARTICLES;
			}
		}

		public void tick( double t ){
			int i = dataTail;
			while ( i != dataHead ){
				int offset = i * PARTICLE_SIZE;

				data[ offset + 0 ] += data[ offset + 2 ] * t;
				data[ offset + 1 ] += data[ offset + 3 ] * t;
				if ( data[ offset + 1 ] < -1.0f ){ // bounce?
					data[ offset + 1 ] = -2.0f - data[ offset + 1 ]; // bounce distance
					data[ offset + 3 ] = data[ offset + 3 ] * -0.8f; // invert the vertical velocity and reduce slightly
				}
				data[ offset + 3 ] -= 0.5f * t; // g
				data[ offset + 5 ] -= t;
				
				if ( data[ offset + 5 ] < 0.0 ){
					// this has expired
					dataTail = i; // catchup the tail.
				}
				
				i = (i+1) % MAX_PARTICLES;
			}
		}

		public void render(){
			int i = dataTail;
			while ( i != dataHead ){
				int offset = i * PARTICLE_SIZE;
				float x = data[ offset + 0 ];
				float y = data[ offset + 1 ];
				float c = data[ offset + 4 ];
				float b = (data[ offset + 5 ] / PARTICLE_LIFE);
				if ( b > 0.0f ){
					mInnerRenderTarget.drawShape( mSquare , c , b , x, y , b * 90.0f , 1.0f );
				}
				i = (i+1) % MAX_PARTICLES;
			}
		}
	}
}