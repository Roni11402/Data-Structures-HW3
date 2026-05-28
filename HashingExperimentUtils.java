import java.awt.event.MouseMotionAdapter;
import java.util.Collections; // can be useful

public class HashingExperimentUtils {
    final private static int k = 16;

    public static double[] measureInsertionsProbing() {
        double[] factorArr = {0.5, 0.75, 0.875, 0.9375};
        double[] timeArr = new double[4];
        MultiplicativeShiftingHash factory = new MultiplicativeShiftingHash();
        int experiments = 25;
        for (int i = 0; i < timeArr.length; i++) {
            double totalTime = 0;
            for (int exp = 0; exp < experiments; exp++) {
                ProbingHashTable<Long, Long> test = new ProbingHashTable<>(factory, k, factorArr[i]);
                int toInsert = (int) ((1 << k) * factorArr[i]) - 1;
                long startTime = System.nanoTime();
                for (int j = 0; j < toInsert; j++) {
                    test.insert((long) j, (long) j);
                }
                long endTime = System.nanoTime();
                totalTime += (double)(endTime - startTime) / toInsert;
            }
            timeArr[i] = totalTime / experiments;
        }
        return timeArr;
    }

    public static double[] measureSearchesProbing() {
        double[] factorArr = {0.5, 0.75, 0.875, 0.9375};
        double[] timeArr = new double[4];
        MultiplicativeShiftingHash factory = new MultiplicativeShiftingHash();
        int experiments = 25;
        for (int i = 0; i < timeArr.length; i++) {
            double totalTime = 0;
            for (int exp = 0; exp < experiments; exp++) {
                ProbingHashTable<Long, Long> test = new ProbingHashTable<>(factory, k, factorArr[i]);
                int toInsert = (int) ((1 << k) * factorArr[i]) - 1;
                HashingUtils utils = new HashingUtils();
                Long[] items = utils.genUniqueLong(toInsert * 2);
                for (int j = 0; j < toInsert; j++) {
                    test.insert(items[j], items[j]);
                }
                long startTime = System.nanoTime();
                for (int j = 0; j < toInsert; j++) {
                    if (j % 2 == 0) {
                        test.search(items[j]);
                    } else {
                        test.search(items[toInsert + j]);
                    }
                }
                for (int j = 0; j < toInsert; j++) {
                    test.search((long) j);
                }
                long endTime = System.nanoTime();
                totalTime += (double)(endTime - startTime) / toInsert;
            }
            timeArr[i] = totalTime / experiments;
        }
        return timeArr;
    }

    public static double[] measureInsertionsChaining() {
        double[] factorArr = {0.5, 0.75, 1, 1.5, 2};
        double[] timeArr = new double[5];
        ModularHash factory = new ModularHash();
        int experiments = 25;
        for (int i = 0; i < timeArr.length; i++) {
            double totalTime = 0;
            for (int exp = 0; exp < experiments; exp++) {
            ChainedHashTable<Integer, Integer> test = new ChainedHashTable(factory, k, factorArr[i]);
            int toInsert = (int) ((1 << k) * factorArr[i]);
            long startTime = System.nanoTime();
            for (int j = 0; j < toInsert; j++) {
                test.insert(j, j);
            }
            long endTime = System.nanoTime();
            totalTime += (double)(endTime - startTime) / toInsert;
            }
            timeArr[i] = totalTime / experiments;
        }
        return timeArr;
    }

    public static double[] measureSearchesChaining() {
        double[] factorArr = {0.5, 0.75, 1, 1.5, 2};
        double[] timeArr = new double[5];
        ModularHash factory = new ModularHash();
        int experiments = 25;
        for (int i = 0; i < timeArr.length; i++) {
            double totalTime = 0;
            for (int exp = 0; exp < experiments; exp++) {
            ChainedHashTable<Integer, Integer> test = new ChainedHashTable<>(factory, k, factorArr[i]);
            int toInsert = (int) ((1 << k) * factorArr[i]);
            HashingUtils utils = new HashingUtils();
            Integer[] items = utils.genUniqueIntegers(toInsert * 2);
            for (int j = 0; j < toInsert; j++) {
                test.insert(items[j], items[j]);
            }
            long startTime = System.nanoTime();
            for (int j = 0; j < toInsert; j++) {
                if (j % 2 == 0) {
                    test.search(items[j]);
                } else {
                    test.search(items[toInsert + j]);
                }
            }
            long endTime = System.nanoTime();
                totalTime += (double)(endTime - startTime) / toInsert;
            }
            timeArr[i] = totalTime / experiments;
        }
        return timeArr;
    }
}
