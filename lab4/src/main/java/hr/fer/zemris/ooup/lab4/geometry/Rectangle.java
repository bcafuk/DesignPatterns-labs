package hr.fer.zemris.ooup.lab4.geometry;

public record Rectangle(int x, int y, int width, int height) {
    public int left() {return x;}

    public int right() {return x + width;}

    public int top() {return y;}

    public int bottom() {return y + height;}
}
