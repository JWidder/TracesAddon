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
			
			Set<String> setStringsLinks=ini.get("Typ").keySet();
			listeTyp = new ArrayList<>(setStringsLinks.size());			
			for (String name : setStringsLinks) {
				NodeTyp tag = new NodeTyp();
				tag.setName(ini.get("Typ",name));
				listeTyp.add(tag);
			}
			
			
			Set<String> setStringsTags=ini.get("Tags").keySet();
			listeTags = new ArrayList<>(setStringsTags.size());			
			for (String name : setStringsTags) {
				NodeTag tag = new NodeTag();
				tag.setName(ini.get("Tags",name));
				listeTags.add(tag);
			}
			new ReadData(args[0],listeTags,listeTyp);
			
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}