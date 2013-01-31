package edu.illinois.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Tianyi Wang
 */
public class Utils {
	
	public static String inputStreamToString(InputStream is) {
		String line;
		StringBuilder total = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try {
			while ((line = br.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return total.toString();
	}
	
	/**
	 * Convert a character to its binary form represented using {@link String}.
	 * 
	 * @param ch the character.
	 * @return {@link String} of character 0's and 1's.
	 */
	public static String charToBinaryString(char ch) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 8; i++) {
			if ((ch & 1) == 1) {
				sb.insert(0, '1');
			} else {
				sb.insert(0, '0');
			}
			ch >>= 1;
		}
		
		return sb.toString();
	}
	
	/**
	 * Convert a short int to its binary form represented using {@link String}.
	 * 
	 * @param sh the short int.
	 * @return {@link String} of short int in 0's and 1's.
	 */
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