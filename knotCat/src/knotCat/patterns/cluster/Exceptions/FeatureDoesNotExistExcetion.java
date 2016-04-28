package knotCat.patterns.cluster.Exceptions;

public class FeatureDoesNotExistExcetion extends Exception {

	int m1;
	String m2;

	private static final long serialVersionUID = 1L;

	public FeatureDoesNotExistExcetion() {
        super();
    }
	
	public FeatureDoesNotExistExcetion(String m2){
		this.m2 = m2;
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
		String msg;
		
		if(getM1() > 0){
			msg = "The feature " + getM1() + " is not present for this knot: " + getM2();
		}
		if(getM1() == 0){
			msg = "The feature " + getM2() + " does not exist.";
		}
		else{
			msg = "This shouldn't be the error message..... You cannot insert a Feature with a negative index (-1)";
		}
		
		return msg;
	}
	 	
}
