import java.util.ArrayList;
import java.util.Comparator;

public class AVLTree<K,V>{
  protected class AVLNode<K,V>{
    protected int height;
    Entry<K,V> element;
    AVLNode<K,V> parent;
    AVLNode<K,V> left;
    AVLNode<K,V> right;
    AVLNode(K key, V value, AVLNode<K,V> parent, AVLNode<K,V> left, AVLNode<K,V> right, int height){
      this.element = new Entry<K,V>(key,value);
      this.parent = parent;
      this.right = right;
      this.left = left;
      this.height = height;
      if(left!=null)
        height = Math.max(height, left.getHeight());
      if(right!=null)
        height = Math.max(height, right.getHeight());
    }
    protected void setNodeHeight(int h){
      height = h;
    }
    protected int getHeight(){
      return height;
    }
    public boolean isInternal(){
      return ((left != null) || (right!=null));
    }
  }
  /*
   * attributs de AVLTree
   */
  protected int height;
  protected AVLNode<K,V> root;
  protected Comparator<K> c;
  public AVLTree(){
    root = null;
    c = new DefaultComparator<K>();
  }
  public boolean isRoot(AVLNode<K,V> node){
    return node.equals(root);
  }
  public int getHeight() {
    return height;
  }
  private void setHeight(AVLNode<K,V> node){
    if(node.left == null && node.right==null)
      node.setNodeHeight(1);
    else if(node.left == null)
      node.setNodeHeight(node.right.getHeight());
    else if (node.right==null)
      node.setNodeHeight(node.left.getHeight());
    else
      node.setNodeHeight(1+Math.max(node.left.getHeight(),node.right.getHeight()));
  }
  private boolean isBalanced(AVLNode<K,V> node){
    int dif;
    if (node.left == null && node.right==null)
      dif = 0;
    else if(node.left == null)
      dif = node.right.getHeight();
    else if (node.right==null)
      dif = node.left.getHeight();
    else
      dif = node.left.getHeight() - node.right.getHeight();
    return ((-1<=dif)&&(dif<=1));
  }
  private AVLNode<K,V> tallerChild(AVLNode<K,V> node){
    if(node.left.getHeight()>node.right.getHeight()) return node.left;
    else if(node.left.getHeight()<node.right.getHeight()) return node.right;
    if(isRoot(node)) return node.left;
    if(node.equals(node.parent.left)) return node.left; // correction (avant : node.right?)
    else return node.right;
  }
  private void rebalance(AVLNode<K,V> z){
    if(z.isInternal())
      setHeight(z);
    while(!isRoot(z)){
      z = z.parent;
      setHeight(z);
      if(!isBalanced(z)){
        z = restructure(z);
        //TODO on doit refaire set ici ?
        setHeight(z.left);
        setHeight(z.right);
        setHeight(z);
      }
    }
  }

  private AVLNode<K,V> restructure(AVLNode<K,V> z){

    AVLNode<K,V> y = tallerChild(z);
    AVLNode<K,V> x = tallerChild(y);
    AVLNode<K,V> a = null;
    AVLNode<K,V> b = null;
    AVLNode<K,V> c = null;
    AVLNode<K,V> t0 = null;
    AVLNode<K,V> t1 = null;
    AVLNode<K,V> t2 = null;
    AVLNode<K,V> t3 = null;

    if (z.left.equals(y)) {
      if (y.left.equals(x)) {
        // single rotation right:
        a = x;
        b = y;
        c = z;
        t0 = x.left;
        t1 = x.right;
        t2 = y.right;
        t3 = z.right;
      } else {
        // double rotation left-right:
        a = y;
        b = x;
        c = z;
        t0 = y.left;
        t1 = x.left;
        t2 = x.right;
        t3 = z.right;
      }
    } else if (y.left.equals(x)) {
      // double rotation right-left:
      a = z;
      b = x;
      c = y;
      t0 = z.left;
      t1 = x.left;
      t2 = x.right;
      t3 = y.right;
    } else {
      // single rotation left:
      a = z;
      b = y;
      c = x;
      t0 = z.left;
      t1 = y.left;
      t2 = x.left;
      t3 = x.right;
    }
    /*
     * Link nodes
     */
    if (z.parent == null) {
      root = b;
    } else {
      if (z.parent.left.equals(z))
        z.parent.left = b;
      else
        z.parent.right = b;
    }
    b.parent = z.parent;
    b.left = a;
    a.parent = b;
    b.right = c;
    c.parent = b;
    a.left = t0;
    if (t0 != null) {
      t0.parent = a;
    }
    a.right = t1;
    if (t1 != null) {
      t1.parent = a;
    }
    c.left = t2;
    if (t2 != null) {
      t2.parent = c;
    }
    c.right = t3;
    if (t3 != null) {
      t3.parent = c;
    }
    /*
     * Set the new height
     */
    setHeight(a);
    setHeight(c);
    setHeight(b);
    return b;
  }

