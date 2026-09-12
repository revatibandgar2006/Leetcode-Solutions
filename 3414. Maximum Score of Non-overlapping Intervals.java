import java.util.*;

class Solution {
    static class Interval {
        int l, r, w, idx;

        Interval(int l, int r, int w, int idx) {
            this.l = l;
            this.r = r;
            this.w = w;
            this.idx = idx;
        }
    }

    static class State {
        long score;
        int[] indices;

        State(long score, int[] indices) {
            this.score = score;
            this.indices = indices;
        }
    }

    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        Interval[] arr = new Interval[n];

        for (int i = 0; i < n; i++) {
            arr[i] = new Interval(
                intervals.get(i).get(0),
                intervals.get(i).get(1),
                intervals.get(i).get(2),
                i
            );
        }

        Arrays.sort(arr, (a, b) -> Integer.compare(a.r, b.r));

        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = arr[i].r;
        }

        State[][] dp = new State[n + 1][5];

        for (int i = 0; i <= n; i++) {
            for (int k = 0; k <= 4; k++) {
                dp[i][k] = new State(0, new int[0]);
            }
        }

        for (int i = 1; i <= n; i++) {
            Interval cur = arr[i - 1];

            // Number of intervals ending strictly before cur.l
            int p = lowerBound(ends, i - 1, cur.l);

            for (int k = 1; k <= 4; k++) {
                State skip = dp[i - 1][k];

                State previous = dp[p][k - 1];

                int[] candidateIndices =
                    Arrays.copyOf(previous.indices,
                                   previous.indices.length + 1);

                candidateIndices[candidateIndices.length - 1] = cur.idx;

                Arrays.sort(candidateIndices);

                State take = new State(
                    previous.score + cur.w,
                    candidateIndices
                );

                dp[i][k] = better(skip, take);
            }
        }

        return dp[n][4].indices;
    }

    // First position where ends[pos] >= target
    static int lowerBound(int[] ends, int length, int target) {
        int lo = 0, hi = length;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (ends[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    static State better(State a, State b) {
        if (a.score != b.score) {
            return a.score > b.score ? a : b;
        }

        return compareLexicographically(a.indices, b.indices) <= 0
            ? a : b;
    }

    static int compareLexicographically(int[] a, int[] b) {
        int len = Math.min(a.length, b.length);

        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }

        return Integer.compare(a.length, b.length);
    }
}