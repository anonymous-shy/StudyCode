package xyz.shy.DataStructure;

/**
 * Created by Shy on 2018/7/9
 * 冒泡排序
 * 1,比较相邻的元素，如果前一个比后一个大，就把它们两个调换位置。
 * 2,对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。
 * 3,针对所有的元素重复以上的步骤，除了最后一个。
 * 4,持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
 * <p>
 * 分类 -------------- 内部比较排序
 * 数据结构 ---------- 数组
 * 最差时间复杂度 ---- O(n^2)
 * 最优时间复杂度 ---- 如果能在内部循环第一次运行时,使用一个旗标来表示有无需要交换的可能,可以把最优时间复杂度降低到O(n)
 * 平均时间复杂度 ---- O(n^2)
 * 所需辅助空间 ------ O(1)
 * 稳定性 ------------ 稳定
 */

public class BubbleSort {

    public static void main(String[] args) {

    }

    static void bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) { //外层循环控制排序趟数
            for (int j = 0; j < len - i - 1; j++) {  //内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
        System.out.println("冒泡排序后的数组为: ");
        for (int n : arr) {
            System.out.println("num: " + n);
        }
    }
}
