package com.foo.esthersgame;

import android.os.Bundle;

public class BasicGameState extends AbstractGameState {

	private static final int MOVERS_COUNT = 20;
	
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
		if ( particle != null ){ // check we have a functioning particle system
			float c = (float)Math.random();
			for ( int i=0;i<20;i++){
				double d = Math.random() * Math.PI * 2;
				double s = (Math.random()*0.5) + 0.5;
				particle.create(x, y, 
						(float)(Math.sin(d) * s), 
						(float)(Math.cos(d) * s),
						c );
			}
		}
/*		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].x = x;
			movers[n].y = y;
			
		}*/
	}

	@Override
	public void render(IRenderTarget render) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].render( n , render );
		}	
	}

	public class Mover {
		public float x;
		public float y;
		public float vx;
		public float vy;
		private float r;
		private float vr;
		private float c;
		private float vc;
		

		public Mover(String hexCode, Bundle savedInstanceState) {
			x = (float) Math.random() * 0.0f;
			y = (float) Math.random() * 0.0f;
			r = (float) Math.random() * 360.0f;
			c = (float) Math.random();
			vx = (float) (Math.random() - 0.5);
			vy = (float) (Math.random() - 0.5);
			vr = (float) (Math.random() - 0.5) * 60;
			vc = (float) (Math.random() - 0.5);
		}

		public void render(int n , IRenderTarget render) {
			render.drawShape( n , c , x ,y , r );
		}

		private static final float RRANGE = 180.0f;
		private static final float CRANGE = (float)(Math.PI);
		
		public void tick( double t ) {
			x += vx * t;
			y += vy * t;
			r += (vr * t);
			c += (vc * t);
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
