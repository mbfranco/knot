package gui;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.eclipse.swt.widgets.*;

import knotCat.algorithms.clustering.distanceFunctions.DistanceFunction;
import knotCat.algorithms.clustering.distanceFunctions.DistanceHamming;
import knotCat.algorithms.clustering.hierarchical_clustering.AlgoHierarchicalClustering;
import knotCat.patterns.cluster.Browser;
import knotCat.patterns.cluster.Feature;
import knotCat.patterns.cluster.Knot;

public class Gui {

	public static void main (String [] args) throws Exception{
		
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
			atomContent="";
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
//		  DistanceFunction distanceFunction = new DistanceEuclidian();
        DistanceFunction distanceFunction = new DistanceHamming();
//        DistanceFunction distanceFunction = new DistanceJaccard();
//        DistanceFunction distanceFunction = new DistanceCosine();
        
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
		
		
//------------------------------------------------------------------------------------------------------------//
		
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
}
