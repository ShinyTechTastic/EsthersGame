package com.foo.esthersgame;

public interface IRenderTarget {

	void drawShape(int shape, float colour, float x, float y , float rotation , float scale );
	void drawShape(int shape, float colour, float x, float y , float rotation);
	void drawShape(int shape, float colour, float x, float y );

}
