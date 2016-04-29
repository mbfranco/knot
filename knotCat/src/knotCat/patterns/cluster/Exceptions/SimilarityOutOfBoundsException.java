package knotCat.patterns.cluster.Exceptions;

public class SimilarityOutOfBoundsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimilarityOutOfBoundsException() {
        super();
    }
	
	public SimilarityOutOfBoundsException(String message) {
        super(message);
    }
	
	@Override
	public String getMessage() {
		String msg = "The similiarity is out of bounds: " + super.getMessage();
		
		
		return msg;
	}
	 
}
