import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    private HashingUtils utils;

    public MultiplicativeShiftingHash() {
        this.utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(int k) {
            Random rand = new Random();
            long tempA = 0;
            while (tempA <= 1) {
                tempA = rand.nextLong();
            }
            this.a = tempA;
            this.k = k;
        }
        @Override
        public int hash(Long key) {
            return (int)(this.a*key)>>>(WORD_SIZE - this.k);
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
