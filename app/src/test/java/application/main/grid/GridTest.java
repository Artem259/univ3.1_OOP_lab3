package application.main.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import application.main.common.Coord;

public class GridTest {
    @Test
    public void freePathSearching() {
        Grid grid = new Grid(5, 5);
        for (int i=0; i<grid.getRows(); i++) {
            for (int j=0; j<grid.getColumns(); j++) {
                grid.setCellAt(new Coord(i, j), 1);
            }
        }

        /*
        1 1 0 0 1
        1 0 0 1 0
        0 0 1 0 0
        1 0 0 1 0
        1 1 0 0 0
         */
        grid.setCellAt(new Coord(0, 2), 0);
        grid.setCellAt(new Coord(0, 3), 0);
        grid.setCellAt(new Coord(1, 1), 0);
        grid.setCellAt(new Coord(1, 2), 0);
        grid.setCellAt(new Coord(1, 4), 0);
        grid.setCellAt(new Coord(2, 0), 0);
        grid.setCellAt(new Coord(2, 1), 0);
        grid.setCellAt(new Coord(2, 3), 0);
        grid.setCellAt(new Coord(2, 4), 0);
        grid.setCellAt(new Coord(3, 1), 0);
        grid.setCellAt(new Coord(3, 2), 0);
        grid.setCellAt(new Coord(3, 4), 0);
        grid.setCellAt(new Coord(4, 2), 0);
        grid.setCellAt(new Coord(4, 3), 0);
        grid.setCellAt(new Coord(4, 4), 0);

        ArrayList<Coord> path = new ArrayList<>();
        path.add(new Coord(4, 4));
        path.add(new Coord(4, 3));
        path.add(new Coord(4, 2));
        path.add(new Coord(3, 2));
        path.add(new Coord(3, 1));
        path.add(new Coord(2, 1));
        path.add(new Coord(1, 1));
        path.add(new Coord(1, 2));
        path.add(new Coord(0, 2));
        path.add(new Coord(0, 3));

        assertEquals(path, grid.pathFindingByRelated(new Coord(4, 4), new Coord(0, 3)));
        Collections.reverse(path);
        assertEquals(path, grid.pathFindingByRelated(new Coord(0, 3), new Coord(4, 4)));

        path.clear();
        path.add(new Coord(3, 2));
        assertEquals(path, grid.pathFindingByRelated(new Coord(3, 2), new Coord(3, 2)));

        path.clear();
        path.add(new Coord(3, 2));
        path.add(new Coord(4, 2));
        assertEquals(path, grid.pathFindingByRelated(new Coord(3, 2), new Coord(4, 2)));
        Collections.reverse(path);
        assertEquals(path, grid.pathFindingByRelated(new Coord(4, 2), new Coord(3, 2)));
    }
}
