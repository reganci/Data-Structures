/**
 * Classe Main du programme.
 */
public class Main
{
  public static void main(String[] args)
  {
    InputFile input = null;
    OutputFile output = null;
    //Check input
    if(args.length>0)
    {
      input = InputFile.fromFile(args[0]);
    } 
    else
    {
      input = new InputFile(System.in);
    }
    //Check output
    if (args.length>1)
    {
      output = OutputFile.fromFile(args[1]);     
    }
    else
    {
      output = new OutputFile(System.out);
    }
    Interpreter i = new Interpreter(input, output);
	boolean ok = i.derive();
    input.close();
    output.close();
    if(!ok)
    {
      System.exit(1);
    }
  }
}
