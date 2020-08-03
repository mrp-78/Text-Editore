public interface Node<E> {
    void setNext(E next);

    void setPrev(E prev);

    E getNext();

    E getPrev();
}