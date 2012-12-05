package edu.illinois.compression;

/**
 * @author Tianyi Wang
 */
public class TreeNode {
	private char character;
	private int count;
	private TreeNode leftNode;		// 0
	private TreeNode rightNode;		// 1
	private TreeNode parentNode;

	public TreeNode(char character, int count) {
		this.character = character;
		this.count = count;
		this.leftNode = null;
		this.rightNode = null;
		this.parentNode = null;
	}
	
	public TreeNode(int count, TreeNode leftNode, TreeNode rightNode) {
		this.count = count;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.parentNode = null;
	}

	public int getCount() {
		return count;
	}
	
	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}
}