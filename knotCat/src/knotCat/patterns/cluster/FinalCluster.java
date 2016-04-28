package knotCat.patterns.cluster;

import java.util.List;

public abstract class FinalCluster {
	
	public void print() {}
	
	public abstract BitArray getFeatures();

	public abstract List<String> getNames();

	public void printDendrogram(String prefix, boolean isTail){}
	
	public StringBuilder toString(StringBuilder prefix, boolean isTail,StringBuilder sb){
		return sb;
	}
}
