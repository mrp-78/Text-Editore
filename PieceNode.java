public class PieceNode implements Node<PieceNode> {
    private int start, length;
    boolean origin;
    private PieceNode next, prev;

    public PieceNode(int start, int length, boolean origin) {
        this.start = start;
        this.length = length;
        this.next = null;
        this.prev = null;
        this.origin = origin;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public boolean isOrigin() {
        return origin;
    }

    @Override
    public PieceNode getNext() {
        return next;
    }

    @Override
    public void setNext(PieceNode next) {
        this.next = next;
    }

    @Override
    public PieceNode getPrev() {
        return prev;
    }

    @Override
    public void setPrev(PieceNode prev) {
        this.prev = prev;
    }
}
