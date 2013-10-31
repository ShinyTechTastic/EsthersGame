package com.foo.esthersgame;

import android.os.Bundle;

public class BasicGameState implements IGameState {

	private static final int MOVERS_COUNT = 1;
	
	private Mover[] movers = new Mover[ MOVERS_COUNT ];
	
	@Override
	public void prepare(Bundle savedInstanceState) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			String hexCode = "Mover."+Integer.toHexString(n);
			movers[n]= new Mover( hexCode , savedInstanceState );
		}
	}

	@Override
	public void tick() {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			movers[n].tick();
		}
	}

	@Override
	public void press(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(IRenderTarget render) {
		for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
			render.drawShape( n , movers[n].x , movers[n].y );
		}	
	}
	//@Override
	//public void saveState(Bundle instanceState) {
	//	for ( int n =0 ; n < MOVERS_COUNT ; n++ ){
	//		String hexCode = "Mover."+Integer.toHexString(n);
	//		movers[n].saveState( hexCode , instanceState );
	//	}	
	//}

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

		private static final double XRANGE = 10.0;
		private static final double YRANGE = 10.0;
		
		public void tick() {
			x += vx * 0.1;
			y += vy * 0.1;
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
