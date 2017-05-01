package main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.ini4j.Wini;
import org.junit.Test;

import main.ReadData.aktivität;

/**
 * @author Johannes Widder
 *
 */
public class ReadDataTest {

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataLink() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput = new StringReader("test [$Test $Cust 2] Ende [$Test $Cust 23] ");
		readData.readData(testInput, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);

		assertEquals(2, readData.getNodeLink().size());
		assertEquals("Test", readData.getNodeLink().get(0).getTyp());
		assertEquals("Cust", readData.getNodeLink().get(0).getName());
		assertEquals("2", readData.getNodeLink().get(0).getNummer());

		assertEquals(2, readData.getNodeLink().size());
		assertEquals("Test", readData.getNodeLink().get(1).getTyp());
		assertEquals("Cust", readData.getNodeLink().get(1).getName());
		assertEquals("23", readData.getNodeLink().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataQuelle() throws Exception {
		List<NodeTyp> listeNodeTyp = new ArrayList<>();
		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput = new StringReader("test [$Cust 2] Ende [$Cust 23] ");

		readData.readData(testInput, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);

		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust", readData.getNodeQuelle().get(0).getName());
		assertEquals("2", readData.getNodeQuelle().get(0).getNummer());

		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust", readData.getNodeQuelle().get(1).getName());
		assertEquals("23", readData.getNodeQuelle().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataTraces() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput1 = new StringReader("test [$Cust 2] Ende [$Cust 23] ");
		StringReader testInput2 = new StringReader("test [$Satisfies $Cust 2] Ende [$Satisfies $Cust 23] ");

		readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		readData.readData(testInput2, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);

		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust", readData.getNodeQuelle().get(0).getName());
		assertEquals("2", readData.getNodeQuelle().get(0).getNummer());

		assertEquals("Cust", readData.getNodeQuelle().get(1).getName());
		assertEquals("23", readData.getNodeQuelle().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataTracesLeer() throws Exception {
		List<NodeTyp> listeNodeTyp = new ArrayList<>();
		List<NodeTag> listeNodeTag = new ArrayList<>();

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);
		StringReader testInput = new StringReader("test Ende ");
		readData.readData(testInput, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		assertEquals(0, readData.getNodeQuelle().size());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReplaceDataLeer() throws Exception {
		List<NodeTyp> listeNodeTyp = new ArrayList<>();
		List<NodeTag> listeNodeTag = new ArrayList<>();

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String test = new String("test Ende ");

		StringReader testInput = new StringReader("test Ende ");

		String result = readData.readData(testInput, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);

		assertEquals(test, result);
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReplaceDataLink() throws Exception {
		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput1 = new StringReader("test [$Cust 2] Ende [$Cust 23] ");
		StringReader testInput2 = new StringReader("test [$Satisfies $Cust 2] Ende [$Satisfies $Det 23] ");

		readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		readData.readData(testInput2, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);

		StringReader testInput3 = new StringReader("test [$Satisfies $FMEA_A 2] Ende");
		readData.readData(testInput3, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		new String("test <a href=\"file:///FileName\"> $FMEA_A 2 </a> Ende");

		// assertEquals(test,result);
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataSourceSyntax() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput1 = new StringReader("test [$Cust 2] Ende [$Goal 23] ");

		try {
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		assertEquals(1, readData.getNodeQuelle().size());
	}

	/**
	 * Dies prüft
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataSyntaxTyp() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Custt"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput1 = new StringReader("test [$Cust 2] Ende [$Cust 23] ");
		StringReader testInput2 = new StringReader("test [$Testt $Cust 2] Ende [$Satisfies $Cust 23] ");

		try {
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try{
			readData.readData(testInput2, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Should have thrown an exception");			
		}
		catch(Exception e){
			assertTrue(true);
		}

		assertEquals(0, readData.getNodeQuelle().size());
	}

	
	/**
	 * Dies prüft
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataDoubleSource() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		StringReader testInput1 = new StringReader("test [$Cust 2] Ende [$Cust 2] ");

		try {
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		assertEquals(1, readData.getNodeQuelle().size());
	}

}
