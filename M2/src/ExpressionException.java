@SuppressWarnings("serial")
public class ExpressionException extends Exception {
  int col;
  public ExpressionException(String err, int col) {
    super(err);
    this.col = col;
  }
  public int getCol () {
    return col;
  }
}