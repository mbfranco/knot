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
 * This class implements the Manhattan distance function. It is a subclass of the
 * DistanceFunction class which represents any distance function.
 * <br/><br/>
 * 
 * @see DistanceFunction
 * @author Philippe Fournier-Viger
 */

public class DistanceManhattan extends DistanceFunction {
	/** the name of this distance function */
	static String NAME = "manhattan";

	/**
	 * Calculate the Manathan distance between two vectors of doubles.
	 * @param vector1 the first vector
	 * @param vector2 the second vector
	 * @return the distance
	 */
	public double calculateDistance(BitArray vector1, BitArray vector2) {
		double sum = 0;	
		for(int i = 0; i < vector1.length(); i++){
			int v1 = 0;
			int v2 = 0;
			if(vector1.get(i)){
				v1=1;
			}
			if(vector2.get(i)){
				v2=1;
			}
			sum += Math.abs(v1 - v2);
		}
		return sum;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
//	
//	public static void main(String[] args) {
//		DoubleArray array1 = new DoubleArray(new double[] {0,2});
//		DoubleArray array2 = new DoubleArray(new double[] {2,0});
//		System.out.println(new DistanceManathan().calculateDistance(array1,array2));
//	}
	

}
