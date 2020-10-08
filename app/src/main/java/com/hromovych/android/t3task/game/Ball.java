package com.hromovych.android.t3task.game;

import android.widget.Button;

public class Ball {

    public static final int DEFAULT_BALL = 0;
    public static final int DEATH_BALL = 1;
    public static final int TIME_BALL = 2;

    private Button view;
    private int type;
    private int value;

    public Ball(Button view, int type, int value) {
        this.view = view;
        this.type = type;
        this.value = value;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
