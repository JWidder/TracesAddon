package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Aufgabe diser Klasse ist es eine neue HTML-Datei anzulegen
 * 
 * @author Johannes Widder
 *
 */
public abstract class GenerateHTML {

	/**
	 * 
	 * @param inName
	 * @throws IOException
	 */
	static void printHTMLFrame(String inName) throws IOException {
		Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
		XMLOutputter out = new XMLOutputter(expanded);

		Element element = addHeader("Sources")
				.addContent(new Element("frameset").setAttribute("id", "mainframe").setAttribute("cols", "20%, 80%")
						.addContent(new Element("frameset").setAttribute("id", "mainframe")
								.setAttribute("rows", "20%, 50%, 30%")
								.addContent(new Element("frame").setAttribute("name", "tags").setAttribute("src",
										FileName.getRelTagListeName()))
								.addContent(new Element("frame").setAttribute("name", "source")
										.setAttribute("src", FileName.getRelNilName()))
								.addContent(new Element("frame").setAttribute("name", "target").setAttribute("src",
										FileName.getRelNilName())))
						.addContent(
								new Element("frameset").setAttribute("id", "mainframe").setAttribute("rows", "50%, 50%")
										.addContent(new Element("frame").setAttribute("name", "doku")
												.setAttribute("src", FileName.getRelDxygeDokuName()))
										.addContent(new Element("frame").setAttribute("name", "details")
												.setAttribute("src", FileName.getRelNilName()))));

		outFile(inName, out.outputString(element));
	}

	/**
	 * 
	 * 
	 * @throws IOException
	 */
	static void printHTMLNil() throws IOException {
		Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
		XMLOutputter out = new XMLOutputter(expanded);

		Element element = new Element("html").addContent(new Element("head"))
				.addContent(new Element("body").addContent(new Element("p").setText("Leer")));

		outFile(FileName.getNilName(), out.outputString(element));
	}

	/**
	 * 
	 * @param listeSource
	 * @param listeTyp
	 * @param nodeLinkListe
	 * @param inNodeTag
	 * @throws IOException
	 */
	static void printHTMLSource(List<NodeSource> listeSource, List<NodeTyp> listeTyp, List<NodeTarget> nodeLinkListe,
			NodeTag inNodeTag) throws IOException {
		Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
		XMLOutputter out = new XMLOutputter(expanded);

		Element element = new Element("html").addContent(new Element("title").setText("Sources"));
		Element body = new Element("body");
		element.addContent(body);

		int count = 0;
		for (NodeSource node : listeSource) {
			if (node.getName().equals(inNodeTag.getName())) {
				count++;
			}
		}
		if (count == 0) {
			body.addContent(new Element("p").setText(String.format("No reference for: %s", inNodeTag.getName())));
		} else {
			for (NodeSource node : listeSource) {
				if (node.getName().equals(inNodeTag.getName())) {
					body.addContent(new Element("p").addContent(new Element("a")
							.setText(String.format("%s %s", node.getName(), node.getNummer()))
							.setAttribute("href", FileName.getRelLinkName(node)).setAttribute("target", "target")
							.setAttribute("title", "Beschreibung der Klasse").setAttribute("onclick",
									String.format("top.doku.location='%s'; return true;", node.getFileName()))));

					GenerateHTML.printHTMLLink(node, listeTyp, nodeLinkListe);
				}
			}
		}

		outFile(FileName.getSourceName(inNodeTag), out.outputString(element));
	}

	/**
	 * 
	 * @param liste
	 * @param listeSource
	 * @param listeTyp
	 * @param listeLink
	 * @throws IOException
	 */
	static void printHTMLTags(List<NodeTag> liste, List<NodeSource> listeSource, List<NodeTyp> listeTyp,
			List<NodeTarget> listeLink) throws IOException {
		Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
		XMLOutputter out = new XMLOutputter(expanded);

		Element element = addHeader("Sources");
		Element body = new Element("body");
		element.addContent(body);

		for (NodeTag nodeTag : liste) {
			Element link = new Element("p").setText(String.format("%s", nodeTag.getName()));
			body.addContent(link);

			link.addContent(new Element("a").setText(nodeTag.getName())
					.setAttribute("href", FileName.getRelSourceName(nodeTag)).setAttribute("target", "source"));

			GenerateHTML.printHTMLSource(listeSource, listeTyp, listeLink, nodeTag);
		}

		outFile(FileName.getTagListeName(), out.outputString(element));
	}

	/**
	 * 
	 * @param nodeQuelle
	 * @param listeTyp
	 * @param nodeLinkList
	 * @throws IOException
	 */
	static void printHTMLLink(NodeSource nodeQuelle, List<NodeTyp> listeTyp, List<NodeTarget> nodeLinkList)
			throws IOException {
		Format expanded = Format.getPrettyFormat().setExpandEmptyElements(true);
		XMLOutputter out = new XMLOutputter(expanded);

		Element element = addHeader("Sources");
		Element body = new Element("body").addContent(
				new Element("h3").setText(String.format("Links: %s %s", nodeQuelle.getName(), nodeQuelle.getNummer())));
		element.addContent(body);

		// Count number of referencwes
		int count = 0;
		for (NodeTyp node : listeTyp) {
			for (NodeTarget nodeLink : nodeLinkList) {
				if (nodeLink.getTyp().equals(node.getName())) {
					if (nodeQuelle.getName().equals(nodeLink.getName())
							& nodeQuelle.getNummer().equals(nodeLink.getNummer())) {
						count++;
					}
				}
			}
		}
		if (count == 0) {
			body.addContent(new Element("p").setText("No Links"));
		} else {
			for (NodeTyp node : listeTyp) {
				int countLink = 0;
				for (NodeTarget nodeLink : nodeLinkList) {
					if (nodeLink.getTyp().equals(node.getName())) {
						if (nodeQuelle.getName().equals(nodeLink.getName())
								& nodeQuelle.getNummer().equals(nodeLink.getNummer())) {
							countLink++;
						}
					}
				}
				if (countLink == 0) {
					body.addContent(new Element("p").setText(String.format("%s: no Element", node.getName())));
				} else {
					for (NodeTarget nodeLink : nodeLinkList) {
						if (nodeLink.getTyp().equals(node.getName())) {
							if (nodeQuelle.getName().equals(nodeLink.getName())
									& nodeQuelle.getNummer().equals(nodeLink.getNummer())) {
								body.addContent(new Element("p").setText(node.getName())
										.addContent(new Element("a").setText(nodeLink.getName())
												.setAttribute("href", nodeLink.getFileName())
												.setAttribute("title", "Beschreibung der Klasse")
												.setAttribute("target", "details")));

							}
						}
					}
				}
			}
		}

		outFile(FileName.getLinkName(nodeQuelle), out.outputString(element));
	}

	/**************************************************************************/

	/**
	 * 
	 * @param inName
	 * @param output
	 * @throws IOException
	 */
	private static void outFile(String inName, String output) throws IOException {
		try (BufferedWriter _bw = new BufferedWriter(new FileWriter(inName))) {
			_bw.write("<!Doctype HTML>\n");
			_bw.write(output);
		}
	}

	private static Element addHeader(String titel) {
		Element element = new Element("html");
		element.addContent(new Element("title").setText(titel));
		return element;
	}
}