package knotCat.patterns.cluster;

import java.util.List;

import knotCat.patterns.cluster.Exceptions.ProbabilityOutOfBoundsException;

/** When searching in a cluster, the result is a Knot name + the probability of that knot being the correct search
 * @author miguel
 *
 */
public class ClusterSearchResult {

	List<String> knot; // name of the knot
	
	double probability; //certainty of being this knot
	
	public ClusterSearchResult() {}
	
	public ClusterSearchResult(List<String> knotNames, double probability) {
		this.knot = knotNames;
		this.probability = probability;
	}
	
	public List<String> getKnot() {
		return knot;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public void setKnot(List<String> knot) {
		this.knot = knot;
	}
	
	public void setProbability(double probability) {
		if(probability < 0 || probability > 1){
			try {
				throw new ProbabilityOutOfBoundsException(probability);
			} catch (ProbabilityOutOfBoundsException e) {
				e.getMessage();
			}
		}
		this.probability = probability;
	}
	
}
