package knotCat.patterns.cluster;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Knot structure used by clusters
 * @author miguel
 *
 */
public class Knot {
	
	int[] references;
	String[] names;
	BitArray features;
	LinkedList<BitArray> atoms;
	
//	/**
//	 * number of bits to characterize each feature.
//	 * 00 -> feature not present in the knot
//	 * 11 -> feature present in the knot
//	 * 01/10 -> feature probably present/not present
//	 */
//	static final int UNCERTAINTY = 2;

	
	/** Constructor
	 * @param references The references to the ABOK knot entry
	 * @param names The names of the Knot
	 * @param features Present features 
	 * @param atom Present atomic features
	 */
	public Knot(int[] references, String[] names, BitArray features, LinkedList<BitArray> atoms) {
		this.references = references;
		this.names = names;
		this.features = features;
		this.atoms = atoms;
	}
	
	/** Constructor
	 * Knot with no atom features
	 * @param references The references to the ABOK knot entry
	 * @param names The names of the Knot
	 * @param features Present features 
	 * @param atom Present atomic features
	 */
	public Knot(int[] references, String[] names, BitArray features) {
		this.references = references;
		this.names = names;
		this.features = features;
	}
	
//	public int getUncertainty(){
//		return UNCERTAINTY;
//	}

	public BitArray getFeatures() {
		return features;
	}

	public void setFeatures(String feature) {
		//TODO Has to call 
	}

	public int[] getReference() {
		return references;
	}
	
	@SuppressWarnings("unused")
	private String printReference() {
		return Arrays.toString(this.getReference());
	}

	public String[] getName() {
		return names;
	}
	
	public String printName(){
		return Arrays.toString(this.getName());
	}
	
	/** Search nameToIntroduce in the FeatureNames vector
	 * 		if exists NameAlreadyExistsException
	 * 		else insert FeatureNames in the otherName index
	 */
	public void addName(String otherName, String nameToIntroduce){
		//TODO 
	}
	
//	public static void main(String[] args) {
//		
//		int[] r = {1};
//		String[] n = {"knot"};
//		BitArray f = new BitArray(55);
//		
//		f.set(7);
//		f.set(5);
//		f.set(23);
//		
//		Knot kn = new Knot(r, n, f);
//		
//		
//		
//		System.out.println("kn = " + kn.printName() + "  " + kn.printReference() + "  " + kn.getFeatures() );
//	}

}

