package knotCat.patterns.cluster;

import java.awt.LinearGradientPaint;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;

import knotCat.algorithms.clustering.distanceFunctions.DistanceEuclidian;
import knotCat.algorithms.clustering.distanceFunctions.DistanceFunction;
import knotCat.algorithms.clustering.distanceFunctions.DistanceHamming;
import knotCat.algorithms.clustering.hierarchical_clustering.AlgoHierarchicalClustering;
import knotCat.patterns.cluster.Exceptions.AtomNameAlreadyExistsException;
import knotCat.patterns.cluster.Exceptions.FeatureAlreadyExistsException;
import knotCat.patterns.cluster.Exceptions.FeatureDoesNotExistExcetion;

public class Browser {

	
	
	
	//Maximum number of supported features for one knot. Because in each knot the BitArray of features has a static length.
	//Should the feature array length be static? Because in each knot the array of features' length is static..
	static final int NUMFEATURES = 40;
	
	//Maximum number of atom features for each knot
	static final int NUMATOMS = 25;
		
	/**
	 * Tree Cluster of Knots
	 * Each entry in the ArrayListMultimap is a cluster of knots.
	 * This cluster is represented thorough an ArrayListMultimap<K,V> where
	 * Key(K) is an Integer, which corresponds to the number of the cluster
	 * Value(V) is a ClusterKnot, with variables: Features, Atoms, Names, Distance
	 * @see ClusterKnot
	 */
	//ArrayListMultimap<Integer, ClusterKnot> finalCluster;
	List<FinalCluster> finalCluster = new ArrayList<FinalCluster>();
	
	/**
	 * List with all the features' names. All features have different names.
	 */
	LinkedList<Feature> featureNames;

	
	/**
	 * Inverted index of all the atomFeature names. Each entry is
	 * an atomFeature name and the mapping is the features that have
	 * that atomFeature name.
	 * String - AtomFeature name
	 * Integer - index of the Feature
	 */
	SetMultimap<String, Integer> atomFeatureNames = LinkedHashMultimap.<String, Integer>create();
	
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

	
	
	public LinkedList<Feature> getFeatureNames() {
		return featureNames;
	}


	public SetMultimap<String, Integer> getAtomFeatureNames() {
		return atomFeatureNames;
	}
	

	public LinkedList<Knot> getKnotList() {
		return knotList;
	}



	public LinkedList<String> getKnotNames() {
		return knotNames;
	}


	public List<FinalCluster> getFinalCluster() {
		return finalCluster;
	}

	/**  
	 * Returns the index of a certain feature in the featureNames LinkedList
	 * @param name - the name of the feature
	 * @return index of that knot in the featureNames LinkedList
	 * @throws IndexOutOfBoundsException if the feature doesn't exist
	 */
	public int getFeatureIndex(String name) throws IndexOutOfBoundsException{
		
		int i = -1;
		
		for(Feature f : getFeatureNames()){
			if(f.getName().equals(name)){
				i = getFeatureNames().indexOf(f);
				break;
			}
		}
		
		try{
			if(i == -1){
				throw new IndexOutOfBoundsException("The feature \"" + name + "\" does not exist.");
			}
		}catch(IndexOutOfBoundsException e){
			e.getMessage();
		}
		
		return i;
	}
	
	
//	public Knot getKnotByFeatures(BitArray knotFeatures){
//		for(Knot k : this.getKnotList()){
//			k.getFeatures()
//		}
//	}
	
