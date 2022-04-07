import java.util.Iterator;

public class IteratorskiIzvor implements IzvorBrojeva {
    private final Iterator<Integer> iterator;

    public IteratorskiIzvor(Iterator<Integer> iterator) {
        this.iterator = iterator;
    }

    @Override
    public int dohvatiBroj() {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return -1;
        }
    }
}
