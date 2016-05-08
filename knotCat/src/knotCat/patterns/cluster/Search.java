package knotCat.patterns.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import knotCat.algorithms.clustering.distanceFunctions.DistanceFunction;
import knotCat.patterns.cluster.Exceptions.ProbabilityOutOfBoundsException;
import knotCat.patterns.cluster.Exceptions.SimilarityOutOfBoundsException;

public class Search {

	File input;
	Browser browser;

	String urlDomain = "http://words.bighugelabs.com/api/2/3798afc1ef8cdcc0382aa79a402b8d61/";
	String wordToSearch = "word";
	
	public Search() {}

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

		/*
		 * Pre-set the Data. Prepare a set of arrays to be searched in the cluster
		 *
		 */


		if(uncertaintyThreshold > 1 || uncertaintyThreshold < -1){
			try {
				throw new ProbabilityOutOfBoundsException(uncertaintyThreshold);
			} catch (ProbabilityOutOfBoundsException e) {
				System.err.println(e.getMessage());
			}
		}

		int bitArrayDimention = Browser.NUMFEATURES;
		int atomsBitArrayDimention = Browser.NUMATOMS;

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

			//(0) Check if the word stands for uncertainty
			int u = word.indexOf('?');
			if(u > -1){
				word = word.replace("?", "");

				uncertaintyFlag = true;
			}