  private AVLNode<K,V> treeSearch(K key, AVLNode<K,V> currentNode)
  {
    if(!currentNode.isInternal()) return currentNode;
    else {
      K currentKey = currentNode.element.key;
      int comp = c.compare(key, currentKey);
      if (comp<0 && currentNode.left!=null) return treeSearch(key, currentNode.left);
      else if (comp>0 && currentNode.right!=null) return treeSearch(key, currentNode.right);
      return currentNode;
    }
  }

  public Entry<K,V> put(K key, V v) throws InvalidKeyException
  {
    if(key==null) throw new InvalidKeyException("invalid entry");
    if(root == null){
      root = new AVLNode<K, V>(key, v, null, null, null, 0);
      rebalance(root);
      return null;
    }
    AVLNode<K,V> newNode;
    AVLNode<K,V> currentNode;

    currentNode = treeSearch(key,root);
    K currentKey = currentNode.element.key;
    int comp = c.compare(key, currentKey);
    if(comp<0){ // left children of currentNode
      newNode = new AVLNode<K, V>(key,v, currentNode, null, null, 0);
      currentNode.left = newNode;
      rebalance(newNode);
      return null;
    }
    else {
      if(comp>0){ // right children of currentNode
        newNode = new AVLNode<K, V>(key, v, currentNode, null, null, 0);
        currentNode.right = newNode;
        rebalance(newNode);
        return null;
      }
    }
    if(currentNode != null)
      return currentNode.element; // si le noeud existe deja, on modifie son contenu en dehors de l'AVL
    else
      return null;
  }

  protected AVLNode<K,V> leftMost(AVLNode<K,V> subTree)
  {
    if(subTree.left==null) return subTree;
    return (leftMost(subTree.left));
  }

  public void replace (AVLNode<K,V> old, AVLNode<K,V> young) {
    if (old.parent == null) {
      root = young;
    } else if (old.parent.left == old) {
      old.parent.left = young;
    } else {
      old.parent.right = young;
    }
    if (young != null) {
      young.parent = old.parent;
    }
  }
  public Entry<K,V> remove(K key) throws InvalidKeyException
  {
    if(key==null) throw new InvalidKeyException("invalid entry");

    AVLNode<K,V> currentNode;

    currentNode = treeSearch(key,root);
    K currentKey = currentNode.element.key;
    int comp = c.compare(key, currentKey);
    if(comp!=0) return null; // if comp==0, currentNode is the node to remove

    if (currentNode.left == null) {
      replace(currentNode, currentNode.right);
      // FIXME ???
      if(currentNode.right != null) //arrive seulement si moins de deux noeud => equilibré
        rebalance(currentNode.right);
    }
    else if(currentNode.right == null) {
      replace(currentNode, currentNode.left);
      // FIXME ???
      if(currentNode.left != null) //arrive seulement si moins de deux noeud => equilibré
        rebalance(currentNode.left);
    }
    else
    {
      AVLNode<K,V> replaceNode = leftMost(currentNode.right);
      replace(replaceNode, replaceNode.right);
      replace(currentNode, replaceNode);
      replaceNode.right = currentNode.right;
      if (replaceNode.right != null) {
        replaceNode.right.parent = replaceNode;
      }
      replaceNode.left = currentNode.left;
      if (replaceNode.left != null) {
        replaceNode.left.parent = replaceNode;
      }
      rebalance(replaceNode);
    }
    return currentNode.element;
  }

  public Entry<K,V> get(K key)
  {
    if (root == null) {
      return null;
    }
    AVLNode<K,V> currentNode = treeSearch(key, root);

    K currentKey = currentNode.element.key;
    int comp = c.compare(key, currentKey);
    if (comp<0)
      currentNode = currentNode.left;
    else if (comp>0)
      currentNode = currentNode.right;

    if(currentNode != null)
      return currentNode.element;
    else
      return null;
  }

  protected void inorder(ArrayList<V> list, AVLNode<K,V> currentNode)
  {
    if(currentNode.left!=null) inorder(list, currentNode.left);
    list.add(currentNode.element.value);
    if(currentNode.right!=null) inorder(list, currentNode.right);
  }

  public ArrayList<V> getOrderedList()
  {
    ArrayList<V> list = new ArrayList<V>();
    if(root!=null) inorder(list, root);
    return list;
  }
}