public class IndexableSkipList extends AbstractSkipList {
    final protected double p;    // p is the probability for "success" in the geometric process generating the height of each node.

    public IndexableSkipList(double probability) {
        super();
        this.p = probability;
    }

    @Override
    public void decreaseHeight() {
        head.removeLevel();
        tail.removeLevel();
    }

    @Override
    public SkipListNode find(int key) {
        SkipListNode p = this.head;
        int rank = 0;
        for (int i = p.height(); i >= 0; i--) {
            while (p.getNext(i) != null && p.getNext(i).key() <= key) {
                p = p.getNext(i);
                rank = rank + p.getWidth(i);
            }
        }
        return p;
    }

    @Override
    public int generateHeight() {
        int height = 0;
        while (Math.random() < p) {
            height++;
        }
        return height;
    }

    public int rank(int key) {
        SkipListNode p = head;
        int rank = 0;
        for (int i = head.height(); i >= 0; i--) {
            while (p.getNext(i) != null && p.getNext(i).key() < key) {
                rank = rank + p.getWidth(i);
                p = p.getNext(i);
            }
        }
        return rank;
    }

    public int select(int index) {
        SkipListNode currNode = this.head;
        int dist = -1;
        for (int i = currNode.height(); i >= 0; i--) {
            while (currNode.getNext(i) != null && currNode.getWidth(i) + dist <= index && !currNode.getNext(i).equals(tail)) {
                dist = dist + currNode.getWidth(i);
                currNode = currNode.getNext(i);
            }
        }
        return currNode.key();

    }
}
