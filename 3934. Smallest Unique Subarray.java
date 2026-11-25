
import java.util.*;

class Solution {
    public int smallestUniqueSubarray(int[] nums) {
        int n = nums.length;

        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = nums[i];
        }

        // Build suffix array
        for (int k = 1; k < n; k *= 2) {
            final int len = k;
            final int[] currentRank = rank;

            Integer[] order = new Integer[n];

            for (int i = 0; i < n; i++) {
                order[i] = i;
            }

            Arrays.sort(order, (a, b) -> {
                if (currentRank[a] != currentRank[b]) {
                    return Integer.compare(
                        currentRank[a], currentRank[b]
                    );
                }

                int x = a + len < n ? currentRank[a + len] : -1;
                int y = b + len < n ? currentRank[b + len] : -1;

                return Integer.compare(x, y);
            });

            int classes = 0;
            temp[order[0]] = 0;

            for (int i = 1; i < n; i++) {
                int a = order[i - 1];
                int b = order[i];

                int x = a + len < n ? rank[a + len] : -1;
                int y = b + len < n ? rank[b + len] : -1;

                if (rank[a] != rank[b] || x != y) {
                    classes++;
                }

                temp[b] = classes;
            }

            for (int i = 0; i < n; i++) {
                sa[i] = order[i];
                rank[i] = temp[i];
            }

            if (classes == n - 1) {
                break;
            }
        }

        // Inverse suffix array
        int[] pos = new int[n];

        for (int i = 0; i < n; i++) {
            pos[sa[i]] = i;
        }

        // Kasai's LCP algorithm
        int[] lcp = new int[n];
        int h = 0;

        for (int i = 0; i < n; i++) {
            int p = pos[i];

            if (p == 0) {
                h = 0;
                continue;
            }

            int j = sa[p - 1];

            while (i + h < n && j + h < n &&
                   nums[i + h] == nums[j + h]) {
                h++;
            }

            lcp[p] = h;

            if (h > 0) {
                h--;
            }
        }

        // Find the shortest valid unique subarray
        int ans = n;

        for (int p = 0; p < n; p++) {
            int start = sa[p];

            int common = 0;

            if (p > 0) {
                common = Math.max(common, lcp[p]);
            }

            if (p < n - 1) {
                common = Math.max(common, lcp[p + 1]);
            }

            int requiredLength = common + 1;

            // Important: the subarray must fit in the suffix
            if (requiredLength <= n - start) {
                ans = Math.min(ans, requiredLength);
            }
        }

        return ans;
    }
}
