package com.foo.esthersgame;

import android.os.Bundle;

public interface IGameState {

	void prepare(Bundle savedInstanceState);
	void tick();
	void press( double x , double y );
	//void saveState( Bundle instanceState );
	
	void render( IRenderTarget render );
}
