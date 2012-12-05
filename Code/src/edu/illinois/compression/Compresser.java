package edu.illinois.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author Tianyi Wang
 * 
 * Huffman Coding for compression.
 */
public class Compresser {
	
	public Compresser() {
	}
	
	public String Compress(String originalMsg) {
		HashMap<Character, Integer> characterToCount = new HashMap<Character, Integer>();
		for (int i = 0; i < originalMsg.length(); i++) {
			char curChar = originalMsg.charAt(i);
			if (characterToCount.containsKey(curChar)) {
				Integer count = characterToCount.get(curChar);
				characterToCount.put(curChar, count + 1);
			} else {
				characterToCount.put(curChar, 1);
			}
		}
		
		Comparator<TreeNode> comparator = new TreeNodeComparator();
		PriorityQueue<TreeNode> pQueue = new PriorityQueue<TreeNode>(characterToCount.size(), comparator);
		
		// Create TreeNode and put them in the priority queue one by one.
		for (Map.Entry<Character, Integer> entry : characterToCount.entrySet()) {
			char key = entry.getKey();
			int value = entry.getValue();
			
			TreeNode treeNode = new TreeNode(key, value);
			pQueue.add(treeNode);
		}
		
		// Now build the Huffman Tree using the priority queue.
		
		
		return null;
	}
	
	public String Decompress(String encryptedMsg) {
		return null;
	}
}