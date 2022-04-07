import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public class MedijanskiPromatrac implements PromatracSlijeda {
    @Override
    public void obradi(Collection<Integer> brojevi) {
        System.out.println(medijan(brojevi));
    }

    private double medijan(Collection<Integer> brojevi) {
        List<Integer> sortirani = brojevi.stream().sorted().toList();

        if (sortirani.size() == 0)
            throw new NoSuchElementException();

        if (sortirani.size() % 2 == 1) {
            return sortirani.get(sortirani.size() / 2);
        } else {
            return (sortirani.get(sortirani.size() / 2 - 1) + sortirani.get(sortirani.size() / 2)) * 0.5;
        }
    }
}
