package knotCat.patterns.cluster;


public class AtomFeature {

	Feature feature;
	String atom;

	public AtomFeature(){}

	public AtomFeature(Feature feature, String atomName) {

		this.feature = feature;
		this.atom = atomName;

	}

	public String getAtomName() {
		return atom;
	}

	public Feature getFeature() {
		return feature;
	}

}
