package edu.illinois.compression;

/**
 * @author Tianyi Wang
 */
public class BitStream {
	private byte[] bytes;

	/**
	 * Constructor.
	 * 
	 * @param bits a {@link String} of '0's and '1's.
	 */
	public BitStream(String bits) {
		
		int numBytes = bits.length();
		numBytes = ((numBytes + 7) & (~7)) >> 3;
		
		bytes = new byte[numBytes + 1];
		
		byte curByte = 0;
		int bitPos = 7;
		int byteCount = 1;
		for (int i = 0; i < bits.length(); i++) {
			char c = bits.charAt(i);
			
			if (c == '1') {
				curByte |= (1 << bitPos);
			}
			bitPos--;
			
			if (bitPos < 0) {
				bytes[byteCount] = curByte;
				curByte = 0;
				bitPos = 7;
				byteCount++;
			}
		}
		
		if (bitPos != 7) {
			bytes[byteCount] = curByte;
			bytes[0] = (byte) (bitPos + 1);
		} else {
			bytes[0] = 0;
		}
	}
	
	public byte[] getBytes() {
		return bytes;
	}
}