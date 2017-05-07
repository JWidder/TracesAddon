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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Die Klasse stellt die Namen der evrschiedenen Dateien bereit.
 * 
 * @author Johannes Widder
 */
public class FileUtils {
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

	public static String getRelDxygeDokuName() {
		return ("indexDoxygen.html");
	}

	public static String getRelIndexName() {
		return ("index.html");
	}
}
