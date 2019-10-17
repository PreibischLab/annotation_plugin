package net.preibisch.ijannot.util;

import java.awt.Color;
import java.util.Random;

public class Tools {

	public static double[] sigma(int x, int numDimensions) {
		double[] sigma = new double[numDimensions];

		for (int d = 0; d < numDimensions; ++d)
			sigma[d] = x;

		return sigma;
	}

	public static Color randomColor() {
		Random rand = new Random();

		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		Color color = new Color(R, G, B);
		return color;
	}
}
