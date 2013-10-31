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
	public void press(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(IRenderTarget render) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].render( n , render );
		}	
	}

	public class Mover {
		public double x;
		public double y;
		public double vx;
		public double vy;
		

		public Mover(String hexCode, Bundle savedInstanceState) {
			if ( savedInstanceState != null &&
				savedInstanceState.containsKey(hexCode+".x") &&
				savedInstanceState.containsKey(hexCode+".y")
					){
				x = savedInstanceState.getDouble( hexCode+".x" );
				y = savedInstanceState.getDouble( hexCode+".y" );
				vx = savedInstanceState.getDouble( hexCode+".vx" );
				vy = savedInstanceState.getDouble( hexCode+".vy" );
			}else{
				x = Math.random();
				y = Math.random();
				vx = Math.random() - 0.5;
				vy = Math.random() - 0.5;
			}
		}

		public void render(int n , IRenderTarget render) {
			render.drawShape( n , x ,y );
		}

		private static final double XRANGE = 1.0;
		private static final double YRANGE = 1.0;
		
		public void tick( double t ) {
			x += vx * t;
			y += vy * t;
			if ( x > XRANGE ) x -= XRANGE * 2;
			if ( y > YRANGE ) y -= YRANGE * 2;
			if ( x < -XRANGE ) x += XRANGE * 2;
			if ( y < -YRANGE ) y += YRANGE * 2;
		}

	//	public void saveState(String hexCode, Bundle instanceState) {
	//		instanceState.set
	//	}
		
		
	}


}
