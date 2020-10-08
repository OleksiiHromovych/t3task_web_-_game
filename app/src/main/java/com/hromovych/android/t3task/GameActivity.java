package com.hromovych.android.t3task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hromovych.android.t3task.game.Ball;

import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BEST_SCORE = "best score";

    private int screenWidth;
    private int screenHeight;
    private RelativeLayout gameBoard;

    private int[] ballsColors;
    private int[] ballsTextColors;
    private Ball[] ballsArray;

    private TextView bestScoreView;
    private TextView scoreView;
    private TextView timeView;

    private CountDownTimer mCountDownTimer;

    private int current_score;
    private int best_score;
    private long current_time;
    private boolean mTimerRunning;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, GameActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = (RelativeLayout) findViewById(R.id.relGameBoard);
        bestScoreView = (TextView) findViewById(R.id.best_score);
        scoreView = (TextView) findViewById(R.id.score);
        timeView = (TextView) findViewById(R.id.time);

        final Button startBtn = (Button) findViewById(R.id.startButton);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                startBtn.setVisibility(View.GONE);
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        ballsColors = getResources().getIntArray(R.array.ballsBackground);
        ballsTextColors = getResources().getIntArray(R.array.ballsText);

        gameBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_score -= 1;
                updateScore();
            }
        });


        ballsArray = new Ball[10];
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(MainActivity.GET_SHARED_PREFERENCES, MODE_PRIVATE);
        best_score = prefs.getInt(BEST_SCORE, 0);
        bestScoreView.setText(getString(R.string.bestScore, best_score));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(MainActivity.GET_SHARED_PREFERENCES, MODE_PRIVATE);
        prefs.edit().putInt(BEST_SCORE, best_score).apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

    }

    private void startGame() {
        current_score = 0;
        current_time = (long) (0.7 * 60000);

        startTimer();

        updateScore();
    }

    @Override
    public void onClick(View v) {
        Ball ball = ballsArray[v.getId()]; // id == index in array
        switch (ball.getType()) {
            case Ball.DEFAULT_BALL:
                current_score += ball.getValue();
                break;
            case Ball.DEATH_BALL:
                gameOver();
                break;
            case Ball.TIME_BALL:
                updateTimer(current_time + ball.getValue() * 1000);
                break;
            default:
                Toast.makeText(this, "Unknown ball type", Toast.LENGTH_SHORT).show();
        }
//        ball.getView().setVisibility(View.GONE);
        gameBoard.removeView(gameBoard.findViewById(v.getId()));
        updateScore();
    }

    private void updateScore() {
        scoreView.setText(getString(R.string.score, current_score));
    }

    private void gameOver() {
        gameBoard.removeAllViews();
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Game Over")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                })
                .create()
                .show();
        pauseTimer();
        if (current_score > best_score) {
            best_score = current_score;
            bestScoreView.setText(getString(R.string.bestScore, best_score));
        }
    }

    private Ball constructBall(int id) {
        Random random = new Random();
        int color_ind = random.nextInt(ballsColors.length);

        // circle shape
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(ballsColors[color_ind]);
        Button button = new Button(this);
        button.setBackground(shape);

        int rad = random.nextInt(350) + 100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rad, rad);
//        random location on screen
        lp.setMargins(random.nextInt(screenWidth - rad * 2), random.nextInt(screenHeight - rad * 2), 0, 0);
        button.setLayoutParams(lp);

        button.setId(id);
        button.setTextColor(ballsTextColors[color_ind]);
        button.setOnClickListener(this);

        int value = random.nextInt(5) + 1;
        //Construct Ball
        String text = String.valueOf(value);
        int type = Ball.DEFAULT_BALL;
        if (ballsColors[color_ind] == getResources().getColor(R.color.colorDeath)) {
            type = Ball.DEATH_BALL;
            value = -1;
            text = "☠";
        } else if (ballsColors[color_ind] == getResources().getColor(R.color.colorTime)) {
            type = Ball.TIME_BALL;
            value = random.nextInt(5) + 1;
            text = "♥";
        }
        button.setText(text);
        return new Ball(button, type, value);
    }

    private void addNewBalls() {
        Random random = new Random();
        int ind = random.nextInt(ballsArray.length);
        if (ballsArray[ind] != null && ballsArray[ind].getView().isAttachedToWindow()) {
            current_score -= ballsArray[ind].getValue();
            updateScore();
            gameBoard.removeView(gameBoard.findViewById(ballsArray[ind].getView().getId()));
        }
        ballsArray[ind] = constructBall(ind);
        gameBoard.addView(ballsArray[ind].getView());
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(current_time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                current_time = millisUntilFinished;
                updateCountDownText();
                addNewBalls();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                gameOver();
            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void updateTimer(long newTime) {
        pauseTimer();
        current_time = newTime;
        startTimer();
    }

    private void updateCountDownText() {
        int minutes = (int) ((current_time / 1000) % 3600) / 60;
        int seconds = (int) (current_time / 1000) % 60;
        String timeLeftFormatted;

        timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);

        timeView.setText(timeLeftFormatted);
    }
}


