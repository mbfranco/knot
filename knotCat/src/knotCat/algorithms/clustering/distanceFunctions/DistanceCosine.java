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
 * This class implements the cosine distance function. It is a subclass of the
 * DistanceFunction class which represent any distance function.
 * <br/><br/>
 * 
 * @see DistanceFunction
 * @author Philippe Fournier-Viger
 */

public class DistanceCosine extends DistanceFunction {
	/** the name of this distance function */
	static String NAME = "cosine";
	
	/**
	 * Calculate the Cosine distance between two vectors of doubles.
	 * @param vector1 the first vector
	 * @param vector2 the second vector
	 * @return the distance
	 */
	public double calculateDistance(BitArray vector1, BitArray vector2) {
		double dotproduct = 0;	
		double norm1 = 0;
		double norm2 = 0;
		for(int i=0; i< vector1.length(); i++){
			int n=0,m=0;
			if(vector1.get(i)){n=1;}
			if(vector2.get(i)){m=1;}
			dotproduct += n * m;
			norm1 += Math.pow(n,2);
			norm2 += Math.pow(m,2);
		}
		if(norm1 == 0 || norm2 == 0) {
			return 0;
		}
		return 1d - (dotproduct / (Math.sqrt(norm1) * Math.sqrt(norm2)));
	}
		
	@Override
	public String getName() {
		return NAME;
	}
	

}
