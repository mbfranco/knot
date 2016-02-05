package knotCat.patterns.cluster;

import knotCat.patterns.cluster.Exceptions.*;

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

	public LinkedList<AtomFeature> getAtomFeatures() {
		return atom;
	}

	public void addAtomFeature(String atomFeature) throws Exception{
		AtomFeature a = new AtomFeature(this, atomFeature);

		try{
			for(AtomFeature at : this.getAtomFeatures()){
				if(at.getAtomName().equals(atomFeature)){
					throw new AtomNameAlreadyExistsException(atomFeature);
				}
			}

			atom.add(a);
		}catch(AtomNameAlreadyExistsException e){
			System.err.println(e.getMessage());
		}
	}

	//	public static void main(String[] args) throws Exception {
	//		LinkedList<AtomFeature> l = new LinkedList<>();
	//		Feature f = new Feature("strong", l);
	//		
	//		f.addAtomFeature("very");
	//		f.addAtomFeature("tension");
	//		f.addAtomFeature("tension");
	//		System.out.println("Feature: " + f.getName());
	//		for(AtomFeature a : f.getAtomArray())
	//			System.out.println("Atom Features: " + a.getAtomName());
	//	}

}
