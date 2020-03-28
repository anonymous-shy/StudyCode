package xyz.shy.DataStructure;

/**
 * 单链表测试
 */
public class LoopNode {

    // 节点内容
    int data;

    // 下一个节点
    LoopNode next = this;

    public LoopNode(int data) {
        this.data = data;
    }

    public LoopNode next() {
        return this.next;
    }

    public int getData() {
        return this.data;
    }

    public void removeNext() {
        // 将下下个节点设置为下个节点
        this.next = this.next.next;
    }

    // 插入一个节点作为当前节点的下一个节点
    public void insert(LoopNode node) {
        // 取出下一个节点作为下下个节点
        LoopNode nextNext = this.next;
        this.next = node;
        node.next = nextNext;
    }

    public static void main(String[] args) {
        // 创建节点
        LoopNode n1 = new LoopNode(1);
        LoopNode n2 = new LoopNode(2);
        LoopNode n3 = new LoopNode(3);
        LoopNode n4 = new LoopNode(4);
        //
        n1.insert(n2);
        n2.insert(n3);
        n3.insert(n4);

        // 取出下一个节点
        System.out.println(n1.next().getData());
        System.out.println(n2.next().getData());
        System.out.println(n3.next().getData());
        System.out.println(n4.next().getData());

    }
}
