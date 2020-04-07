package xyz.shy.DataStructure.TreeDemo;

public class ArrayBinaryTree {

	int[] data;

	public ArrayBinaryTree(int[] data) {
		this.data = data;
	}

	public void frontShow() {
		frontShow(0);
	}

	public void frontShow(int index) {
		if (data == null || data.length == 0)
			return;
		// 先遍历当前节点的内容
		System.out.println(data[index]);
		// 2*index+1 : 处理左子树
		if (2 * index + 1 < data.length)
			frontShow(2 * index + 1);
		// 2*index+2 : 处理右子树
		if (2 * index + 2 < data.length)
			frontShow(2 * index + 2);
	}

	public static void main(String[] args) {
		int[] data = new int[]{1, 2, 3, 4, 5, 6, 7};
		ArrayBinaryTree tree = new ArrayBinaryTree(data);
		tree.frontShow();
	}
}
