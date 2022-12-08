package application.main.grid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import application.main.common.Coord;

public class Grid {
    private final int[][] field;
    private final int rows;
    private final int columns;
    private final int emptyValue = 0;

    public Grid(int rows, int columns) {
        this.field = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public int getEmptyValue() {
        return emptyValue;
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
        return (field[coord.row()][coord.col()] == emptyValue);
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

        res.removeIf(neighbour -> (neighbour.row() < 0 || neighbour.row() > rows - 1)
                || (neighbour.col() < 0 || neighbour.col() > columns - 1));

        return res;
    }

    public ArrayList<Coord> relatedNeighboursTo(Coord coord) {
        int row = coord.row();
        int col = coord.col();
        ArrayList<Coord> res = allNeighboursOf(coord);

        res.removeIf(neighbour -> (neighbour.row() != row && neighbour.col() != col));

        return res;
    }

    public ArrayList<Coord> pathFindingByRelated(Coord start, Coord finish) {
        if (field[finish.row()][finish.col()] != emptyValue) {
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
                if (field[neighbour.row()][neighbour.col()] == emptyValue
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

    public ArrayList<Coord> setCellAtDestroy(Coord coord, int v) {
        final int inRow = 5;
        field[coord.row()][coord.col()] = v;
        int rowV, colV;
        ArrayList<Coord> res = new ArrayList<>();

        for (int caseI=0; caseI<4; caseI++) {
            switch (caseI) {
                case 0: rowV = 0; colV = -1; break;
                case 1: rowV = -1; colV = -1; break;
                case 2: rowV = -1; colV = 0; break;
                case 3: rowV = -1; colV = 1; break;
                default: throw new RuntimeException();
            }

            int row = coord.row() + rowV;
            int col = coord.col() + colV;
            ArrayList<Coord> currentRes = new ArrayList<>();
            for (int i=0; i<2; i++) {
                while (row >=0 && row < rows && col >=0 && col < columns && field[coord.row()][coord.col()] == field[row][col]
                        && field[row][col] != emptyValue) {
                    if (!coord.equals(new Coord(row, col))) {
                        currentRes.add(new Coord(row, col));
                    }
                    row += rowV;
                    col += colV;
                }
                rowV *= -1;
                colV *= -1;
                row = coord.row() + rowV;
                col = coord.col() + colV;
            }

            if (currentRes.size() >= inRow-1) {
                res.addAll(currentRes);
            }
        }
        if (!res.isEmpty()) {
            res.add(coord);
            for (Coord c : res) {
                field[c.row()][c.col()] = emptyValue;
            }
        }
        return res;
    }
}
