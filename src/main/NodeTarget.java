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
