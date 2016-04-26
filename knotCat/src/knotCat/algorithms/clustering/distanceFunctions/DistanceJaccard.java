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
 * This class implements the Jaccard distance function. This distance
 * function is suitable for vectors of binary values (0 or 1). It should
 * not be used with vectors containing non binary numbers.
 * It is a subclass of the
 * DistanceFunction class which represents any distance function.
 * <br/><br/>
 * 
 * @see DistanceFunction
 * @author Philippe Fournier-Viger
 */

public class DistanceJaccard extends DistanceFunction {
	/** the name of this distance function */
	static String NAME = "jaccard";

	/**
	 * Calculate the Jaccard distance between two vectors of doubles, which are
	 * assumed to be either 0s or 1s.
	 * @param vector1 the first vector
	 * @param vector2 the second vector
	 * @return the distance
	 */
	public double calculateDistance(BitArray vector1, BitArray vector2) {
		double count11 = 0;	  // count of M11
		double count10or01or11 = 0; // count of M01, M10 and M11
		
		// for each position in the vector
		for(int i=0; i< vector1.length(); i++){
			// if it is not  two 0s
			if(vector1.get(i) || vector2.get(i)) {
				// if it is two 1s
				if(vector1.get(i)  && vector2.get(i)) {
					count11++;
				}
				// increase the count of not two 0s
				count10or01or11++;
			}
			
		}
		return count11 / count10or01or11;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
