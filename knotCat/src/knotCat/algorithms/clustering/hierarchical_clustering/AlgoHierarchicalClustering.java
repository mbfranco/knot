package knotCat.algorithms.clustering.hierarchical_clustering;
/* This file is copyright (c) 2008-2012 Philippe Fournier-Viger
 * 
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * 
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import knotCat.patterns.cluster.BitArray;
import knotCat.patterns.cluster.Browser;
import knotCat.patterns.cluster.ClusterKnot;
import knotCat.algorithms.clustering.distanceFunctions.DistanceFunction;
import knotCat.patterns.cluster.ClusterWithMean;
import knotCat.patterns.cluster.FinalCluster;
import knotCat.patterns.cluster.Knot;
import knotCat.patterns.cluster.Node;
import knotCat.tools.MemoryLogger;


/**
 * This is an implementation of generic Hierarchical Clustering Algorithm as described
 * in this webpage:
 * http://home.dei.polimi.it/matteucc/Clustering/tutorial_html/hierarchical.html
 * <br/><br/>
 * 
 * This is a Hierarchical Clustering with a constant "threshold" that indicate
 * the maximal distance between two clusters to group them. The algorithm stops
 * when no cluster can be merged.
 * <br/><br/>
 * 
 * The distance between two clusters is calculated as the distance between the
 * medians of the two clusters.
 * 
 * @author Philippe Fournier-Viger
 */

public class AlgoHierarchicalClustering {

	private Browser browser;

	Node currentNode = new Node();

	Set<BitArray> clusterControl = new HashSet<BitArray>();

	// parameter
	private double maxDistance =0;  // maximum distance allowed for merging two clusters

	// list of clusters
	List<ClusterWithMean> clusters = null;

	// for statistics
	private long startTimestamp;  // start time of latest execution
	private long endTimestamp;    // end time of latest execution
	private long iterationCount; // number of iterations performed


	/* The distance function to be used for clustering */
	private DistanceFunction distanceFunction = null;

	private ArrayList<String> names;

	/**
	 * Default constructor
	 */
	public AlgoHierarchicalClustering() {
	}

	/**
	 * Run the algorithm.
	 * @param maxDistance  the maximum distance allowed for merging two clusters
	 * @param distanceFunction 
	 * @return a list of Clusters
	 * @throws IOException exception if error while reading the file
	 */
	public List<ClusterWithMean> runAlgorithm(double maxDistance, DistanceFunction distanceFunction, Browser browser) throws NumberFormatException, IOException {
		// record start time
		startTimestamp = System.currentTimeMillis();

		// save the parameter
		this.maxDistance = maxDistance;

		// save the distance function
		this.distanceFunction = distanceFunction;

		// create an empty list of clusters
		clusters = new ArrayList<ClusterWithMean>();

		this.browser = browser;

		for(Knot k : browser.getKnotList()){
			BitArray theVector = k.getFeatures().copy();
			Map<Integer, BitArray> theVectorAtoms = k.getAtoms();
			List<String> theVectorNames = k.getName();

			// Initially we create a cluster for each vector
			ClusterWithMean cluster = new ClusterWithMean(theVector.length());
			cluster.addVector(theVector);
			cluster.addNames(theVectorNames);
			cluster.addAtomFeatures(theVectorAtoms);
			cluster.setMean(theVector.copy());
			clusters.add(cluster);
		}

		// (2) Loop to combine the two closest clusters into a bigger cluster
		// until no clusters can be combined.
		boolean changed = false;
		do {
			// merge the two closest clusters
			changed = mergeTheClosestCluster();
			// record memory usage
			MemoryLogger.getInstance().checkMemory();
		} while (changed);

		//add to the dendrogram clusters that haven't been merged
		for(int index = 0; index < clusters.size(); index++){
			for(BitArray ba : clusters.get(index).getVectors()){
				if(!clusterControl.contains(ba)){
					ClusterKnot ck = new ClusterKnot(clusters.get(index).getNames(), maxDistance+100, clusters.get(index).getVectors().get(0), clusters.get(index).getAtoms());
					browser.getFinalCluster().add(ck);
					clusterControl.add(ba);
				}
			}
		}

		// record end time
		endTimestamp = System.currentTimeMillis();

		// return the clusters
		return clusters;
	}

