public class LinkedList {
    Node head, tail;
    int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public LinkedList(Node start) {
        this.head = start;
        this.tail = start;
        this.size = 1;
        if (start != null)
            start.setPrev(null);
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public void add(Node obj, Node prev) {
        if (prev == null) {
            obj.setNext(head);
            obj.setPrev(null);
            if (size > 0)
                head.setPrev(obj);
            head = obj;
            if (size == 0) {
                tail = obj;
            }
            size++;
        } else if (prev == tail) {
            addToEnd(obj);
        } else {
            obj.setNext(prev.getNext());
            obj.setPrev(prev);
            prev.setNext(obj);
            if (obj.getNext() != null)
                ((Node) obj.getNext()).setPrev(obj);
            size++;
        }
    }

    public void addToEnd(Node obj) {
        if (size == 0) {
            head = obj;
            tail = obj;
        } else {
            tail.setNext(obj);
            obj.setPrev(tail);
            tail = obj;
        }
        size++;
    }

    public void remove(Node prev) {
        Node p, n;
        if (prev == null) {
            p = head;
            head = (Node) p.getNext();
            p.setNext(null);
            if (head != null)
                head.setPrev(null);
            if (size == 1)
                tail = null;
        } else {
            p = (Node) prev.getNext();
            n = (Node) p.getNext();
            if (n != null)
                n.setPrev(prev);
            prev.setNext(n);
            p.setNext(null);
            p.setPrev(null);
            if (p.equals(tail)) {
                if (n != null)
                    tail = n;
                else
                    tail = prev;
            }
        }
        size--;
    }

    public Node getIndex(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        Node p;
        if (index < size / 2) {
            p = head;
            for (int i = 0; i < index; i++) {
                p = (Node) p.getNext();
            }
        } else {
            p = tail;
            for (int i = size - 1; i > index; i--) {
                p = (Node) p.getPrev();
            }
        }
        return p;
    }
}
