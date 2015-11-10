package knotCat.patterns.cluster;



/**
 * BitSet array used by clusters
 * @author miguel
 *
 */
/**
 * @author miguel
 *
 */
public class Knot extends BitArray{
	
	/**
	 * number of bits to characterize each feature.
	 * 00 -> feature not present in the knot
	 * 11 -> feature present in the knot
	 * 01/10 -> feature probably present/not present
	 */
	static final int UNCERTAINTY = 3;

	
	/** Constructor
     * @param  size of the bit array
     */	
	public Knot(int n) {
		super(n * UNCERTAINTY);
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

