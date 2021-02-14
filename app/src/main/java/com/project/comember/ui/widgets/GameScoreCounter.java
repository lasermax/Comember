package com.project.comember.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class GameScoreCounter extends AppCompatTextView {

    int mScore = 0;

    public GameScoreCounter(@NonNull Context context) {
        this(context, null);
    }

    public GameScoreCounter(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameScoreCounter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setText(Integer.toString(mScore));
    }

    public void increment() {
        this.setText(++mScore);
    }

    public int getScore() {
        return mScore;
    }
}
