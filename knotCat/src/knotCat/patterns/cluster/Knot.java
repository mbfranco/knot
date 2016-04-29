package knotCat.patterns.cluster;

import java.util.*;


import knotCat.patterns.cluster.Exceptions.FeatureDoesNotExistExcetion;
import knotCat.patterns.cluster.Exceptions.KnotNameAlreadyExistsException;
import knotCat.patterns.cluster.Exceptions.KnotNameDoesNotExistException;

/**
 * Knot structure used by clusters
 * @author miguel
 *
 */
public class Knot {

	List<Integer> references; //Implemented as an ArrayList
	List<String> names; //Implemented as an ArrayList
	BitArray features;
	Map<Integer, BitArray> atoms; //Implemented as TreeMap

	//	/**
	//	 * number of bits to characterize each feature.
	//	 * 00 -> feature not present in the knot
	//	 * 11 -> feature present in the knot
	//	 * 01/10 -> feature probably present/not present
	//	 */
	//	static final int UNCERTAINTY = 2;


	/** Constructor
	 * @param references The references to the ABOK knot entry (a knot may have different entries)
	 * @param names The names of the Knot (a knot may have different names)
	 * @param features Knot's present features in the form of a binary representation
	 * @param atm Knot's present atomic features. Atoms are associated to present Features.
	 * The key is the index of a present feature and the in a binary representation. 
	 */
	public Knot(List<Integer> references, List<String> names, BitArray features, Map<Integer, BitArray> atm) {
		this.references = references;
		this.names = names;
		this.features = features;
		this.atoms = atm;
	}

	/** Constructor
	 * Knot with no atom features
	 * @param r The references to the ABOK knot entry (a knot may have different entries)
	 * @param n The names of the Knot (a knot may have different names)
	 * @param features Knot's present features in the form of a binary representation
	 */
	public Knot(List<Integer> r, List<String> n, BitArray features) {
		this.references = r;
		this.names = n;
		this.features = features;
	}

	//	public int getUncertainty(){
	//		return UNCERTAINTY;
	//	}

	public BitArray getFeatures() {
		return features;
	}

	public List<Integer> getReference() {
		return references;
	}
	
	public List<String> getName() {
		return names;
	}
	
	public Map<Integer, BitArray> getAtoms(){
		return atoms;
	}
	
	
	/** Gets the AtomFeatures for the Nth feature of the knot
	 * @param featureIndex 
	 * @return BitArray 
	 * @throws FeatureDoesNotExistExcetion
	 */
	public BitArray getAtomFeature(int featureIndex) throws Exception{
		
		try{

			if(this.getFeatures().get(featureIndex) == false){
				throw new FeatureDoesNotExistExcetion(featureIndex, this.getName().toString());
			}


		}catch(FeatureDoesNotExistExcetion e){
			System.err.println(e.getMessage());
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("The max value for featureIndex should be " + this.getFeatures().size + " and is " + featureIndex);
		}
		//TODO This return shouldn't be here because the exception persists -> ArrayIndexOutOfBoundsException
		return this.atoms.get(featureIndex);

	}
	
	/**
	 * @return
	 */
	public String printReference() {
		return this.getReference().toString();
	}

	/**
	 * @return
	 */
	public String printName(){
		return this.getName().toString();
	}

	 
	/** Adds Name to an existing Knot
	 * @param existingName Existing name for the knot
	 * @param nameToIntroduce Name to introduce in this knot
	 * @throws KnotNameDoesNotExistException if the existingName doesn't characterize this knot
	 * @throws KnotNameAlreadyExistsException if the nameToIntroduce already characterizes this knot
	 */
	public void addName(String existingName, String nameToIntroduce) throws Exception{
		
		try{
			if(this.getName().contains(existingName) == false){
				throw new KnotNameDoesNotExistException(existingName);
			}
			
			if(this.getName().contains(nameToIntroduce)){
				throw new KnotNameAlreadyExistsException(nameToIntroduce);				
			}

			this.getName().add(nameToIntroduce);
			
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
	
	//TODO public void addFeature(String knot, String feature)


	public static void main(String[] args) throws Exception {
			
		List<Integer> r = new ArrayList<Integer>();
		List<Integer> r1 = new ArrayList<Integer>();
		List<String> n = new ArrayList<String>();
		List<String> n1 = new ArrayList<String>();
		BitArray f = new BitArray(55);
		BitArray f1 = new BitArray(55);

		r.add(1);
		n.add("knot A");
		f.set(7);
		f.set(5);
		f.set(23);

		r1.add(2);
		r1.add(78);
		n1.add("knot B");
		n1.add("knot B1");
		f1.set(2);
		f1.set(45);
		f1.set(44);
		f1.set(13);
		f1.set(21);

		Map<Integer,BitArray> atom = new TreeMap<>();
		BitArray af2 = new BitArray(10);
		BitArray af13 = new BitArray(10);
		af2.set(1);
		af13.set(8);
		af13.set(2);

		atom.put(2, af2);
		atom.put(13, af13);

		Knot kn = new Knot(r, n, f);
		Knot kn1 = new Knot(r1, n1, f1, atom);

		System.out.println("References lenght: " + r.size());
		System.out.println("Names lenght: " + n.size());

		System.out.println("kn = " + kn.printName() + "  " + kn.printReference() + "  " + kn.getFeatures() );

		r.add(5);
		n.add("figure-eight");

		System.out.println("kn = " + kn.printName() + "  " + kn.printReference() + "  " + kn.getFeatures() );

		System.out.println("-------------------------");

		System.out.println("References lenght: " + r1.size());
		System.out.println("Names lenght: " + n1.size());

		System.out.println("kn1 = " + kn1.printName() + "  " + kn1.printReference() + "  " + kn1.getFeatures() + "  " + kn1.getAtoms());
		System.out.println("Inexistent Atom Feature: " + kn1.getAtomFeature(4));

		kn1.addName("knot B1", "knot B2");
		kn1.addName("Fail", "This Fails");
		kn1.addName("knot B1", "knot B2");

		System.out.println("kn1 = " + kn1.printName() + "  " + kn1.printReference() + "  " + kn1.getFeatures() + "  " + kn1.getAtoms());
		System.out.println("Get unbounded FeatureIndex: " + kn1.getAtomFeature(1564));

	}

	public void setFeatures(BitArray bitArray) {
		this.features=bitArray;
		
	}

}

