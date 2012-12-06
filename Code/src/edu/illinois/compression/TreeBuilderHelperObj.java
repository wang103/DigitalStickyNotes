package edu.illinois.compression;

/**
 * @author Tianyi Wang
 */
public class TreeBuilderHelperObj {

	private byte[] bytes;
	private int byteCount;
	private int bitPos;

	public TreeBuilderHelperObj(byte[] bytes, int byteCount, int bitPos) {
		this.bytes = bytes;
		this.byteCount = byteCount;
		this.bitPos = bitPos;
	}
	
	public boolean getBit() {
		byte curByte = bytes[byteCount];
		return (curByte & (1 << bitPos)) != 0 ? true : false;
	}
	
	public void advanceOneBit() {
		bitPos--;
		if (bitPos < 0) {
			byteCount++;
			bitPos = 7;
		}
	}
	
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
	
	public int getByteCount() {
		return byteCount;
	}

	public int getBitPos() {
		return bitPos;
	}
}