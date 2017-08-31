import java.util.ArrayList;

/*
 * Implémente 5 arbres reliant les journaux de manière différentes,
 * - titleTree,
 * - rankTree,
 * - forTree1, 2 ou 3,
 * chacun permettant de manipuler et de trier des éléments journaux par
 *  - ordre alphabétique,
 *  - par rang,
 *  - par champ de recherche,
 */
public class Library{
  AVLTree<String,Journal> titleTree = new AVLTree<String, Journal>();
  AVLTree<String, AVLTree<String,Journal>> rankTree = new AVLTree<String, AVLTree<String, Journal>>();
  AVLTree<String, AVLTree<String,Journal>> forTree1 = new AVLTree<String, AVLTree<String, Journal>>();
  AVLTree<String, AVLTree<String,Journal>> forTree2 = new AVLTree<String, AVLTree<String, Journal>>();
  AVLTree<String, AVLTree<String,Journal>> forTree3 = new AVLTree<String, AVLTree<String, Journal>>();

  /*
   * Ajoute le journal j dans l'ensemble des arbres
   * @pre Journal != null afin de créer un key valide
   * @post
   *  si key déjà dans la library : retourne l'ancien journal (override)
   *  null sinon
   */
  public Journal put(Journal j) throws InvalidKeyException{
    String titleKey = makeKey(j.getTitle());
    Entry<String,Journal> ret = titleTree.put(titleKey,j);

    //Rank
    ret = subPut(j, titleKey, j.getRank(), rankTree);

    //For1
    if(!j.getFor1().equals("")) {//on ajoute pas si il n'y a pas de For
      ret = subPut(j, titleKey, j.getFor1(), forTree1);
    }

    //For2
    if(!j.getFor2().equals("")) {
      ret = subPut(j, titleKey, j.getFor2(), forTree2);
    }

    //For3
    if(!j.getFor3().equals("")) {
      ret = subPut(j, titleKey, j.getFor3(), forTree3);
    }

    //Return si on retire
    if(ret == null)
      return null;
    else
      return ret.value;
  }

  public Entry<String, Journal> subPut (Journal j, String titleKey, String key, AVLTree<String,AVLTree<String,Journal>> tree) throws InvalidKeyException {
    Entry<String, AVLTree<String, Journal>> entry = tree.get(key);
    AVLTree<String,Journal> subTree = null;
    if (entry == null) {
      subTree = new AVLTree<String, Journal>();
      tree.put(key, subTree);
    } else {
      subTree = entry.value;
    }
    return subTree.put(titleKey, j);
  }

  /*
   * Récupère le journal avec le titre title
   * @pre title != null afin de créer un key valide
   * @post
   *  retourne le journal avec la même key il existe
   *  null sinon
   */
  public Journal get(String title) throws InvalidKeyException{
    String titleKey = makeKey(title);
    Entry<String,Journal> entry = titleTree.get(titleKey);
    if (entry == null) {
      return null;
    } else {
      return entry.value;
    }
  }

  /*
   * Retire le journal j correspondant au titre title de l'ensemble des arbres
   * @pre title != null afin de créer un key valide
   * @post
   *  si key déjà dans la library : return le journal
   *  null sinon
   */
  public Journal remove(String title) throws InvalidKeyException{
    String titleKey = makeKey(title);
    Entry<String,Journal> ret = titleTree.remove(titleKey);
    if(ret == null) return null; //si il n'est pas dans l'un, il n'est pas dans les autres

    //Rank
    Entry<String, AVLTree<String, Journal>> subTree = rankTree.get(ret.value.getRank());
    if(subTree == null) return null; //cas impossible si arbre rempli en même temps
    else subTree.value.remove(titleKey);

    //For1
    subTree = forTree1.get(ret.value.getFor1());
    if(subTree != null)
      subTree.value.remove(titleKey);



    //For2
    subTree = forTree2.get(ret.value.getFor2());
    if(subTree != null)
      subTree.value.remove(titleKey);

    //For3
    subTree = forTree3.get(ret.value.getFor3());
    if(subTree != null)
      subTree.value.remove(titleKey);

    return ret.value;
  }

  /*
   * retourne l'ensemble des journaux par ordre alphabétique
   * @post
   *  ArrayList contenant l'ensemble des journaux
   */
  public ArrayList<Journal> getOrderedAll(){
    return titleTree.getOrderedList();
  }

  /*
   * retourne l'ensemble des journaux de rang = rank par ordre alphabétique
   * @post
   *  ArrayList contenant l'ensemble des journaux
   */
  public ArrayList<Journal> getOrderedRank(String rank){
    return subGet(rankTree, rank);
  }

  /*
   * retourne l'ensemble des journaux de champ de recherche numéro = forNumber et de nom = for par ordre alphabétique
   * @post
   *  ArrayList contenant l'ensemble des journaux
   */
  public ArrayList<Journal> getOrderedFor(String foR, int forNumber) throws InvalidKeyException{
    if(forNumber == 1)
      return subGet(forTree1, foR);
    else if(forNumber == 2)
      return subGet(forTree2, foR);
    else if(forNumber == 3)
      return subGet(forTree3, foR);
    else return null; //TODO error
  }

  private ArrayList<Journal> subGet(AVLTree<String, AVLTree<String,Journal>> tree, String key) {
    Entry<String,AVLTree<String,Journal>> entry = tree.get(key);
    if (entry == null) {
      return new ArrayList<Journal>();
    } else {
      return entry.value.getOrderedList();
    }
  }

  public String makeKey(String title){
    return title;
  }
}