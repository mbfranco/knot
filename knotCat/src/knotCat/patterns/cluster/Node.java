package knotCat.patterns.cluster;

import java.util.ArrayList;
import java.util.List;

import knotCat.patterns.cluster.Exceptions.MoreThanTwoChildException;

/** Distance is an element of the final dendrogram that has TWO branches. They are either:
 * ClusterKnot (leaf) + Distance (non-leaf element);
 * or ClusterKnot (leaf) + ClusterKnot (leaf)
 * @author miguel
 *
 */
public class Node extends FinalCluster {

	Browser browser;
	int distance;
	List<FinalCluster> branches = new ArrayList<FinalCluster>();
	
	public Node(int distance, List<FinalCluster> finalCluster) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void print() {
		
		for(FinalCluster fc : this.branches){
			System.out.println("Distance " + this.distance);
			fc.print();
		}
		
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public Browser getBrowser() {
		return browser;
	}
	
	public void add(FinalCluster branch){
		if(this.getFinalCluster().size() > 2){
			String msg = "";
			try {
				throw new MoreThanTwoChildException(msg + this.getFinalCluster().size());
			} catch (MoreThanTwoChildException e) {
				e.getMessage();
			}
		}else{
			branches.add(branch);
		}
	}
	
	public List<FinalCluster> getFinalCluster() {
		return branches;
	}
	
}
