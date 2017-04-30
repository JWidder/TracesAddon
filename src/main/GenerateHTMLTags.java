package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Ausgabe der Liste der Tags, die im IniFile definiert wurden.#
 * 
 * Diese Linste wird oebe Links in dem Feld Taget ausgegeben
 * 
 * @author Johannes Widder
 */
public class GenerateHTMLTags extends GenerateHTML{
	static void printHTML(List<NodeTag> liste, List<NodeSource> listeSource,List<NodeTyp> listeTyp, List<NodeTarget> listeLink) {
		try {
			Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
			XMLOutputter out = new XMLOutputter(expanded);
			
			Element element = addElement("html");
			Element head = addElement("head",element);
			addElement("title",head,"Abbruch");
			Element body = addElement("body",element);
			addElement("h1",body,"Elemente");
			for (NodeTag nodeTag : liste)
			{
				Element link = addElement("p",body,String.format("%s", nodeTag.getName()));
				Element link1 = addElement ("a",link,nodeTag.getName());
				addAttribute(link1, "href", FileName.getRelSourceName(nodeTag));
				addAttribute(link1, "target", "source");
				
				GenerateHTMLSource.printHTML(listeSource, listeTyp,listeLink, nodeTag);
			}
			addElement("hr",body);
			
			String outputText = out.outputString(element);
			
			String fileName=FileName.getTagListeName();
			try (BufferedWriter _bw = new BufferedWriter(new FileWriter(fileName))) {
				_bw.write("<!Doctype HTML>\n");
				_bw.write(outputText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
