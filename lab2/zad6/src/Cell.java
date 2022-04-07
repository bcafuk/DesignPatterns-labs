import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cell {
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("\\A([A-Z]+)([0-9]+)\\z");

    private String exp;
    private Expression parsedExp;
    private List<Cell> referencedCells = List.of();
    private Integer value;

    private final Sheet sheet;
    private final CellObserver ownObserver = (c) -> this.evaluate();

    private final List<CellObserver> observers = new LinkedList<>();

    public record CellAddress(int column, int row) {}

    public Cell(Sheet sheet) {
        this.sheet = Objects.requireNonNull(sheet);

        setExp("0");
    }

    public int getValue() {
        if (value == null)
            evaluate();

        return value;
    }

    public void setExp(String exp) {
        Expression parsedExp = new ExpressionParser(Objects.requireNonNull(exp)).parse();

        List<Cell> referencedCells = parsedExp.getReferences()
                                              .stream()
                                              .map(sheet::cell)
                                              .toList();

        if (referencedCells.contains(this))
            throw new CircularReferenceException();

        Queue<Cell> queue = new LinkedList<>(referencedCells);
        Set<Cell> visited = new HashSet<>(referencedCells);

        while (!queue.isEmpty()) {
            Cell cell = queue.remove();

            for (Cell reference : cell.getReferences()) {
                if (reference == this)
                    throw new CircularReferenceException();

                if (visited.contains(reference))
                    continue;

                visited.add(reference);
                queue.add(reference);
            }
        }

        for (Cell referencedCell : this.referencedCells)
            referencedCell.removeObserver(ownObserver);

        for (Cell referencedCell : referencedCells)
            referencedCell.addObserver(ownObserver);

        this.exp = exp;
        this.parsedExp = parsedExp;
        this.referencedCells = referencedCells;
        this.value = null;

        notifyObservers();
    }

    private void evaluate() {
        Integer oldValue = value;
        value = this.parsedExp.evaluate(sheet);

        if (oldValue == null || !oldValue.equals(value))
            notifyObservers();
    }

    public List<Cell> getReferences() {
        return Collections.unmodifiableList(referencedCells);
    }

    public void addObserver(CellObserver observer) {
        this.observers.add(Objects.requireNonNull(observer));
    }

    public boolean removeObserver(CellObserver observer) {
        return this.observers.remove(observer);
    }

    private void notifyObservers() {
        for (CellObserver observer : observers) {
            observer.updated(this);
        }
    }

    public static CellAddress parseAddress(String address) {
        Objects.requireNonNull(address);

        Matcher matcher = ADDRESS_PATTERN.matcher(address);

        if (!matcher.matches())
            throw new IllegalArgumentException(address + " is not a valid cell address");

        int column = columnNameToIndex(matcher.group(1));
        int row = Integer.parseUnsignedInt(matcher.group(2)) - 1;

        return new CellAddress(column, row);
    }

    private static int columnNameToIndex(String columnName) {
        Objects.requireNonNull(columnName);

        if (columnName.isEmpty())
            throw new IllegalArgumentException("A column name can not be empty");

        long index = 0;

        for (int i = 0; i < columnName.length(); i++) {
            char c = columnName.charAt(i);

            if (c < 'A' || c > 'Z')
                throw new IllegalArgumentException("The character '" + c + "' is not valid as a column name");

            // Digit ranges from 1 to 26, not from 0 to 25
            int digit = c - 'A' + 1;
            index = index * ('Z' - 'A' + 1) + digit;

            if (index > Integer.MAX_VALUE)
                throw new NumberFormatException("The column name " + columnName + " is beyond the range of valid column names");
        }

        return (int) (index - 1);
    }
}
