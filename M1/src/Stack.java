public class Stack {
  private class Node {
    Object value;
    Node next;
    /*
     * @post instantie un node avec comme valeur `value` et comme next `next`
     */
    public Node (Object value, Node next) {
      this.value = value;
      this.next = next;
    }
  }
  private Node top;
  /*
   * @post instantie une pile vide
   */
  public Stack () {
    top = null;
  }
  /*
   * @post ajoute `value` au sommet de la pile
   */
  public void push (Object value) {
    top = new Node(value, top);
  }
  /*
   * @post retire le sommet de la pile et retourne sa valeur
   */
  public Object pop () {
    if (top == null) {
      throw new EmptyStackException("Stack is empty");
    }
    Object value = top.value;
    top = top.next;
    return value;
  }
  /*
   * @post retourne `true` si la pile est vide et `false` sinon
   */
  public boolean isEmpty() {
    return top == null;
  }
}