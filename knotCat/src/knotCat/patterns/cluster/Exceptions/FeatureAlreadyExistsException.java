package knotCat.patterns.cluster.Exceptions;

public class FeatureAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public FeatureAlreadyExistsException() {
        super();
    }
	
	public FeatureAlreadyExistsException(String message) {
        super(message);
    }
	
	@Override
	public String getMessage() {
		String msg = "A feature \"" + super.getMessage() + "\" already exists";
		
		
		return msg;
	}
	
}
