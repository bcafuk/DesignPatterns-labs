import java.util.List;

public class Sheet {
    private final int width;
    private final int height;

    private final Cell[][] cells;

    public Sheet(int width, int height) {
        this.width = width;
        this.height = height;

        this.cells = new Cell[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                this.cells[y][x] = new Cell(this);
    }

    public void set(String address, String expression) {
        cell(address).setExp(expression);
    }

    public void print() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(cells[y][x].getValue());
                System.out.print('\t');
            }
            System.out.println();
        }
    }

    public Cell cell(String address) {
        Cell.CellAddress ca = Cell.parseAddress(address);

        if (ca.column() >= width || ca.row() >= height) {
            throw new IllegalArgumentException("The address " + address + " is outside the sheet");
        }

        return cells[ca.row()][ca.column()];
    }

    public List<Cell> getrefs(Cell cell) {
        return cell.getReferences();
    }

    private int evaluate(Cell cell) {
        return cell.getValue();
    }
}
