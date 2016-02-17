package knotCat.patterns.cluster;

/* This file is copyright (c) 2008-2012 Philippe Fournier-Viger
* 
* This file is part of the SPMF DATA MINING SOFTWARE
* (http://www.philippe-fournier-viger.com/spmf).
* 
* SPMF is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* SPMF. If not, see <http://www.gnu.org/licenses/>.
*/

/**
* This class represents a cluster found by a clustering algorithm such as K-Means.
* A cluster is a list of vectors of doubles.
* 
*  @see BitArray
 * @author Philippe Fournier-Viger
 */
public class ClusterWithMean extends Cluster {
	private BitArray mean;  // the mean of the vectors in this cluster
	
	BitArray sum; // the sum of all vectors in this clusters 
	// (used to calculate the mean efficiently)
	
	/**
	 * Constructor
	 * @param vectorsSize the size of the vectors to be stored in this cluster
	 */
	public ClusterWithMean(int vectorsSize){
		super();
		sum = new BitArray(vectorsSize);
	}
	
	/**
	 * Setter for the mean of this cluster.
	 * @param mean A vector of double that will be set as the mean of this cluster.
	 */
	public void setMean(BitArray mean){
		this.mean = mean;
	}
	
	/**
	 * Add a vector of bit to this cluster.
	 * @param vector The vector of doubles to be added.
	 */
	public void addVector(BitArray vector) {
		super.addVector(vector);
		for(int i=0; i < vector.length(); i++){
			boolean isFeaturePresent;
			isFeaturePresent = vector.get(i);
			if(isFeaturePresent){
				sum.set(i);
			}
		}
	}

	/**
	 * Getter for the mean
	 * @return return the mean of this cluster
	 */
	public BitArray getmean() {
		return mean;
	}

	/**
	 * This method is called by clustering algorithms to recompute the mean
	 * of the cluster.
	 */
	public void recomputeClusterMean() {
		for(int i=0; i < sum.getBarray().length; i++){
			mean.getBarray()[i] = sum.getBarray()[i] / vectors.size();
		}
	}

	
	/**
	 * Method to remove a vector from this cluster and update
	 * the internal sum of vectors at the same time.
	 * @param vector  the vector to be removed
	 */
	public void remove(BitArray vector) {
		super.remove(vector);
		// remove from sum
		for(int i=0; i < vector.getBarray().length; i++){
			sum.getBarray()[i] -= vector.getBarray()[i];
		}
		
	}

	
}