package com.foo.esthersgame;

import android.os.Bundle;

public class BasicGameState extends AbstractGameState {

	private static final int MOVERS_COUNT = 10;
	private static final float MOVERS_SPEED = 0.3f;
	
	private Mover[] movers = new Mover[ MOVERS_COUNT ];
	
	@Override
	public void prepare(Bundle savedInstanceState) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			String hexCode = "Mover."+Integer.toHexString(n);
			movers[n]= new Mover( hexCode , savedInstanceState );
		}
	}

	@Override
	public void tick( double t ) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].tick( t );
		}
	}

	@Override
	public void press(float x, float y) {
		int nClosest = -1;
		float dClosest = 0.01f;
		
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			if ( movers[n].size > 0.9f ){
				float dx = movers[n].x - x;
				float dy = movers[n].y - y;
				float dSquared = (dx*dx) + (dy*dy);
				
				if ( dSquared < dClosest ){
					dClosest = dSquared;
					nClosest = n;
				}
			}
		}
			
		if ( nClosest > -1 ){ /* about 100th of the screen size from the centre*/
			float c = movers[nClosest].c;

			// Fire some particles (about 20, of roughly the right colour)
			if ( particle != null ){ // check we have a functioning particle system
				for ( int i=0;i<10;i++){
							double d = Math.random() * Math.PI * 2;
				double s = (Math.random()*0.5) + 0.5;
				particle.create(movers[nClosest].x, movers[nClosest].y, 
						(float)(Math.sin(d) * s), 
						(float)(Math.cos(d) * s),
						(float)(c + (Math.random() - 0.5 ) *0.1f) );
						}	
			}
			
			// recreate the shape somewhere else
			movers[nClosest].randomColour();
			movers[nClosest].randomPos();
			movers[nClosest].randomRotation();
			movers[nClosest].randomEmergeTime( 3.0f );// sometime within 3 seconds
		}
	}

	@Override
	public void render(IRenderTarget render) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].render( n , render );
		}	
	}

	public class Mover {
		public float size;
		public float x;
		public float y;
		public float vx;
		public float vy;
		private float r;
		private float vr;
		private float c;
		private float vc;
		

		public Mover(String hexCode, Bundle savedInstanceState) {
			randomPos();
			randomRotation();
			randomColour();
			randomEmergeTime( 10.0f );// sometime within 10 seconds
		}

		private void randomColour() {
			c = (float) Math.random();
			vc = (float) (Math.random() - 0.5);
		}

		private void randomRotation() {
			r = (float) Math.random() * 360.0f;
			vr = (float) (Math.random() - 0.5) * 60;
		}

		private void randomPos() {
			x = (float)(Math.random() - 0.5) * 2.0f;
			y = (float)(Math.random() - 0.5) * 2.0f;
			vx = (float) (Math.random() - 0.5) * MOVERS_SPEED;
			vy = (float) (Math.random() - 0.5) * MOVERS_SPEED;
		}
		
		private void randomEmergeTime( float scale ){
			size = (float) (scale * Math.random());
		}

		public void render(int n , IRenderTarget render) {
			if ( size > 0.0f ){
				render.drawShape( n , c , x ,y , r , size );
			}
		}

		private static final float RRANGE = 180.0f;
		private static final float CRANGE = (float)(Math.PI);
		
		public void tick( double t ) {
			x += vx * t;
			y += vy * t;
			r += (vr * t);
			c += (vc * t);
			if ( size < 1.0f ){
				size += t;
			}else{
				size = 1.0f;
			}
			if ( x >  viewMaxX ) x -= viewWidth;
			if ( x <  viewMinX ) x += viewWidth;
			if ( y >  viewMaxY ) y -= viewHeight;
			if ( y <  viewMinY ) y += viewHeight;
			if ( r >  RRANGE ) r -= RRANGE * 2;
			if ( r < -RRANGE ) r += RRANGE * 2;
			if ( c >  CRANGE ) c -= CRANGE * 2;
			if ( c < -CRANGE ) c += CRANGE * 2;
		}

	//	public void saveState(String hexCode, Bundle instanceState) {
	//		instanceState.set
	//	}
		
		
	}


}
