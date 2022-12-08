package application.main.ui.listeners;

import android.view.View;

import application.main.ui.MainActivity;

public class ResetButtonListener implements View.OnClickListener {
    private final MainActivity mainActivity;

    public ResetButtonListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        mainActivity.reset();
    }
}
