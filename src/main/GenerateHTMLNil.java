package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GenerateHTMLNil extends GenerateHTML{
	/**
	 * Ausgabe einer leeren Seite, die von den Frames 
	 * @param inName
	 * @throws IOException
	 */
	static void printHTML(String inName) throws IOException
	{
		try {
			Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
			XMLOutputter out = new XMLOutputter(expanded);
			
			Element element = addElement("html");
			Element head = addElement("head",element);
			addElement("title",head,"Abbruch");
			addElement("Leer",element);
			
			String outputText = out.outputString(element);

			try (BufferedWriter _bw = new BufferedWriter(new FileWriter(inName))) {
				_bw.write("<!Doctype HTML>\n");
				_bw.write(outputText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
