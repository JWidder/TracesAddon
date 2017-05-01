package main;

public class ExceptionParser extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String message;

	public ExceptionParser(String inMessage) {
		message = inMessage;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
