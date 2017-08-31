import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintStream;

public class OutputFile extends PrintStream implements Output
{
  private boolean needClose;
  private boolean first = true;
  private String separator = "\n";
  
  /*
   * @pre `fileName` n'est pas NULL
   * @post le fichier a été ouvert en `encoding`
   *       et une nouvelle instance de OutputFile est créée
   */
  public OutputFile (String fileName, String encoding) throws FileNotFoundException, UnsupportedEncodingException
  {
             super(fileName, encoding);
             // I have opened it, I need to close it
             needClose = true;
  }
  /*
   * @pre `out` n'est pas NULL
   * @post le fichier a été ouvert en UTF-8
   * (car tous les fichiers doivent être encodés en UTF-8)
   * et une nouvelle instance de OutputFile est créée
   */
  public OutputFile (PrintStream out) {
    super(out);
    // I have not opened it, not my business
    needClose = false;
  }
  
  /*
   * @pre `fileName` n'est pas NULL
   * @post le fichier a été ouvert en UTF-8
   *       (car tous les fichiers doivent être encodés en UTF-8)
   *       et une nouvelle instance de OutputFile est créée
   */
  public static OutputFile fromFile (String fileName) {
    OutputFile out = null;
    try {
      out = new OutputFile(fileName, "UTF-8");
    } catch (FileNotFoundException e) {
      System.err.println(fileName + " : No such file or directory.");
      System.exit(1);
    } catch (UnsupportedEncodingException e) {
      System.err.println("UTF-8 is not supported.");
      System.exit(1);
    }
    return out;
  }


  /*
   * @pre `newSeparator` n'est pas NULL
   * @post le séparateur est maintenant `newSeparator`
   */
  public void setSeparator (String newSeparator) {
    separator = newSeparator;
  }
  private void separate () {
    if (!first) {
      print(separator);
    }
    first = false;
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putString (String x) {
    separate();
    print(x);
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putShort (short x) {
    separate();
    print(x);
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putInt (int x) {
    separate();
    print(x);
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putBoolean (boolean x) {
    separate();
    print(x);
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putFloat (float x) {
    separate();
    print(x);
  }
  /*
   * @pre `x` n'est pas NULL et le fichier n'est pas fermé
   * @post imprime le séparateur si ce n'est pas le premier élément puis
   * imprime `x`
   */
  public void putDouble (double x) {
    separate();
    print(x);
  }

  /*
   * @pre le fichier n'est pas fermé
   * @post fait un retour à la ligne puis ferme le fichier s'il a été
   * ouvert par le constructeur `OutputFile(String)`.
   */
  public void close () {
    println();
    if (needClose) {
      super.close();
    }
  }
}