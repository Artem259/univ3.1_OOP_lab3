package application.main.grid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import application.main.common.Coord;

public class Grid {
    private final int[][] field;
    private final int rows;
    private final int columns;
    private static final int EMPTY_VALUE = 0;

    public Grid(int rows, int columns) {
        this.field = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getCellAt(Coord coord) {
        return field[coord.row()][coord.col()];
    }

    public void setCellAt(Coord coord, int v) {
        field[coord.row()][coord.col()] = v;
    }

    public boolean isEmptyAt(Coord coord) {
        return (field[coord.row()][coord.col()] == EMPTY_VALUE);
    }

    public ArrayList<Coord> allNeighboursOf(Coord coord) {
        int row = coord.row();
        int col = coord.col();
        if ((row < 0 || row > rows-1) || (col < 0 || col > columns-1)) {
            throw new IllegalArgumentException();
        }

        ArrayList<Coord> res = new ArrayList<>();
        res.add(new Coord(row-1, col-1));
        res.add(new Coord(row-1, col));
        res.add(new Coord(row-1, col+1));
        res.add(new Coord(row, col+1));
        res.add(new Coord(row+1, col+1));
        res.add(new Coord(row+1, col));
        res.add(new Coord(row+1, col-1));
        res.add(new Coord(row, col-1));

        Iterator<Coord> iterator = res.iterator();
        while (iterator.hasNext()) {
            Coord neighbour = iterator.next();
            if ((neighbour.row() < 0 || neighbour.row() > rows-1)
                    || (neighbour.col() < 0 || neighbour.col() > columns-1)) {
                iterator.remove();
            }
        }

        return res;
    }

    public ArrayList<Coord> relatedNeighboursTo(Coord coord) {
        int row = coord.row();
        int col = coord.col();
        ArrayList<Coord> res = allNeighboursOf(coord);

        Iterator<Coord> iterator = res.iterator();
        while (iterator.hasNext()) {
            Coord neighbour = iterator.next();
            if (neighbour.row() != row && neighbour.col() != col) {
                iterator.remove();
            }
        }

        return res;
    }

    public ArrayList<Coord> pathFindingByRelated(Coord start, Coord finish) {
        if (field[start.row()][start.col()] != EMPTY_VALUE
                || field[finish.row()][finish.col()] != EMPTY_VALUE) {
            throw new IllegalArgumentException();
        }
        Coord[][] parents = new Coord[rows][columns];
        ArrayDeque<Coord> queue = new ArrayDeque<>();

        parents[start.row()][start.col()] = start;
        queue.add(start);

        while (!queue.isEmpty()) {
            Coord cell = queue.poll();
            assert cell != null;
            if (cell.equals(finish)) {
                break;
            }
            ArrayList<Coord> neighbours = relatedNeighboursTo(cell);

            for (Coord neighbour : neighbours) {
                if (field[neighbour.row()][neighbour.col()] == EMPTY_VALUE
                        && parents[neighbour.row()][neighbour.col()] == null) {
                    parents[neighbour.row()][neighbour.col()] = new Coord(cell);
                    queue.add(neighbour);
                }
            }
        }

        if (parents[finish.row()][finish.col()] == null) {
            return new ArrayList<>();
        }
        ArrayList<Coord> res = new ArrayList<>();
        for (Coord cell = finish; !cell.equals(start); cell = parents[cell.row()][cell.col()]) {
            res.add(cell);
        }
        res.add(start);

        Collections.reverse(res);
        return res;
    }
}
