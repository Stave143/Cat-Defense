package com.example.cade;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;
    private double x;
    private double y;
    private int padding;
    private int ms;
    public Sprite(double x, double y, Bitmap bitmap, int AmountFrames, int ms, double frameTime) {
        this.x = x;
        this.y = y;

        this.bitmap = bitmap;

        this.frames = new ArrayList<Rect>();
        this.frameWidth = bitmap.getWidth() / AmountFrames;
        this.frameHeight = bitmap.getHeight();
        for(int i = 0; i < AmountFrames; i++){
            Rect initFrame = new Rect(frameWidth  * i + 2, 0, frameWidth * (i + 1), frameHeight);
            this.frames.add(initFrame);
        }

        this.timeForCurrentFrame = 0.0;
        this.currentFrame = 0;
        this.padding = 0;
        this.ms = ms;
        this.frameTime = frameTime;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setPadding(int padding){
        this.padding = padding;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.frameWidth = bitmap.getWidth() / this.frames.size();
        this.frameHeight = bitmap.getHeight();
        for(int i = 0; i < this.frames.size(); i++){
            Rect initFrame = new Rect(frameWidth  * i + 2, 0, frameWidth * (i + 1), frameHeight);
            this.frames.set(i, initFrame);
        }
    }

    public void update () {
        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame -= frameTime;
        }
    }
    public void draw (Canvas canvas) {
        Paint p = new Paint();
        Rect destination = new Rect((int)(x - frameWidth / 2), (int)(y - frameHeight / 2), (int)(x + frameWidth / 2), (int)(y + frameHeight / 2));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  p);
    }
    public Rect getBoundingBoxRect () {
        return new Rect((int)(x - frameWidth / 2) + padding, (int)(y - frameHeight / 2), (int)(x + frameWidth / 2) - padding, (int)(y + frameHeight / 2));
    }

    public boolean intersect (Sprite s) {
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect());
    }
}
