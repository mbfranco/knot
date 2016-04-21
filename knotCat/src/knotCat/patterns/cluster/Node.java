package knotCat.patterns.cluster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import knotCat.patterns.cluster.Exceptions.MoreThanTwoChildException;

/** Distance is an element of the final dendrogram that has TWO branches. They are either:
 * ClusterKnot (leaf) + Node (non-leaf element);
 * or ClusterKnot (leaf) + ClusterKnot (leaf);
 * or Node (non-leaf element) + Node (non-leaf element)
 * @author miguel
 *
 */
public class Node extends FinalCluster {

//	Browser browser;
	double distance;
	ArrayList<FinalCluster> branches = new ArrayList<FinalCluster>();
	
	public Node(double distance, List<FinalCluster> finalCluster) {
		this.distance = distance;
	}
	
	public Node() {
	}

	@Override
	public void print() {
		
		System.out.println("Distance: " + this.getDistance());
		//Iterator<FinalCluster> nodeIterator = this.branches.iterator();
		Iterator<FinalCluster> branchesIterator = branches.iterator();
		
		while(branchesIterator.hasNext()){
			
			FinalCluster fc = branchesIterator.next();
			fc.print();
		}
		
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
//	public Browser getBrowser() {
//		return browser;
//	}
	
	public void add(FinalCluster branch){
		if(this.getFinalCluster().size() >= 2){
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
