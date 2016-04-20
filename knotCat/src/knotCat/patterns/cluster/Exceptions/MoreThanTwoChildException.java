package knotCat.patterns.cluster.Exceptions;

public class MoreThanTwoChildException extends Exception {

	private static final long serialVersionUID = 1L;

	public MoreThanTwoChildException() {
        super();
    }
	
	public MoreThanTwoChildException(String message) {
        super(message);
    }
	
	@Override
	public String getMessage() {
		String msg = "This node already has a size of "+ super.getMessage() + " children" ;
				
		return msg;
	}
	
}
