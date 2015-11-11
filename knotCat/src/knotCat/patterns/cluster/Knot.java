package knotCat.patterns.cluster;


/**
 * Knot structure used by clusters
 * @author miguel
 *
 */
public class Knot {
	
	int reference;
	String name;
	BitArray features;
	
	/**
	 * number of bits to characterize each feature.
	 * 00 -> feature not present in the knot
	 * 11 -> feature present in the knot
	 * 01/10 -> feature probably present/not present
	 */
	static final int UNCERTAINTY = 2;

	
	/** Constructor
	 * @param ref The reference to the ABOK knot entry
	 * @param name The name of the Knot
	 * @param nfeatures Number of features 
	 */
	public Knot(int ref, String name,int nfeatures) {
		reference = ref;
		this.name = name;
		this.features = new BitArray(nfeatures * UNCERTAINTY);
	}
	
	public int getUncertainty(){
		return UNCERTAINTY;
	}

	public BitArray getFeatures() {
		return features;
	}

	public void setFeatures(BitArray features) {
		this.features = features;
	}

	public int getReference() {
		return reference;
	}

	public String getName() {
		return name;
	}
	
	
//	public static void main(String[] args) {
//		
//		Knot kn = new Knot(4);
//		
//		kn.set(1);
//		
//		System.out.println("kn = " + kn.toString() + "   kn.size() = " + kn.length());
//	}
}

