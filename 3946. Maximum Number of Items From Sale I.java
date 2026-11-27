
import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;

        // Calculate reward for selecting each item type
        int[] reward = new int[n];

        for (int i = 0; i < n; i++) {
            reward[i] = 1;

            for (int j = 0; j < n; j++) {
                if (i != j && items[j][0] % items[i][0] == 0) {
                    reward[i]++;
                }
            }
        }

        int NEG = -1000000000;

        // dp[b] = maximum reward using items with
        // prices strictly greater than the current price
        int[] dp = new int[budget + 1];
        Arrays.fill(dp, NEG);
        dp[0] = 0;

        int answer = 0;

        // Process prices from highest to lowest
        for (int p = 1500; p >= 1; p--) {

            // Subsets that contain at least one item
            // having the current price p
            int[] hasPrice = new int[budget + 1];
            Arrays.fill(hasPrice, NEG);

            // Process all items with price p
            for (int i = 0; i < n; i++) {
                if (items[i][1] != p) {
                    continue;
                }

                int cost = p;
                int value = reward[i];

                // 0/1 knapsack update
                for (int b = budget; b >= cost; b--) {

                    // Add this item to an existing subset
                    if (hasPrice[b - cost] != NEG) {
                        hasPrice[b] = Math.max(
                            hasPrice[b],
                            hasPrice[b - cost] + value
                        );
                    }

                    // Select this as the first item of price p
                    if (dp[b - cost] != NEG) {
                        hasPrice[b] = Math.max(
                            hasPrice[b],
                            dp[b - cost] + value
                        );
                    }
                }
            }

            // Calculate answer for subsets whose minimum price is p
            for (int b = 0; b <= budget; b++) {
                if (hasPrice[b] != NEG) {
                    int total = hasPrice[b] + (budget - b) / p;
                    answer = Math.max(answer, total);
                }
            }

            // Add all items of price p to the DP
            // for the next lower price
            for (int i = 0; i < n; i++) {
                if (items[i][1] != p) {
                    continue;
                }

                int cost = p;
                int value = reward[i];

                for (int b = budget; b >= cost; b--) {
                    if (dp[b - cost] != NEG) {
                        dp[b] = Math.max(
                            dp[b],
                            dp[b - cost] + value
                        );
                    }
                }
            }
        }

        return answer;
    }
}
