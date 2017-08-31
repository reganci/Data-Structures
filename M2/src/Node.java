public class Node<E>
{
  private E element;
  private Node<E> left, right, parent;

  public Node(E element, Node<E> left, Node<E> right, Node<E> parent)
  {
	setElement(element);
	setLeft(left);
	setRight(right);
	setParent(parent);
  }

  public E getElement() {
    return element;
  }

  public void setElement(E element) {
	this.element = element;
  }

  public Node<E> getLeft() {
	return left;
  }

  public void setLeft(Node<E> left) {
	this.left = left;
  }

  public Node<E> getRight() {
	return right;
  }

  public void setRight(Node<E> right) {
	this.right = right;
  }

  public Node<E> getParent() {
	return parent;
  }

  public void setParent(Node<E> parent) {
	this.parent = parent;
  }
}