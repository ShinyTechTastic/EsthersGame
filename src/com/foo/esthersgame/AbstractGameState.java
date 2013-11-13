package com.foo.esthersgame;

import com.foo.esthersgame.particle.IPartcleSystem;

import android.os.Bundle;

public abstract class AbstractGameState {
	
	private static final float VIEW_MARGIN = 0.1f; 
	// This defines an area around the edge of the screen to stop shapes popping as there
	//  position is their centre
	
	private static final long MAX_TIME_STEP = 100;
	
	protected long lastUpdate = 0;
	protected IPartcleSystem particle;
	protected float viewWidth;
	protected float viewHeight;
	protected float viewMinX;
	protected float viewMinY;
	protected float viewMaxX;
	protected float viewMaxY;
	
	public double tick(){
		long now = System.currentTimeMillis();
		long delta = now - lastUpdate;
		lastUpdate = now;
		if ( delta > MAX_TIME_STEP ){
			delta = MAX_TIME_STEP;
		}
		double d = (double)delta / 1000.0 ;
		tick( d );
		return d;
	}
	
	public void setParticleSystem( IPartcleSystem particle ){
		this.particle = particle;
	}
	
	public void resizeView( float width, float height ){
		this.viewWidth = width + (2.0f*VIEW_MARGIN);
		this.viewHeight = height + (2.0f*VIEW_MARGIN);
		this.viewMinX = -width/2.0f ;
		this.viewMinY = -height/2.0f;
		this.viewMaxX = width/2.0f;
		this.viewMaxY = height/2.0f;
	}
	
	abstract void prepare(Bundle savedInstanceState);
	abstract void tick( double t );
	abstract void press( float x , float y );
	//void saveState( Bundle instanceState );
	
	abstract void render( IRenderTarget render );
}
