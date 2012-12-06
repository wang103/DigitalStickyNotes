package edu.illinois.compression;

/**
 * @author Tianyi Wang
 */
public class BitStream {
	private byte[] bytes;
	private byte garbageBitsAtEnd;
	
	public BitStream(String bits, byte garbageBitsAtEnd) {
		this.bytes = bits.getBytes();
		this.garbageBitsAtEnd = garbageBitsAtEnd;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param bits a {@link String} of '0's and '1's.
	 */
	public BitStream(String bits) {
		
		int numBytes = bits.length();
		numBytes = (numBytes + 7) & (~7);
		
		bytes = new byte[numBytes];
		
		byte curByte = 0;
		int bitPos = 7;
		int byteCount = 0;
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
			garbageBitsAtEnd = (byte) (bitPos + 1);
		} else {
			garbageBitsAtEnd = 0;
		}
	}
	
	/**
	 * First byte is the number of bits to skip at the end, following the first
	 * byte are the actual bits.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(garbageBitsAtEnd);
		sb.append(bytes);
		
		return sb.toString();
	}
}