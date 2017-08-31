/**
 * Classe permettant de lire des formules arithmétiques dans un fichier input
 * et d'écrire le résultat dérivé dans un fichier output.
 *
 */
public class Interpreter
{
  /**
   * Représente le fichier d'entrée.
   */
  private InputFile input;
  
  /**
   * Représente le fichier de sortie.
   */
  private Output output;

  /**
   * Constructeur.
   * @param inputFileName
   * @param outputFileName
   */
  public Interpreter(InputFile input, Output output)
  {
    this.input = input;
    this.output = output;
  }

  /**
   * Permet de dériver les formules arithmétiques et d'écrire le résultat dans le fichier de sortie.
   */
  public boolean derive()
  {
    boolean ok = true;
    if (input != null && output != null)
    {
      for (int line = 1; input.hasNext(); line++)
      {
        FormalExpressionTree f = null;
        try {
          f = new ExpressionTree(input.next());
        } catch (ExpressionException e) {
          System.err.println("line " + line + ", " +
              "col " + e.getCol() + " : " + e.getMessage());
          ok = false;
        }
        if (f != null) {
          FormalExpressionTree df = f.derive();
          output.putString(df.toString());
        }
      }
    }
    return ok;
  }
}