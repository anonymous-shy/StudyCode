package xyz.shy.DataStructure;

/**
 * Created by Shy on 2018/7/6
 * 二分查找
 *  折半查找的时间复杂度为O(logn)，远远好于顺序查找的O(n)。
 *  虽然二分查找的效率高，但是要将表按关键字排序。而排序本身是一种很费时的运算。既使采用高效率的排序方法也要花费O(nlgn)的时间。
 *  二分查找只适用顺序存储结构。为保持表的有序性，在顺序结构里插入和删除都必须移动大量的结点。
 *  因此，二分查找特别适用于那种一经建立就很少改动、而又经常需要查找的线性表。
 *  对那些查找少而又经常需要改动的线性表，可采用链表作存储结构，进行顺序查找。链表上无法实现二分查找。
 */

public class BinarySearch {

    public static void main(String[] args) {

    }

    static int binarySearch(int[] arr, int key) {
        int lowerBound = 0;
        int upperBound = arr.length - 1;
        int currIdx;
        while (lowerBound <= upperBound) {
            currIdx = (lowerBound + upperBound) / 2;
            if (arr[currIdx] == key) {
                return currIdx;
            } else if (arr[currIdx] < key) {
                lowerBound = currIdx + 1;
            } else {
                upperBound = currIdx - 1;
            }
        }
        return -1;
    }
}
