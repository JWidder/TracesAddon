package main;

/**
 * @author Johannes Widder
 *
 */
@SuppressWarnings("javadoc")
public class NodeSource {
	
	private String name;
	private String nummer;
	private String fileName;
	
	/**
	 * 
	 * @param inFileName
	 * @param inNamem
	 * @param inNummer
	 */
	public NodeSource(String inFileName, String inNamem, String inNummer) {
		setName(inNamem);
		setNummer(inNummer);
		setFileName(inFileName);
	}

	public String getNummer() {
		return this.nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public void setName(String name) {
		this.name= name;
	}
	public String getName() {
		return this.name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return String.format("Quelle: %s %s in %s", getName(),getNummer(),getFileName());
	}
	
}
