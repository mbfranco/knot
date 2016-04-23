package knotCat.patterns.cluster;

import java.util.ArrayList;
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
	//A BitArray with all the present features in this node's leafs
	BitArray features;
	
	List<String> names;
	
	public Node(double distance, List<FinalCluster> finalCluster) {
		this.distance = distance;
	}
	
	public Node() {}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public ArrayList<FinalCluster> getBranches() {
		return branches;
	}
	
	public BitArray getFeatures() {
		return features;
	}
	
	public void setFeatures(BitArray features) {
		this.features = features;
	}
	
	public List<String> getNames() {
		return names;
	}
	
	public void setNames(List<String> names) {
		this.names = names;
	}
	
//	public Browser getBrowser() {
//	return browser;
//	}
	
	@Override
	public void print() {
		
		System.out.println("Distance: " + this.getDistance());
		
		for(FinalCluster fc : branches){
			fc.print();
		}
		
//		Iterator<FinalCluster> branchesIterator = branches.iterator();
//		
//		while(branchesIterator.hasNext()){
//			
//			FinalCluster fc = branchesIterator.next();
//			fc.print();
//		}
	}
	
	public void add(FinalCluster branch){
		if(this.getBranches().size() >= 2){
			String msg = "";
			try {
				throw new MoreThanTwoChildException(msg + this.getBranches().size());
			} catch (MoreThanTwoChildException e) {
				e.getMessage();
			}
		}else{
			branches.add(branch);
			if(features == null){
				features = branch.getFeatures();
			}else{
				BitArray b = this.getFeatures();
				features = b.or(branch.getFeatures());
			}
			if(names == null){
				names = branch.getNames();
			}else{
				List<String> l = this.getNames();
				l.addAll(branch.getNames());
				names = l;
			}
			
		}
	}
}
