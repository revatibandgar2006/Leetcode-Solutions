
class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        List<Integer>[] tree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        // Build the original tree
        for (int i = 1; i < n; i++) {
            tree[parent[i]].add(i);
        }

        int[] newParent = parent.clone();

        // Store the closest ancestor for each character
        int[] last = new int[26];
        Arrays.fill(last, -1);

        // Find new parents using DFS
        dfs(0, tree, s, last, newParent);

        // Build the modified tree
        List<Integer>[] newTree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            newTree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            newTree[newParent[i]].add(i);
        }

        // Calculate subtree sizes
        int[] answer = new int[n];

        calculateSize(0, newTree, answer);

        return answer;
    }

    private void dfs(int node, List<Integer>[] tree,
                     String s, int[] last, int[] newParent) {

        int ch = s.charAt(node) - 'a';

        int previous = last[ch];

        if (node != 0 && previous != -1) {
            newParent[node] = previous;
        }

        // Mark current node as the closest ancestor
        last[ch] = node;

        for (int child : tree[node]) {
            dfs(child, tree, s, last, newParent);
        }

        // Restore previous ancestor after DFS
        last[ch] = previous;
    }

    private int calculateSize(int node,
                              List<Integer>[] tree,
                              int[] answer) {

        int size = 1;

        for (int child : tree[node]) {
            size += calculateSize(child, tree, answer);
        }

        answer[node] = size;

        return size;
    }
}
