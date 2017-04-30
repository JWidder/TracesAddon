package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Einlesen und Prozessieren der eingelesenen Daten
 * 
 * 
 * @author Johannes Widder
 *
 */
public class ReadData {

	List<NodeSource> nodeQuelleList = new ArrayList<>();
	List<NodeTarget> nodeLinkList = new ArrayList<>();
	// String ausgabe = "html\\$$$%s.html";

	/**
	 * 
	 * 
	 * @param basePathName
	 *            Name des Pfades mit der Doxygenausgabe
	 * @param listeTags
	 *            Liste der Tags, die für diese Struktur im Inifile definiert
	 *            wurden.
	 * @param listeTyp
	 *            Typen von Verbindungen, die für diese Struktur im Inifile
	 *            definiert wurden.
	 * @throws IOException
	 */
	public ReadData(String basePathName, List<NodeTag> listeTags, List<NodeTyp> listeTyp) throws IOException {
		nodeLinkList.clear();
		nodeQuelleList.clear();

		getValues(new File(basePathName), aktivität.einlesen);

		// Liste der Tags links oben ausgaben
		GenerateHTML.printHTMLTags(listeTags, nodeQuelleList, listeTyp, nodeLinkList);
		GenerateHTML.printHTMLNil(FileName.getNilName());

		getValues(new File(basePathName), aktivität.schreiben);
	}

	/**
	 * Konstruktor speziell für den Test.
	 */
	public ReadData() {
		return;
	}

	enum status {
		LESE_BUCHSTABE, WAIT_DOLLAR, START_IDENTIFIER, READ_IDENTIFIER, ENDE_IDENTIFIER, READ_START_NEXT_IDENTIFIER, READ_NEXT_IDENTIFIER, END_NEXT_IDENTIFIER, state9a, state9b, state10, state11, state12, state13, stateError
	};

	public enum aktivität {
		einlesen, schreiben
	};

