import java.io.FileNotFoundException;
import java.util.List;

public class Zad5 {
    public static void main(String[] args) throws FileNotFoundException {
        IzvorBrojeva izvor = new IteratorskiIzvor(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9).iterator());

        SlijedBrojeva slijed = new SlijedBrojeva(izvor);

        slijed.dodajPromatraca(new ZbrajajuciPromatrac());
        slijed.dodajPromatraca(new BiljezeciPromatrac("ispis.txt"));

        slijed.kreni();
    }
}
