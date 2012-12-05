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
		String str = "Hello, world!";
		String encryptedStr = compresser.Compress(str);
		
		assertNotNull(encryptedStr);
		assertTrue(str.equals(encryptedStr) == false);
		
		String oriStr = compresser.Decompress(encryptedStr);
		
		assertNotNull(oriStr);
		assertEquals(str, oriStr);
	}
}