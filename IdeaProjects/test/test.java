
public class test {

    public static void main(String[] args) {
        System.out.println(absolute(1));
        System.out.println(absolute(-1));
    }

    public static int absolute(int x) {
        int mask1 = x >> 31;
        int mask2 = -1 >>> 1;
        return ((x + mask1) ^ mask1);
    }

}
