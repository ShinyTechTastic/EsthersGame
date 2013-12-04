package com.foo.esthersgame;

import java.lang.reflect.Constructor;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private static final String GAMESTATE_CLASS = "GameState-Class";
	private static final String GAME_TAG = "Esther";
	
	private AbstractGameState gameState;
	


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(GAME_TAG, "Recieved Key down "+keyCode);
		if ( keyCode == KeyEvent.KEYCODE_BACK) {
		    // Don't quit on back button, yet...
			event.startTracking();
		    return true;
		}
		return super.onKeyDown(keyCode, event);    
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.i(GAME_TAG, "Recieved Key up "+keyCode);
		if (    keyCode == KeyEvent.KEYCODE_BACK ) {
		    // Don't quit on back button, yet...
			event.startTracking();
		    return true;
		}
		return super.onKeyUp(keyCode, event);    
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Log.i(GAME_TAG, "Recieved Long press "+keyCode);
		if (    keyCode == KeyEvent.KEYCODE_BACK ) {
			this.finish(); // close the app
		}
		return super.onKeyLongPress(keyCode, event);
	}


	private GLSurfaceView mGLView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN );
        
        if ( savedInstanceState != null ){
	    	String className = savedInstanceState.getString( GAMESTATE_CLASS , com.foo.esthersgame.BasicGameState.class.getCanonicalName() );
	        try{
	        	Class<?> targetClass = MainActivity.class.getClassLoader().loadClass( className );
	        	Constructor<?> classConstruction = targetClass.getConstructor();
	        	Object instance = classConstruction.newInstance();
	        	gameState = (AbstractGameState)instance;
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
        
        // force the device to be used PORTRAIT
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, AbstractGameState gameState) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer( gameState );
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
            float[] worldPos = mRenderer.getWorldPosition( x , y );
            
            x = worldPos[0];
            y = worldPos[1];
 
            gameState.press(x, y);
            
            break;
        }
        return true; // handled
    }
}
}
