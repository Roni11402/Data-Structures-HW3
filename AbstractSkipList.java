import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSkipList {
    final protected SkipListNode head;
    final protected SkipListNode tail;

    public AbstractSkipList() {
        head = new SkipListNode(Integer.MIN_VALUE);
        tail = new SkipListNode(Integer.MAX_VALUE);
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        tail.addLevel(null, head);
    }

    abstract void decreaseHeight();

    abstract SkipListNode find(int key);

    abstract int generateHeight();

    public SkipListNode search(int key) {
        SkipListNode curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public SkipListNode insert(int key) {
        int nodeHeight = generateHeight();

        while (nodeHeight > head.height()) {
            increaseHeight();
        }
        int[] rank_update = new int[head.height + 1];
        SkipListNode prevNode = this.head;
        SkipListNode[] update = new SkipListNode[prevNode.height + 1];
        int rank = 0;
        for (int i = prevNode.height(); i >= 0; i--) {
            while (prevNode.getNext(i) != null && prevNode.getNext(i).key() <= key) {
                rank = rank + prevNode.getWidth(i);
                prevNode = prevNode.getNext(i);
            }
            update[i] = prevNode;
            rank_update[i] = rank;
        }
        if (prevNode.key() == key) {
            return null;
        }

        SkipListNode newNode = new SkipListNode(key);
        for (int i = 0; i <= nodeHeight; i++) {
            newNode.addLevel(null, null);
        }
        for (int level = 0; level <= nodeHeight; ++level) {
            SkipListNode left = update[level];
            SkipListNode right = left.getNext(level);

            int oldWidth = left.getWidth(level);
            int distToNewNode = rank - rank_update[level] + 1;
            left.setWidth(level, distToNewNode);
            newNode.setWidth(level, oldWidth - distToNewNode + 1);
            newNode.setNext(level, right);
            newNode.setPrev(level, left);
            left.setNext(level, newNode);
            if (right != null) {
                right.setPrev(level, newNode);
            }
        }
        for (int i = nodeHeight + 1; i <= head.height(); i++) {
            update[i].setWidth(i, update[i].getWidth(i) + 1);
        }
        return newNode;
    }

    public boolean delete(SkipListNode skipListNode) {
        if (skipListNode == null || skipListNode == head) return false;
        SkipListNode[] update = new SkipListNode[head.height() + 1];
        SkipListNode prevNode = this.head;
        int rank = 0;
        for (int i = head.height(); i >= 0; i--) {
            while (prevNode.getNext(i) != null && prevNode.getNext(i).key() < skipListNode.key()) {
                prevNode = prevNode.getNext(i);
            }
            update[i] = prevNode;
        }
        if (update[0].getNext(0) != skipListNode) {
            return false;
        }

        for (int level = 0; level <= head.height(); level++) {
            SkipListNode prev = update[level];
            SkipListNode next;
            if (skipListNode.height >= level) {
                next = skipListNode.getNext(level);
            } else {
                next = null;
            }

            if (level <= skipListNode.height()) {
                int newWidth = prev.getWidth(level) + skipListNode.getWidth(level) - 1;
                prev.setWidth(level, newWidth);
                prev.setNext(level, next);
                if (next != null) {
                    next.setPrev(level, prev);
                }
            } else {
                prev.setWidth(level, prev.getWidth(level) - 1);
            }
        }
        while (head.height() > 0 && head.getNext(head.height()) == this.tail) {
            decreaseHeight();
        }

        return true;
    }

    public SkipListNode predecessor(SkipListNode skipListNode) {
        return skipListNode.getPrev(0);
    }

    public SkipListNode successor(SkipListNode skipListNode) {
        return skipListNode.getNext(0);
    }

    public SkipListNode minimum() {
        if (head.getNext(0) == tail) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return head.getNext(0);
    }

    public SkipListNode maximum() {
        if (tail.getPrev(0) == head) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return tail.getPrev(0);
    }

    private void levelToString(StringBuilder s, int level) {
        s.append("H    ");
        SkipListNode curr = head.getNext(0);

        while (curr != tail) {
            if (curr.height >= level) {
                s.append(curr.key());
                s.append("    ");
            } else {
                s.append("    ");
                for (int i = 0; i < curr.key().toString().length(); i = i + 1)
                    s.append(" ");
            }

            curr = curr.getNext(0);
        }

        s.append("T\n");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString(str, level);
        }

        return str.toString();
    }

    public static class SkipListNode extends Element<Integer, Object> {
        final private List<SkipListNode> next;
        final private List<SkipListNode> prev;
        final private List<Integer> width;
        private int height;

        public SkipListNode(int key) {
            super(key);
            next = new ArrayList<>();
            prev = new ArrayList<>();
            width = new ArrayList<>();
            this.height = -1;

        }

        public SkipListNode getPrev(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return prev.get(level);
        }

        public SkipListNode getNext(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return next.get(level);
        }

        public void setNext(int level, SkipListNode next) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.next.set(level, next);
        }

        public void setPrev(int level, SkipListNode prev) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.prev.set(level, prev);
        }

        public void addLevel(SkipListNode next, SkipListNode prev) {
            ++height;
            this.next.add(next);
            this.prev.add(prev);
            this.width.add(0);
        }

        public void removeLevel() {
            this.next.remove(height);
            this.prev.remove(height);
            --height;
        }

        public int height() {
            return height;
        }

        public int getWidth(int i) {
            return this.width.get(i);
        }

        public void setWidth(int i, int width) {
            this.width.set(i, width);
        }
    }
}
