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
