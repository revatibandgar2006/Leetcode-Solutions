
import java.util.*;

class Solution {

    class Fenwick {
        int n;
        long[] bit;

        Fenwick(int n) {
            this.n = n;
            bit = new long[n + 1];
        }

        void update(int i, long val) {
            i++;

            while (i <= n) {
                bit[i] += val;
                i += i & -i;
            }
        }

        long query(int i) {
            long sum = 0;
            i++;

            while (i > 0) {
                sum += bit[i];
                i -= i & -i;
            }

            return sum;
        }

        int kth(long k) {
            int idx = 0;

            int step = Integer.highestOneBit(n);

            while (step > 0) {
                int next = idx + step;

                if (next <= n && bit[next] < k) {
                    idx = next;
                    k -= bit[next];
                }

                step >>= 1;
            }

            return idx;
        }
    }

    public long minOperations(int[] nums, int x, int k) {

        int n = nums.length;

        // Coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int m = 0;

        for (int v : sorted) {
            if (m == 0 || sorted[m - 1] != v) {
                sorted[m++] = v;
            }
        }

        int[] values = Arrays.copyOf(sorted, m);

        Map<Integer, Integer> pos = new HashMap<>();

        for (int i = 0; i < m; i++) {
            pos.put(values[i], i);
        }

        Fenwick count = new Fenwick(m);
        Fenwick sums = new Fenwick(m);

        long total = 0;

        long[] costs = new long[n - x + 1];

        // Part 1: Sliding Window
        for (int i = 0; i < n; i++) {

            int v = nums[i];
            int idx = pos.get(v);

            count.update(idx, 1);
            sums.update(idx, v);
            total += v;

            // Remove element outside window
            if (i >= x) {
                int old = nums[i - x];
                int oldIdx = pos.get(old);

                count.update(oldIdx, -1);
                sums.update(oldIdx, -old);
                total -= old;
            }

            if (i >= x - 1) {

                // Find median
                int midIdx = count.kth((x + 1L) / 2);
                long median = values[midIdx];

                long leftCount = count.query(midIdx);
                long leftSum = sums.query(midIdx);

                long rightCount = x - leftCount;
                long rightSum = total - leftSum;

                // Calculate minimum operations
                long cost =
                    median * leftCount - leftSum
                    + rightSum - median * rightCount;

                costs[i - x + 1] = cost;
            }
        }

        // Part 2: Dynamic Programming
        long INF = Long.MAX_VALUE / 4;

        long[][] dp = new long[k + 1][n + 1];

        for (int t = 0; t <= k; t++) {
            Arrays.fill(dp[t], INF);
        }

        // Zero subarrays require zero operations
        Arrays.fill(dp[0], 0);

        for (int t = 1; t <= k; t++) {

            for (int i = 1; i <= n; i++) {

                // Option 1: Skip current element
                dp[t][i] = dp[t][i - 1];

                // Option 2: Select window of size x
                if (i >= x) {

                    int start = i - x;

                    if (dp[t - 1][start] != INF) {

                        dp[t][i] = Math.min(
                            dp[t][i],
                            dp[t - 1][start] + costs[start]
                        );
                    }
                }
            }
        }

        return dp[k][n];
    }
}
