import java.util.Collection;

public class ZbrajajuciPromatrac implements PromatracSlijeda {
    @Override
    public void obradi(Collection<Integer> brojevi) {
        System.out.println(brojevi.stream()
                                  .mapToInt(b -> b)
                                  .sum());
    }
}
