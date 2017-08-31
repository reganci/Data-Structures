public class MagicNode {
  private String key_placeholder = null;
  private Journal journal_placeholder = null;
  private Journal here = null;
  private MagicNode[] next;
  public MagicNode(String k, Journal j) {
    if (k.length() == 0) {
      here = j;
    } else {
      key_placeholder = k;
      journal_placeholder = j;
    }
    next = new MagicNode[26];
  }
  private Journal subAdd(String key, Journal journal) {
    int i = key.charAt(0) - 'a';
    if (next[i] == null) {
      next[i] = new MagicNode(key.substring(1, key.length()), journal);
      return null;
    } else
      return next[i].add(key.substring(1, key.length()), journal);
  }
  /**
   * @pre journal != null
   * @post Add journal to the node or to one of its child,
   *       remove the previous one with the same key if there is one.
   * @return The previous one if there is one and null otherwise.
   */
  public Journal add(String key, Journal journal) {
    if (key.length() == 0) {
      Journal old = here;
      here = journal;
      return old;
    } else if (key_placeholder != null) {
      if (key.equals(key_placeholder)) {
        Journal old = journal_placeholder;
        journal_placeholder = journal;
        return old;
      } else {
        subAdd(key_placeholder, journal_placeholder);
        return subAdd(key, journal);
      }
    } else {
      return subAdd(key, journal);
    }
  }


  private Journal subRem(String key) {
    int i = key.charAt(0) - 'a';
    if (next[i] == null) {
      return null;
    } else {
      return next[i].remove(key.substring(1, key.length()));
    }
  }
  /**
   * @pre
   * @post Set the journal from node or one of its child with matching key to null
   * @return The removed journal if there is one and null otherwise.
   */
  public Journal remove(String key) {
    if (key.length() == 0) {
      Journal ret = here;
      here = null;
      return ret;
    }
    else if (key.equals(key_placeholder)) {
      Journal ret = journal_placeholder;
      journal_placeholder = null;
      key_placeholder = null;
      return ret;
    }
    else {
      return subRem(key);
    }
  }


  /**
   * @pre
   * @post
   * @return The journal from node or one of its child if keys match and null otherwise
   */
  private Journal subGet(String key) {
    int i = key.charAt(0) - 'a';
    if (next[i] == null) {
      return null;
    } else {
      return next[i].get(key.substring(1, key.length()));
    }
  }
  public Journal get(String key) {
    if (key.length() == 0) {
      return here;
    }
    else if (key.equals(key_placeholder)) {
      return journal_placeholder;
    }
    else {
      return subGet(key);
    }
  }

}