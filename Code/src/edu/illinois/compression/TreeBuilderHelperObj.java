package edu.illinois.compression;

/**
 * A helper class that helps when building the Huffman Tree from its binary
 * form.
 * 
 * @author Tianyi Wang
 */
public class TreeBuilderHelperObj {

	private byte[] bytes;
	private int byteCount;
	private int bitPos;
	
	/**
	 * Get the current bit.
	 * 
	 * @return true for bit 1, false for bit 0.
	 */
	public boolean getBit() {
		byte curByte = bytes[byteCount];
		return (curByte & (1 << bitPos)) != 0 ? true : false;
	}
	
	/**
	 * Move on to the next bit.
	 */
	public void advanceOneBit() {
		bitPos--;
		if (bitPos < 0) {
			byteCount++;
			bitPos = 7;
		}
	}
	
	/**
	 * Get the current char.
	 * 
	 * @return the 8-bit character.
	 */
	public char getChar() {
		char theChar = 0;
		
		for (int i = 7; i >= 0; i--) {
			boolean theBit = getBit();
			advanceOneBit();
			
			if (theBit) {
				theChar |= (1 << i);
			}
		}
		
		return theChar;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param bytes the Huffman Tree in its binary form.
	 * @param byteCount starting position in bytes.
	 * @param bitPos starting position in bits.
	 */
	public TreeBuilderHelperObj(byte[] bytes, int byteCount, int bitPos) {
		this.bytes = bytes;
		this.byteCount = byteCount;
		this.bitPos = bitPos;
	}
	
	public int getByteCount() {
		return byteCount;
	}

	public int getBitPos() {
		return bitPos;
	}
}