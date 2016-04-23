package knotCat.patterns.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClusterKnot extends FinalCluster{

	List<String> names = new ArrayList<String>();
	
	double distance;
	
	BitArray features;
	
	//atomFeatures where the Key is the feature Index,
	//and the value is the BitArray of AtomFeatures for that feature
	Map<Integer, BitArray> atomFeatures;
	
	public ClusterKnot() {}
	
	/**
	 * @param list The names of the knot
	 * @param minClusterDistance The distance to the nearest knot
	 * @param features The BitArray of features that characterize the knot
	 * @param atomFeatures Multimap where I is the index number in the atomFeatures LinkedList and V is the atomFeatureBitarray 
	 */
	public ClusterKnot(List<String> names, double minClusterDistance, BitArray features, Map<Integer, BitArray> atomFeatures) {
		this.names = names;
		this.distance = minClusterDistance;
		this.features = features;
		this.atomFeatures = atomFeatures;
	}

	
	public List<String> getNames() {
		return names;
	}

	public double getDistance() {
		return distance;
	}

	public BitArray getFeatures() {
		return features;
	}

	public Map<Integer, BitArray> getAtomFeatures() {
		return atomFeatures;
	}

	
	/**
	 * Checks if the name exists for the knot
	 * @param name
	 * @return true if the name exists for the knot
	 */
	public boolean searchName(String name){
		for(String s : this.getNames()){
			if(name.equals(s)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void print() {
		for(String n : this.getNames()){
			System.out.println("Knot: " + n);
		}

		System.out.println("Features: " + this.getFeatures());

			
		//TODO review this "for each" search
		for(Entry<Integer, BitArray> i : this.getAtomFeatures().entrySet()){
			//TODO Get the name of the feature 
			System.out.println("\tThe feature in the index \"" + i.getKey() + "\" has the AtomFeatures " + i.getValue());
		}

	}


}
