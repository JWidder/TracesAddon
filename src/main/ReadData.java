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

	private List<NodeSource> nodeQuelleList = new ArrayList<>();
	private List<NodeTarget> nodeLinkList = new ArrayList<>();
	// private List<NodeTyp> nodeTypList;

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
	 * @throws Exception
	 */
	public ReadData(String basePathName, List<NodeTag> listeTags, List<NodeTyp> listeTyp) throws ExceptionParser, IOException {
		nodeLinkList.clear();
		nodeQuelleList.clear();
		// nodeTypList = listeTyp;

		getValues(new File(basePathName), aktivität.einlesen, listeTags, listeTyp);

		// Liste der Tags links oben ausgaben
		GenerateHTML.printHTMLTags(listeTags, nodeQuelleList, listeTyp, nodeLinkList);
		GenerateHTML.printHTMLNil();

		getValues(new File(basePathName), aktivität.schreiben, listeTags, listeTyp);
	}

	/**
	 * Konstruktor speziell für den Test.
	 * 
	 * @param listeNodeTag
	 */
	public ReadData(List<NodeTag> listeNodeTag, List<NodeTyp> listeTyp) {
		return;
	}

	enum status {
		LESE_BUCHSTABE, WAIT_DOLLAR, START_IDENTIFIER, READ_IDENTIFIER, ENDE_IDENTIFIER, READ_START_NEXT_IDENTIFIER, READ_NEXT_IDENTIFIER, END_NEXT_IDENTIFIER, ERKENNE_NUMBER, ERKENNE_NUMBER_1, ENDE_ERKENNE_NUMBER, state11, state12, state13, stateError
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
	 * @param listeTags
	 * @param listeTyp
	 * @throws Exception
	 */
	public void getValues(File inFile, aktivität inAktivität, List<NodeTag> listeTags, List<NodeTyp> listeTyp)
			throws ExceptionParser {
		File[] entries = inFile.listFiles();
		for (File wert : entries) {
			if (wert.isDirectory()) {
				getValues(wert, inAktivität, listeTags, listeTyp);
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
								String ausgabe = readData(reader, fileName, inAktivität, listeTags, listeTyp);
								if (inAktivität == aktivität.schreiben) {
									String name = wert.getAbsolutePath();
									wert.delete();

									String newName;
									if (wert.getName().equals(FileName.getRelIndexName())) {
										GenerateHTML.printHTMLFrame(name);
									}
									newName = name.replace(FileName.getRelIndexName(), FileName.getRelDxygeDokuName());
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
	 * @param listeNodeTag
	 * @param listeNodeTyp
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public String readData(Reader inReader, String fileName, aktivität inAktivität, List<NodeTag> listeNodeTag,
			List<NodeTyp> listeNodeTyp) throws ExceptionParser, IOException {
		StringBuilder nameTagTyp = new StringBuilder();
		StringBuilder nameTag = new StringBuilder();
		StringBuilder nameNummer = new StringBuilder();
		StringBuilder nummber = new StringBuilder();

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
					nameTagTyp.setLength(0);
					nameTagTyp.append((char) ch);
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
					nameTagTyp.append((char) ch);
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
						zustand = status.ERKENNE_NUMBER_1;
						nameNummer.setLength(0);
						nameNummer.append((char) ch);
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
					nameTag.setLength(0);
					nameTag.append((char) ch);
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
					nameTag.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						// Erstes Wort erkannt
						System.out.println(String.format(">>> %s", nameTag));
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
						nameNummer.setLength(0);
						nameNummer.append((char) ch);
						zustand = status.ERKENNE_NUMBER;
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
			case ERKENNE_NUMBER:
				if (Character.isDigit(ch)) {
					nameNummer.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_ERKENNE_NUMBER;
						switch (inAktivität) {
						case einlesen:
							checkLinkTyp(nameTagTyp, listeNodeTyp);
							checkLinkTag(nameTag, listeNodeTag);
							createLink(fileName, nameTagTyp, nameTag, nameNummer);
							break;
						case schreiben:
							reserve.append((char) ch);
							ausgabe.append(this.ausgabeLink(reserve.toString(), nameTagTyp.toString(),
									nameTag.toString(), nameNummer.toString()));
							break;
						}
					} else {
						if (ch == ']') {
							zustand = status.LESE_BUCHSTABE;
							switch (inAktivität) {
							case einlesen:
								checkLinkTyp(nameTagTyp, listeNodeTyp);
								checkLinkTag(nameTag, listeNodeTag);
								createLink(fileName, nameTagTyp, nameTag, nameNummer);
								break;
							case schreiben:
								reserve.append((char) ch);
								ausgabe.append(this.ausgabeLink(reserve.toString(), nameTagTyp.toString(),
										nameTag.toString(), nameNummer.toString()));
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
			case ERKENNE_NUMBER_1: // Ziffer in Quelle Erkannt
				if (Character.isDigit(ch)) {
					nameNummer.append((char) ch);
					if (aktivität.schreiben == inAktivität) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_ERKENNE_NUMBER;
						switch (inAktivität) {
						case einlesen:
							checkLinkTag(nameTagTyp, listeNodeTag);
							this.createNodeQuelle(fileName, nameTagTyp.toString(), nameNummer.toString(), listeNodeTag);
							break;
						case schreiben:
							reserve.append((char) ch);
							ausgabe.append(this.ausgabeQuelle(reserve.toString(), nameTagTyp.toString(),
									nameNummer.toString()));
							break;
						}
					} else {
						if (Character.isJavaIdentifierStart(ch)) {
							nummber.setLength(ch);
							nummber.append((char) ch);
							zustand = status.state11;
						} else {
							if (ch == ']') {
								zustand = status.LESE_BUCHSTABE;
								switch (inAktivität) {
								case einlesen:
									checkLinkTag(nameTagTyp, listeNodeTag);
									this.createNodeQuelle(fileName, nameTagTyp.toString(), nameNummer.toString(),
											listeNodeTag);
									break;
								case schreiben:
									reserve.append((char) ch);
									ausgabe.append(this.ausgabeQuelle(reserve.toString(), nameTagTyp.toString(),
											nameNummer.toString()));
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
			case ENDE_ERKENNE_NUMBER:
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
					nummber.append((char) ch);
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_ERKENNE_NUMBER;
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
	 * @param inTyp
	 * @param inTag
	 * @param listeTag
	 * @param listeTyp
	 * @throws Exception
	 */
	private void checkLinkTyp(StringBuilder inTyp, List<NodeTyp> listeTyp) throws ExceptionParser {
		boolean status = false;

		for (NodeTyp node : listeTyp) {
			if (node.getName().equals(inTyp.toString())) {
				status = true;
			}
		}
		if (status == false) {
			throw new ExceptionParser(String.format("Typ: %s is not defined in Inifile", inTyp.toString()));
		}
	}

	/**
	 * Verifies whether the link 
	 * @param inTag
	 * @param listeTag
	 * @throws Exception
	 */
	private void checkLinkTag(StringBuilder inTag, List<NodeTag> listeTag) throws ExceptionParser {
		boolean status = false;

		for (NodeTag node : listeTag) {
			if (node.getName().equals(inTag.toString())) {
				status = true;
			}
		}
		if (status == false) {
			throw new ExceptionParser(String.format("Tag: %s is not defined in Inifile", inTag.toString()));
		}
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
	 * @throws Exception
	 */
	private void createNodeQuelle(String inFileName, String inTag, String inNummer, List<NodeTag> listeTag)
			throws ExceptionParser {
		// Check whether the node is alread defined
		for (NodeSource node : nodeQuelleList) {
			if (node.getName().equals(inTag) & node.getNummer().equals(inNummer)) {
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append(String.format("Multiple defined Tag \"%s\"\n",inTag.toString()));
				for (NodeSource nodeError : nodeQuelleList)
				{
					if (nodeError.getName().equals(inTag) & nodeError.getNummer().equals(inNummer))
					{
						errorMessage.append(String.format("Tag \"%s\" is already declared in: %s", inTag,nodeError.getFileName()));
					}
				}
				throw new ExceptionParser(errorMessage.toString());
			}
		}
		// Check whether the type is declared ini Ini-File
		boolean status = false;
		for (NodeTag node : listeTag) {
			if (node.getName().equals(inTag)) {
				status = true;
			}
		}
		if (status == false) {
			throw new ExceptionParser("Undefined Tag");
		}
		// Add Node to List
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
	 *            return
	 */
	private String ausgabeLink(String eingabe, String inTyp, String inTag, String inNummer) {
		// Suche die Referenz
		// for (NodeSource node : this.nodeQuelleList) {
		// if (node.getName().equals(inTag) &&
		// node.getNummer().equals(inNummer)) {
		// if (node.getName().startsWith("FMEA")) {
		// TODO Als Link ausgeben.
		// return (eingabe);
		// }
		// }
		// }
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
