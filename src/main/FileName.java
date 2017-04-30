package main;

/**
 * Die Klasse stellt die Namen der evrschiedenen Dateien bereit.
 * 
 * @author Johannes Widder
 */
public class FileName {
	private static String path = "html\\";
	private static String pattern = "%s$$$%s.html";
	private static String relPattern = "$$$%s.html";

	/**
	 * @return Name der Datei in der das Frame ausgegeben wird.
	 */
	public static String getFrameName() {
		return String.format("%s%s", path, "index.html");
	}

	/**
	 * @return Name der HTML-Datei mit allen Tags
	 */
	public static String getTagListeName() {
		return String.format(pattern, path, "Tags");
	}
	public static String getRelTagListeName() {
		return String.format(relPattern, "Tags");
	}

	/**
	 * @param inTag
	 *            Individuelles Tag
	 * @param Nummer
	 *            Nummer des Tags
	 * @return
	 */
	public static String getSourceName(NodeTag nodeTag) {
		return String.format(pattern, path, nodeTag.getName());
	}
	public static String getRelSourceName(NodeTag nodeTag) {
		return String.format(relPattern, nodeTag.getName());
	}

	/**
	 * @param inTyp
	 * @return
	 */
	public static String getLinkName(NodeSource nodeQuelle) {
		return String.format(pattern, path, String.format("%s_%s", nodeQuelle.getName(), nodeQuelle.getNummer()));
	}
	public static String getRelLinkName(NodeSource nodeQuelle) {
		return String.format(relPattern, String.format("%s_%s", nodeQuelle.getName(), nodeQuelle.getNummer()));
	}

	public static String getNilName() {
		return String.format(pattern, path, "Nil");
	}
	public static String getRelNilName() {
		return String.format(relPattern, "Nil");
	}
}
