package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateHTMLFrames extends GenerateHTML {
	/**
	 * Ausgabed er Frames. Ersetzte typisch index.html als Startseite.
	 * @return
	 * @throws IOException 
	 */
	static void printHTML(String inName) throws IOException
	{
		StringBuilder ausgabe = new StringBuilder();
		ausgabe.append("<!DOCTYPE html>\n");
		ausgabe.append("<!-- saved from url=(0016)http://localhost --> <!-- MOTW -->\n");
		ausgabe.append("<html>\n");
		ausgabe.append("	<head>\n");
		ausgabe.append("    	<title>Full view </title>\n");
		ausgabe.append("    	<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\" />\n");
		ausgabe.append("	</head>\n");
		ausgabe.append("  	<frameset id=\"mainframe\" cols=\"20%, 80%\">\n");
		ausgabe.append("    	<frameset id=\"left\" rows=\"20%, 50%, 30%\">\n");
		ausgabe.append("     		<frame name=\"tags\" title=\"Types\" src=\"$$$Tags.html\"></frame>\n");
		ausgabe.append("      		<frame name=\"source\" src=\"$$$nil.html\"></frame>\n");
		ausgabe.append("			<frame name=\"target\" src=\"$$$nil.html\"></frame>\n");
		ausgabe.append("		</frameset>\n");
		ausgabe.append("		<frameset id=\"right\" rows=\"50%, 50%\">\n");
		ausgabe.append("			<frame name=\"doku\" title=\"detail\" src=\"indexDoxygen.html\"></frame>\n");
		ausgabe.append("			<frame name=\"details\" src=\"$$$nil.html\"></frame>\n");
		ausgabe.append("		</frameset>\n");
		ausgabe.append("	</frameset>\n");
		ausgabe.append("</html>\n");
		
		try (BufferedWriter _bw = new BufferedWriter(new FileWriter(inName))) {
			_bw.write("<!Doctype HTML>\n");
			_bw.write(ausgabe.toString());
		}
	}
}