	public int getAtomFeatureIndex(int featureIndex, String atom){
		
		int i = -1;
		Feature feature = getFeatureNames().get(featureIndex);
		for(AtomFeature a : feature.getAtomFeatures()){
			if(a.getAtomName().equals(atom)){
				i = getFeatureNames().get(featureIndex).getAtomFeatures().indexOf(a);
				break;
			}
		}
		
		return i;
	}
	
	
	/**Returns the index of a certain atom of a feature in the featureNames LinkedList
	 * @param feature name of the feature
	 * @param atom name of the atom
	 * @return index of the atom feature
	 */
	public int getAtomFeatureIndex(String feature, String atom) throws IndexOutOfBoundsException{

		int fi = -1;
		int ai = -1;

		try{
			
			fi = getFeatureIndex(feature);
			ai = getAtomFeatureIndex(fi, atom);
			
			if(fi == -1){
				throw new IndexOutOfBoundsException("The feature \"" + feature + "\" does not exist.");
			}
			if(ai == -1){
				throw new IndexOutOfBoundsException("The atom \"" + atom + "\" does not exist for the feature \"" + feature + "\".");
			}

			return ai;


		}catch(IndexOutOfBoundsException e){
			e.getMessage();
		}
		
		return ai;

	}

	
	/** Method that should be used for adding a new knot. It creates the knot and updates the lists that control the system integrity
	 * @param references ABOK reference numbers
	 * @param names Knot names
	 * @param features Knot features
	 * @param atomsF Knot Atom Features - Features
	 * @param atomsA Knot Atom Features - Atoms
	 * @throws Exception see Knot class
	 */
	public void addNewKnot(List<Integer> references, List<String> names, ArrayList<String> features, Vector<String> atomsF, Vector<String> atomsA) throws Exception{
		
		//TODO use LinkedMultimap to avoid having atomsF and atomA (cleaner code)
		
		BitArray featureBitArray = new BitArray(NUMFEATURES);
		
		Map<Integer, BitArray> atm = new TreeMap<>();
		
		//Knot's Features
		for(String f : features){
			//updates the "featureNames" LinkedList 
			insertFeature(f);
			//get the this knot's feature BitArray index and updates it
			int idx = getFeatureIndex(f);
			featureBitArray.set(idx);
		}
		
		//Knot's AtomFeatures
		for (int i = 0; i < atomsF.size() && (atomsF.get(i) != null); i++) {
			BitArray atomBitArray = new BitArray(NUMATOMS);
			//Get the Feature at which the Atom belongs
			String featureName = atomsF.get(i);//elementAt(i);
			
			String atomName = atomsA.get(i);//elementAt(i);
			//Check if the atom already exists for the feature
			insertAtomFeature(featureName, atomName); 
	
			//update knot's atom BitArray
			Integer atomIndex = getAtomFeatureIndex(featureName, atomName);
			Integer featureIndex = getFeatureIndex(featureName);
			
			if(atm.get(featureIndex) != null){
				atomBitArray = atm.get(featureIndex);
			}
			
			atomBitArray.set(atomIndex);
			
			
			atm.put(featureIndex, atomBitArray);
			
		}
		
		
		
		Knot k = new Knot(references, names, featureBitArray, atm);
		//Updates knotList & knotNames
		insertKnot(k);
		
		
	}
	
	
	
	
	/**
	 * Inserts a feature in the featureNames LinkedList
	 * @param name - feature's name
	 */
	public void insertFeature(String name) throws Exception{
		
		if(getFeatureNames().isEmpty()){
			LinkedList<AtomFeature> l = new LinkedList<AtomFeature>();
			Feature ft = new Feature(name, l);
			getFeatureNames().add(ft);
		}else if(!getFeatureNames().isEmpty()){
			try{
				for(Feature f : getFeatureNames()){
					if(f.getName().equals(name)){
						throw new FeatureAlreadyExistsException(name);
					}
				}
				LinkedList<AtomFeature> l = new LinkedList<AtomFeature>();
				Feature ft = new Feature(name, l);
				getFeatureNames().add(ft);
			}catch(Exception e){
				e.getMessage();
			}
		}
	}
	
	/**
	 * Adds an atomFeature to the atomFeatureNames LinkedList
	 * @param name - atom feature's name
	 */
	
	public void addAtomFeatureToList(String name, int index) throws Exception{

		try{
			
			getAtomFeatureNames().put(name, index);
			
			System.out.println("Put Atom " + name + " in the feature " + getFeatureNames().get(index).getName() + " (index = " + index + ")");
			
		}catch(Exception e){
			e.getMessage();
		}

	}
	
	
	
	/** Updates knotList and knotNames when adding a knot
	 * @param knot
	 */
	public void insertKnot(Knot knot){
		knotList.add(knot);
		for(String k : knot.getName()){
			knotNames.addLast(k);
		}
	}

	/**Inserts a new Atom for a certain Feature. Checks if it an existing Feature and if the Atom is not already present
	 * @param feature feature's name
	 * @param atom atom's name
	 */
	public void insertAtomFeature(String feature, String atom) throws Exception{
		int index = -1;
		
		try{
			for(Feature a : getFeatureNames()){
				if(a.getName().equals(feature)){
					index = getFeatureNames().indexOf(a);
					break;
				}
			}
			if(index == -1){
				throw new FeatureDoesNotExistExcetion(index, feature);
			}

			Feature f = getFeatureNames().get(index);


			for(AtomFeature a : f.getAtomFeatures()){
				if(a.getAtomName().equals(atom)){
					throw new AtomNameAlreadyExistsException();
				}
			}
			
			AtomFeature a = new AtomFeature(f,atom);
			
			//Updates the Atoms for this Feature
			getFeatureNames().get(index).getAtomFeatures().addLast(a);
			
			//Updates the LinkedList of AtomFeatures
			//addAtomFeatureToList(f.getName(), index);
			
			//TODO probably is
			addAtomFeatureToList(atom, index);
			
		}catch(Exception e){
			e.getMessage();
		}
	}

