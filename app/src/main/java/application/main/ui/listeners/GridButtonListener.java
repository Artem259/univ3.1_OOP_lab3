package application.main.ui.listeners;

import android.view.View;

import application.main.ui.MainActivity;

public class GridButtonListener implements View.OnClickListener {
    private final MainActivity mainActivity;

    public GridButtonListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        mainActivity.gridButtonClicked((int) view.getTag());
    }
}
