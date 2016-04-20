package knotCat.patterns.cluster;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

public class ClusterKnot extends FinalCluster{

	ArrayList<String> names;
	
	int distance;
	
	BitArray features;
	
	//atomFeatures where the Key is the feature Index,
	//and the value is the BitArray of AtomFeatures for that feature
	Multimap<Integer, BitArray> atomFeatures;
	
	public ClusterKnot() {}
	
	/**
	 * @param names The names of the knot
	 * @param distance The distance to the nearest knot
	 * @param features The BitArray of features that characterize the knot
	 * @param atomFeatures Multimap where I is the index number in the atomFeatures LinkedList and V is the atomFeatureBitarray 
	 */
	public ClusterKnot(ArrayList<String> names, int distance, BitArray features, Multimap<Integer, BitArray> atomFeatures) {
		this.names = names;
		this.distance = distance;
		this.features = features;
		this.atomFeatures = atomFeatures;
	}

	
	public ArrayList<String> getName() {
		return names;
	}

	public int getDistance() {
		return distance;
	}

	public BitArray getFeatures() {
		return features;
	}

	public Multimap<Integer, BitArray> getAtomFeatures() {
		return atomFeatures;
	}

	
	/**
	 * Checks if the name exists for the knot
	 * @param name
	 * @return true if the name exists for the knot
	 */
	public boolean searchName(String name){
		for(String s : this.getName()){
			if(name.equals(s)){
				return true;
			}
		}
		
		return false;
	}

	public List<String> getNames() {
		return names;
	}

	@Override
	public void print() {
		for(String n : this.getNames()){
			System.out.println("Knot: " + n);
		}
		
		System.out.println("Features: " + this.getFeatures());
		
		//TODO review this "for each" search
		for(Integer i : this.getAtomFeatures().keySet()){
			//TODO Get the name of the feature 
			for(BitArray b : this.getAtomFeatures().get(i)){
				System.out.println("Atom Features: " + b);
			}
		}
		
	}

}
