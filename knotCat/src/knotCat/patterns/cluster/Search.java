package knotCat.patterns.cluster;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import knotCat.algorithms.clustering.distanceFunctions.DistanceFunction;
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

	public Search(Browser browser){
		this.browser = browser;
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
	 * @param uncertaintyThreshold - use 1 to ignore the threshold.
	 * The minimum probability of a knot being similar with the one you are looking.
	 * The value is between 0 and 1: [0..1]
	 * @return a set of the search results (i.e. Knots + probability)
	 */
	public List<ClusterSearchResult> searchForKnot(String features, double uncertaintyThreshold, String distanceFunction){

		if(uncertaintyThreshold > 1 || uncertaintyThreshold < -1){
			try {
				throw new ProbabilityOutOfBoundsException(uncertaintyThreshold);
			} catch (ProbabilityOutOfBoundsException e) {
				System.err.println(e.getMessage());
			}
		}

		int bitArrayDimention = Browser.NUMFEATURES;
		int atomsBitArrayDimention = Browser.NUMFEATURES;

		//the list of knots to search
		LinkedList<Knot> knotsToSearch = new LinkedList<Knot>();
		List<Integer> b = new ArrayList<Integer>();
		List<String> n = new ArrayList<String>();
		BitArray f = new BitArray(bitArrayDimention);
		Map<Integer,BitArray> a = new TreeMap<Integer,BitArray>();
		Knot knot = new Knot(b, n, f, a);
		//only the fields "features" and "atomFeatures" matter
		knotsToSearch.add(knot);

		boolean uncertaintyFlag = false;

		//the words to search can be: features, atomFeatures. They can be certain or uncertain
		//Ex: feature->"strong", atomFeature->"strong.very", w/uncertainty->"?strong" "?strong.very"
		String[] wordsToSearch = features.split("[ ]+");

		//create the knots to search
		for(String word : wordsToSearch){

			System.out.println("Looking for Word: " + word);

			//(0) Check if the word stands for uncertainty
			int u = word.indexOf('?');
			if(u > -1){
				word = word.replace("?", "");
				System.out.println("Looking for Uncertainty: " + word);

				uncertaintyFlag = true;
			}

			//(1) Check if the word is a feature
			int index = getBrowser().getFeatureIndex(word);
			//If the feature doesn't exist, getFeatureIndex(name) returns -1
			if(index > -1){
				System.out.println("\tLooking for Feature: " + word);
				if(uncertaintyFlag){
					List<Knot> tempKnotList = new ArrayList<>();
					for(Knot k : knotsToSearch){
						BitArray k1f = k.getFeatures().copy();
						Map<Integer, BitArray> k1a = new TreeMap<Integer,BitArray>(k.getAtoms());
						Knot k1 = new Knot(null, null, k1f, k1a);
						tempKnotList.add(k1);
						k.getFeatures().set(index);
					}
					for(Knot k : tempKnotList){
						knotsToSearch.add(k);
					}

				}else{
					for(Knot k : knotsToSearch){
						k.getFeatures().set(index);
					}
				}
			}

			//(2) Check if the word is an atomFeature
			//Either is in the form "feature.atom"/"atom.feature"
			//TODO make the string to match the form (word1 word2 word3 ... wordN)
			else if(word.matches("[a-zA-Z\\'\\-]+\\.[a-zA-Z\\'\\-]+")){

				System.out.println("\tLooking for Atom: " + word);
				String[] words1and2 = word.split("\\.");

				index = getBrowser().getFeatureIndex(words1and2[0]);
				int index2 = getBrowser().getFeatureIndex(words1and2[1]);

				//TODO If both word1.word2 and word2.word1 exist, equals uncertainty

				//check atomFeature word1.word2
				//word1 is a feature
				if(index > -1){
					//word2 is an atomFeature of word1 (word1.word2)
					if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[1], index)){
						//do we have to double the number of knots?
						if(uncertaintyFlag){
							List<Knot> tempKnotList = new ArrayList<>();
							for(Knot k : knotsToSearch){
								Knot k1 = new Knot(null, null, k.getFeatures(), k.getAtoms());
								tempKnotList.add(k1);

								System.out.println("(W1A)Knot prior to set(index): index=" + index + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update feature BitArray
								k.getFeatures().set(index);

								System.out.println("(W1A)Knot after to set(index): index=" + index + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update atomFeature1
								int atomId = getBrowser().getAtomFeatureIndex(index, words1and2[1]);
								if(!k.getAtoms().containsKey(index)){
									BitArray at = new BitArray(atomsBitArrayDimention);
									at.set(atomId);
									k.getAtoms().put(index, at);
								}else{
									BitArray atomBA = k.getAtoms().get(index);
									atomBA.set(atomId);
									k.getAtoms().put(index, atomBA);
								}
							}
							for(Knot k : tempKnotList){
								knotsToSearch.add(k);
							}
						}
						else{
							List<Knot> tempKnotList = new ArrayList<>();
							for(Knot k : knotsToSearch){
								Knot k1 = new Knot(null, null, k.getFeatures(), k.getAtoms());
								tempKnotList.add(k1);

								System.out.println("(W1B)Knot prior to set(index): index=" + index + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update feature BitArray
								k.getFeatures().set(index);

								System.out.println("(W1B)Knot after to set(index): index=" + index + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update atomFeature1
								int atomId = getBrowser().getAtomFeatureIndex(index, words1and2[1]);
								if(!k.getAtoms().containsKey(index)){
									BitArray at = new BitArray(atomsBitArrayDimention);
									at.set(atomId);
									k.getAtoms().put(index, at);
								}else{
									BitArray atomBA = k.getAtoms().get(index);
									atomBA.set(atomId);
									k.getAtoms().put(index, atomBA);
								}
							}
						}
					}
				}

				//check if word2 is a feature
				else if(index2 > -1){
					//word1 is an atomFeature of word2 (word2.word1)
					if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[0], index2)){
						//do we have to double the knots?
						if(uncertaintyFlag){
							for(Knot k : knotsToSearch){
								Knot k1 = k;
								knotsToSearch.add(k1);

								System.out.println("(W2A)Knot prior to set(index): index=" + index2 + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update feature BitArray
								k.getFeatures().set(index2);

								System.out.println("(W2A)Knot after to set(index): index=" + index2 + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update atomFeature1
								int atomId = getBrowser().getAtomFeatureIndex(index2, words1and2[1]);
								if(!k.getAtoms().containsKey(index2)){
									BitArray at = new BitArray(atomsBitArrayDimention);
									at.set(atomId);
									k.getAtoms().put(index2, at);
								}else{
									BitArray atomBA = k.getAtoms().get(index2);
									atomBA.set(atomId);
									k.getAtoms().put(index2, atomBA);
								}
							}
						}
						else{
							for(Knot k : knotsToSearch){

								System.out.println("(W2B)Knot prior to set(index): index=" + index2 + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update feature BitArray
								k.getFeatures().set(index2);

								System.out.println("(W2B)Knot after to set(index): index=" + index2 + "\n\tF:" + k.getFeatures() + "\n\tA:" + k.getAtoms());

								//update atomFeature1
								int atomId = getBrowser().getAtomFeatureIndex(index2, words1and2[1]);
								if(!k.getAtoms().containsKey(index2)){
									BitArray at = new BitArray(atomsBitArrayDimention);
									at.set(atomId);
									k.getAtoms().put(index2, at);
								}else{
									BitArray atomBA = k.getAtoms().get(index2);
									atomBA.set(atomId);
									k.getAtoms().put(index2, atomBA);
								}
							}
						}
					}
				}

				//The word does not exist in the cluster
				else{
					//TODO Check WordNet and find synonymous and use them to perform searches
				}
			}

			//TODO Or the the "word" is an atomFeature and again we have Uncertainty
			//(i.e. is not in the form "[a-z]+\\.[a-z]+" but is an atomFeature)

			uncertaintyFlag = false;

		}

		//Search in the cluster for knots the are similar and are above the minimum similarity threshold
		return searchCluster(knotsToSearch, uncertaintyThreshold, distanceFunction);
	}


	/**Searches in the cluster for a certain knot. Either the knot is present or knot.
	 * If a knot is present, that is result of the search. If the knot is not present,
	 * the result is another knot, with a certain degree of uncertainty.
	 * @param knotsToSearch
	 * @param threshold The minimum uncertainty allowed in the search for the knot (value between 0 and 1)
	 * @return knots that are enough similar to the knots that are being searched
	 */
	private List<ClusterSearchResult> searchCluster(LinkedList<Knot> knotsToSearch, double threshold, String distanceFunction) {

		if(threshold < 0 && threshold != -1){
			try {
				throw new ProbabilityOutOfBoundsException(threshold);
			} catch (ProbabilityOutOfBoundsException e) {
				System.err.println(e.getMessage());
			}
		}

		List<ClusterSearchResult> knotsToReturn = new ArrayList<ClusterSearchResult>();

		ClusterKnot cKnot = new ClusterKnot();
		double probability = -1;
		FinalCluster cluster = null;
		ClusterSearchResult knotToAdd = new ClusterSearchResult(cKnot, probability, cluster);

		Stack<FinalCluster> lastVisitedNode = new Stack<FinalCluster>();		


		for(Knot currentKnot : knotsToSearch){

			for(FinalCluster fc : browser.getFinalCluster()){

				if(fc instanceof ClusterKnot){
					knotToAdd = checkClusterKnot(currentKnot, fc, threshold, distanceFunction);

					if(knotToAdd == null){
						break;
					}

					knotsToReturn.add(knotToAdd);

					Collections.sort(knotsToReturn, new Comparator<ClusterSearchResult>() {

						@Override
						public int compare(ClusterSearchResult o1, ClusterSearchResult o2) {
							if(o1.getProbability() > o2.getProbability()) return -1;
							if(o1.getProbability() < o2.getProbability()) return 1;
							return 0;
						}

						//		    @Override
						//		    public int compare(ClusterSearchResult o1, ClusterSearchResult o2) {
						//		        return o1.getProbability(),o2.getProbability());
						//		    }
					});

				}

				if(fc instanceof Node){

					List<ClusterSearchResult> k2Add = checkNode(currentKnot, fc, threshold, distanceFunction);

					for(ClusterSearchResult ksr : k2Add){
						knotsToReturn.add(ksr);
					}

					Collections.sort(knotsToReturn, new Comparator<ClusterSearchResult>() {

						@Override
						public int compare(ClusterSearchResult o1, ClusterSearchResult o2) {
							if(o1.getProbability() > o2.getProbability()) return -1;
							if(o1.getProbability() < o2.getProbability()) return 1;
							return 0;
						}
					});

				}

			}

		}

		return knotsToReturn;

	}

	/** Compares the Knot of the search with the knots in the Node
	 * @param currentKnot Knot that is being searched
	 * @param fc Cluster knot that is under search
	 * @param threshold similarity double value [-1, 1]
	 * @param distanceFunction not being used
	 * @return List of knots that are being added to the overall knots to return
	 */
	private List<ClusterSearchResult> checkNode(Knot currentKnot, FinalCluster fc, double threshold, String distanceFunction) {

		List<ClusterSearchResult> knotsToReturn = new ArrayList<ClusterSearchResult>();

		for(FinalCluster fic : ((Node) fc).getBranches()){

			ClusterKnot cKnot = new ClusterKnot();
			double probability = -1;
			FinalCluster cluster = null;
			ClusterSearchResult knotToAdd = new ClusterSearchResult(cKnot, probability, cluster);

			if(fic instanceof ClusterKnot){

				knotToAdd = checkClusterKnot(currentKnot, fic, threshold, distanceFunction);

				if(knotToAdd == null){
					break;
				}

				knotsToReturn.add(knotToAdd);

			}
			//instance of Node
			else{

				//lastVisitedNode.push(fc);
				for(FinalCluster finClu : ((Node) fic).getBranches()){
					
					if(finClu instanceof Node){
						List<ClusterSearchResult> knotsToReturn1 = checkNode(currentKnot, finClu, threshold, distanceFunction);
						if(knotsToReturn1 != null){
							knotsToReturn.addAll(knotsToReturn1);
						}
					}
					
					else if(finClu instanceof ClusterKnot){
						knotToAdd = checkClusterKnot(currentKnot, finClu, threshold, distanceFunction);
					}
				}
			}

		}
		return knotsToReturn;
	}


	/** Compares the Knot of the search with the knot on the Cluster
	 * @param currentKnot Knot that is being searched
	 * @param fc Cluster knot that is under search
	 * @param threshold similarity double value [-1, 1]
	 * @param distanceFunction not being used
	 * @return knot that is being added (or not) to the overall knots to return
	 */
	private ClusterSearchResult checkClusterKnot(Knot currentKnot, FinalCluster fc, double threshold,
			String distanceFunction) {

		ClusterKnot knotNames = new ClusterKnot();
		double probability = -1;
		FinalCluster cluster = null;
		ClusterSearchResult knotToAdd = new ClusterSearchResult(knotNames, probability, cluster);

		int nFeaturesCurerntKnot = currentKnot.getFeatures().count();
		BitArray presentFeatures = currentKnot.getFeatures().and(fc.getFeatures());
		int nPresentFeatures = presentFeatures.count();

		knotNames = ((ClusterKnot) fc);
		//the probability may simply be the number of presentFeatures / number of features in the knot to search
		probability = (double)nPresentFeatures/(double)nFeaturesCurerntKnot;

		//or the probability = similarity function = (2/|Ca|)(Ca and B) - 1, [-1, 1]
		//probability = (2/nFeaturesCurerntKnot)*(nPresentFeatures) - 1;

		if(probability < threshold){
			return null;
		}

		knotToAdd.setKnot(knotNames);
		knotToAdd.setProbability(probability);
		knotToAdd.setTreeClusterResult(fc);

		return knotToAdd;

	}

}
