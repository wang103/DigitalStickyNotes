package edu.illinois.compression;

import junit.framework.TestCase;

/**
 * The class for testing compression algorithm.
 * 
 * @author tianyiw
 */
public class CompresserTest extends TestCase {

	private Compresser compresser;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.compresser = new Compresser();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCompresser0() {
		String str = "She handed him a handsome chocolate egg decorated with " +
				"small, iced Snitches and, according to the packaging, " +
				"containing a bag of Fizzing Whizzbees. Harry looked at it for " +
				"a moment, then, to his horror, felt a lump rise in his throat.";
		byte[] compressedStr = compresser.Compress(str);
		
		assertNotNull(compressedStr);
		assertTrue(str.equals(compressedStr) == false);
		assertTrue(compressedStr.length <= str.length());
		
		String oriStr = compresser.Decompress(compressedStr);
		
		assertEquals(str, oriStr);
	}

	public void testCompresser1() {
		String str = "A";
		byte[] compressedStr = compresser.Compress(str);
		
		assertNotNull(compressedStr);
		assertTrue(str.equals(compressedStr) == false);

		String oriStr = compresser.Decompress(compressedStr);
		
		assertEquals(str, oriStr);
	}

	public void testCompresser2() {
		String str = "A1";
		byte[] compressedStr = compresser.Compress(str);
		
		assertNotNull(compressedStr);
		assertTrue(str.equals(compressedStr) == false);

		String oriStr = compresser.Decompress(compressedStr);
		
		assertEquals(str, oriStr);
	}

	public void testCompresser3() {
		String str = "CCC";
		byte[] compressedStr = compresser.Compress(str);
		
		assertNotNull(compressedStr);
		assertTrue(str.equals(compressedStr) == false);

		String oriStr = compresser.Decompress(compressedStr);
		
		assertEquals(str, oriStr);
	}
}