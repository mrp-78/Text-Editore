public class LineNode implements Node<LineNode> {
    LinkedList pieces;
    LineNode next, prev;
    int txtSize;

    public LineNode(LinkedList first) {
        pieces = first;
    }

    public int getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    public LinkedList getPieces() {
        return pieces;
    }

    public void setPieces(LinkedList pieces) {
        this.pieces = pieces;
    }

    @Override
    public LineNode getNext() {
        return next;
    }

    @Override
    public void setNext(LineNode next) {
        this.next = next;
    }

    @Override
    public LineNode getPrev() {
        return prev;
    }

    @Override
    public void setPrev(LineNode prev) {
        this.prev = prev;
    }
}
