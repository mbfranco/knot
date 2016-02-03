package knotCat.patterns.cluster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import knotCat.patterns.cluster.Exceptions.AtomNameAlreadyExistsException;
import knotCat.patterns.cluster.Exceptions.FeatureAlreadyExistsException;
import knotCat.patterns.cluster.Exceptions.FeatureDoesNotExistExcetion;

public class Browser {

	//Maximum number of supported features for one knot. Because in each knot the BitArray of features has a static length.
	//Should the feature array length be static? Because in each knot the array of features' length is static..
		static int NUMFEATURES = 55;
	//	
	//	Feature[] featureArray = new Feature[NUMFEATURES]; //

	static String SESSION = "Browser";

	/**
	 * List with all the features' names. All features have different names.
	 */
	LinkedList<Feature> featureNames;

	/**
	 * List with all the knots. All knots are different.
	 */
	LinkedList<Knot> knotList;
	/**
	 * List with all the knots' names. All knots have different names.
	 */
	LinkedList<String> knotNames;

	public Browser(LinkedList<Feature> featureNames, LinkedList<Knot> knotList, LinkedList<String> knotNames) {
		this.featureNames = featureNames;
		this.knotList = knotList;
		this.knotNames = knotNames;
	}

	/**  
	 * Returns the index of a certain feature in the featureNames LinkedList
	 * @param name - the name of the feature
	 * @return index of that knot in the featureNames LinkedList
	 */
	public int getFeatureIndex(String name) {
		return featureNames.indexOf(name);
	}

	
	/** Method that should be used for adding a knot because it creates the knot and updates the lists that control the system integrity
	 * @param references ABOK reference numbers
	 * @param names Knot names
	 * @param features Knot features
	 * @param atoms Knot Atom Features
	 * @throws Exception see Knot class
	 */
	public void addKnot(List<Integer> references, List<String> names, ArrayList<String> features, Vector<String> atomsF, Vector<String> atomsA) throws Exception{
		
		BitArray feat = new BitArray(NUMFEATURES);
		Map<Integer, BitArray> atm = new TreeMap<Integer,BitArray>();
		
		for(String f : features){
			//updates the "featureNames" LinkedList 
			insertFeature(f);
			//updates the knot BitArray of features
			int idx = features.indexOf(f);
			feat.set(idx);
		}
		
		//update AtomFeatures
		for (int i = 0; i < atomsF.size(); i++) {
			String f = atomsF.elementAt(i);
			int j = this.featureNames.indexOf(f);
			j
			
		}
		
		
		
		Knot k = new Knot(references, names, feat, atm);
		//Updates knotList & knotNames
		insertKnot(k);
		
		
	}
	
	
	
	
	/**
	 * Inserts a feature in the featureNames LinkedList
	 * @param name - feature's name
	 */
	protected void insertFeature(String name) throws Exception{
		try{
			if(featureNames.contains(name)){
				throw new FeatureAlreadyExistsException(name);
			}else{
				LinkedList<AtomFeature> l = new LinkedList<AtomFeature>();
				Feature f = new Feature(name, l);
				featureNames.add(f);
			}
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	
	/** Updates knotList and knotNames when adding a knot
	 * @param knot
	 */
	protected void insertKnot(Knot knot){
		knotList.add(knot);
		for(String k : knot.getName()){
			knotNames.addLast(k);
		}
	}

	/**Inserts a new Atom for a certain Feature. Checks if it an existing Feature and if the Atom is not already present
	 * @param feature feature's name
	 * @param atom atom's name
	 */
	protected void insertAtom(String feature, String atom) throws Exception{
		try{
			int index = this.featureNames.indexOf(feature);
			if(index == -1){
				throw new FeatureDoesNotExistExcetion(index, feature);
			}

			Feature f = this.featureNames.get(index);
			//TODO verify if it works for an Empty List
			LinkedList<AtomFeature> l = f.getAtomArray();
			AtomFeature a = new AtomFeature(f,atom);

			ListIterator<AtomFeature> li = l.listIterator();		
			while(li.hasNext()){
				if(li.equals(a)){
					throw new AtomNameAlreadyExistsException();
				}
			}
			
			l.addLast(a); //TODO check if it adds the element if the list is empty
			
		}catch(Exception e){
			e.getMessage();
		}
	}

	public static void main(String[] args) throws Exception{
		
		LinkedList<Feature> featureNames = new LinkedList<Feature>();
		LinkedList<Knot> knotList = new LinkedList<Knot>();
		LinkedList<String> knotNames = new LinkedList<String>();
		
		Browser browser = new Browser(featureNames, knotList, knotNames);

		String inputFileName = "source.txt";
		Path inputPath = Paths.get(inputFileName);
		File outputFile = new File("output.txt");
		
		//content to be written to file
		String numberContent = "";
		String nameContent = "";
		String featureContent = "";
		String atomContent = "";
		
		// if the output file doesn't exist, then create it
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		
		Scanner scanner = new Scanner(inputPath);
		//read line by line
		
        while(scanner.hasNextLine()){
            //process each line
        	String line = scanner.nextLine();
        	String[] ss = line.split(", ");
        	
        	//save each Knot field in a local variable
        	int i = 0;
        	for(String s : ss){
        		if(i % 4 == 0){
        			System.out.println("ABOK number: " + s);
        			numberContent = s;
        			i++;	
        		}
        		else if(i % 4 == 1){
        			System.out.println("Name: " + s);
        			nameContent = s;
        			i++;	
        		}
        		else if(i % 4 == 2){
        			System.out.println("Features: " + s);
        			featureContent = s;
        			i++;	
        		}
        		else if(i % 4 == 3){
        			System.out.println("Atom Features: " + s);
        			atomContent = s;
        			i++;	
        		}
        	}
        	
        	//references
        	List<Integer> references = new ArrayList<Integer>();
        	String[] sn = numberContent.split(" ");
        	for(String a : sn){
        		System.out.println("\tEach number: " + a + "\n");
        		
        		int n =	Integer.parseInt(a);
        		references.add(n);
        		
        		System.out.println("Ref: "+references);
        	}
        	
        	//names
			List<String> names = new ArrayList<String>();
			String[] na = nameContent.split(" ");
        	for(String a : na){
        		System.out.println("\tEach Name: " + a + "\n");
        		
        		names.add(a);
        		
        		System.out.println("Name: "+names);
        	}

        	//features
			ArrayList<String> features = new ArrayList<>();
			String[] nf = featureContent.split(" ");
        	for(String a : nf){
        		System.out.println("\tEach Feature: " + a + "\n");
        		
        		features.add(a);
        		
        		System.out.println("Features: "+features);
        	}
        	
        	//atoms
        	int j = 0;
        	Vector<String> atomsF = new Vector<String>();
        	Vector<String> atomsA = new Vector<String>();
        	Map<String, String> atoms = new TreeMap<String, String>();
        	String[] aa = atomContent.split("\\.| ");
        	for(String a : aa){
        		//split feature from atom
        		if(j%2==0){
        			System.out.println("\t\tFeature: "+ aa[j]);
        			atomsF.addElement(aa[j]);
        		        			
        		}else{
        			System.out.println("\t\tAtoms: "+ aa[j]);
        			atomsA.addElement(aa[j]);
        			
        			//the atom is ready to be created in the second iteration only
//        			atoms.put(fea, ato);
        		}
        		j++;
        	}
        	System.out.println("Atoms: "+atoms);

			
			//creating the knot..
			browser.addKnot(references, names, features, atomsF, atomsA);

        }
        		
	}
}