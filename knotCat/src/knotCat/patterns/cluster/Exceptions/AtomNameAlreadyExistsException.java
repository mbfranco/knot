package knotCat.patterns.cluster.Exceptions;

public class AtomNameAlreadyExistsException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AtomNameAlreadyExistsException() {
        super();
    }
	
	public AtomNameAlreadyExistsException(String message) {
        super(message);
    }
	
	@Override
	public String getMessage() {
		String msg = "The AtomFeature \"" + super.getMessage() + "\" already exists";
		
		
		return msg;
	}
	 
}
