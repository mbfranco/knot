package knotCat.patterns.cluster;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import knotCat.patterns.cluster.Exceptions.ProbabilityOutOfBoundsException;

public class Search {

	File input;
	Browser browser;

	public Search() {

	}

	public Search(File knotsFile, Browser browser) {
		this.input=knotsFile;
		this.browser=browser;
	}

	public Browser getBrowser() {
		return browser;
	}

	public File getInput() {
		return input;
	}


	/**
	 * Search for a knot in the cluster, given the features to search
	 * @param features - string of n features/atomFeatures (that comes from the user query)
	 * to split into n individual features.
	 * @param uncertaintyThreshold - The minimum probability of a knot being similar with the one you are looking.
	 * The value is between 0 and 1: [0..1]
	 * @return 
	 */
	public ArrayList<ClusterSearchResult> searchForKnot(String features, double uncertaintyThreshold){

		if(uncertaintyThreshold < 0 || uncertaintyThreshold > 1){
			try {
				throw new ProbabilityOutOfBoundsException(uncertaintyThreshold);
			} catch (ProbabilityOutOfBoundsException e) {
				e.getMessage();
			}
		}

		//get browser to suppress warning. TODO does it get the browser instance? int n = getBrowser().NUMFEATURES;
		int bitArrayDimention = Browser.NUMFEATURES;

		//the list of knots to search
		LinkedList<Knot> knotsToSearch = new LinkedList<Knot>();
		List<Integer> b = new ArrayList<Integer>();
		List<String> n = new ArrayList<String>();
		BitArray f = new BitArray(bitArrayDimention);
		Map<Integer,BitArray> a = new TreeMap<Integer,BitArray>();
		Knot knot = new Knot(b, n, f, a);
		knotsToSearch.add(knot);

		boolean uncertaintyFlag = false;

		//the words to search can be: features, atomFeatures. They can be certain or uncertain
		//Ex: feature->"strong", atomFeature->"strong.very", w/uncertainty->"?strong" "?strong.very"
		String[] wordsToSearch = features.split("[ ]+");

		//create the knots to search
		for(String word : wordsToSearch){

			System.out.println("Palava a procurar: " + word);
			
			//(0) Check if the word stands for uncertainty
			int u = word.indexOf('?');
			if(u > -1){
				word = word.replace("?", "");
				for(Knot k : knotsToSearch){
					Knot k1 = k;
					knotsToSearch.add(k1);
				}
				uncertaintyFlag = true;
			}

			//(1) Check if the word is a feature
			int index = getBrowser().getFeatureIndex(word);
			//If the feature doesn't exist, getFeatureIndex(name) returns -1
			if(index > -1){
				if(uncertaintyFlag){
					for(int j = 0; j < knotsToSearch.size()/2; j++){
						knotsToSearch.get(j).getFeatures().set(index);
					}
				}else{
					for(Knot k : knotsToSearch){
						k.getFeatures().set(index);
					}
				}
			}

			//(2) Check if the word is an atomFeature
			//Either is in the form "feature.atom"/"atom.feature"
			else if(word.matches("[a-zA-Z\\'\\-]+\\.[a-zA-Z\\'\\-]+")){

				System.out.println("\t\tThe word is " + word);
				String[] words1and2 = word.split("\\.");

				index = getBrowser().getFeatureIndex(words1and2[0]);
				int index1 = getBrowser().getFeatureIndex(words1and2[1]);

				//If both words are features
				if(index > -1 && index1 > -1){
					//check if both are feature and atomFeature of each other (unlikely)
					boolean bothWork = false;
					
					//If the feature doesn't exist, getFeatureIndex(name) returns -1
					if(index > -1){
						//Check if second word is an atomFeature
						if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[1], index)){
							for(Knot k : knotsToSearch){
								//update feature BitArray
								k.getFeatures().set(index);
								//update atomFeature BitArray
								int atomI = getBrowser().getAtomFeatureIndex(index, words1and2[1]);
								BitArray atomBA = k.getAtoms().get(index);
								atomBA.set(atomI);
								k.getAtoms().put(index, atomBA);
								//update the flag in case both words are feature/atom of each other
								bothWork = true;
							}
						}
					}

					//Check if the second word is a feature
					index = index1;
					//If the feature doesn't exist, getFeatureIndex(name) returns -1
					if(index > -1){
						//Check if first word is an atomFeature
						if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[0], index)){
							for(Knot k : knotsToSearch){
								if(bothWork){
									//TODO duplicate the knotsToSearch
									//update feature BitArray
									k.getFeatures().set(index);
									//update atomFeature BitArray
									int atomI = getBrowser().getAtomFeatureIndex(index, words1and2[0]);
									BitArray atomBA = k.getAtoms().get(index);
									atomBA.set(atomI);
									k.getAtoms().put(index, atomBA);
								}
								else{
									//update feature BitArray
									k.getFeatures().set(index);
									//update atomFeature BitArray
									int atomI = getBrowser().getAtomFeatureIndex(index, words1and2[0]);
									BitArray atomBA = k.getAtoms().get(index);
									atomBA.set(atomI);
									k.getAtoms().put(index, atomBA);
								}
							}
						}
					}


				}else if(index > -1 && index1 == -1){
					if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[1], index)){
						for(Knot k : knotsToSearch){
							//update feature BitArray
							k.getFeatures().set(index);
							//update atomFeature BitArray
							int atomI = getBrowser().getAtomFeatureIndex(index, words1and2[1]);
							BitArray atomBA = k.getAtoms().get(index);
							atomBA.set(atomI);
							k.getAtoms().put(index, atomBA);
						}
					}

				}else if(index1 > -1 && index == -1){
					if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[0], index)){
						for(Knot k : knotsToSearch){
							//update feature BitArray
							k.getFeatures().set(index);
							//update atomFeature BitArray
							int atomI = getBrowser().getAtomFeatureIndex(index, words1and2[0]);
							BitArray atomBA = k.getAtoms().get(index);
							atomBA.set(atomI);
							k.getAtoms().put(index, atomBA);
						}
					}
				}
			}

			//TODO Or the the "word" is an atomFeature and again we have Uncertainty
			//(i.e. is not in the form "[a-z]+\\.[a-z]+" but is an atomFeature)

			uncertaintyFlag = false;
		}

		//Search in the cluster for knots the are similar and ate above the minimum similarity threshold
		return searchCluster(knotsToSearch, uncertaintyThreshold);
	}


	/**Searches in the cluster for a certain knot. Either the knot is present or knot.
	 * If a knot is present, that is result of the search. If the knot is not present,
	 * the result is another knot, with a certain degree of uncertainty.
	 * @param knotsToSearch
	 * @param threshold The minimum uncertainty allowed in the search for the knot (value between 0 and 1)
	 * @return
	 */
	private ArrayList<ClusterSearchResult> searchCluster(LinkedList<Knot> knotsToSearch, double threshold) {
		//TODO Check if all the clusters are searched in the "for method"

		if(threshold < 0 || threshold > 1){
			try {
				throw new ProbabilityOutOfBoundsException(threshold);
			} catch (ProbabilityOutOfBoundsException e) {
				e.getMessage();
			}
		}

		ArrayList<ClusterSearchResult> knotsToReturn = new ArrayList<ClusterSearchResult>();

		List<String> knotNames = new ArrayList<String>();
		double probability = -1;
		ClusterSearchResult knotToAdd = new ClusterSearchResult(knotNames, probability);

		//Check if there is a knot in the cluster that has the same features as the knotToSearch
		//for(int i=0; i<browser.getFinalCluster().size(); i++){
		for(FinalCluster fc : browser.getFinalCluster()){
			if(fc instanceof Node){
				//TODO
			}

			if(fc instanceof ClusterKnot){
				for(Knot k : knotsToSearch){
					if(((ClusterKnot) fc).getFeatures().equals(k.getFeatures())){

						knotNames = ((ClusterKnot) fc).getNames();
						probability = 1;

						knotToAdd.setKnot(knotNames);
						knotToAdd.setProbability(probability);

						knotsToReturn.add(knotToAdd);
					}
				}
			}
		}
		//}

		return knotsToReturn;
	}



}
