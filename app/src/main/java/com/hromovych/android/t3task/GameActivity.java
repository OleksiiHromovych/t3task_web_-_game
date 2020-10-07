package com.hromovych.android.t3task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int screenWidth;
    private int screenHeight;
    private int ballRadius = 40;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, GameActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        RelativeLayout gameBoard = (RelativeLayout) findViewById(R.id.relGameBoard);

        gameBoard.addView(constructBall());
        gameBoard.addView(constructBall());
        gameBoard.addView(constructBall());


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "tost", Toast.LENGTH_SHORT).show();
    }


    private ImageButton constructBall() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(getResources().getColor(R.color.colorPrimary));
        ImageButton imageButton = new ImageButton(this);
        imageButton.setBackground(shape);

        Random random = new Random();
        int rad = random.nextInt(350)+100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rad, rad);
        lp.setMargins(random.nextInt(screenWidth - rad), random.nextInt(screenHeight - rad), 0, 0);
        imageButton.setLayoutParams(lp);
        imageButton.setOnClickListener(this);
//        imageButton.setBackgroundColor(Color.TRANSPARENT);

        return imageButton;
    }
}
