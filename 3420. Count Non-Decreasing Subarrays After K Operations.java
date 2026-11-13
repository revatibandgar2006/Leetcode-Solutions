
import java.util.*;

class Solution {
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;

        Deque<int[]> dq = new ArrayDeque<>();

        long ans = 0;
        long cost = 0;

        int right = n - 1;

        for (int left = n - 1; left >= 0; left--) {
            int val = nums[left];
            int count = 1;

            // Merge smaller or equal values from the front
            while (!dq.isEmpty() && dq.peekFirst()[0] <= val) {
                int[] curr = dq.pollFirst();

                cost += (long) (val - curr[0]) * curr[1];
                count += curr[1];
            }

            dq.offerFirst(new int[]{val, count});

            // Reduce window until cost becomes valid
            while (cost > k) {
                int[] last = dq.pollLast();

                cost -= (long) (last[0] - nums[right]);
                right--;

                if (last[1] > 1) {
                    dq.offerLast(new int[]{
                        last[0], last[1] - 1
                    });
                }
            }

            ans += right - left + 1;
        }

        return ans;
    }
}