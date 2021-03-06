package knotCat.algorithms.clustering.distanceFunctions;

import knotCat.patterns.cluster.BitArray;

/* This file is copyright (c) 2008-2015 Philippe Fournier-Viger
* 
* This file is part of the SPMF DATA MINING SOFTWARE
* (http://www.philippe-fournier-viger.com/spmf).
* 
* SPMF is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* SPMF. If not, see <http://www.gnu.org/licenses/>.
*/
/**
 * This class implements the correlation distance function. It is a subclass of the
 * DistanceFunction class which represents any distance function.
 * The correlation distance function calculates the distance between
 * two vectors of double and returns a value in [0,1].
 * A result of 0 means that there is a positive linear relationship.
 * A result of 0.5 means that there is a negative linear relationship. 
 * A result of 1 means that there is no linear relationship (but there might be
 * a non linear relationship).
 * Note that this implementation is different from the typical correlation
 * similarity measure which returns a value in [-1,1] where 1 is positive and -1 is a negative
 * relationship. The reason why we use
 * [0,1] instead is that in clustering a distance measure should in general 
 * return a value of 0 
 * when two objects are similar and a high value when they are different.
 * <br/><br/>
 * 
 * @see DistanceFunction
 * @author Philippe Fournier-Viger
 */

public class DistanceCorrelation extends DistanceFunction {
	/** the name of this distance function */
	static String NAME = "correlation";
	
	/**
	 * Calculate the Correlation distance between two vectors of doubles. The
	 * correlation distance function calculates the distance between two vectors
	 * of double and returns a value in [0,1]. A result of 0 means that there is
	 * a positive linear relationship. A result of 1 means that there is a
	 * negative linear relationship. A result of 0.5 means that there is no linear
	 * relationship (but there might be a non linear relationship).
	 * 
	 * @param vector1
	 *            the first vector
	 * @param vector2
	 *            the second vector
	 * @return the distance
	 */
	public double calculateDistance(BitArray vector1, BitArray vector2) {
		double mean1 = calculateMean(vector1);	
		double mean2 = calculateMean(vector2);	
		double standardDeviation1 = calculateStdDeviation(vector1, mean1);	
		double standardDeviation2 = calculateStdDeviation(vector2, mean2);	
		
		double correlation = 0;
		for(int i=0; i< vector1.length(); i++){
			int n = 0 , m = 0;
			if(vector1.get(i)){n=1;}
			if(vector1.get(i)){m=1;}
			correlation -= (n - mean1) * (m - mean2);
		}
		// protection to avoid dividing by 0
		if(standardDeviation1 == 0) {
			standardDeviation1 = 0.0001;
		}
		// protection to avoid dividing by 0
		if(standardDeviation2 == 0) {
			standardDeviation2 = 0.0001;
		}
		double bottom = (standardDeviation1 * standardDeviation2 * (vector1.length() - 1));
		correlation = correlation / (bottom );
		return (1.0 + correlation) / 2.0;
		// 0.5 0 1 1 
	}
	
	/**
	 * This method calculate the mean of a list of doubles
	 * @param list the list of doubles
	 * @return the mean 
	 */
	private static double calculateMean(BitArray vector) {
		double sum = vector.count();
		return sum / vector.length();
	}

	/**
	 * This method calculate the standard deviation of a list of double.
	 * Note that it divides by n-1 instead of n, assuming that it is
	 * the standard deviation of a sample rather than a population.
	 * @param list the list of doubles
	 * @param the man of the list of double values
	 * @return the standard deviation
	 */
	private static double calculateStdDeviation(BitArray vector, double mean) {
		double deviation = 0;
		for (int i = 0; i<vector.length(); i++) {
			int n = 0;
			if(vector.get(i)){n=1;}
			deviation += Math.pow(n, 2);
		}
		return Math.sqrt(deviation / (vector.length() - 1));
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	

}
