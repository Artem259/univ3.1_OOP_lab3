package application.main.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

import application.main.R;
import application.main.common.Coord;
import application.main.common.GridAction;
import application.main.grid.GridManager;
import application.main.ui.listeners.GridButtonListener;
import application.main.ui.listeners.ResetButtonListener;

public class MainActivity extends AppCompatActivity {
    private GridManager gridManager;
    private View[][] cells;
    private View[] nextColors;
    private TextView scoreView;
    private Coord selectedCell;
    private static final int GRID_SIZE = 9;

    private int getColorByNumber(int colorNumber) {
        int color;
        switch (colorNumber) {
            case 0:  {
                color = ContextCompat.getColor(getBaseContext(), R.color.grid_background); break;
            }
            case 1: color = ContextCompat.getColor(getBaseContext(), R.color.ball_1); break;
            case 2: color = ContextCompat.getColor(getBaseContext(), R.color.ball_2); break;
            case 3: color = ContextCompat.getColor(getBaseContext(), R.color.ball_3); break;
            case 4: color = ContextCompat.getColor(getBaseContext(), R.color.ball_4); break;
            case 5: color = ContextCompat.getColor(getBaseContext(), R.color.ball_5); break;
            case 6: color = ContextCompat.getColor(getBaseContext(), R.color.ball_6); break;
            case 7: color = ContextCompat.getColor(getBaseContext(), R.color.ball_7); break;
            default: throw new IllegalArgumentException();
        }
        return color;
    }

    private void setBallColor(Coord coord, int colorNumber) {
        int color = getColorByNumber(colorNumber);
        ((ImageView) cells[coord.row()][coord.col()]).setColorFilter(color);
    }

    private void setBackgroundColor(Coord coord, CellBackgroundStatus status) {
        int color = 0;
        switch (status) {
            case NONE: {
                color = ContextCompat.getColor(getBaseContext(), R.color.grid_background);
                break;
            }
            case SELECTED: {
                color = ContextCompat.getColor(getBaseContext(), R.color.selected_cell);
                break;
            }
            case GAME_OVER: {
                color = ContextCompat.getColor(getBaseContext(), R.color.game_over_cell);
                break;
            }
            case NO_PATH_ERROR: {
                color = ContextCompat.getColor(getBaseContext(), R.color.no_path_error);
                break;
            }
        }
        cells[coord.row()][coord.col()].setBackgroundColor(color);
    }

    public synchronized void updateScore() {
        scoreView.setText(String.format(Locale.getDefault(), "%d", gridManager.getScore()));
    }

    public synchronized void updateNextColors() {
        int[] colors = gridManager.getNextColors();
        for (int i=0; i<3; i++) {
            int color = getColorByNumber(colors[i]);
            ((ImageView) nextColors[i]).setColorFilter(color);
        }
    }

    public synchronized void updateSelectedCell() {
        if (selectedCell != null) {
            setBackgroundColor(selectedCell, CellBackgroundStatus.NONE);
        }
        selectedCell = gridManager.getSelectedCell();
        if (selectedCell != null) {
            setBackgroundColor(gridManager.getSelectedCell(), CellBackgroundStatus.SELECTED);
        }
    }

    public synchronized void reset() {
        for (int i=0; i<cells.length; i++) {
            for (int j=0; j<cells.length; j++) {
                setBallColor(new Coord(i, j), 0);
                setBackgroundColor(new Coord(i, j), CellBackgroundStatus.NONE);
            }
        }

        gridManager.reset();
        nextMove();

        updateSelectedCell();
        updateScore();
        updateNextColors();
    }

    public synchronized void nextMove() {
        int[] newColors = gridManager.getNextColors().clone();
        Coord[] newBallsPos = gridManager.nextMove();
        ArrayList<Coord> destroyed = gridManager.getLastDestroyedCopy();
        if (!destroyed.isEmpty()) {
            for (Coord c : destroyed) {
                setBallColor(c, 0);
            }
            updateScore();
        }
        for (int i=0; i<3; i++) {
            if (newBallsPos[i] != null) {
                setBallColor(newBallsPos[i], newColors[i]);
            }
        }
        if (gridManager.isGameIsOver()) {
            for (int i=0; i<cells.length; i++) {
                for (int j=0; j<cells.length; j++) {
                    setBackgroundColor(new Coord(i, j), CellBackgroundStatus.GAME_OVER);
                }
            }
        }
        updateNextColors();
    }

    public synchronized void gridButtonClicked(int tag) {
        int row = tag / GRID_SIZE;
        int col = tag % GRID_SIZE;

        GridAction action = gridManager.gridButtonClicked(new Coord(row, col));
        if (action == GridAction.NONE) {
            return;
        }

        if (action == GridAction.SELECTED) {
            updateSelectedCell();
        } else if (action != GridAction.NO_PATH) {
            int color = gridManager.getColorOfCellAt(new Coord(row, col));
            setBallColor(selectedCell, 0);
            setBallColor(new Coord(row, col), color);

            updateSelectedCell();

            if (action == GridAction.MOVED_AND_DESTROYED) {
                ArrayList<Coord> destroyed = gridManager.getLastDestroyedCopy();
                for (Coord c : destroyed) {
                    setBallColor(c, 0);
                }
                updateScore();
            } else {
                nextMove();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gridManager = new GridManager();
        this.cells = new View[GRID_SIZE][GRID_SIZE];
        this.nextColors = new View[3];
        this.scoreView = findViewById(R.id.score_number_textView);

        GridLayout grid = findViewById(R.id.grid);
        int count = grid.getChildCount();
        for (int i=0; i<count; i++) {
            View cell = grid.getChildAt(i);
            int row = i / GRID_SIZE;
            int col = i % GRID_SIZE;
            cells[row][col] = cell;
            cell.setTag(i);
            cell.setOnClickListener(new GridButtonListener(this));
        }

        nextColors[0] = findViewById(R.id.next_color_1);
        nextColors[1] = findViewById(R.id.next_color_2);
        nextColors[2] = findViewById(R.id.next_color_3);

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new ResetButtonListener(this));

        reset();
    }
}
