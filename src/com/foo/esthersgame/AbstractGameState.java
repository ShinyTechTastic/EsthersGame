package com.foo.esthersgame;

import com.foo.esthersgame.particle.IPartcleSystem;

import android.os.Bundle;

public abstract class AbstractGameState {
	
	protected long lastUpdate = 0;
	protected IPartcleSystem particle;
	
	public double tick(){
		long now = System.currentTimeMillis();
		long delta = now - lastUpdate;
		lastUpdate = now;
		if ( delta > 1000 ){
			delta = 1000;
		}
		double d = (double)delta / 1000.0 ;
		tick( d );
		return d;
	}
	
	public void setParticleSystem( IPartcleSystem particle ){
		this.particle = particle;
	}
	
	abstract void prepare(Bundle savedInstanceState);
	abstract void tick( double t );
	abstract void press( float x , float y );
	//void saveState( Bundle instanceState );
	
	abstract void render( IRenderTarget render );
}
