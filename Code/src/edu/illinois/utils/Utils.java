package edu.illinois.utils;

/**
 * @author Tianyi Wang
 */
public class Utils {
	
	public static String charToBinaryString(char ch) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 8; i++) {
			if ((ch & 0x80) == 1) {
				sb.append("1");
			} else {
				sb.append("0");
			}
			ch <<= 1;
		}
		
		return sb.toString();
	}
	
	public static String shortToBinaryString(short sh) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 16; i++) {
			if ((sh & 1) == 1) {
				sb.insert(0, '1');
			} else {
				sb.insert(0, '0');
			}
			sh >>= 1;
		}
		
		return sb.toString();
	}
}