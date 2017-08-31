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
   */
  public static void main (String[] args)
  {
    //Check input
    if(args.length>0)
    {
      new Interpreter(args[0]);
    } 
  }
}