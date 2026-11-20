
import java.util.Arrays;

class Solution {
    public int maxSelectedElements(int[] nums) {
        Arrays.sort(nums);

        Map<Integer, Integer> dp = new HashMap<>();
        int ans = 0;

        for (int x : nums) {
            int count1 = dp.getOrDefault(x, 0);
            int count2 = dp.getOrDefault(x - 1, 0);

            dp.put(x + 1, count1 + 1);
            dp.put(x, count2 + 1);

            ans = Math.max(ans, Math.max(dp.get(x), dp.get(x + 1)));
        }

        return ans;
    }
}
 