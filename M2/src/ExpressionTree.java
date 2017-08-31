public class ExpressionTree implements FormalExpressionTree {
  /**
   * Représente	la racine de l'arbre.
   */
  private ExpressionNode root;
  public ExpressionTree (ExpressionNode root) {
    this.root = root;
  }
  public ExpressionTree (String expression)
    throws ExpressionException {
    this.root = new ExpressionNode(new Expression(expression));
  }
  public FormalExpressionTree derive()
  {
    try {
      return new ExpressionTree(root.derive());
    } catch (DerivationException e) {
      return null;
    }
  }
  public String toString() {
    return root.toString();
  }
}