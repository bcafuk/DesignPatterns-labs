import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class BiljezeciPromatrac implements PromatracSlijeda {
    private final PrintStream outputStream;

    public BiljezeciPromatrac(String putanja) throws FileNotFoundException {
        this.outputStream = new PrintStream(new FileOutputStream(putanja));
    }

    @Override
    public void obradi(Collection<Integer> brojevi) {
        outputStream.print('[');
        outputStream.print(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        outputStream.print(']');

        brojevi.forEach(b -> {
            outputStream.print(' ');
            outputStream.print(b);
        });

        outputStream.println();
    }
}
