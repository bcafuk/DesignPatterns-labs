package hr.fer.zemris.ooup.lab3.textEditor;

public record LocationRange(Location start, Location end) {
    public boolean isReverse() {
        if (start.line() < end.line())
            return false;

        if (start.line() > end.line())
            return true;

        return start.column() > end.column();
    }

    public Location left() {
        if (isReverse())
            return end;
        else
            return start;
    }

    public Location right() {
        if (isReverse())
            return start;
        else
            return end;
    }

    public LocationRange normalize() {
        if (isReverse())
            return new LocationRange(end, start);
        else
            return this;
    }
}
