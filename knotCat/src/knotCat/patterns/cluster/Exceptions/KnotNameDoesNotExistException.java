package knotCat.patterns.cluster.Exceptions;

public class KnotNameDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KnotNameDoesNotExistException() {
		super();
	}

	public KnotNameDoesNotExistException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		String msg = "The name " + super.getMessage() + " is not a name for this knot";


		return msg;
	}

}
