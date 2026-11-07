
import java.util.*;

class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {

        int n = servers.length;
        int m = tasks.length;

        int[] result = new int[m];

        // Available servers: [weight, index]
        PriorityQueue<int[]> available = new PriorityQueue<>(
            (a, b) -> {
                if (a[0] != b[0]) {
                    return a[0] - b[0];
                }
                return a[1] - b[1];
            }
        );

        // Busy servers: [finishTime, weight, index]
        PriorityQueue<long[]> busy = new PriorityQueue<>(
            (a, b) -> {
                if (a[0] != b[0]) {
                    return Long.compare(a[0], b[0]);
                }
                if (a[1] != b[1]) {
                    return Long.compare(a[1], b[1]);
                }
                return Long.compare(a[2], b[2]);
            }
        );

        // Initially, all servers are available
        for (int i = 0; i < n; i++) {
            available.offer(new int[]{servers[i], i});
        }

        long time = 0;

        for (int i = 0; i < m; i++) {

            time = Math.max(time, i);

            // Release completed servers
            while (!busy.isEmpty() && busy.peek()[0] <= time) {

                long[] server = busy.poll();

                available.offer(new int[]{
                    (int) server[1],
                    (int) server[2]
                });
            }

            // If no server is available, wait
            if (available.isEmpty()) {

                time = busy.peek()[0];

                // Release all servers completed by this time
                while (!busy.isEmpty() && busy.peek()[0] <= time) {

                    long[] server = busy.poll();

                    available.offer(new int[]{
                        (int) server[1],
                        (int) server[2]
                    });
                }
            }

            // Select the best available server
            int[] server = available.poll();

            int weight = server[0];
            int index = server[1];

            result[i] = index;

            // Assign task and calculate finishing time
            busy.offer(new long[]{
                time + tasks[i],
                weight,
                index
            });
        }

        return result;
    }
}
