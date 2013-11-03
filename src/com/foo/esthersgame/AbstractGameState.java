package com.foo.esthersgame;

import android.os.Bundle;

public abstract class AbstractGameState {

	long lastUpdate = 0;
	
	void tick(){
		long now = System.currentTimeMillis();
		long delta = now - lastUpdate;
		lastUpdate = now;
		if ( delta > 1000 ){
			delta = 1000;
		}
		tick( (double)delta / 1000.0 );
	}
	
	abstract void prepare(Bundle savedInstanceState);
	abstract void tick( double t );
	abstract void press( float x , float y );
	//void saveState( Bundle instanceState );
	
	abstract void render( IRenderTarget render );
}
