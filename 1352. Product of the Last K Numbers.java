
import java.util.*;

class ProductOfNumbers {

    ArrayList<Long> prefix;

    public ProductOfNumbers() {
        prefix = new ArrayList<>();
        prefix.add(1L);
    }

    public void add(int num) {

        if (num == 0) {
            prefix.clear();
            prefix.add(1L);
        } else {
            long last = prefix.get(prefix.size() - 1);
            prefix.add(last * num);
        }
    }

    public int getProduct(int k) {

        int n = prefix.size();

        if (k >= n) {
            return 0;
        }

        long product = prefix.get(n - 1) /
                       prefix.get(n - 1 - k);

        return (int) product;
    }
}