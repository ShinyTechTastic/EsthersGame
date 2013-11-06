package com.foo.esthersgame;

import com.foo.esthersgame.shapes.AbstractShape;

public interface IRenderTarget {

	void drawShape(AbstractShape shape, float colour, float brightness,
			float x, float y, float rotation, float scale);
	void drawShape(int shape, float colour, float x, float y , float rotation , float scale );
	void drawShape(int shape, float colour, float x, float y , float rotation);
	void drawShape(int shape, float colour, float x, float y );

}
