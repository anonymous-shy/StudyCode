package xyz.shy.DataStructure;

public class DoubleNode {
    // 双向循环链表
    // 上一个节点
    DoubleNode pre = this;
    // 下一个节点
    DoubleNode next = this;
    // 节点数据
    int data;

    public DoubleNode(int data) {
        this.data = data;
    }

    // 插入节点
    public void insert(DoubleNode node) {
        // 原来的下一个节点变成下下个节点
        DoubleNode nextNext = next;
        // 新节点作为当前节点的下个节点
        this.next = node;
        // 当前节点作为新节点的前一个节点
        node.pre = this;
        // 原来节点的下一个变成新节点的下一个
        node.next = nextNext;
        // 原来节点的下一个节点的上一个变成上一个
        nextNext.pre = node;
    }

    // 下一个节点
    public DoubleNode next() {
        return this.next;
    }

    // 上一个节点
    public DoubleNode pre() {
        return this.pre;
    }

    public int getData() {
        return data;
    }

    public static void main(String[] args) {

        DoubleNode n1 = new DoubleNode(1);
        DoubleNode n2 = new DoubleNode(2);
        DoubleNode n3 = new DoubleNode(3);

        // 添加节点
        n1.insert(n2);
        n2.insert(n3);

        System.out.println(n2.pre().getData());
        System.out.println(n2.getData());
        System.out.println(n2.next().getData());
        System.out.println(n3.next().getData());
        System.out.println(n1.pre().getData());
    }
}
