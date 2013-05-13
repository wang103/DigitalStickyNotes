package edu.illinois.compression;

/**
 * A class representing a tree node in the Huffman Tree.
 * 
 * @author Tianyi Wang
 */
public class TreeNode {
	
	private char character;			// only used for leaf node.
	private int count;				// count of the node.
	private TreeNode leftNode;		// path of 0.
	private TreeNode rightNode;		// path of 1.
	private TreeNode parentNode;
	
	/**
	 * Determine if this node is a leaf node.
	 * 
	 * @return true for leaf node, false for internal node.
	 */
	public boolean isLeafNode() {
		return leftNode == null && rightNode == null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param count the count of the character.
	 * @param leftNode left node of this node.
	 * @param rightNode right node of this node.
	 */
	public TreeNode(int count, TreeNode leftNode, TreeNode rightNode) {
		this.count = count;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.parentNode = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param character the character of this node in the tree.
	 * @param count the count of the character.
	 */
	public TreeNode(char character, int count) {
		this.character = character;
		this.count = count;
		this.leftNode = null;
		this.rightNode = null;
		this.parentNode = null;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public int getCount() {
		return count;
	}
	
	public TreeNode getLeftNode() {
		return leftNode;
	}
	
	public TreeNode getRightNode() {
		return rightNode;
	}
	
	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}
}