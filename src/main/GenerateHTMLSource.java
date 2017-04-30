package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Ausgabe der Liste alle Quellen
 * 
 * @author Johannes Widder
 */
public class GenerateHTMLSource extends GenerateHTML {
	/**
	 * Ausgabe der HTML-Dateien für die in der Quelle defiierten Links.
	 * 
	 *  Diese HTML-Datei wird in der Mitte auf der linken Seite ausgegeben. 
	 * 
	 * @param listeQelle
	 * @param tagName
	 * @param Name
	 */
	static void printHTML(List<NodeSource>listeSource, List<NodeTyp>listeTyp, List<NodeTarget>nodeLinkListe, NodeTag inNodeTag) {
		try {
			Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
			XMLOutputter out = new XMLOutputter(expanded);

			Element element = addElement("html");
			Element head = addElement("head", element);
			addElement("title", head, "Abbruch");
			Element body = addElement("body", element);
			addElement("h1", body, "Elemente");
			
			// Schleife über allen Typen von Tags, die in der ini-Datei definiert sind. 
			for (NodeSource node : listeSource) {
				if (node.getName().equals(inNodeTag.getName())) {
					Element link = addElement("p", body, inNodeTag.getName());
					Element link1 = addElement("a",link,inNodeTag.getName());
					String fileName=FileName.getRelLinkName(node);
					addAttribute(link1, "href", fileName);
					addAttribute(link1, "title", "Beschreibung der Klasse");
					addAttribute(link1, "target", "target");
					addAttribute(link1, "onclick", String.format("top.doku.location='%s'; return true;",node.getFileName()));
					GenerateHTMLTarget.printHTML(node,listeTyp,nodeLinkListe);
				}
			}
			addElement("hr", body);

			try (BufferedWriter _bw = new BufferedWriter(new FileWriter(FileName.getSourceName(inNodeTag)))) {
				_bw.write("<!Doctype HTML>\n");
				_bw.write(out.outputString(element));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}