/**
 * Classe représentant un journal.t
 *
 */
public class Journal
{
	/**
	 * Rang du journal.
	 */
	private String rank;
	
	/**
	 * Titre du journal.
	 */
	private String title;
	
	/**
	 * For1 du journal.
	 */
	private String for1;
	
	/**
	 * For1Name du journal.
	 */
	private String for1Name;
	
	/**
	 * For2 du journal.
	 */
	private String for2;
	
	/**
	 * For2Name du journal.
	 */
	private String for2Name;
	
	/**
	 * For3 du journal.
	 */
	private String for3;
	
	/**
	 * For3Name du journal.
	 */
	private String for3Name;
	
	/**
	 * Constructeur.
	 * @param rank
	 * @param title
	 * @param for1
	 * @param for1Name
	 * @param for2
	 * @param for2Name
	 * @param for3
	 * @param for3Name
	 */
	public Journal(String rank, String title, String for1, String for1Name, String for2, String for2Name, String for3, String for3Name)
	{
		setRank(rank);
		setTitle(title);
		setFor1(for1);
		setFor1Name(for1Name);
		setFor2(for2);
		setFor2Name(for2Name);
		setFor3(for3);
		setFor3Name(for3Name);
	}
	
	/**
	 * Constructeur.
	 */
	public Journal()
	{
	  this("", "", "", "", "", "", "", "");
	}
	
	@Override
	public String toString()
	{
	  return getRank()+","+getTitle()+","+getFor1()+","+getFor1Name()+","+getFor2()+","+getFor2Name()+","+getFor3()+","+getFor3Name();	
	}
	
	//GETTERS ET SETTERS
	
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFor1() {
		return for1;
	}
	public void setFor1(String for1) {
		this.for1 = for1;
	}
	public String getFor1Name() {
		return for1Name;
	}
	public void setFor1Name(String for1Name) {
		this.for1Name = for1Name;
	}
	public String getFor2() {
		return for2;
	}
	public void setFor2(String for2) {
		this.for2 = for2;
	}
	public String getFor2Name() {
		return for2Name;
	}
	public void setFor2Name(String for2Name) {
		this.for2Name = for2Name;
	}
	public String getFor3() {
		return for3;
	}
	public void setFor3(String for3) {
		this.for3 = for3;
	}
	public String getFor3Name() {
		return for3Name;
	}
	public void setFor3Name(String for3Name) {
		this.for3Name = for3Name;
	}
}