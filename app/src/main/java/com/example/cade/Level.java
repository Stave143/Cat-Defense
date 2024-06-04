package com.example.cade;

import android.graphics.Bitmap;

public class Level extends Sprite{
    private int time = 0;
    private int EnemyGroup = 0;
    public Level(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap, 1, 0, 0);
    }
}
