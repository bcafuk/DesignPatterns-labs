package hr.fer.zemris.ooup.lab4.geometry;

public record Point(int x, int y) {
    public Point translate(Point dp) {
        return new Point(x + dp.x, y + dp.y);
    }

    public Point difference(Point p) {
        return new Point(x - p.x, y - p.y);
    }
}
