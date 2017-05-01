package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class TracesAddon {
	public static void main(String[] args) {

		System.out.println("Inifile einlesen");
		// Inifile einlesen
		List<NodeTag> listeTags;
		List<NodeTyp> listeTyp;
		try {
			Wini ini = new Wini(new File("traces.ini"));

			Set<String> setStringsLinks = ini.get("Typ").keySet();
			listeTyp = new ArrayList<>(setStringsLinks.size());
			for (String name : setStringsLinks) {
				listeTyp.add(new NodeTyp(ini.get("Typ", name)));
			}

			Set<String> setStringsTags = ini.get("Tags").keySet();
			listeTags = new ArrayList<>(setStringsTags.size());
			for (String name : setStringsTags) {
				listeTags.add(new NodeTag(ini.get("Tags", name)));
			}
			new ReadData(args[0], listeTags, listeTyp);

		} catch (ExceptionParser e) {
			System.out.println();
			System.out.println("=================================================");
			System.out.println("The program detected an input error while parsing");
			System.out.println();
			System.out.print(e.getMessage());
			System.out.println();
			System.out.println();
			System.out.print("Program aborts");
			System.out.println();
			System.out.println();
		}
		catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}