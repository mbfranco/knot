package knotCat.patterns.cluster;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	 * @param features - string of n features (that comes from the user query)
	 * to split into n individual features.
	 * @param uncertaintyThreshold - The minimum probability of a knot being similar with the one you are looking.
	 * The value is between 0 and 1
	 * @return 
	 */
	public ArrayList<ClusterSearchResult> searchForKnot(String features, double uncertaintyThreshold){
		//get browser to supress warning. TODO does it get the browser instance? int n = getBrowser().NUMFEATURES;
		
		if(uncertaintyThreshold < 0 || uncertaintyThreshold > 1){
			try {
				throw new ProbabilityOutOfBoundsException(uncertaintyThreshold);
			} catch (ProbabilityOutOfBoundsException e) {
				e.getMessage();
			}
		}
		
		int n = Browser.NUMFEATURES;
		BitArray knotToSearch = new BitArray(n);
		String[] featuresToSearch = features.split("[ ]+");
		
		//create the knot to search
		for(String name : featuresToSearch){
			int i = getBrowser().getFeatureIndex(name);
			//If the feature doesn't exist, getFeatureIndex(name) returns -1
			if(i > -1){
				knotToSearch.set(i);
			}
		}
		
		//Search in the cluster for knots the are similar above the minimum threshold
		//The search
		//- a knot with the exact same features has the 
		return searchCluster(knotToSearch, uncertaintyThreshold);
		
		
	}

	
	/**Searches in the cluster for a certain knot. Either the knot is present or knot.
	 * If a knot is present, that is result of the search. If the knot is not present,
	 * the result is another knot, with a certain degree of uncertainty.
	 * @param knotToSearch
	 * @param threshold The minimum uncertainty allowed in the search for the knot (value between 0 and 1)
	 * @return
	 */
	private ArrayList<ClusterSearchResult> searchCluster(BitArray knotToSearch, double threshold) {
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
				if(((ClusterKnot) fc).getFeatures().equals(knotToSearch)){
			
				knotNames = ((ClusterKnot) fc).getNames();
				probability = 1;

				knotToAdd.setKnot(knotNames);
				knotToAdd.setProbability(probability);

				knotsToReturn.add(knotToAdd);
				}
			}
		}
		//}
		
		//TODO Check Alternatives to the knot (undertainty)
		
		
		return knotsToReturn;
	}

	
	
}
