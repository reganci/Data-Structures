import java.util.Iterator;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

// Can't inherit from Scanner, it is final :(
public class InputFile implements Iterator<String> {
  private boolean needClose;
  private Scanner in;

  /*
   * @pre `fileName` n'est pas NULL
   * @post le fichier a été ouvert en lecture et une nouvelle
   *       instance de InputFile a été créée
   */
  public static InputFile fromFile (String fileName) {
    InputFile in = null;
    try {
      in = new InputFile(fileName);
    } catch (FileNotFoundException e) {
      System.err.println(fileName + " : No such file or directory.");
      System.exit(1);
    }
    return in;
  }

  /*
   * Instantie un fichier depuis un `fileName`
   * @pre `fileName` n'est pas NULL
   * @post le fichier a été ouvert et retourné
   */
  public InputFile (String fileName) throws FileNotFoundException {
    this.in = new Scanner(new FileReader(fileName));
    // I have opened it, I need to close it
    needClose = true;
  }
  /*
   * Instantie un `InputFile` depuis un `InputStream`
   * @pre `in` n'est pas NULL
   * @post `InputFile` a été créé
   */
  public InputFile (InputStream in) {
    this.in = new Scanner(in, "UTF-8");
    // I have not opened it, not my business
    needClose = false;
  }

  // Iterator
  /*
   * @post retourne vrai s'il y a encore un token, faux sinon
   * mais lance
   * IllegalStateException - si le fichier est fermé
   */
  public boolean hasNext () {
    return in.hasNext();
  }
  /*
   * @post retourne le prochain token
   * mais lance
   * NoSuchElementException - s'il n'y a plus d'input
   * IllegalStateException - si le fichier est fermé
   */
  public String next () {
    return in.next();
  }
  /*
   * @post supprime le prochain token
   * mais lance
   * NoSuchElementException - s'il n'y a plus d'input
   * IllegalStateException - si le fichier est fermé
   */
  public void remove () {
    in.next();
  }

  /*
   * @post ferme le stream s'il a été ouvert par `InputFile`,
   * c'est à dire qu'il a été instantié par `InputFile(String)`
   * mais lance
   * IllegalStateException - si le fichier est fermé
   */
  public void close () {
    if (needClose) {
      in.close();
    }
  }

  /*
   * @post retourne vrai si le prochain token est un token
   * mais lance
   * IllegalStateException - si le fichier est fermé
   */
  public boolean hasNextDouble () {
    return in.hasNextDouble();
  }
  /*
   * @pre le prochain token doit être un double
   * @post retourne le prochain token sous forme de double
   * mais lance
   * InputMismatchException - si le prochain token
   * n'est pas un double valide
   * NoSuchElementException - s'il n'y a plus d'input
   * IllegalStateException - si le fichier est fermé
   */
  public double nextDouble () {
    return in.nextDouble();
  }
}