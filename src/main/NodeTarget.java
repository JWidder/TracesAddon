package main;

/**
 * @author Johannes Widder
 *
 */
@SuppressWarnings("javadoc")
public class NodeTarget extends NodeSource {

	private String typ;
	public NodeTarget(String inFileName,String inTyp, String inNamem, String inNummer) {
		super(inFileName,inNamem, inNummer);
		setTyp(inTyp);
	}
	public String getTyp() {
		return this.typ;
	}
	public void setTyp(String link) {
		this.typ= link;
	}
	
	@Override
	public
	String toString(){return String.format("%s %s",this.typ,super.toString());
	}
}
