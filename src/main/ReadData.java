// Copyright 2017 Johannes Widder
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

	/**
	 * 
	 * 
	 * @param basePathName
	 *            Name des Pfades mit der Doxygenausgabe
	 * @param listeTags
	 *            Liste der Tags, die f�r diese Struktur im Inifile definiert
	 *            wurden.
	 * @param listeTyp
	 *            Typen von Verbindungen, die f�r diese Struktur im Inifile
	 *            definiert wurden.
	 * @throws IOException
	 * @throws Exception
	 */
	public ReadData(String basePathName, List<NodeTag> listeTags, List<NodeTyp> listeTyp)
			throws ExceptionParser, IOException {
		nodeLinkList.clear();
		nodeQuelleList.clear();
		// nodeTypList = listeTyp;

		getValues(new File(basePathName), aktivit�t.einlesen, listeTags, listeTyp);

		// Liste der Tags links oben ausgaben
		GenerateHTML.printHTMLTags(listeTags, nodeQuelleList, listeTyp, nodeLinkList);
		GenerateHTML.printHTMLNil();

		getValues(new File(basePathName), aktivit�t.schreiben, listeTags, listeTyp);
	}

	/**
	 * Konstruktor speziell f�r den Test.
	 * 
	 * @param listeNodeTag
	 */
	public ReadData(List<NodeTag> listeNodeTag, List<NodeTyp> listeTyp) {
		return;
	}

	enum status {
		LESE_BUCHSTABE, WAIT_DOLLAR, START_IDENTIFIER, READ_IDENTIFIER, ENDE_IDENTIFIER, READ_START_NEXT_IDENTIFIER, READ_NEXT_IDENTIFIER, END_NEXT_IDENTIFIER, ERKENNE_NUMBER, ERKENNE_NUMBER_1, ENDE_ERKENNE_NUMBER, state11, state12, state13, stateError
	};

	public enum aktivit�t {
		einlesen, schreiben
	};

	/**
	 * - Die Methode wird zwei Mal aufgerufen. Im ersten Druchgang werden alle
	 * HTML-Dateien eingelesen. Im zweiten Durchgang werden die Links in die
	 * Dateien eingetragen.
	 * 
	 * Tests:
	 * 
	 * - Was passiert, wenn inDriectory ein Datei und kein verzeichnis ist.
	 * 
	 * @param inDirectory
	 * @param inAktivit�t
	 *            W�hrend dem Durchgang auszuf�hrende Aktivit�ten.
	 * @param listeTags
	 * @param listeTyp
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	public void getValues(File inDirectory, aktivit�t inAktivit�t, List<NodeTag> listeTags, List<NodeTyp> listeTyp)
			throws ExceptionParser, IOException {

		File[] entries = inDirectory.listFiles();
		for (File tempFile : entries) {
			if (tempFile.isDirectory()) {
				getValues(tempFile, inAktivit�t, listeTags, listeTyp);
			} else {
				String extension = this.getFileExtendsion(tempFile);
				if (inAktivit�t == aktivit�t.schreiben) {
					if (tempFile.getName().contains("menudata.js")) {
						File f = new File( tempFile.getPath() );
						FileInputStream inputStream= new FileInputStream(f);
						InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
						
						BufferedReader mbufferedReader = new BufferedReader(inputStreamReader);
						
						StringBuilder stringBuilder = new StringBuilder();
						
						stringBuilder.append(mbufferedReader.readLine());
						while(mbufferedReader.ready())
						{
							stringBuilder.append("\r\n");
							stringBuilder.append(mbufferedReader.readLine());
						}
						mbufferedReader.close();
						
						String outputText = stringBuilder.toString().replace(FileUtils.getRelIndexName(),FileUtils.getRelDxygeDokuName());
						
						String name = tempFile.getAbsolutePath();
						tempFile.delete();

						BufferedWriter bw = new BufferedWriter(new FileWriter(name));
						bw.write(outputText);
						bw.close();
					}
				}
				if (extension.equals("html") || extension.equals("htm")) {
					System.out.println(String.format("%s %s", tempFile.isDirectory() ? "dir: " : "file:",
							tempFile.getAbsolutePath()));
					Reader reader = null;
					try {
						reader = new FileReader(tempFile.getAbsolutePath());
						String fileName = tempFile.getName();
						if (!tempFile.getName().contains("trace_data")) {
							if (!tempFile.getName().startsWith("$")) {
								String ausgabe = readData(reader, fileName, inAktivit�t, listeTags, listeTyp);
								if (inAktivit�t == aktivit�t.schreiben) {
									String name = tempFile.getAbsolutePath();
									tempFile.delete();

									String newName;
									if (tempFile.getName().equals(FileUtils.getRelIndexName())) {
										GenerateHTML.printHTMLFrame(name);
									}
									newName = name.replace(FileUtils.getRelIndexName(),
											FileUtils.getRelDxygeDokuName());
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
	 * Parse HTML-File
	 * 
	 * The Parser reads the HTML-File and puffers the values in output. If the
	 * parser starts reading a keyword, that bytes are buffered in reserve until
	 * reading the keyword is either finished successfully, or reading something
	 * else triggered a wrong alaram. In this case, that buffer reserve is just
	 * added to output to ensure leaving a correct file.
	 * 
	 * @param inReader
	 * @param fileName
	 * @param inAktivit�t
	 * @param listeNodeTag
	 * @param listeNodeTyp
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String readData(Reader inReader, String fileName, aktivit�t inAktivit�t, List<NodeTag> listeNodeTag,
			List<NodeTyp> listeNodeTyp) throws ExceptionParser, IOException {
		StringBuilder nameTagTyp = new StringBuilder();
		StringBuilder nameTag = new StringBuilder();
		StringBuilder nameNummer = new StringBuilder();
		StringBuilder nummber = new StringBuilder();

		StringBuilder output = new StringBuilder();

		StringBuilder reserve = new StringBuilder();

		if (fileName.startsWith("$"))
			return ("");

		status zustand = status.LESE_BUCHSTABE;
		for (int ch; (ch = inReader.read()) != -1;) {

			switch (zustand) {
			case LESE_BUCHSTABE:
				if (ch == '[') {
					zustand = status.WAIT_DOLLAR;
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.setLength(0);
						reserve.append((char) ch);
					}
				} else {
					if (aktivit�t.schreiben == inAktivit�t) {
						output.append((char) ch);
					}
				}
				break;
			case WAIT_DOLLAR:
				if (Character.isWhitespace(ch)) {
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (ch == '$') {
						zustand = status.START_IDENTIFIER;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.LESE_BUCHSTABE;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
							output.append(reserve.toString());
						}
					}
				}
				break;
			case START_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					zustand = status.READ_IDENTIFIER;
					nameTagTyp.setLength(0);
					nameTagTyp.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					zustand = status.LESE_BUCHSTABE;
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
						output.append(reserve.toString());
					}
				}
				break;
			case READ_IDENTIFIER:
				if (Character.isJavaIdentifierPart(ch)) {
					nameTagTyp.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_IDENTIFIER;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.LESE_BUCHSTABE;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
							output.append(reserve.toString());
						}
					}
				}
				break;
			case ENDE_IDENTIFIER:
				if (Character.isWhitespace(ch)) {
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isDigit(ch)) {
						zustand = status.ERKENNE_NUMBER_1;
						nameNummer.setLength(0);
						nameNummer.append((char) ch);
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					} else {
						if (ch == '$') {
							zustand = status.READ_START_NEXT_IDENTIFIER;
							if (aktivit�t.schreiben == inAktivit�t) {
								reserve.append((char) ch);
							}
						} else {
							throw new ExceptionParser("Wort and er falschen Stelle");
						}
					}
				}
				break;
			case READ_START_NEXT_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					zustand = status.READ_NEXT_IDENTIFIER;
					nameTag.setLength(0);
					nameTag.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					zustand = status.stateError;
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				}
				break;
			case READ_NEXT_IDENTIFIER:
				if (Character.isJavaIdentifierStart(ch)) {
					nameTag.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						// Erstes Wort erkannt
						System.out.println(String.format(">>> %s", nameTag));
						zustand = status.END_NEXT_IDENTIFIER;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					} else {
						zustand = status.stateError;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					}
				}
				break;
			case END_NEXT_IDENTIFIER:
				if (Character.isWhitespace(ch)) {
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isDigit(ch)) {
						nameNummer.setLength(0);
						nameNummer.append((char) ch);
						zustand = status.ERKENNE_NUMBER;
						if (aktivit�t.schreiben == inAktivit�t) {
							reserve.append((char) ch);
						}
					} else {
						if (ch == '$') {
							zustand = status.READ_START_NEXT_IDENTIFIER;
							if (aktivit�t.schreiben == inAktivit�t) {
								reserve.append((char) ch);
							}
						} else {
							throw new ExceptionParser("Wert1");
						}

					}
				}
				break;
			case ERKENNE_NUMBER:
				if (Character.isDigit(ch)) {
					nameNummer.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_ERKENNE_NUMBER;
						switch (inAktivit�t) {
						case einlesen:
							checkLinkTyp(nameTagTyp, listeNodeTyp);
							checkLinkTag(nameTag, listeNodeTag);
							createLink(fileName, nameTagTyp, nameTag, nameNummer);
							break;
						case schreiben:
							reserve.append((char) ch);
							output.append(this.ausgabeLink(reserve.toString(), nameTagTyp.toString(),
									nameTag.toString(), nameNummer.toString()));
							reserve.setLength(0);
							break;
						}
					} else {
						if (ch == ']') {
							zustand = status.LESE_BUCHSTABE;
							switch (inAktivit�t) {
							case einlesen:
								checkLinkTyp(nameTagTyp, listeNodeTyp);
								checkLinkTag(nameTag, listeNodeTag);
								createLink(fileName, nameTagTyp, nameTag, nameNummer);
								break;
							case schreiben:
								reserve.append((char) ch);
								output.append(this.ausgabeLink(reserve.toString(), nameTagTyp.toString(),
										nameTag.toString(), nameNummer.toString()));
								reserve.setLength(0);
								break;
							}
						} else {
							zustand = status.stateError;
							if (aktivit�t.schreiben == inAktivit�t) {
								reserve.append((char) ch);
							}
						}
					}
				}
				break;
			case ERKENNE_NUMBER_1: // Ziffer in Quelle Erkannt
				if (Character.isDigit(ch)) {
					nameNummer.append((char) ch);
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (Character.isWhitespace(ch)) {
						zustand = status.ENDE_ERKENNE_NUMBER;
						switch (inAktivit�t) {
						case einlesen:
							checkLinkTag(nameTagTyp, listeNodeTag);
							this.createNodeQuelle(fileName, nameTagTyp.toString(), nameNummer.toString(), listeNodeTag);
							break;
						case schreiben:
							reserve.append((char) ch);
							output.append(this.ausgabeQuelle(reserve.toString(), nameTagTyp.toString(),
									nameNummer.toString()));
							reserve.setLength(0);
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
								switch (inAktivit�t) {
								case einlesen:
									checkLinkTag(nameTagTyp, listeNodeTag);
									this.createNodeQuelle(fileName, nameTagTyp.toString(), nameNummer.toString(),
											listeNodeTag);
									break;
								case schreiben:
									reserve.append((char) ch);
									output.append(this.ausgabeQuelle(reserve.toString(), nameTagTyp.toString(),
											nameNummer.toString()));
									reserve.setLength(0);
									break;
								}
							} else {
								zustand = status.stateError;
								switch (inAktivit�t) {
								case einlesen:
									break;
								case schreiben:
									output.append(reserve.toString());
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
					if (aktivit�t.schreiben == inAktivit�t) {
						reserve.append((char) ch);
					}
				} else {
					if (ch == ']') {
						zustand = status.LESE_BUCHSTABE;
						if (aktivit�t.schreiben == inAktivit�t) {
							output.append((char) ch);
						}
					} else {
						// Hier darf nichts mehr kommen.
						throw new ExceptionParser("Nach Numer kommt ein Text");
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
		return output.toString();
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
	 * 
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
				errorMessage.append(String.format("Multiple defined Tag \"%s\"\n", inTag.toString()));
				for (NodeSource nodeError : nodeQuelleList) {
					if (nodeError.getName().equals(inTag) & nodeError.getNummer().equals(inNummer)) {
						errorMessage.append(
								String.format("Tag \"%s\" is already declared in: %s", inTag, nodeError.getFileName()));
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
		// Dieser Zustand d�rfte nie Eintreten
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
