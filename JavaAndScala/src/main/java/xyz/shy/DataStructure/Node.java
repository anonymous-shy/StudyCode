package xyz.shy.DataStructure;

import scala.reflect.internal.Trees;

/**
 * 单链表测试
 */
public class Node {

    // 节点内容
    int data;

    // 下一个节点
    Node next;

    public Node(int data) {
        this.data = data;
    }

    // 为节点追加节点
    public Node append(Node next) {
        // 当前节点
        Node currNode = this;
        // 循环查找下一个节点
        while (true) {
            // 取出下一个节点
            Node nextNode = currNode.next;
            // 如果下一个节点为null,当前节点已经是最后一个节点
            if (nextNode == null) {
                break;
            }
            // 赋值给当前节点
            currNode = nextNode;
        }
        // 把需要追加的节点追加为找到的当前节点的下一个节点
        currNode.next = next;
        return this;
    }

    public Node next() {
        return this.next;
    }

    public int getData() {
        return this.data;
    }

    public boolean isLast() {
        return next == null;
    }

    public void removeNext() {
        // 将下下个节点设置为下个节点
        this.next = this.next.next;
    }

    public void showAllNodes() {
        Node currNode = this;
        while (true) {
            System.out.print(currNode.data + " ");
            currNode = currNode.next;
            if (currNode == null) {
                break;
            }
        }
        System.out.println();
    }

    // 插入一个节点作为当前节点的下一个节点
    public void insert(Node node) {
        // 取出下一个节点作为下下个节点
        Node nextNext = this.next;
        this.next = node;
        node.next = nextNext;
    }

    public static void main(String[] args) {
        // 创建节点
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        //
        n1.append(n2).append(n3);

        // 取出下一个节点
        System.out.println(n1.next().next().getData());
        System.out.println(n1.isLast());
        System.out.println(n1.next().next().isLast());

        n1.showAllNodes();
        n1.removeNext();
        n1.showAllNodes();

        n1.insert(new Node(7));
        n1.showAllNodes();
    }
}
