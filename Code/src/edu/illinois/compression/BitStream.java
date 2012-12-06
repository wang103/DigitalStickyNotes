package edu.illinois.compression;

/**
 * A helper class helps to convert a {@link String} of character 0's and 1's
 * into the binary form: an array of byte.
 * 
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
		
		int numBits = bits.length();
		int numBytes = ((numBits + 7) & (~7)) >> 3;
		
		// Leave room for an extra byte at the beginning, it's for storing how
		// many garbage bits are at the end.
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