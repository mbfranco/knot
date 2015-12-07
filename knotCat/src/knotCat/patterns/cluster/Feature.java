package knotCat.patterns.cluster;

import java.util.LinkedList;

/**
 * @author miguel
 *
 */
public class Feature {
	
	String name;
	LinkedList<AtomFeature> atom;
	
	public Feature(){}
	
	public Feature(String name, LinkedList<AtomFeature> atom){
		this.name = name;
		this.atom = atom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public LinkedList<AtomFeature> getAtomArray() {
		return atom;
	}
	

	
}