			//(1) Check if the word is a feature
			int index = getBrowser().getFeatureIndex(word);
			//If the feature doesn't exist, getFeatureIndex(name) returns -1
			if(index > -1){
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
								Map<Integer, BitArray> kAf = new TreeMap<Integer, BitArray>();
								kAf.putAll(k.getAtoms());
								Knot k1 = new Knot(null, null, k.getFeatures().copy(), kAf);
								tempKnotList.add(k1);

								//update feature BitArray
								k.getFeatures().set(index);

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
							//Double the number of knots because of the uncertainty
							for(Knot k : tempKnotList){
								knotsToSearch.add(k);
							}
						}
						//There is no uncertainty
						else{
							for(Knot k : knotsToSearch){

								//update feature BitArray
								k.getFeatures().set(index);

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
				//this "else" impedes the knotsToSearch to have
				//both feature1 and feature2 (if both word1 and word2
				//are features) in the same BitArray
				else if(index2 > -1){
					//word1 is an atomFeature of word2 (word2.word1)
					if(getBrowser().getAtomFeatureNames().containsEntry(words1and2[0], index2)){
						//do we have to double the knots?
						if(uncertaintyFlag){
							for(Knot k : knotsToSearch){
								Knot k1 = k;
								knotsToSearch.add(k1);

								//update feature BitArray
								k.getFeatures().set(index2);

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
						//There is no uncertainty
						else{
							for(Knot k : knotsToSearch){

								//update feature BitArray
								k.getFeatures().set(index2);

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
		List<ClusterSearchResult> knotResult = searchCluster(knotsToSearch, uncertaintyThreshold, distanceFunction);

		knotResult = bayesRule(knotResult);

		return knotResult;
	}


	/**Searches in the cluster for a certain knot. Either the knot is present or knot.
	 * If a knot is present, that is result of the search. If the knot is not present,
	 * the result is another knot, with a certain degree of uncertainty.
	 * @param knotsToSearch
	 * @param threshold The minimum uncertainty allowed in the search for the knot (value between 0 and 1)
	 * @return knots that are enough similar to the knots that are being searched
	 */
	private List<ClusterSearchResult> searchCluster(LinkedList<Knot> knotsToSearch, double threshold, String distanceFunction) {

		if(threshold < 0 && threshold != -1 || threshold > 1){
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

		//Stack<FinalCluster> lastVisitedNode = new Stack<FinalCluster>();		

		//For every knot that is being searched
		for(Knot currentKnot : knotsToSearch){

			//Search the TreeCluster
			for(FinalCluster fc : browser.getFinalCluster()){

				//If it is a leaf of the TreeCluster
				if(fc instanceof ClusterKnot){
					knotToAdd = checkClusterKnot(currentKnot, (ClusterKnot)fc, threshold, distanceFunction);



					if(knotToAdd == null){
						break;
					}

					knotsToReturn.add(knotToAdd);

					//sort the knots to return. The 1st element has the best probability of being the right knot
					Collections.sort(knotsToReturn, new Comparator<ClusterSearchResult>() {

						@Override
						public int compare(ClusterSearchResult o1, ClusterSearchResult o2) {
							if(o1.getProbability() > o2.getProbability()) return -1;
							if(o1.getProbability() < o2.getProbability()) return 1;
							return 0;
						}
					});

				}

				//If it is a Node in the TreeCluster
				if(fc instanceof Node){

					List<ClusterSearchResult> k2Add = checkNode(currentKnot, fc, threshold, distanceFunction);

					//					for(ClusterSearchResult ksr : k2Add){
					//						knotsToReturn.add(ksr);
					//					}

					if(!k2Add.isEmpty()){
						knotsToReturn.addAll(k2Add);
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

				knotToAdd = checkClusterKnot(currentKnot, (ClusterKnot)fic, threshold, distanceFunction);

				if(knotToAdd == null){
					break;
				}

				knotsToReturn.add(knotToAdd);

			}
			//instance of Node
			else{

				for(FinalCluster finClu : ((Node) fic).getBranches()){

					if(finClu instanceof Node){
						List<ClusterSearchResult> knotsToReturn1 = checkNode(currentKnot, finClu, threshold, distanceFunction);
						if(knotsToReturn1 != null){
							knotsToReturn.addAll(knotsToReturn1);
						}
					}

					else if(finClu instanceof ClusterKnot){

						knotToAdd = checkClusterKnot(currentKnot, (ClusterKnot)finClu, threshold, distanceFunction);
						if(knotToAdd != null){
							knotsToReturn.add(knotToAdd);
						}
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
	private ClusterSearchResult checkClusterKnot(Knot currentKnot, ClusterKnot fc, double threshold,
			String distanceFunction) {

		//Choose if you want to use SIMILARITY FUNCTION or AND FUNCTION
		//true = similarity
		//false = AND (jaccard)
		boolean chooseSimilarityFuntion = false;

		ClusterKnot knotNames = new ClusterKnot();
		double probability = -1;
		FinalCluster cluster = null;
		ClusterSearchResult knotToAdd = new ClusterSearchResult(knotNames, probability, cluster);

		
		//Check if the feature that is being searched does have an atom feature
		for(Entry<Integer, BitArray> fea : currentKnot.getAtoms().entrySet()){
			int isAtomFeaturePresentInClusterKnot = 0;
			boolean isFeaturePresentInClusterKnot = fc.getAtomFeatures().containsKey(fea.getKey()) && fc.getAtomFeatures().get(fea.getKey()) != null;
			if(isFeaturePresentInClusterKnot){
				isAtomFeaturePresentInClusterKnot = fc.getAtomFeatures().get(fea.getKey()).and(fea.getValue()).count();
			}

			if(isFeaturePresentInClusterKnot && isAtomFeaturePresentInClusterKnot == 0){
				fc.getFeatures().clear(fea.getKey());
			}
		}
		
		
		//the value should be [-1, 1]
		double similarity = calculateSimilarity(currentKnot.getFeatures(), fc.getFeatures(), chooseSimilarityFuntion);


		//		int nFeaturesCurerntKnot = currentKnot.getFeatures().count();
		//		BitArray presentFeatures = currentKnot.getFeatures().and(fc.getFeatures());
		//		int nPresentFeatures = presentFeatures.count();

		knotNames = ((ClusterKnot) fc);


		//the probability may simply be the number of presentFeatures / number of features in the knot to search
		//		probability = (double)nPresentFeatures/(double)nFeaturesCurerntKnot;

		//or the probability = similarity function = (2/|Ca|)(|Ca and B|) - 1, [-1, 1]
		//probability = (2/nFeaturesCurerntKnot)*(nPresentFeatures) - 1;

		//		if(probability < threshold){
		//			return null;
		//		}

		try{
			if(similarity < -1 || similarity > 1){
				throw new SimilarityOutOfBoundsException("" + similarity);
			}
		}catch(Exception e){
			e.getMessage();
			return null;
		}

		if(similarity < threshold){
			return null;
		}

		double prob;
		//normalize similarity to be between 0 and 1
		if(chooseSimilarityFuntion){
			prob = (similarity + 1.0) / 2.0;
		}
		else{
			prob = similarity;
		}

		knotToAdd.setKnot(knotNames);
		knotToAdd.setProbability(prob);
		knotToAdd.setTreeClusterResult(fc);


		return knotToAdd;

	}

	/** Measures the similarity between two BitArrays [-1,1]
	 * @param currentKnot Search knot
	 * @param clusterKnot Category
	 * @param chooseSimilarityFuntion2 
	 * @return
	 */
	private double calculateSimilarity(BitArray currentKnotFeatures, BitArray clusterKnot, boolean chooseSimilarityFuntion) {

		double similarity = -2;
		
		if(chooseSimilarityFuntion){
			//similarity value should be [-1, 1]
			similarity = -2;
			
			int clusterKnotModule = clusterKnot.count();
			BitArray sharedFeatures = clusterKnot.and(currentKnotFeatures);
			int sharedFeaturesModule = sharedFeatures.count();

			similarity = (2.0 / (double)clusterKnotModule) * (double)sharedFeaturesModule - 1.0;

			return similarity;
		}
		else{
			//AND similarity
			BitArray sharedFeatures = clusterKnot.and(currentKnotFeatures);
			int sharedFeaturesModule = sharedFeatures.count();
			BitArray bothFeatures = clusterKnot.or(currentKnotFeatures);
			int bothFeaturesModule = bothFeatures.count();

			similarity = (double)sharedFeaturesModule/(double)bothFeaturesModule;

			return similarity;
		}
	}

	
	public List<String> getSynonymous(String word) throws Exception{

		List<String> words = new ArrayList<String>();

		URL url = new URL(urlDomain + wordToSearch + "/");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null){
			String[] wordList = inputLine.split("\\|");
			words.add(wordList[2]);
		}
		in.close();

		

		return words;
	}


	private List<ClusterSearchResult> bayesRule(List<ClusterSearchResult> knotResult) {

		//if there is only one element on the list that has a certainty > 1, than calculate Bayes Rule


		if(!knotResult.isEmpty()){
			if(knotResult.get(1).getProbability() == 0.0 && knotResult.get(0).getProbability() != 0.0){

				ClusterSearchResult element = knotResult.get(0);
				element.setProbability(1);
				knotResult.clear();
				knotResult.add(0, element);
			}
		}
		return knotResult;
	}
}
