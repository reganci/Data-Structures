public class ExpressionNode extends Node<String>
/**
 * This class is immutable !!!!
 */
{
  // TODO calculate isNumber in the constructor and store it in a boolean
  // then just return it
  private ExpressionNode (ExpressionNode left, ExpressionNode right, String op) {
    /**
     * Arguments in inverse polish order
     */
    super(op, left, right, null);
  }
  // Drop parent in Node.java ?
  // parent is not needed and is difficult to deal with
  // with derive

  public ExpressionNode (Expression expression) throws ExpressionException {
    super(null, null, null, null);
    char c = expression.next();

    if (c == '(') {
      // +, -, *, /
      setLeft(new ExpressionNode(expression));
      c = expression.next();
      switch (c) {
      case '+':
        setElement("+");
        break;
      case '-':
        setElement("-");
        break;
      case '*':
        setElement("*");
        break;
      case '/':
        setElement("/");
        break;
      case '^':
        setElement("^");
        break;
      default:
        expression.error("Invalid operator '" + c + "'.");
        break;

      }
      setRight(new ExpressionNode(expression));
      expression.expectChar(')');
    }

    else if (c == 'x') {
      setElement("x");
    }

    else if (Character.isDigit(c) || c == '-') {
      int last = 0;
      while (expression.relativeHas(last) &&
          Character.isDigit(expression.relativeAt(last))) {
        last++;
      }
      // TODO check if valid int
      setElement(expression.sub(last+1));
    }

    else {

      String op = expression.sub(3);
      if (op.equals("sin") || op.equals("cos")) {
        setElement(op);
      } else {
        expression.error("Invalid function '" + op + "'.", -2);
      }
      expression.expectChar('(');
      setRight(new ExpressionNode(expression));
      expression.expectChar(')');
    }

  }

  private ExpressionNode numNode (int x) {
    return new ExpressionNode(null, null, String.valueOf(x));
  }
  private ExpressionNode expNode (ExpressionNode left, ExpressionNode right) throws DerivationException{
    if (left.equal(0)) {
      if (right.equal(0)) {
        throw new DerivationException("Derivation Error: 0^0");
      } else {
        return numNode(0);
      }
    } else if (left.equal(1)) {
      return numNode(1);
    } else if (right.equal(0)) {
      return numNode(1);
    } else if (right.equal(1)) {
      return left;
    } else {
      return new ExpressionNode(left, right, "^");
    }
  }
  private ExpressionNode prodNode (ExpressionNode left, ExpressionNode right) {
    if (left.equal(0)) {
      return numNode(0);
    } else if (left.equal(1)) {
      return right;
    } else if (right.equal(0)) {
      return numNode(0);
    } else if (right.equal(1)) {
      return left;
    } else {
      return new ExpressionNode(left, right, "*");
    }
  }
  private ExpressionNode divNode (ExpressionNode left, ExpressionNode right) throws DerivationException {
    if (left.equal(0)) {
      return numNode(0);
    } else if (right.equal(0)) {
      throw new DerivationException("Derivation error : Division by zero");
    } else if (right.equal(1)) {
      return left;
    } else {
      return new ExpressionNode(left, right, "/");
    }
  }
  private ExpressionNode plusNode (ExpressionNode left, ExpressionNode right) {
    if (left.equal(0)) {
      return right;
    } else if (right.equal(0)) {
      return left;
    } else {
      return new ExpressionNode(left, right, "+");
    }
  }
  private ExpressionNode minusNode (ExpressionNode left, ExpressionNode right) {
    // On garde 0 - right, on retire le 0 au toString
    if (right.equal(0)) {
      return left;
    } else {
      return new ExpressionNode(left, right, "-");
    }
  }

  public ExpressionNode derive () throws DerivationException {
    String e = getElement();
    ExpressionNode left = (ExpressionNode) this.getLeft();
    ExpressionNode right = (ExpressionNode) this.getRight();
    boolean numLeft = false;
    boolean numRight = false;
    if (left != null) {
      numLeft = left.isNumber();
    }
    if (right != null) {
      numRight = right.isNumber();
    }
    if (e.equals("sin")) {
      // immutable so we can use `this`
      return prodNode(right.derive(),
          new ExpressionNode(null, right, "cos"));
    } else if (e.equals("cos")) {
      // immutable so we can use `this`
      return prodNode(right.derive(),
          minusNode(numNode(0), new ExpressionNode(null, right, "sin")));
    } else if (e.equals("+") || e.equals("-") || e.equals ("*")
        || e.equals("/") || e.equals("^")) {
      if (numLeft) {
        if (numRight) {
          return numNode(0);
        }
      } else if (numRight) {
        if (e.equals("+") || e.equals("-")) {
          return left.derive();
        } else if (e.equals("/")) {
          // left right unchanged because we do not always evaluate (ex: 1/12)
          return divNode(left.derive(), right);
        } else if (e.equals("*")) {
          return prodNode(left.derive(), right);
        } else {
          return prodNode(right,
              prodNode(expNode(left, minusNode(right,numNode(1))),
                  left.derive()));
        }
      }
      ExpressionNode ld = left.derive();
      ExpressionNode rd = right.derive();
      if (e.equals("*")) {
        return plusNode(prodNode(ld, right), prodNode(left, rd));
      } else if (e.equals("/")) {
        return divNode(
            minusNode(
                prodNode(ld, right),
                prodNode(left, rd)),
                expNode(right, numNode(2)));
      } else if (e.equals("^")) {
        throw new DerivationException("Derivation Error: ^ is only supported with numbers");
      } else {
        return new ExpressionNode(ld, rd, e);
      }
    } else if (e.equals("x")) {
      return numNode(1);
    } else {
      return numNode(0);
    }
  }
  
  public String toString () {
    String e = getElement();
    if (e.equals("sin") || e.equals("cos")) {
      return e + "(" + getRight() + ")";
    } else if (e.equals("+") ||
        e.equals("-") ||
        e.equals("*") ||
        e.equals("/") ||
        e.equals("^")) {
      ExpressionNode left = (ExpressionNode) getLeft();
      String l = "";
      if (!(e.equals("-") && left.equal(0))) {
        l = left.toString();
      }
      String leftPar = "", rightPar = "";
      if (!this.isNumber() && !l.equals("")) {
        leftPar = "(";
        rightPar = ")";
      }
      return leftPar + l + e + getRight() + rightPar;
    } else {
      // x ou un nombre
      return e;
    }
  }

  private boolean isNumber () {
    String e = getElement();
    ExpressionNode left = (ExpressionNode) getLeft();
    ExpressionNode right = (ExpressionNode) getRight();
    if (left == null && right == null) {
      if (e.equals("x")) {
        return false;
      } else {
        try {
          Integer x = Integer.parseInt(e);
        } catch (NumberFormatException ex) {
          return false;
        }
        return true;
      }
    } else if (left == null) {
      return right.isNumber();
    } else {
      return left.isNumber() && right.isNumber();
    }
  }
  // TODO custom exception
  // This class should only be used to see if an expression is 1 or 0
  // because they are absorbant and neutral for some operations
  private int getNumber () throws NumberFormatException {
    String e = getElement();
    ExpressionNode left = (ExpressionNode) getLeft();
    ExpressionNode right = (ExpressionNode) getRight();
    if (left == null && right == null) {
      if (e.equals("x")) {
        throw new NumberFormatException();
      } else {
        return Integer.parseInt(e);
      }
    } else if (left == null) {
      if (e.equals("sin")) {
        return (int) Math.sin(right.getNumber());
      } else {
        return (int) Math.cos(right.getNumber());
      }
    } else {
      int l = left.getNumber();
      int r = right.getNumber();
      switch (e.charAt(0)) {
      case '+':
        return l + r;
      case '-':
        return l - r;
      case '*':
        return l * r;
      case '/':
        return l / r;
      case '^':
        return (int) Math.pow(l, r);
      default:
        assert(false); // should not happen
        return 0;
      }
    }
  }
  private boolean equal (double x) {
    try {
      return this.isNumber() && doubleEquals(x, this.getNumber());
    } catch (NumberFormatException ex) {
      assert false; // should not happend since isNumber is called first
      return false;
    }
  }
  private static boolean doubleEquals (double a, double b) {
    //return Math.abs((a - b) / a) < 1e-6; div by 0!!!
    return Math.abs(a - b) < 1e-6;
  }
}