package edu.illinois.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import edu.illinois.utils.Utils;

/**
 * @author Tianyi Wang
 * 
 * Huffman Coding for compression.
 */
public class Compresser {
	
	public Compresser() {
	}
	
	private void convertTreeToBinary(TreeNode node, StringBuilder sb) {
		if (node.isLeafNode()) {
			sb.append("0");
			sb.append(Utils.charToBinaryString(node.getCharacter()));
			return;
		} else {
			sb.append("1");
			convertTreeToBinary(node.getLeftNode(), sb);
			convertTreeToBinary(node.getRightNode(), sb);
		}
	}
	
	private TreeNode convertBinaryToTree(TreeBuilderHelperObj helperObj) {
		boolean bit = helperObj.getBit();
		helperObj.advanceOneBit();
		
		if (!bit) {
			char character = helperObj.getChar();
			TreeNode node = new TreeNode(character, 0);
			return node;
		}
		
		TreeNode leftNode = convertBinaryToTree(helperObj);
		TreeNode rightNode = convertBinaryToTree(helperObj);
		
		return new TreeNode(0, leftNode, rightNode);
	}
	
	/**
	 * Build the Huffman Coding table with the Huffman Tree.
	 * 
	 * @param table table to be built.
	 * @param node {@link TreeNode} object.
	 * @param s current path string.
	 */
	private void buildCodeTable(HashMap<Character, String> table, TreeNode node, String s) {
		if (node.isLeafNode() == false) {
			buildCodeTable(table, node.getLeftNode(), s + "0");
			buildCodeTable(table, node.getRightNode(), s + "1");
			return;
		}
		
		table.put(node.getCharacter(), s);
	}
	
	private byte[] CompressWithHuffmanTree(TreeNode rootNode, String originalMsg) {
		HashMap<Character, String> codingTable = new HashMap<Character, String>();
		buildCodeTable(codingTable, rootNode, "");
		
		// Use the coding table to compress the message.
		StringBuilder sbForMsg = new StringBuilder();
		for (int i = 0; i < originalMsg.length(); i++) {
			char c = originalMsg.charAt(i);
			sbForMsg.append(codingTable.get(c));
		}
		String compressedMsg = sbForMsg.toString();
		
		// The message is compressed, now we need to have the binary form of the
		// Huffman Tree.
		StringBuilder treeBinaryStringBuilder = new StringBuilder();
		convertTreeToBinary(rootNode, treeBinaryStringBuilder);
		String treeBinaryString = treeBinaryStringBuilder.toString();
		
		// Convert the "binary" string into real binary.
		String resultBinaryString = treeBinaryString + compressedMsg;
		BitStream bs = new BitStream(resultBinaryString);
		return bs.getBytes();
	}
	
	public byte[] Compress(String originalMsg) {
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
		while (pQueue.size() > 1) {
			TreeNode treeNode1 = pQueue.remove();
			TreeNode treeNode2 = pQueue.remove();
			
			TreeNode pNode = new TreeNode(treeNode1.getCount() + treeNode2.getCount(),
					treeNode1, treeNode2);
			treeNode1.setParentNode(pNode);
			treeNode2.setParentNode(pNode);
			
			pQueue.add(pNode);
		}
		
		TreeNode rootNode = pQueue.remove();
		
		return CompressWithHuffmanTree(rootNode, originalMsg);
	}
	
	public String Decompress(byte[] bytes) {		
		byte garbageBitsAtEnd = bytes[0];
		
		TreeBuilderHelperObj helperObj = new TreeBuilderHelperObj(bytes, 1, 7);
		TreeNode rootNode = convertBinaryToTree(helperObj);
		
		int bitsLeft = (bytes.length - helperObj.getByteCount()) * 8 - 7 + helperObj.getBitPos() - garbageBitsAtEnd;
		
		StringBuilder sb = new StringBuilder();
		TreeNode node = null;
		for (int i = 0; i < bitsLeft; i++) {
			if (node == null) {
				node = rootNode;
			}
			
			boolean bit = helperObj.getBit();
			helperObj.advanceOneBit();
			
			if (!bit) {
				// Go to left node.
				node = node.getLeftNode();
			} else {
				// Go to right node.
				node = node.getRightNode();
			}
			
			if (node.isLeafNode()) {
				sb.append(node.getCharacter());
				node = null;
			}
		}
		
		return sb.toString();
	}
}