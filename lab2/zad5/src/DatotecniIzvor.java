import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatotecniIzvor implements IzvorBrojeva{
    private final Scanner scanner;

    public DatotecniIzvor(String putanja) throws FileNotFoundException {
        scanner = new Scanner(new FileInputStream(putanja));
    }

    @Override
    public int dohvatiBroj() {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            scanner.close();
            return -1;
        }
    }
}
