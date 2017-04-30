package main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

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
		ReadData readData = new ReadData();
		
		StringReader  testInput = new StringReader("test [$Cust $test 2] Ende [$Cust $test 23] ");
		readData.readData(testInput,"FileName",aktivität.einlesen);
		
		assertEquals(2, readData.getNodeLink().size());
		assertEquals("Cust",readData.getNodeLink().get(0).getTyp());
		assertEquals("test",readData.getNodeLink().get(0).getName());
		assertEquals("2",readData.getNodeLink().get(0).getNummer());
		
		assertEquals(2, readData.getNodeLink().size());
		assertEquals("Cust",readData.getNodeLink().get(1).getTyp());
		assertEquals("test",readData.getNodeLink().get(1).getName());
		assertEquals("23",readData.getNodeLink().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataQuelle() throws Exception {
		ReadData readData = new ReadData();
		
		StringReader  testInput = new StringReader("test [$Cust 2] Ende [$Cust 23] ");
		readData.readData(testInput,"FileName",aktivität.einlesen);
		
		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust",readData.getNodeQuelle().get(0).getName());
		assertEquals("2",readData.getNodeQuelle().get(0).getNummer());
		
		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust",readData.getNodeQuelle().get(1).getName());
		assertEquals("23",readData.getNodeQuelle().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataTraces() throws Exception {
		ReadData readData = new ReadData();
		
		StringReader  testInput1 = new StringReader("test [$Cust 2] Ende [$Cust 23] ");
		StringReader  testInput2 = new StringReader("test [$Satisfies $Cust 2] Ende [$Satisfies $Cust 23] ");
				
		readData.readData(testInput1,"FileName",aktivität.einlesen);
		readData.readData(testInput2,"FileName",aktivität.einlesen);
		
		assertEquals(2, readData.getNodeQuelle().size());
		assertEquals("Cust",readData.getNodeQuelle().get(0).getName());
		assertEquals("2",readData.getNodeQuelle().get(0).getNummer());
		
		assertEquals("Cust",readData.getNodeQuelle().get(1).getName());
		assertEquals("23",readData.getNodeQuelle().get(1).getNummer());
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReadDataTracesLeer() throws Exception {
		ReadData readData = new ReadData();
		StringReader  testInput = new StringReader("test Ende ");
		readData.readData(testInput,"FileName",aktivität.einlesen);
		assertEquals(0, readData.getNodeQuelle().size());
	}

	/**
	 * @throws IOException
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReplaceDataLeer() throws IOException
	{
		ReadData readData = new ReadData();

		String test = new String("test Ende ");
		
		StringReader  testInput = new StringReader("test Ende ");

		String result = readData.readData(testInput,"FileName", aktivität.schreiben);
		
		assertEquals(test,result);
	}

	/**
	 * @throws IOException
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testReplaceDataLink() throws IOException
	{
		ReadData readData = new ReadData();

		StringReader  testInput1 = new StringReader("test [$FMEA_A 2] Ende [$FMEA_A 23] ");
		StringReader  testInput2 = new StringReader("test [$Satisfies $FMEA_A 2] Ende [$Satisfies $FMEA_A 23] ");
				
		readData.readData(testInput1,"FileName",aktivität.einlesen);
		readData.readData(testInput2,"FileName",aktivität.einlesen);
		
		StringReader  testInput3 = new StringReader("test [$Satisfies $FMEA_A 2] Ende");
		String result=readData.readData(testInput3,"FileName",aktivität.schreiben);
		String test = new String ("test <a href=\"file:///FileName\"> $FMEA_A 2 </a> Ende");
		
		// assertEquals(test,result);
	}

}
