// Copyright 2017 Johannes Widder
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package main;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
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

		String input = "test [$Test $Cust 2] Ende [$Test $Cust 23] ";
		StringReader testInput = new StringReader(input);
		readData.readData(testInput, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		testInput.close();

		testInput = new StringReader(input);
		String result = readData.readData(testInput, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		assertEquals(input, result);
		testInput.close();

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

		String input = "test [$Cust 2] Ende [$Cust 23] ";
		StringReader testInput = new StringReader(input);
		readData.readData(testInput, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		testInput.close();

		testInput = new StringReader(input);
		String result = readData.readData(testInput, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		testInput.close();

		assertEquals(input, result);

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

		try {
			readData.readData(testInput2, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}

		assertEquals(0, readData.getNodeQuelle().size());
	}

	/**
	 * Test whether multible tags trigger an exception.
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

	/**
	 * Test whether multible tags trigger an exception.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String input = "test [$Cust] Ende [$Cus] ";
		StringReader testInput1 = new StringReader(input);
		readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		testInput1.close();
		testInput1 = new StringReader(input);
		String result = readData.readData(testInput1, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		testInput1.close();
		assertEquals(input, result);

		assertEquals(0, readData.getNodeQuelle().size());
	}

	/**
	 * Test whether multible tags trigger an exception.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive2() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String input = "t [$Cust 1 ]";
		StringReader testInput1 = new StringReader(input);
		readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		testInput1.close();
		testInput1 = new StringReader(input);
		String result = readData.readData(testInput1, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		testInput1.close();
		assertEquals(input, result);

		assertEquals(1, readData.getNodeQuelle().size());
	}

	/**
	 * Test whether multible tags trigger an exception.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive3() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String input = "t [$Cust 1 a]";
		try {
			StringReader testInput1 = new StringReader(input);
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Exception expected");

		} catch (ExceptionParser e) {
			assertTrue(true);
		}
	}

	/**
	 * Test whether multible tags trigger an exception.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive4() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String input = "t [$Test $Cust 1 a]";
		try {
			StringReader testInput1 = new StringReader(input);
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Exception expected");

		} catch (ExceptionParser e) {
			assertTrue(true);
		}
	}

	/**
	 * Test whether multible tags trigger an exception.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive5() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		try {
			String input = "t [$Test a $Cust 1 a]";
			StringReader testInput1 = new StringReader(input);
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Exception expected");

		} catch (ExceptionParser e) {
			assertTrue(true);
		}
	}

	/**
	 * Test
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive6() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		try {
			String input = "t [$Test $Cust a 1]";
			StringReader testInput1 = new StringReader(input);
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Exception expected");

		} catch (ExceptionParser e) {
			assertTrue(true);
		}
	}

	/**
	 * Test
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive7() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		try {
			String input = "t [$T£est $Cust 1]";
			StringReader testInput1 = new StringReader(input);
			readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
			fail("Exception expected");

		} catch (ExceptionParser e) {
			assertTrue(true);
		}
	}

	/**
	 * Test
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataFalsePositive8() throws Exception {

		List<NodeTyp> listeNodeTyp = new ArrayList<>(2);
		listeNodeTyp.add(new NodeTyp("Satisfies"));
		listeNodeTyp.add(new NodeTyp("Test"));

		List<NodeTag> listeNodeTag = new ArrayList<>();
		listeNodeTag.add(new NodeTag("Cust"));
		listeNodeTag.add(new NodeTag("Det"));

		ReadData readData = new ReadData(listeNodeTag, listeNodeTyp);

		String input = "t [$1 Test $Cust 1]";
		StringReader testInput1 = new StringReader(input);
		readData.readData(testInput1, "FileName", aktivität.einlesen, listeNodeTag, listeNodeTyp);
		testInput1 = new StringReader(input);
		String result = readData.readData(testInput1, "FileName", aktivität.schreiben, listeNodeTag, listeNodeTyp);
		assertEquals(input, result);
	}
}