	/**
	 * Run the algorithm only for the features.
	 * @param inputFile an input file containing vectors of BitArrays
	 * @param maxDistance  the maximum distance allowed for merging two clusters, i.e. the threshold
	 * @param distanceFunction euclidean or hamming
	 * @param browser this Browser
	 * @return a list of Clusters 
	 * @throws IOException exception if error while reading the file
	 */
	//	public List<ClusterWithMean> runOnlyFeaturesAlgorithm(String inputFile, double maxDistance, DistanceFunction distanceFunction, Browser browser) throws NumberFormatException, IOException {
	//		// record start time
	//		startTimestamp = System.currentTimeMillis();
	//		
	//		// save the parameter
	//		this.maxDistance = maxDistance;
	//		
	//		// save the distance function
	//		this.distanceFunction = distanceFunction;
	//		
	//		// create an empty list of clusters
	//		clusters = new ArrayList<ClusterWithMean>();
	//		
	//		for(Knot k : browser.getKnotList()){
	//			BitArray theVector = k.getFeatures().copy();
	//			
	//			// Initially we create a cluster for each vector
	//			ClusterWithMean cluster = new ClusterWithMean(theVector.length());
	//			cluster.addVector(theVector);
	//			cluster.setMean(theVector.copy());
	//			clusters.add(cluster);
	//		}
	//
	//		// (2) Loop to combine the two closest clusters into a bigger cluster
	//		// until no clusters can be combined.
	//		boolean changed = false;
	//		do {
	//			// merge the two closest clusters
	//			changed = mergeTheClosestCluster();
	//			// record memory usage
	//			MemoryLogger.getInstance().checkMemory();
	//		} while (changed);
	//
	//		// record end time
	//		endTimestamp = System.currentTimeMillis();
	//		
	//		// return the clusters
	//		return clusters;
	//	}


