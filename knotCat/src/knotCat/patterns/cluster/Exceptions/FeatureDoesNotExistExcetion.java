package knotCat.patterns.cluster.Exceptions;

public class FeatureDoesNotExistExcetion extends Exception {

	int m1;
	String m2;

	private static final long serialVersionUID = 1L;

	public FeatureDoesNotExistExcetion() {
        super();
    }
	
	public FeatureDoesNotExistExcetion(int m1, String m2) {
		this.m1 = m1;
		this.m2 = m2;
    }
	
	private int getM1() {
		return m1;
	}

	private String getM2() {
		return m2;
	}

	@Override
	public String getMessage() {
		
		String msg = "The feature " + getM1() + " is not present for this knot: " + getM2();

		return msg;
	}
	 	
}
