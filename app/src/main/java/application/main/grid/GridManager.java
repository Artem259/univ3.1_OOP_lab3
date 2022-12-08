package application.main.grid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import application.main.common.Coord;
import application.main.common.GridAction;
import application.main.ui.MainActivity;

public class GridManager {
    private Grid grid;
    private Coord selectedCell;
    private int score;
    private final int[] nextColors;
    private final ArrayList<Coord> emptyCells;
    private ArrayList<Coord> lastPath;
    private ArrayList<Coord> lastDestroyed;
    private boolean gameIsOver;
    private static final int GRID_SIZE = 9;
    private static final int COLORS = 7;

    private void generateNextColors() {
        for (int i=0; i<3; i++) {
            nextColors[i] = ThreadLocalRandom.current().nextInt(1, COLORS + 1);
        }
    }

    public GridManager() {
        this.grid = new Grid(GRID_SIZE, GRID_SIZE);
        this.selectedCell = null;
        this.score = 0;
        this.nextColors = new int[3];
        this.emptyCells = new ArrayList<>();
        this.lastPath = new ArrayList<>();
        this.lastDestroyed = new ArrayList<>();
        this.gameIsOver = false;
    }

    public int getScore() {
        return score;
    }

    public int[] getNextColors() {
        return nextColors;
    }

    public Coord getSelectedCell() {
        return selectedCell;
    }

    public ArrayList<Coord> getLastPathCopy() {
        return new ArrayList<>(lastPath);
    }

    public ArrayList<Coord> getLastDestroyedCopy() {
        return new ArrayList<>(lastDestroyed);
    }

    public int getColorOfCellAt(Coord coord) {
        return grid.getCellAt(coord);
    }

    public boolean isGameIsOver() {
        return gameIsOver;
    }

    public void reset() {
        grid = new Grid(GRID_SIZE, GRID_SIZE);
        selectedCell = null;
        score = 0;
        generateNextColors();

        for (int i=0; i<GRID_SIZE; i++) {
            for (int j=0; j<GRID_SIZE; j++) {
                emptyCells.add(new Coord(i, j));
            }
        }

        lastPath = new ArrayList<>();
        lastDestroyed = new ArrayList<>();
        gameIsOver = false;
    }

    public GridAction gridButtonClicked(Coord coord) {
        if (Objects.equals(selectedCell, coord) || isGameIsOver()) {
            return GridAction.NONE;
        }
        if (selectedCell != null && grid.isEmptyAt(coord)) {
            lastPath = grid.pathFindingByRelated(selectedCell, coord);
            if (lastPath.isEmpty()) {
                return GridAction.NO_PATH;
            }

            emptyCells.add(selectedCell);
            emptyCells.remove(coord);

            int color = grid.getCellAt(selectedCell);
            grid.setCellAt(selectedCell, grid.getEmptyValue());
            selectedCell = null;
            lastDestroyed = grid.setCellAtDestroy(coord, color);
            if (lastDestroyed.isEmpty()) {
                return GridAction.MOVED;
            }

            score += lastDestroyed.size();
            emptyCells.addAll(lastDestroyed);
            return GridAction.MOVED_AND_DESTROYED;
        } else if (!grid.isEmptyAt(coord)) {
            selectedCell = coord;
            return GridAction.SELECTED;
        }
        return GridAction.NONE;
    }

    public Coord[] nextMove() {
        Coord[] newBallsPos = new Coord[3];
        for (int i=0; i<3; i++) {
            if (emptyCells.isEmpty()) {
                break;
            }
            int index = ThreadLocalRandom.current().nextInt(0, emptyCells.size());
            newBallsPos[i] = emptyCells.get(index);
            emptyCells.remove(index);
            lastDestroyed = grid.setCellAtDestroy(newBallsPos[i], nextColors[i]);
            score += lastDestroyed.size();
            emptyCells.addAll(lastDestroyed);
        }
        generateNextColors();
        if (emptyCells.isEmpty()) {
            gameIsOver = true;
            return newBallsPos;
        }

        return newBallsPos;
    }
}
