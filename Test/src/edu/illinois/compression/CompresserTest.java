package edu.illinois.compression;

import junit.framework.TestCase;

public class CompresserTest extends TestCase {

	private Compresser compresser;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.compresser = new Compresser();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCompresser() {
		String str = "She handed him a handsome chocolate egg decorated with " +
				"small, iced Snitches and, according to the packaging, " +
				"containing a bag of Fizzing Whizzbees. Harry looked at it for " +
				"a moment, then, to his horror, felt a lump rise in his throat.";
		byte[] compressedStr = compresser.Compress(str);
		
		assertNotNull(compressedStr);
		assertTrue(str.equals(compressedStr) == false);
		assertTrue(compressedStr.length <= str.length());
		
		String oriStr = compresser.Decompress(compressedStr);
		
		assertNotNull(oriStr);
		assertEquals(str, oriStr);
	}
}