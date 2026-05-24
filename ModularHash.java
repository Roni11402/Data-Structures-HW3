import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    private Random rand;
    private HashingUtils utils;

    public ModularHash() {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int k){
            throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
        }

        @Override
        public int hash(Integer key) {
            throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
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
