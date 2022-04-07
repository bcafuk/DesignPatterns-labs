import java.io.FileNotFoundException;
import java.util.List;

public class Zad5 {
    public static void main(String[] args) throws FileNotFoundException {
        IzvorBrojeva izvor = new DatotecniIzvor("ulaz.txt");

        SlijedBrojeva slijed = new SlijedBrojeva(izvor);

        slijed.dodajPromatraca(new ZbrajajuciPromatrac());
        slijed.dodajPromatraca(new BiljezeciPromatrac("ispis.txt"));

        slijed.kreni();
    }
}
