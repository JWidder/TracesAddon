package main;

import org.jdom2.Element;

/**
 * Aufgabe diser Klasse ist es eine neue HTML-Datei anzulegen
 * 
 * @author Johannes Widder
 *
 */
public abstract class GenerateHTML {


	protected static Element addElement(String name) {
		return addElement(name, null);
	}

	/**
	 * Test
	 * 
	 * @param name XML-Element, dass in den Baum eingetragen werden soll
	 * @param element Element im Baum an das neue Element hinzugefügtw erden soll
	 * @return Neu anlgelegtes Element im XML-Baum
	 */
	protected static Element addElement(String name, Element element) {
		Element newElement = new Element(name);
		if (element != null) {
			element.addContent(newElement);
		}
		return newElement;
	}

	/**
	 * 
	 * @param name
	 * @param element
	 * @param text
	 * @return
	 */
	protected static Element addElement(String name, Element element, String text) {
		Element newElement = new Element(name);
		newElement.setText(text);
		if (element != null) {
			element.addContent(newElement);
		}
		return newElement;
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param value
	 * @return
	 */
	protected static Element addAttribute(Element element, String name, String value) {
		element.setAttribute(name, value);
		return element;
	}
}