	/**
	 * Merge the two closest clusters in terms of distance.
	 * @return true if a merge was done, otherwise false.
	 */
	private boolean mergeTheClosestCluster() {

		// These variables will contain the two closest clusters that
		// can be merged
		ClusterWithMean clusterToMerge1 = null;
		ClusterWithMean clusterToMerge2 = null;
		double minClusterDistance = Integer.MAX_VALUE;


		// find the two closest clusters with distance > threshold
		// by comparing all pairs of clusters i and j
		for (int i = 0; i < clusters.size(); i++) {
			for (int j = i + 1; j < clusters.size(); j++) {
				// calculate the distance between i and j
				double distance = distanceFunction.calculateDistance(clusters.get(i).getmean(), clusters.get(j).getmean());
				// if the distance is less than the max distance allowed
				// and if it is the smallest distance until now
				if (distance < minClusterDistance && distance <= maxDistance) {
					// record this pair of clusters
					minClusterDistance = distance;
					clusterToMerge1 = clusters.get(i);
					clusterToMerge2 = clusters.get(j);
				}
			}
		}

		// if no close clusters were found, return false
		if (clusterToMerge1 == null) {
			return false;
		}


		//Add the clusters to the dendrogram (browser.finalCluster), before the distance is lost and the clusters are merged
		
		//If it is a Leaf...
		if(clusterToMerge1.getVectors().size() == 1){
			
			//...merging with a Leaf
			if(clusterToMerge2.getVectors().size() == 1){
				
				//create the node that merges the two leaves
				List<FinalCluster> branches = new ArrayList<FinalCluster>();
				Node newNode = new Node(minClusterDistance, branches);		

				//get parameters of the first branch
				List<String> names = clusterToMerge1.getNames();
				Map<Integer, BitArray> atomFeatures = clusterToMerge1.getAtoms();
				ClusterKnot branch = new ClusterKnot(names, 0, clusterToMerge1.getVectors().get(0), atomFeatures);
				
				//add the first branch to the newNode
				newNode.getBranches().add(branch);
				newNode.setFeatures(clusterToMerge1.getVectors().get(0));
				newNode.setNames(names);

				//get the parameters of the second branch
				names = clusterToMerge2.getNames();
				atomFeatures = clusterToMerge2.getAtoms();
				branch = new ClusterKnot(names, 0, clusterToMerge2.getVectors().get(0), atomFeatures);
				
				//add the second branch to the newNode
				newNode.getBranches().add(branch);
				newNode.setFeatures(newNode.getFeatures().or(clusterToMerge2.getVectors().get(0)));
				newNode.getNames().addAll(names);

				currentNode = newNode;

				clusterControl.add(clusterToMerge1.getVectors().get(0));
				clusterControl.add(clusterToMerge2.getVectors().get(0));

				//currentNode.setFeatures(clusterToMerge1.getVectors().get(0).or(clusterToMerge2.getVectors().get(0)));
				
				browser.getFinalCluster().add(newNode);
			}
			
			//...merging with a Node (clusterToMerge2.getVectors().size() == 2)
			else{

				//create the node that merges the two leaves
				List<FinalCluster> branches = new ArrayList<FinalCluster>();
				Node newNode = new Node(minClusterDistance, branches);		

				//add the first Branch to the newNode
				currentNode = searchForNodeInTheDendrogram(clusterToMerge2.getNames());
				newNode.getBranches().add(currentNode);

				//Add knot in the same index that the previous was deleted
				int index = browser.getFinalCluster().indexOf(currentNode);
				browser.getFinalCluster().remove(currentNode);

				//get parameters of the second branch
				List<String> names = clusterToMerge1.getNames();
				Map<Integer, BitArray> atomFeatures = clusterToMerge1.getAtoms();
				ClusterKnot branch = new ClusterKnot(names, 0, clusterToMerge1.getVectors().get(0), atomFeatures);
				
				//add the leaf to the newNode
				newNode.getBranches().add(branch);

				//set new Node parameters
				newNode.setFeatures(currentNode.getFeatures().or(branch.getFeatures()));
				newNode.setNames(currentNode.getNames());
				newNode.getNames().addAll(branch.getNames());

				clusterControl.add(clusterToMerge2.getVectors().get(0));
				clusterControl.add(clusterToMerge2.getVectors().get(1));
				clusterControl.add(clusterToMerge1.getVectors().get(0));

				browser.getFinalCluster().add(index, newNode);
			}
		}

		//If it is a Node...
		else if(clusterToMerge1.getVectors().size() == 2){
			
			//...merging with a Leaf
			if(clusterToMerge2.getVectors().size() == 1){
				
				//create the node that merges the two leaves
				List<FinalCluster> branches = new ArrayList<FinalCluster>();
				Node newNode = new Node(minClusterDistance, branches);		

				//add the first Branch to the newNode
				currentNode = searchForNodeInTheDendrogram(clusterToMerge1.getNames());
				newNode.getBranches().add(currentNode);

				//Add knot in the same index that the previous was deleted
				int index = browser.getFinalCluster().indexOf(currentNode);
				browser.getFinalCluster().remove(currentNode);

				//get parameters of the second branch
				List<String> names = clusterToMerge2.getNames();
				Map<Integer, BitArray> atomFeatures = clusterToMerge2.getAtoms();
				ClusterKnot branch = new ClusterKnot(names, 0, clusterToMerge2.getVectors().get(0), atomFeatures);
				
				//add the leaf to the newNode
				newNode.getBranches().add(branch);

				//set new Node parameters
				newNode.setFeatures(currentNode.getFeatures().or(branch.getFeatures()));
				newNode.setNames(currentNode.getNames());
				newNode.getNames().addAll(branch.getNames());

				clusterControl.add(clusterToMerge1.getVectors().get(0));
				clusterControl.add(clusterToMerge1.getVectors().get(1));
				clusterControl.add(clusterToMerge2.getVectors().get(0));

				browser.getFinalCluster().add(index, newNode);
			}
			
			//...merging with a Node
			else{
				
				//create the node that merges the two leaves
				List<FinalCluster> branches = new ArrayList<FinalCluster>();
				Node newNode = new Node(minClusterDistance, branches);		

				//add the first Branch to the newNode
				currentNode = searchForNodeInTheDendrogram(clusterToMerge1.getNames());
				newNode.getBranches().add(currentNode);
				
				//add the first Branch to the newNode
				Node currentNode2 = searchForNodeInTheDendrogram(clusterToMerge2.getNames());
				newNode.getBranches().add(currentNode2);
				
//				//Add knot in the same index that the previous was deleted
//				int index = browser.getFinalCluster().indexOf(currentNode);
				browser.getFinalCluster().remove(currentNode);
				browser.getFinalCluster().remove(currentNode2);

				//set new Node parameters
				newNode.setFeatures(currentNode.getFeatures().or(currentNode2.getFeatures()));
				newNode.setNames(currentNode.getNames());
				newNode.getNames().addAll(currentNode2.getNames());

				clusterControl.add(clusterToMerge1.getVectors().get(0));
				clusterControl.add(clusterToMerge1.getVectors().get(1));
				clusterControl.add(clusterToMerge2.getVectors().get(0));
				clusterControl.add(clusterToMerge2.getVectors().get(1));

				browser.getFinalCluster().add(newNode);
			}
		}

		// else, merge the two closest clusters
		for(BitArray vector : clusterToMerge2.getVectors()){
			clusterToMerge1.addVector(vector);
		}
		// after merging, we need to recompute the mean of the resulting cluster
		clusterToMerge1.recomputeClusterMean();
		// we delete the cluster that was merged
		clusters.remove(clusterToMerge2);

		// increase iteration count for statistics
		iterationCount++;
		return true;
	}


