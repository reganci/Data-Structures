import java.util.ArrayList;

/**
 *
 * Classe s'occupant de lire les données dans un fichier input et gérant les commandes utilisateurs.
 *
 */
public class Interpreter
{
  /**
   * La structure de données.
   */
  private Library lib;

  /**
   * Lecteur du terminal.
   */
  private InputFile userIn;

  /**
   * Indique si l'utilisateur a quitté l'application.
   */
  private boolean quit;
  
  private boolean verbose;
  private boolean dontPanic;

  /**
   * Constructeur.
   * @param inputPath
   */
  public Interpreter(String inputPath, boolean verbose, boolean dontPanic)
  {
    this.verbose = verbose;
    this.dontPanic = dontPanic;
    lib = new Library();
    userIn = new InputFile(System.in);
    quit = false;
    if (inputPath != null) {
      //Init
      InputFile input = InputFile.fromFile(inputPath);
      //Reading
      if(input.hasNext())
        input.nextLine(); //Skip first line
      String line;
      for(int it = 1; input.hasNext(); it++)
      {
        line = input.nextLine();
        if(!line.equals(""))
        {
          Journal j = decode(line);
          if(j != null)
          {
            try
            {
              lib.put(j);
            } catch (InvalidKeyException e) {
              printErr("line " + it + " : invalid data");
              System.exit(1);
            }
          }
        }
      }
      input.close();
    }
    //Waitint for user
    while(!quit)
      applyCommand(userIn.nextLine());
    //Close user scanner
    userIn.close();
  }

  /**
   * Cette méthode convertit une valeur en une clé utilisable par la structure de données.
   * @param value
   * @return key
   */
  public String convertToKey(String value)
  {
    return value.trim().replaceAll("[\\d\\W\\s\\p{Punct}]","").toLowerCase();
  }

  /**
   * Cette méthode décode la donnée texte en un objet journal.s
   * pre: line n'est pas null.
   * post: null est renvoyé si le string n'a pas pu etre décodé, un journal est renvoyé sinon.
   * @param line
   */
  public Journal decode(String line)
  {
    //Split string
    String[] values = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
    if(values.length > 1 & values.length <= 8)
    {
      Journal j = new Journal(values[0], values[1]);
      if(!j.getTitle().equals(""))
      {
        if(values.length > 2)  j.setFor1(values[2]);
        if(values.length > 3)  j.setFor1Name(values[3]);
        if(values.length > 4)  j.setFor2(values[4]);
        if(values.length > 5)  j.setFor2Name(values[5]);
        if(values.length > 6)  j.setFor3(values[6]);
        if(values.length > 7)  j.setFor3Name(values[7]);
        return j;
      }
      else
      {
        printErr("title error : "+line);
        return null;
      }
    }
    else
    {
      printErr("format error : "+line);
      return null;
    }
  }

  /*
   * Cette méthode affiche le résultat de la méthode get
   * @pre output != null
   * @param output les résultats à afficher
   */
  private void print(ArrayList<Journal> output)
  {
    System.out.println(output.size() + " result" + (output.size() > 1 ? "s" : "") + " found" + (output.size() > 0 ? ":" : "."));
    for (Journal out : output) {
      System.out.println("* " + out);
    }
  }
  private void print(Journal output)
  {
    if (output == null)
    {
      System.out.println("Not found.");
    }
    else
    {
      System.out.println("Found:");
      System.out.println("* " + output);
    }
  }
  /**
   * Cette méthode applique la commande utilisée par l'utilisateur.
   * @param value
   */
  public void applyCommand(String cmd)
  {
    cmd = cmd.trim(); //Suppress useless spaces
    String[] values = cmd.split(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
    if(values.length > 0)
    {
      String command = values[0];
      if(command.equals("put"))
      {
        for(int i = 1; i < values.length; i++)
        {
          Journal j = decode(values[i]);
          if(j == null)
            printErr("impossible to add this journal");
          else
          {
            try {
              lib.put(j);
            } catch (InvalidKeyException e) {
              printErr("impossible to add this journal");
            }
            printInfo(values[i] + " added");
          }
        }
      }
      else if(command.equals("get"))
      {
        if (values.length == 1)
        {
          // Get all
          print(lib.getOrderedAll());
        }
        else if (values.length == 3)
        {
          // All sorted by
          if (values[1].equals("name"))
          {
            try
            {
              print(lib.get(values[2]));
            }
            catch (InvalidKeyException e)
            {
              printErr("invalid key for get " + values[1]);
            }
          }
          else if (values[1].equals("rank"))
          {
            print(lib.getOrderedRank(values[2]));
          }
          else if (values[1].length() == 4 && values[1].substring(0, 3).equals("for"))
          {
            int num = Integer.valueOf(values[1].substring(3,4));
            try
            {
              print(lib.getOrderedFor(values[2], num));
            }
            catch (InvalidKeyException e)
            {
              printErr("invalid key for get " + values[1]);
            }
          }
          else
          {
            printErr("invalid field for get");
          }
        }
        else
        {
          printErr("invalid number of arguments for get");
        }
      }
      else if(command.equals("remove"))
      {
        Journal j = null;
        try
        {
          j = lib.remove(values[1]);
        }
        catch (InvalidKeyException e)
        {
          printErr("impossible to remove this journal : Invalid key");
        }
        if (j == null)
          printErr("impossible to remove this journal : No such Journal");
        else
        {
          printInfo("result removed: "+j);
        }
        return;
      }
      else if(command.equals("quit"))
      {
        quit = true;
        printInfo("Application has closed");
        return;
      }
      else
        printErr("unknown command \""+command+"\"");
    }
  }
  private void printErr (String message) {
    System.err.println("EE " + message);
    if (!dontPanic) {
      System.exit(1);
    }
  }
  private void printInfo (String message) {
    if (verbose) {
      System.out.println("II " + message);
    }
  }
}