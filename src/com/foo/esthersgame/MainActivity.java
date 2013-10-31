package com.foo.esthersgame;

import java.lang.reflect.Constructor;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private static final String GAMESTATE_CLASS = "GameState-Class";
	private static final String GAME_TAG = "Esther";
	
	private IGameState gameState;
    private GLSurfaceView mGLView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if ( savedInstanceState != null ){
	    	String className = savedInstanceState.getString( GAMESTATE_CLASS , com.foo.esthersgame.BasicGameState.class.getCanonicalName() );
	        try{
	        	Class<?> targetClass = MainActivity.class.getClassLoader().loadClass( className );
	        	Constructor<?> classConstruction = targetClass.getConstructor();
	        	Object instance = classConstruction.newInstance();
	        	gameState = (IGameState)instance;
	        	gameState.prepare( savedInstanceState );
	        }catch(Exception e ){
	        	Log.e(GAME_TAG, "Error creating GameState of class "+className );
	        }
        }else{
        	gameState = new BasicGameState();
        	gameState.prepare( null );
        }

        mGLView = new MyGLSurfaceView( this.getApplicationContext() , gameState );
        setContentView(mGLView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, IGameState gameState) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer( gameState );
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }

                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}