import java.util.ArrayList;

/**
 *
 * Main classe.
 *
 */
public class Main
{
  /**
   * Main.
   * @param args
   * @throws InvalidKeyException 
   */
  public static void main (String[] args) throws InvalidKeyException
  {
    //Check input
    String inputPath = null;
    boolean verbose = false;
    boolean dontPanic = false;
    boolean example = false;
    for (int i = 0; i < args.length; i++)
    {
      if (args[i].equals("-v")) {
        verbose = true;
      }
      else if (args[i].equals("-d"))
      {
        dontPanic = true;
      }
      else if (args[i].equals("-e"))
      {
        example = true;
      }
      else if (args[i].equals("-i"))
      {
        if (i + 1 >= args.length) {
          System.err.println("EE missing input file");
          System.exit(1);
        }
        else
        {
          inputPath = args[i+1];
          i++;
        }
      }
      else
      {
        System.err.println("EE invalid option " + args[i]);
        System.exit(1);
      }
    }
    if (!example)
    {
      new Interpreter(inputPath, verbose, dontPanic);
    }
    else
    {
      Library lib = new Library();
      Journal j1 = new Journal("A","Journal1","125","bio","","","","");
      Journal j2 = new Journal("A","Journal2","20","chimie","","","","");
      Journal j3 = new Journal("B","Journal3","20","chimie","","","","");
      Journal j4 = new Journal("B","Journal4","10","trolls","","","","");
      Journal j5 = new Journal("B","Journal5","125","bio","","","","");
      lib.put(j3);
      lib.put(j2);
      lib.put(j1);
      lib.put(j4);
      lib.put(j5);

      ArrayList<Journal> bio = lib.getOrderedFor("125",1);
      for(Journal j : bio)
        System.out.println(j.getTitle());

      lib.remove("Journal5");
      bio = lib.getOrderedFor("125",1);
      for(Journal j : bio)
        System.out.println(j.getTitle());
    }
  }
}