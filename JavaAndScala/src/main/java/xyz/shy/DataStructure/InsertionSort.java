package xyz.shy.DataStructure;

/**
 * Created by Shy on 2018/7/9
 * 在第一趟排序中，插入排序最多比较一次，第二趟最多比较两次，依次类推，最后一趟最多比较N-1次。因此有：
 * 1+2+3+...+N-1 =N*N(N-1)/2
 * 因为在每趟排序发现插入点之前，平均来说，只有全体数据项的一半进行比较，我们除以2得到：
 * N*N(N-1)/4
 * 复制的次数大致等于比较的次数，然而，一次复制与一次比较的时间消耗不同，所以相对于随机数据，这个算法比冒泡排序快一倍，比选择排序略快。
 * 与冒泡排序、选择排序一样，插入排序的时间复杂度仍然为O(N2)，这三者被称为简单排序或者基本排序，三者都是稳定的排序算法。
 * 如果待排序数组基本有序时，插入排序的效率会更高。几乎只需要O(N)的时间，这对一个基本有序的文件排序是一个简单有效的方法。
 */

public class InsertionSort {

    static void insertionSort(int[] arr) {
        int len = arr.length;
        /*
         * 外层for循环中，out变量从1开始，向右移动。它标记了未排序部分的最左端数据。
         * 内层while循环中，in变量从out变量开始向左移动，直到temp变量小于in所指的数组值，或者它已经不能再向左移动为止。
         * while循环的每一趟向右移动了一个已排序的数据项。
         */
        int in, out;
        for (out = 1; out < len; out++) {
            int temp = arr[out];
            in = out;
            while (in > 0 && arr[in - 1] > temp) {
                arr[in] = arr[in - 1];
                --in;
            }
            arr[in] = temp;
        }


        for (int i = 1; i < len; i++) {
            int tmp = arr[i];   // remove marked item 存储待排序的元素值
            int insertPoint = i;  //与待排序元素值作比较的元素的下标
            while (insertPoint > 0 && arr[insertPoint - 1] > tmp) {    //当前元素比待排序元素大，直到找到小于待排序值
                arr[insertPoint] = arr[insertPoint - 1];
                --insertPoint;
            }
            arr[insertPoint] = tmp;
        }
    }
}
