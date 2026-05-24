import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    private HashingUtils utils;

    public MultiplicativeShiftingHash() {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(int k) {
            throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
        }
        @Override
        public int hash(Long key) {
            throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
