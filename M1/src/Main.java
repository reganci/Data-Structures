public class Main {
  public static void main (String[] args) {
    InputFile in = null;
    OutputFile out = null;
    if (args.length < 1) {
      in = new InputFile(System.in);
    } else {
      in = InputFile.fromFile(args[0]);
    }
    if (args.length < 2) {
      out = new OutputFile(System.out);
    } else {
      out = OutputFile.fromFile(args[1]);
    }
    out.setSeparator("\n");
    PSInterpreter interpreter =
      new PSInterpreter(in, out);
    interpreter.interpret();
    in.close();
    out.close();
  }
}
