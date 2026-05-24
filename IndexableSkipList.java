public class IndexableSkipList extends AbstractSkipList {
    final protected double p;	// p is the probability for "success" in the geometric process generating the height of each node.
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
        for (int i = p.height(); i >= 0; i--) {
            while (p.getNext(i) != null && p.getNext(i).key() <= key) {
                p = p.getNext(i);
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
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

}
