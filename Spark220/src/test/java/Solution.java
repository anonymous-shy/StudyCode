import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        List<List<Integer>> threeSum = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        System.out.println(threeSum);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new LinkedList<>();
        // 设置K
        for (int k = 0; k < nums.length - 2; k++) {
            if (nums[k] > 0) {
                break;
            }
            if (k == 0 || nums[k] != nums[k - 1]) {
                int lo = k + 1, hi = nums.length - 1, sum = -nums[k];
                while (lo < hi) {
                    // 1. 相等
                    if (nums[lo] + nums[hi] == sum) {
                        res.add(Arrays.asList(nums[k], nums[lo], nums[hi]));
                        while (lo < hi && nums[lo] == nums[lo + 1]) // 去重
                        {
                            lo++;
                        }
                        while (lo < hi && nums[hi] == nums[hi - 1]) // 去重
                        {
                            hi--;
                        }
                        lo++;
                        hi--;
                    } else if (nums[lo] + nums[hi] < sum) {
                        lo++;
                    } else {
                        hi--;
                    }
                }
            }
        }
        return res;
    }
}
