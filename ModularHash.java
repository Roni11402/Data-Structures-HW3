import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    private Random rand;
    private HashingUtils utils;

    public ModularHash() {
        this.rand = new Random();
        this.utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int k) {
            this.a = rand.nextInt(Integer.MAX_VALUE - 1) + 1;
            this.b = rand.nextInt(Integer.MAX_VALUE);
            this.p = utils.genPrime(Integer.MAX_VALUE + 1, Long.MAX_VALUE);
            this.m = 1 << k;
        }

        @Override
        public int hash(Integer key) {
            long calc = (long) a * key + b;
            return (int) utils.mod(utils.mod(calc, p), m);
        }


        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
