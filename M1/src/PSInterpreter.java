import java.util.*;


public class PSInterpreter {
	private InputFile input;
	private Output output;
	private Stack stack;
	private Map<String,Double> map;
	/*
	 * @pre `input` et `output` ne sont pas nuls ni fermés
	 * @post Un interpréteur est créé
	 */
	public PSInterpreter (InputFile input, Output output) {
		this.input = input;
		this.output = output;
		this.stack = new Stack();
		this.map = new HashMap<String,Double>();
	}
	/*
	 * @pre le stack n'est pas vide, il a un String en top
	 * @post pop un boolean du stack
	 */
	private String popString () {
		Object value = stack.pop();
		if (!(value instanceof String)) {
			throw new PostScriptException("A String was expected, not " + value, output);
		}
		return (String) value;
		}
		/*
		 * @pre le stack n'est pas vide, il a un double en top
		 * @post pop un boolean du stack
		 */
		private double popDouble () {
			Object value = stack.pop();
			if (!(value instanceof Double)) {
				throw new PostScriptException("A Double was expected, not " + value, output);
			}
			return (Double) value;
		}
		/*
		 * @pre le stack n'est pas vide, il a un boolean en top
		 * @post pop un boolean du stack
		 */
		private boolean popBoolean () {
			Object value = stack.pop();
			if (value instanceof Boolean) {
				throw new PostScriptException("A Boolean was expected, not " + value, output);
			}
			return (Boolean) value;
		}
		/*
		 * @pre l'`input` n'a pas encore été interprètée
		 * @post interprête l'`input` en tant que PostScript simplifié
		 *       et imprime les résultats dans `output` donné au constructeur
		 */
		public void interpret () {
			while (input.hasNext()) {
				if (input.hasNextDouble()) {
					stack.push(input.nextDouble());
				} else {
					String op = input.next();
					if (op.length() > 1 && op.charAt(0) == '/') {
						String var = op.substring(1);
						if(!var.equals("pstack")&&!var.equals("add")&&!var.equals("sub")&&!var.equals("mul")&&!var.equals("div")&&!var.equals("pstack")&&!var.equals("dup")&&!var.equals("pstack")&&!var.equals("exch")&&!var.equals("eq")&&!var.equals("ne")&&!var.equals("def")&&!var.equals("pop"))
							stack.push(var);
						else {
							throw new PostScriptException(var+" is an invalid variable name", output);
						}
					} else {
						switch (op) {
						case "pstack":
							pstack();
							break;
						case "add":
							add();
							break;
						case "sub":
							sub();
							break;
						case "mul":
							mul();
							break;
						case "div":
							div();
							break;
						case "dup":
							dup();
							break;
						case "exch":
							exch();
							break;
						case "eq":
							eq();
							break;
						case "ne":
							ne();
							break;
						case "def":
							def();
							break;
						case "pop":
							pop();
							break;
						default:
							if (map.containsKey(op)) {
								stack.push(map.get(op));
							} else {
								throw new PostScriptException(op+" is an unknown operation", output);
							}
						}
					}
				}
			}
		}
		private void pstack () {
			Stack temp = new Stack();
			while (!stack.isEmpty()) {
				Object value = stack.pop();
				if (value instanceof Double) {
					output.putDouble((Double) value);
				} else if (value instanceof Boolean) {
					output.putBoolean((Boolean) value);
				} else {
					// We only put string and doubles so it should
					// be a string independently from user input
					output.putString((String) value);
				}
				temp.push(value);
			}
			while (!temp.isEmpty()) {
				stack.push(temp.pop());
			}
		}
		private void add () {
			stack.push(popDouble() + popDouble());
		}
		private void sub () {
			// On est pas sur si java regarde le premier
			// ou le deuxième en premier
			double left = popDouble();
			double right = popDouble();
			stack.push(left - right);
		}
		private void mul () {
			stack.push(popDouble() * popDouble());
		}
		private void div () {
			double num = popDouble();
			double deno = popDouble();
			if (deno == 0) {
				throw new PostScriptException("Division by zero", output);
			}
			stack.push(num / deno);
		}
		private void dup () {
			double value = popDouble();
			stack.push(value);
			stack.push(value);
		}
		private void exch () {
			double value1 = popDouble();
			double value2 = popDouble();
			stack.push(value1);
			stack.push(value2);
		}
		private void eq () {
			stack.push(popDouble() == popDouble());
		}
		private void ne () {
			stack.push(popDouble() != popDouble());
		}
		private void def () {
			double value = popDouble();
			String key = popString();
			map.put(key, value);
		}
		private void pop () {
			stack.pop();
		}
	}