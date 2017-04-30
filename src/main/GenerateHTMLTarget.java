package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GenerateHTMLTarget extends GenerateHTML {
	/**
	 * 
	 * @param nodeQuelle
	 *            Quelle für die die HTML-Datei ausgegeben wird.
	 * @param listeTyp
	 *            Alle Referenztypen, die in der Initfile definiert wurden.
	 * @param nodeLinkList
	 */
	static void printHTML(NodeSource nodeQuelle, List<NodeTyp> listeTyp, List<NodeTarget> nodeLinkList) {
		try {
			Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
			XMLOutputter out = new XMLOutputter(expanded);

			Element element = addElement("html");
			Element head = addElement("head", element);
			addElement("title", head, "Abbruch");
			Element body = addElement("body", element);
			addElement("h1", body, "Elemente");

			for (NodeTyp node : listeTyp) {
				Element link = addElement("p", body, node.getName());
				for (NodeTarget nodeLink : nodeLinkList) {
					if (nodeLink.getTyp().equals(node.getName())) {
						if (nodeQuelle.getName().equals(nodeLink.getName())
								& nodeQuelle.getNummer().equals(nodeLink.getNummer())) {
							addElement("p", link, String.format("%s %s %s", nodeLink.getFileName(), nodeLink.getTyp(),
									nodeLink.getName()));
							Element link1 = addElement("a",link,nodeLink.getName());
							// String fileName=FileName.getRelLinkName(node);
							addAttribute(link1, "href", nodeLink.getFileName());
							addAttribute(link1, "title", "Beschreibung der Klasse");
							addAttribute(link1, "target", "details");
							// addAttribute(link1, "onclick", String.format("top.doku.location='%s'; return true;",node.getFileName()));
						}
					}
				}

			}

			addElement("hr", body);
			String outputText = out.outputString(element);

			String _fileName = FileName.getLinkName(nodeQuelle);
			try (BufferedWriter _bw = new BufferedWriter(new FileWriter(_fileName))) {
				_bw.write("<!Doctype HTML>\n");
				_bw.write(outputText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
