package com.foo.esthersgame;

import android.os.Bundle;

public class BasicGameState implements IGameState {

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

		public Mover(String hexCode, Bundle savedInstanceState) {
			if ( savedInstanceState != null &&
				savedInstanceState.containsKey(hexCode+".x") &&
				savedInstanceState.containsKey(hexCode+".y")
					){
				x = savedInstanceState.getDouble( hexCode+".x" );
				y = savedInstanceState.getDouble( hexCode+".y" );
			}else{
				x = Math.random();
				y = Math.random();
			}
		}

		public void tick() {
			x += (Math.random()/8.0);
			y += (Math.random()/8.0);
			if ( x > 1.0 ) x -= 1.0;
			if ( y > 1.0 ) y -= 1.0;
		}

	//	public void saveState(String hexCode, Bundle instanceState) {
	//		instanceState.set
	//	}
		
		
	}


}
