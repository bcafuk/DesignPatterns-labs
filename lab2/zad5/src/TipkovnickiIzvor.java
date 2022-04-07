import java.util.Scanner;

public class TipkovnickiIzvor implements IzvorBrojeva{
    private final Scanner scanner = new Scanner(System.in);

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
