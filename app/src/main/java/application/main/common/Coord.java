package application.main.common;

import java.util.Objects;

public class Coord {
    private final int row;
    private final int col;

    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coord(Coord coord) {
        this.row = coord.row;
        this.col = coord.col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coord coord = (Coord) o;
        return row == coord.row && col == coord.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "{" + row + ", " + col + "}";
    }
}
