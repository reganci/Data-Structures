public class Expression {
  private String expression;
  private int current;
  public Expression (String expression) {
    this.expression = expression;
    current = 0;
  }
  private boolean hasNext () {
    return current < expression.length();
  }
  public void error (String message, int delta)
    throws ExpressionException {
    throw new ExpressionException(message, current + delta);
  }
  public void error (String message)
    throws ExpressionException {
    error(message, 0);
  }
  public char next ()
    throws ExpressionException {
    if (!hasNext()) {
      error("Unexpected end of line.", 1);
    }
    char c = expression.charAt(current);
    current++;
    return c;
  }
  public boolean relativeHas (int i) {
    return current + i < expression.length();
  }
  public char relativeAt (int i) {
    return expression.charAt(current + i);
  }
  public String sub (int end) {
	// current-1 : car il ne faut pas oublier d'inclure le caractere courant dans 
	// expressionNode (qui est à l'index current-1)
    String s = expression.substring(current-1, current-1 + end);
    current = current-1 + end;
    return s;
  }
  public void expectChar (char expected)
    throws ExpressionException {
    if (!hasNext()) {
      error("Expecting a '" + expected +
          "' but there is no more.", 1);
    } else {
      char c = next();
      if (expected != c) {
        error("Expecting a '" + expected + "' and got " + c + ".");
      }
    }
  }

}