	public static void main(String[] args) throws Exception{
		
		
		LinkedList<Feature> featureNa = new LinkedList<Feature>();
		LinkedList<Knot> knotLi = new LinkedList<Knot>();
		LinkedList<String> knotNam = new LinkedList<String>();
		
		Browser browser = new Browser(featureNa, knotLi, knotNam);

		String inputFileName = "source.txt";
		Path inputPath = Paths.get(inputFileName);
		String outputFileName = "output.txt";
		File outputFile = new File(outputFileName );
		
		//content to be written to file
		String numberContent = "";
		String nameContent = "";
		String featureContent = "";
		String atomContent = "";
		
		// if the output file doesn't exist, then create it
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		
		@SuppressWarnings("resource")
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
//        			System.out.println("ABOK number: " + s);
        			numberContent = s;
        			i++;	
        		}
        		else if(i % 4 == 1){
//        			System.out.println("Name: " + s);
        			nameContent = s;
        			i++;	
        		}
        		else if(i % 4 == 2){
//        			System.out.println("Features: " + s);
        			featureContent = s;
        			i++;	
        		}
        		else if(i % 4 == 3){
//        			System.out.println("Atom Features: " + s);
        			atomContent = s;
        			i++;	
        		}
        	}
        	
        	//references
        	List<Integer> references = new ArrayList<Integer>();
        	String[] sn = numberContent.split(" ");
        	for(String a : sn){
//        		System.out.println("\tEach number: " + a + "\n");
        		
        		int n =	Integer.parseInt(a);
        		references.add(n);
        		
//        		System.out.println("Ref: "+references);
        	}
        	
        	//names
			List<String> names = new ArrayList<String>();
			String[] na = nameContent.split(" ");
        	for(String a : na){
//        		System.out.println("\tEach Name: " + a + "\n");
        		
        		names.add(a);
        		
//        		System.out.println("Name: "+names);
        	}

        	//features
			ArrayList<String> features = new ArrayList<>();
			String[] nf = featureContent.split(" ");
        	for(String a : nf){
//        		System.out.println("\tEach Feature: " + a + "\n");
        		
        		features.add(a);
        		
//        		System.out.println("Features: "+features);
        	}
        	
        	//atoms
        	int j = 0;
        	Vector<String> atomsF = new Vector<String>();
        	Vector<String> atomsA = new Vector<String>();
        	String[] aa = atomContent.split("\\.| ");
        	for(@SuppressWarnings("unused") String a : aa){
        		//split feature from atom
        		if(j%2==0){
//        			System.out.println("\t\tFeature: "+ aa[j]);
        			atomsF.addElement(aa[j]);
        		        			
        		}else{
//        			System.out.println("\t\tAtoms: "+ aa[j]);
        			atomsA.addElement(aa[j]);
        			
        			//the atom is ready to be created in the second iteration only
        		}
        		j++;
        	}
			
			//creating the knot..
			browser.addNewKnot(references, names, features, atomsF, atomsA);

        }

//        System.out.println("-------------------------------------");
//        System.out.println("ALL KNOTS: " + browser.getKnotNames().toString()+"\n");
//        
//        for(Knot f : browser.getKnotList()){
//        	System.out.println(f.getName().toString());
//        	System.out.println(f.getFeatures());
//        	
//        	for(Map.Entry<Integer, BitArray> i : f.getAtoms().entrySet()){
//        		Integer key = i.getKey();
//        		BitArray value = i.getValue();
//        		System.out.println("Feature: "+key+" - Atom: "+value);
//        		
//        		
//        	}
//        }
//        
//        for(Feature f : browser.getFeatureNames()){
//        	System.out.println("\nFeature: " + f.getName());
//        	for(AtomFeature a : f.getAtomFeatures()){
//        		System.out.println("Atom: " + a.getAtomName());
//        	}
//        }
        
        
        AlgoHierarchicalClustering hc = new AlgoHierarchicalClustering();
        //DistanceFunction distanceFunction = new DistanceEuclidian();
        DistanceFunction distanceFunction = new DistanceHamming();
        
        for(int i=0;i<browser.getKnotList().size()-1;i++){
        	System.out.println(browser.getKnotList().get(i).getName().toString());
        	System.out.println(browser.getKnotList().get(i).getFeatures().toString());
        }
        
        
//        for(String s : browser.getAtomFeatureNames().keys()){
//        	System.out.println("Atom Features: " + s);
//        }	
        
        hc.runAlgorithm(6, distanceFunction, browser);
        hc.printStatistics();
        hc.saveToFile(outputFileName);
        //TODO print não está a ser bem feito
        for(FinalCluster fc : browser.getFinalCluster()){
        	fc.print();
        }
        
        Search s = new Search(outputFile, browser);
        
        s.searchForKnot("tie", 0);
        
        
//        while(true){
//        	
//        	System.out.println("Enter the 'features' to search separated by a \"space\": ");
//			System.out.println("If you want any 'sub-features' add a \".\" between the feature and the sub-feature: ");
//            try{
//                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//                String s = bufferRead.readLine();
//                
//                Search search = new Search(outputFile, browser);
//                
//                //System.out.println(search); //TODO print the supposed result of the search
//            }
//            catch(IOException e)
//            {
//                e.printStackTrace();
//            }
        	
//        }
        
	}
}