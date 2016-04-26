package knotCat.patterns.cluster;

import java.util.List;

import knotCat.patterns.cluster.Exceptions.ProbabilityOutOfBoundsException;

/** When searching in a cluster, the result is a Knot name + the probability of that knot being the correct search
 * @author miguel
 *
 */
public class ClusterSearchResult {

	List<ClusterKnot> knot; //the knot
	
	double probability; //certainty of being this knot
	
	FinalCluster treeClusterResult; //the children of this tree cluster
	
	public ClusterSearchResult() {}
	
	public ClusterSearchResult(List<ClusterKnot> knot, double probability, FinalCluster treeClusterResult) {
		this.knot = knot;
		this.probability = probability;
		this.treeClusterResult = treeClusterResult;
	}
	
	public List<ClusterKnot> getKnot() {
		return knot;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public FinalCluster getTreeClusterResult() {
		return treeClusterResult;
	}
	
	public void setKnot(List<ClusterKnot> knot) {
		this.knot = knot;
	}
	
	public void setTreeClusterResult(FinalCluster treeClusterResult) {
		this.treeClusterResult = treeClusterResult;
	}
	
	public void setProbability(double probability) {
		if(probability < 0 || probability > 1){
			try {
				throw new ProbabilityOutOfBoundsException(probability);
			} catch (ProbabilityOutOfBoundsException e) {
				System.err.println(e.getMessage());
			}
		}
		this.probability = probability;
	}
}
