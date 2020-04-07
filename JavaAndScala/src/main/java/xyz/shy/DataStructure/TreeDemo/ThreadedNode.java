package xyz.shy.DataStructure.TreeDemo;

/**
 * 线索二叉树
 */
public class ThreadedNode {
	// 节点的权
	private int value;
	// 左节点
	ThreadedNode leftNode;
	// 右节点
	ThreadedNode rightNode;

	// 标识指针类型
	int leftType;
	int rightType;

	public ThreadedNode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setLeftNode(ThreadedNode leftNode) {
		this.leftNode = leftNode;
	}

	public void setRightNode(ThreadedNode rightNode) {
		this.rightNode = rightNode;
	}

	// 前序遍历
	public void frontShow() {
		// 先遍历当前节点的内容
		System.out.println(value);
		// 在遍历左节点
		if (leftNode != null) {
			leftNode.frontShow();
		}
		// 在遍历右节点
		if (rightNode != null) {
			rightNode.frontShow();
		}
	}

	// 中序遍历
	public void midShow() {
		// 左子节点
		if (leftNode != null) {
			leftNode.midShow();
		}
		// 当前节点
		System.out.println(value);
		// 右子节点
		if (rightNode != null) {
			rightNode.midShow();
		}
	}

	// 后序遍历
	public void afterShow() {
		// 左子节点
		if (leftNode != null) {
			leftNode.afterShow();
		}
		// 右子节点
		if (rightNode != null) {
			rightNode.afterShow();
		}
		// 当前节点
		System.out.println(value);
	}

	// 整体思想与遍历一样
	public ThreadedNode frontSearch(int i) {
		ThreadedNode target = null;
		// 对比当前节点的值
		if (this.value == i) {
			return this;
		} else { // 当前节点的值不是要查找的节点
			// 查找左子节点
			if (leftNode != null)
				target = leftNode.frontSearch(i);
			// 如果不为空，说明已经找到，直接返回
			if (target != null)
				return target;
			// 查找右子节点
			if (rightNode != null)
				target = rightNode.frontSearch(i);
		}
		return target;
	}

	// 删除一棵子树
	public void delete(int i) {
		ThreadedNode parent = this;
		// 判断左子树
		if (parent.leftNode != null && parent.leftNode.value == i) {
			parent.leftNode = null;
			return;
		}
		// 判断右子树
		if (parent.rightNode != null && parent.rightNode.value == i) {
			parent.rightNode = null;
			return;
		}
		// 递归检查并删除左子树
		parent = leftNode;
		if (parent != null) {
			parent.delete(i);
		}
		// 递归删除右子树
		parent = rightNode;
		if (parent != null) {
			parent.delete(i);
		}
	}
}
