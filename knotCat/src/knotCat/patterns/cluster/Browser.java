package knotCat.patterns.cluster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import knotCat.patterns.cluster.Exceptions.FeatureAlreadyExistsException;

public class Browser {

	//	Should the feature array length be static? Because in each knot the array of features' length is static..

	//	static int NUMFEATURES = 5000;
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

	/**
	 * Inserts a feature in the featureNames LinkedList
	 * @param name - feature's name
	 */
	public void insertFeature(String name){
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

	/**
	 * @param feature feature's name
	 * @param atom atom's name
	 */
	public void insertAtom(String feature, String atom){

		int index = this.featureNames.indexOf(feature);
		if(index == -1){
			//TODO throw FeatureDoesntExistException
			System.out.println("A feature with that name doesn't exist");
		}

		Feature f = this.featureNames.get(index);
		//TODO verify
		LinkedList<AtomFeature> l = f.getAtomArray();
		AtomFeature a = new AtomFeature(f,atom);

		ListIterator<AtomFeature> li = l.listIterator();		
		while(li.hasNext()){
			if(li.equals(a)){
				//TODO throw AtomFeatureAlreadyExistsException
				System.out.println("An antom feature with that name already exists");
			}
		}
		l.addLast(a); //TODO check if it adds the element if the list is empty
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//		Scanner scanner = new Scanner(System.in);
		//		while (true) {
		//			
		//			System.out.println("Insert knot: \nint1,int2	\n\"name1\",\"name2\"	\nAtom");
		//			
		//			String question = scanner.nextLine();
		//			if(question.equals("quit")){
		//				break;
		//			}
		//			System.out.println("Insert answer code:");
		//			String answer = scanner.nextLine();
		//			if(answer.equals("quit")){
		//				break;
		//			}

		try {

			String content = "";
			
			File file = new File("test.txt");
			System.out.println(file.getAbsolutePath());
			
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}