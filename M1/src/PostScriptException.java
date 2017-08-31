@SuppressWarnings("serial")
public class PostScriptException extends RuntimeException {
	public PostScriptException(String err, Output output) {
		super(err);
		output.putString("ERROR: "+err);
	}
}