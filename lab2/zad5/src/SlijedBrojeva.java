import java.util.*;

public class SlijedBrojeva {
    private final List<Integer> brojevi = new ArrayList<>();
    private final IzvorBrojeva izvorBrojeva;

    private final Collection<PromatracSlijeda> promatraci = new LinkedList<>();

    public SlijedBrojeva(IzvorBrojeva izvorBrojeva) {
        this.izvorBrojeva = izvorBrojeva;
    }

    public void kreni() {
        while (true) {
            int broj = izvorBrojeva.dohvatiBroj();

            if (broj == -1)
                break;

            brojevi.add(broj);
            obavijestiPromatrace();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Dretva prekinuta");
            }
        }
    }

    public void dodajPromatraca(PromatracSlijeda promatrac) {
        this.promatraci.add(promatrac);
    }

    public boolean ukloniPromatraca(PromatracSlijeda promatrac) {
        return this.promatraci.remove(promatrac);
    }

    private void obavijestiPromatrace() {
        List<Integer> nepromjenjiviBrojevi = Collections.unmodifiableList(brojevi);

        for (PromatracSlijeda promatrac : promatraci) {
            promatrac.obradi(nepromjenjiviBrojevi);
        }
    }
}
