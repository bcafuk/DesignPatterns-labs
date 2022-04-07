import java.util.Collection;

public class UprosjecujuciPromatrac implements PromatracSlijeda {
    @Override
    public void obradi(Collection<Integer> brojevi) {
        System.out.println(brojevi.stream()
                                  .mapToInt(b -> b)
                                  .average()
                                  .orElseThrow());
    }
}
