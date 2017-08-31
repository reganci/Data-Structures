public class MagicTree{
  private MagicNode root = null;
  private int n = 0;

  public Journal get(String key) {
    if(isEmpty()||key==null) return null;
    return root.get(key);
  }


  public Journal put(String key, Journal journal) throws NullJournalException, InvalidKeyException{
    if(key==null) keyError("Can not add with null key");
    if(journal==null) journalError("Null journal at key : "+key);
    if(isEmpty()){
      root = new MagicNode(key, journal);
      n++;
      return null;
    }
    else{
      n++;
      return root.add(key,journal);
    }
  }

  public Journal remove(String key){
    if(isEmpty()||key==null)
      return null;
	n--;
    return root.remove(key);
  }

  public int size() {
    return n;
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public void keyError (String message)
		  throws InvalidKeyException {
	throw new InvalidKeyException(message);
  }
  public void journalError (String message)
		  throws NullJournalException {
	throw new NullJournalException(message);
  }
}