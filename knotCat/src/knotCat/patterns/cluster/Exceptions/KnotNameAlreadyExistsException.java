package knotCat.patterns.cluster.Exceptions;

public class KnotNameAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KnotNameAlreadyExistsException() {
        super();
    }
	
	public KnotNameAlreadyExistsException(String message) {
        super(message);
    }
	
	@Override
	public String getMessage() {
		
		String msg = "The name " + super.getMessage() + " is already a name for this knot";
		
		return msg;
	}
	
}
