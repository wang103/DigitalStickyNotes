package edu.illinois.encryption;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Signature;

/**
 * @author Tianyi Wang
 */
public class Encrypter {
	
	private KeyPair keyPair;	
	private Signature signature;
	
	public Encrypter() {
		try {
			signature.initSign(keyPair.getPrivate(), new SecureRandom());
			signature.initVerify(keyPair.getPublic());
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
	}
	
	public String encrypt(String msg) {
		return null;
	}
	
	public void decrypt() {		
		
	}
	
	public String signForAnother() {
		return null;
	}
	
	public boolean verifyForAnother() {
		return false;
	}
}