	/**
	 * - Die Methode wird zwei Mal aufgerufen. Im ersten Druchgang werden alle
	 * HTML-Dateien eingelesen. Im zweiten Durchgang werden die Links in die
	 * Dateien eingetragen.
	 * 
	 * @param inFile
	 * @param inAktivität
	 *            Während dem Durchgang auszuführende Aktivitäten.
	 */
	public void getValues(File inFile, aktivität inAktivität) {
		File[] entries = inFile.listFiles();
		for (File wert : entries) {
			if (wert.isDirectory()) {
				getValues(wert, inAktivität);
			} else {
				String extension = this.getFileExtendsion(wert);
				if (extension.equals("html") || extension.equals("htm")) {
					System.out.println(
							String.format("%s %s", wert.isDirectory() ? "dir: " : "file:", wert.getAbsolutePath()));
					Reader reader = null;
					try {
						reader = new FileReader(wert.getAbsolutePath());
						String fileName = wert.getName();
						if (!wert.getName().contains("trace_data")) {
							if (!wert.getName().startsWith("$")) {
								String ausgabe = readData(reader, fileName, inAktivität);
								if (inAktivität == aktivität.schreiben) {
									String name = wert.getAbsolutePath();
									wert.delete();

									String newName;
									if (wert.getName().equals("index.html")) {
										GenerateHTML.printHTMLFrame(name);
									}
									newName = name.replace("index.html", "indexDoxygen.html");
									FileWriter fw = new FileWriter(newName);
									BufferedWriter bw = new BufferedWriter(fw);
									bw.write(ausgabe);
									bw.close();
								}
							}
						}
					} catch (IOException e) {
						System.err.println("Fehler beim Lesen der Datei!");
					} finally {
						try {
							reader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return;
	}

	private String getFileExtendsion(File inFile) {
		String name = inFile.getName();
		try {
			return (name.substring(name.lastIndexOf(".") + 1));
		} catch (Exception e) {
			e.printStackTrace();
			return ("");
		}
	}

	/**
	 * 
	 * 
	 * @param inReader
	 * @param fileName
	 * @param inAktivität
	 * @return
	 * @throws IOException
	 */
	public String readData(Reader inReader, String fileName, aktivität inAktivität) throws IOException {
		StringBuilder wort1 = new StringBuilder();
		StringBuilder wort2 = new StringBuilder();
		StringBuilder wort3 = new StringBuilder();
		StringBuilder wort4 = new StringBuilder();

		StringBuilder ausgabe = new StringBuilder();

		StringBuilder reserve = new StringBuilder();

		if (fileName.startsWith("$"))
			return ("");

		status zustand = status.LESE_BUCHSTABE;
		for (int ch; (ch = inReader.read()) != -1;) {

			switch (zustand) {
			case LESE_BUCHSTABE:
				if (ch == '[') {
					zustand = status.WAIT_DOLLAR;
					if (aktivität.schreiben == inAktivität) {
						reserve.setLength(0);
						reserve.append((char) ch);
					}
				} else {
					if (aktivität.schreiben == inAktivität) {
						ausgabe.append((char) ch);
					}
				}
				break;
			case WAIT_DOLLAR:
				if (Character.isWhitespace(ch)) {
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (ch == '$') {
						zustand = status.START_IDENTIFIER;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.LESE_BUCHSTABE;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					}
				}
				break;
			case START_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					zustand = status.READ_IDENTIFIER;
					wort1.setLength(0);
					wort1.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					zustand = status.stateError;
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				}
				break;
			case READ_IDENTIFIER:
				if (Character.isJavaIdentifierPart(ch)) {
					wort1.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_IDENTIFIER;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.stateError;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					}
				}
				break;
			case ENDE_IDENTIFIER:
				if (Character.isWhitespace(ch)) {
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isDigit(ch)) {
						zustand = status.state9b;
						wort3.setLength(0);
						wort3.append((char) ch);
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						if (ch == '$') {
							zustand = status.READ_START_NEXT_IDENTIFIER;
							if (aktivität.schreiben == inAktivität) {
								reserve.append((char) ch);
							}
						} else {
							zustand = status.stateError;
							if (aktivität.schreiben == inAktivität) {
								reserve.append((char) ch);
							}
						}
					}
				}
				break;
			case READ_START_NEXT_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					zustand = status.READ_NEXT_IDENTIFIER;
					wort2.setLength(0);
					wort2.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					zustand = status.stateError;
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				}
				break;
			case READ_NEXT_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					wort2.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						// Erstes Wort erkannt
						System.out.println(String.format(">>> %s", wort2));
						zustand = status.END_NEXT_IDENTIFIER;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.stateError;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					}
				}
				break;
			case END_NEXT_IDENTIFIER:
				if (Character.isWhitespace(ch)) {
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isDigit(ch)) {
						wort3.setLength(0);
						wort3.append((char) ch);
						zustand = status.state9a;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						if (ch == '$') {
							zustand = status.READ_START_NEXT_IDENTIFIER;
							if (aktivität.schreiben == inAktivität) {
								reserve.append((char) ch);
							}
						} else {
							zustand = status.stateError;
							if (aktivität.schreiben == inAktivität) {
								reserve.append((char) ch);
							}
						}
					}
				}
				break;
			case state9a:
				if (Character.isDigit(ch)) {
					// zustand = status.state9a;
					wort3.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.state10;
						switch (inAktivität) {
						case einlesen:
							createLink(fileName, wort1, wort2, wort3);
							break;
						case schreiben:
							reserve.append((char) ch);
							ausgabe.append(this.ausgabeLink(reserve.toString(),wort1.toString(), wort2.toString(), wort3.toString()));
							break;
						}
					} else {
						if (ch == ']') {
							zustand = status.LESE_BUCHSTABE;
							switch (inAktivität) {
							case einlesen:
								createLink(fileName, wort1, wort2, wort3);
								break;
							case schreiben:
								reserve.append((char) ch);
								ausgabe.append(this.ausgabeLink(reserve.toString(), wort1.toString(), wort2.toString(), wort3.toString()));
								break;
							}
						} else {
							zustand = status.stateError;
							if (aktivität.schreiben == inAktivität) {
								reserve.append((char) ch);
							}
						}
					}
				}
				break;
			case state9b: // Ziffer in Quelle Erkannt
				if (Character.isDigit(ch)) {
					wort3.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.state10;
						switch (inAktivität) {
						case einlesen:
							this.createNodeQuelle(fileName, wort1.toString(), wort3.toString());
							break;
						case schreiben:
							reserve.append((char) ch);
							ausgabe.append(this.ausgabeQuelle(reserve.toString(), wort1.toString(), wort3.toString()));
							break;
						}
					} else {
						if (Character.isJavaIdentifierStart(ch)) {
							wort4.setLength(ch);
							wort4.append((char) ch);
							zustand = status.state11;
						} else {
							if (ch == ']') {
								zustand = status.LESE_BUCHSTABE;
								switch (inAktivität) {
								case einlesen:
									this.createNodeQuelle(fileName, wort1.toString(), wort3.toString());
									break;
								case schreiben:
									reserve.append((char) ch);
									ausgabe.append(this.ausgabeQuelle(reserve.toString(), wort1.toString(), wort3.toString()));
									break;
								}
							} else {
								zustand = status.stateError;
								switch (inAktivität) {
								case einlesen:
									break;
								case schreiben:
									ausgabe.append(reserve.toString());
									break;
								}
							}
						}
					}
				}
				break;
			case state10:
				if (Character.isWhitespace(ch)) {
					zustand = status.ENDE_IDENTIFIER;
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (ch == '[') {
						zustand = status.LESE_BUCHSTABE;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.stateError;
						if (aktivität.schreiben == inAktivität) {
							reserve.append((char) ch);
						}
					}
				}
				break;
			case state11:
				if (Character.isJavaIdentifierPart(ch)) {
					wort4.append((char) ch);
					// zustand = status.state11;
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.state10;
					} else {
						if (ch == ']') {
							zustand = status.LESE_BUCHSTABE;
						} else {
							zustand = status.stateError;
						}
					}
				}
				break;
			case state12:
				break;
			case state13:
				break;
			case stateError:
				break;
			}
		}
		return ausgabe.toString();
	}

	/**
	 * 
	 * 
	 * @param fileName
	 * @param inTyp
	 * @param inTag
	 * @param inNummer
	 */
	private void createLink(String fileName, StringBuilder inTyp, StringBuilder inTag, StringBuilder inNummer) {
		NodeTarget nodeLink = new NodeTarget(fileName, inTyp.toString(), inTag.toString(), inNummer.toString());
		this.nodeLinkList.add(nodeLink);
	}

	/**
	 * 
	 * @param inFileName
	 * @param inTag
	 * @param inNummer
	 */
	private void createNodeQuelle(String inFileName, String inTag, String inNummer) {
		NodeSource nodeQuelle = new NodeSource(inFileName, inTag, inNummer);
		this.nodeQuelleList.add(nodeQuelle);
	}

	/**
	 * 
	 * 
	 * @param result
	 * @param inTyp
	 * @param inTag
	 * @param inNummer
	 * 
	 * return
	 */
	private String ausgabeLink(String eingabe, String inTyp, String inTag, String inNummer) {
		// Suche die Referenz
		//for (NodeSource node : this.nodeQuelleList) {
			//if (node.getName().equals(inTag) && node.getNummer().equals(inNummer)) {
				//if (node.getName().startsWith("FMEA")) {
					// TODO Als Link ausgeben.
					//return (eingabe);
				//}
			//}
		//}
		// Dieser Zustand dürfte nie Eintreten
		// TODO Exception werfen, wenn Node nicht erkannt wurde. 
		return eingabe;
	}

	/**
	 * @param eingabe
	 * @param inTag
	 * @param inNummer
	 * 
	 * @return
	 */
	private String ausgabeQuelle(String eingabe, String inTag, String inNummer) {
		// TODO Link eintragen.
		return eingabe;
	}

	List<NodeSource> getNodeQuelle() {
		return this.nodeQuelleList;
	}

	List<NodeTarget> getNodeLink() {
		return this.nodeLinkList;
	}

}