	/** Search for a Node in the browser.finalCluster
	 * @param names The names of the knots in that Node
	 * @return the Node in the final tree cluster (dendrogram)
	 */
	private Node searchForNodeInTheDendrogram(List<String> names) {
		
		for(FinalCluster f : browser.getFinalCluster()){
			if(f.getNames().containsAll(names)){
				
				return (Node)f;
			}
		}
		return null;
//		The next commented lines of code are correct, but unnecessary. The search is always for a root node
//		for(FinalCluster f : browser.getFinalCluster()){
//			Node n = new Node();
//			if(f instanceof Node && f.getNames().containsAll(names)){
//
//				if(((Node) f).getBranches().get(0).getNames().containsAll(names)){
//					n = searchForNodeInTheDendrogram(((Node) f).getBranches().get(0).getNames());
//				}
//				else if(((Node) f).getBranches().get(1).getNames().containsAll(names)){
//					n = searchForNodeInTheDendrogram(((Node) f).getBranches().get(1).getNames());
//				}
//				return n;
//			}
//			//else go to the next cluster
//		}
//		return null;
	}

	/**
	 * Save the clusters to an output file
	 * @param output the output file path
	 * @throws IOException exception if there is some writing error.
	 */
	public void saveToFile(String output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		// for each cluster
		for(int i=0; i< clusters.size(); i++){
			// if the cluster is not empty
			if(clusters.get(i).getVectors().size() >= 1){
				// write the cluster
				writer.write(clusters.get(i).toString());
				// if not the last cluster, add a line return
				if(i < clusters.size()-1){
					writer.newLine();
				}
			}
		}
		// close the file
		writer.close();
	}

	private double getSSE() {
		double sse = 0;
		for(ClusterWithMean cluster : clusters) {
			for(BitArray vector : cluster.getVectors()) {
				sse += Math.pow(distanceFunction.calculateDistance(vector, cluster.getmean()), 2);
			}
		}
		return sse;
	}

	/**
	 * Print statistics about the latest execution to System.out.
	 */
	public void printStatistics() {
		System.out.println("========== HIERARCHICAL CLUSTERING - STATS ============");
		System.out.println(" Distance function: " + distanceFunction.getName());
		System.out.println(" Total time ~: " + (endTimestamp - startTimestamp)
				+ " ms");
		System.out.println(" SSE (Sum of Squared Errors) (lower is better) : " + getSSE());
		System.out.println(" Max memory:" + MemoryLogger.getInstance().getMaxMemory() + " mb ");
		System.out.println(" Iteration count: " + iterationCount);
		System.out.println("=====================================");
	}

}
