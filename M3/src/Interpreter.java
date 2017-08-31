import java.util.Scanner;

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
  private MagicTree tree;
  
  /**
   * Lecteur du terminal.
   */
  private Scanner userIn;
  
  /**
   * Indique si l'utilisateur a quitté l'application.
   */
  private boolean quit;
  
  /**
   * Constructeur.
   * @param inputPath
   */
  public Interpreter(String inputPath)
  {
	//Init
    InputFile input = InputFile.fromFile(inputPath);
    tree = new MagicTree();
    userIn = new Scanner(System.in);
    quit = false;
	//Reading
    if(input.hasNext())
        input.nextLine(); //Skip first line
    for(int it = 1; input.hasNext(); it++)
    {
      Journal j = decode(input.nextLine());
      if(j != null)
      {
    	String key = convertToKey(j.getTitle());
    	System.out.println(key);
    	try
    	{
	 	  tree.put(key, j);
		} catch (NullJournalException | InvalidKeyException e) {
		  System.err.println("line " + it + " : invalid data");
		  System.exit(1);
		}
      }
    }
    input.close();
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
  public static String convertToKey(String value)
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
	String[] values = line.split(",");
	Journal j = new Journal();
	//Load values
	if(values.length <= 8)
	{
      if(values.length > 0)  j.setRank(values[0]);  	
	  if(values.length > 1)  j.setTitle(values[1]);  	
	  if(values.length > 2)  j.setFor1(values[2]);  	
	  if(values.length > 3)  j.setFor1Name(values[3]);  	
	  if(values.length > 4)  j.setFor2(values[4]);  	
	  if(values.length > 5)  j.setFor2Name(values[5]);  	
	  if(values.length > 6)  j.setFor3(values[6]);  	
	  if(values.length > 7)  j.setFor3Name(values[7]);  	
	}
	//If empty title
	if(j.getTitle().equals(""))
      return null;
	else
      return j;
  }
  
  /**
   * Cette méthode applique la commande utilisée par l'utilisateur.
   * @param value
   */
  public void applyCommand(String cmd)
  {
	cmd = cmd.trim(); //Suppress useless spaces
	String value;
	if(cmd.startsWith("get"))
	{
	  value = cmd.substring(3, cmd.length());
	  Journal journal = tree.get(convertToKey(value));
	  if(journal==null) System.out.println(cmd.substring(3, cmd.length())+" not found");
      else System.out.println(journal);
	}
	else if(cmd.startsWith("remove"))
	{
	  value = cmd.substring(6, cmd.length());
	  Journal removedJournal = tree.remove(convertToKey(value));
	  if(removedJournal==null) System.out.println(cmd.substring(6, cmd.length())+" not found");
	  else System.out.println(removedJournal);
	}
	else if(cmd.startsWith("quit"))
	{
	  quit = true;	
	}
  }
}
