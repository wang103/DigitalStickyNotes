package edu.illinois.compression;

import java.util.Comparator;

/**
 * @author Tianyi Wang
 */
public class TreeNodeComparator implements Comparator<TreeNode> {

	@Override
	public int compare(TreeNode lhs, TreeNode rhs) {
		if (lhs.getCount() < rhs.getCount()) {
			return -1;
		} else if (lhs.getCount() > rhs.getCount()) {
			return 1;
		}
		return 0;
	}
}