package knotCat.patterns.cluster.Exceptions;

public class ProbabilityOutOfBoundsException extends Exception {
	
	
	private static final long serialVersionUID = 1L;

	public ProbabilityOutOfBoundsException() {
		super();
	}

	public ProbabilityOutOfBoundsException(double message) {
		super(String.valueOf(message));
	}

	@Override
	public String getMessage() {
		String msg = "The probability " + super.getMessage() + " is not between 0 and 1";

		return msg;
	}
}
