package xyz.shy.DataStructure.TreeDemo;

public class BinaryTree {

	TreeNode root;

	// 设置根节点
	public void setRoot(TreeNode root) {
		this.root = root;
	}

	// 获取根节点
	public TreeNode getRoot() {
		return root;
	}

	void afterShow() {
		if (root != null)
			root.afterShow();
	}

	void midShow() {
		if (root != null)
			root.midShow();
	}

	void frontShow() {
		if (root != null)
			root.frontShow();
	}

	TreeNode frontSearch(int i) {
		return root.frontSearch(i);
	}

	void delete(int i) {
		if (root.getValue() == i) {
			root = null;
		} else {
			root.delete(i);
		}
	}

	public static void main(String[] args) {
		// 创建一棵树
		BinaryTree binTree = new BinaryTree();
		// 创建一个节点
		TreeNode root = new TreeNode(1);
		// 把根节点赋给树
		binTree.setRoot(root);
		// 创建第二层子树 两个节点
		TreeNode L1 = new TreeNode(2);
		TreeNode R1 = new TreeNode(3);
		// 把新创建的节点设置为根节点的子节点
		root.setLeftNode(L1);
		root.setRightNode(R1);
		// 创建第三层子树 四个节点
		L1.setLeftNode(new TreeNode(4));
		L1.setRightNode(new TreeNode(5));
		R1.setLeftNode(new TreeNode(6));
		R1.setRightNode(new TreeNode(7));

		// 1. 前序遍历
		binTree.frontShow();
		System.out.println("--------------------");
		// 2. 中序遍历
		binTree.midShow();
		System.out.println("--------------------");
		// 3. 后序遍历
		binTree.afterShow();
		System.out.println("--------------------");
		// 4. 前序查找
		TreeNode res = binTree.frontSearch(5);
		System.out.println(res.getValue());
		// 5. 删除一个子树
		System.out.println("--------------------");
		binTree.delete(2);
		binTree.frontShow();

	}

}
