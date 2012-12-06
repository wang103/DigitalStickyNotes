package edu.illinois.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import edu.illinois.utils.Utils;

/**
 * Huffman Coding Compression.
 *
 * @author Tianyi Wang
 */
public class Compresser {
	
	public Compresser() {
	}
	
	/**
	 * Convert a Huffman Tree to a binary form. In the binary form, 0 indicates
	 * a leaf node, and it's followed by the actual 8-bit character.
	 * 1 indicates an internal node.
	 * 
	 * @param node root node of the Huffman Tree.
	 * @param sb a {@link StringBuilder} object for storing the binary form.
	 */
	private void convertHuffmanTreeToBinary(TreeNode node, StringBuilder sb) {
		if (node.isLeafNode()) {
			sb.append("0");
			sb.append(Utils.charToBinaryString(node.getCharacter()));
			return;
		} else {
			sb.append("1");
			convertHuffmanTreeToBinary(node.getLeftNode(), sb);
			convertHuffmanTreeToBinary(node.getRightNode(), sb);
		}
	}
	
	/**
	 * Convert a binary form of the Huffman Tree to a Huffman Tree.
	 * 
	 * @param helperObj {@link TreeBuilderHelperObj} object.
	 * @return the root node of the Huffman Tree.
	 */
	private TreeNode convertBinaryToHuffmanTree(TreeBuilderHelperObj helperObj) {
		boolean bit = helperObj.getBit();
		helperObj.advanceOneBit();
		
		if (!bit) {
			// bit is 0, which indicates a leaf node.
			char character = helperObj.getChar();
			TreeNode node = new TreeNode(character, 0);
			return node;
		}
		
		// bit is 1, which indicates an internal node.
		TreeNode leftNode = convertBinaryToHuffmanTree(helperObj);
		TreeNode rightNode = convertBinaryToHuffmanTree(helperObj);
		
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
	
	/**
	 * Compress a message with a Huffman Tree.
	 * 
	 * @param rootNode root node of the Huffman Tree.
	 * @param originalMsg the message to be compressed.
	 * @return the compressed message in bytes.
	 */
	private byte[] CompressWithHuffmanTree(TreeNode rootNode, String originalMsg) {
		// Build the encoding table from the tree.
		HashMap<Character, String> codingTable = new HashMap<Character, String>();
		buildCodeTable(codingTable, rootNode, "");
		
		// Use the encoding table to compress the message.
		StringBuilder sbForMsg = new StringBuilder();
		for (int i = 0; i < originalMsg.length(); i++) {
			char c = originalMsg.charAt(i);
			sbForMsg.append(codingTable.get(c));
		}
		String compressedMsg = sbForMsg.toString();
		
		// The message is compressed, now we need to have the binary form of the
		// Huffman Tree.
		StringBuilder treeBinaryStringBuilder = new StringBuilder();
		convertHuffmanTreeToBinary(rootNode, treeBinaryStringBuilder);
		String treeBinaryString = treeBinaryStringBuilder.toString();
		
		// Convert the entire "binary" string into real binary.
		String resultBinaryString = treeBinaryString + compressedMsg;
		BitStream bs = new BitStream(resultBinaryString);
		return bs.getBytes();
	}
	
	/**
	 * Compress a message using Huffman Compression algorithm.
	 * 
	 * @param originalMsg the message to be compressed.
	 * @return the compressed message in bytes.
	 */
	public byte[] Compress(String originalMsg) {
		// First count the occurrence of all the characters.
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
		
		// Create leaf TreeNode for each character and put them in the priority
		// queue one by one.
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
		
		// Special case: we need at least 1 edge for encoding, so if there's
		// only root node, add a fake root node.
		if (rootNode.isLeafNode()) {
			TreeNode dummyNode = new TreeNode(0, rootNode, rootNode);
			rootNode = dummyNode;
		}
		
		// Finally compress the message with the built Huffman Tree.
		return CompressWithHuffmanTree(rootNode, originalMsg);
	}
	
	/**
	 * Decompress a message.
	 * 
	 * @param bytes the compressed message in bytes.
	 * @return the decompressed message.
	 */
	public String Decompress(byte[] bytes) {
		// First char indicates how many bits are garbage the end.
		byte garbageBitsAtEnd = bytes[0];
		
		// Rebuild the Huffman Tree.
		TreeBuilderHelperObj helperObj = new TreeBuilderHelperObj(bytes, 1, 7);
		TreeNode rootNode = convertBinaryToHuffmanTree(helperObj);
		
		// Start rebuilding the message.
		int bitsLeft = (bytes.length - helperObj.getByteCount()) * 8 - 7 + helperObj.getBitPos() - garbageBitsAtEnd;
		
		StringBuilder sb = new StringBuilder();
		TreeNode node = rootNode;
		for (int i = 0; i < bitsLeft; i++) {
			if (node == null) {
				node = rootNode;
			}
			
			boolean bit = helperObj.getBit();
			helperObj.advanceOneBit();
			
			if (!bit) {
				// bit is 0, go to left node.
				node = node.getLeftNode();
			} else {
				// bit is 1, go to right node.
				node = node.getRightNode();
			}
			
			if (node.isLeafNode()) {
				// Reached a leaf node, get its character.
				sb.append(node.getCharacter());
				node = null;
			}
		}
		
		if (node == rootNode) {
			sb.append(rootNode.getCharacter());
		}
		
		return sb.toString();
	}
}