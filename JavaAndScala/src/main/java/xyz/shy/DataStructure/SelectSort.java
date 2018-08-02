package xyz.shy.DataStructure;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shy on 2018/7/9
 * 初始时在序列中找到最小（大）元素，放到序列的起始位置作为已排序序列；
 * 然后，再从剩余未排序元素中继续寻找最小（大）元素，放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
 * 选择排序和冒泡排序一样的时间复杂度 ---- O(n^2)
 * 但是，选择排序更快，因为进行的交换要少，当N值较小时，特别是如果交换的时间比比较的时间级要大得多时，选择排序是要快的多。
 */

public class SelectSort {

    public static void main(String[] args) {
        List<String> l = new LinkedList<>();
    }

    static void selectSort(int[] arr) { //循环次数
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            int min = i;
            for (int j = i + 1; j < len; j++) { //找到最小的值和位置
                if (arr[j] < arr[i]) {
                    min = j;    // 替换较小值
                }
            }
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
    }
}
