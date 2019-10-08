package net.preibisch.ijannot.util;

public class Tools {

	public static double[] sigma(int x, int numDimensions) {
		double[] sigma = new double[numDimensions];

		for (int d = 0; d < numDimensions; ++d)
			sigma[d] = x;

		return sigma;
	}

}
