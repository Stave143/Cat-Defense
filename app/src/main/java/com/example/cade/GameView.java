package com.example.cade;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View{
    private Level level;
    private Bitmap levelBitmap;
    private Bitmap MouseBitmap;
    private Bitmap CatBitmap;
    private Bitmap CatBitmapUpend;
    private Sprite Interface;
    private Bitmap InterfaceBitmap;
    private Number BodyNum;
    private Bitmap CatAtackBitmap;
    private Bitmap CatSeeBitmap;
    private Bitmap CatSeeBitmapUpend;
    private Bitmap PauseBitmap;
    private Bitmap LoseBitmap;
    private Bitmap WinBitmap;
    private Sprite Pause;
    private List<Enemy> EnemyList = new ArrayList<Enemy>();
    private List<Tower> TowerList = new ArrayList<Tower>();
    private List<Atack> AtackList = new ArrayList<Atack>();
    private List<Sprite> CatSeeList = new ArrayList<Sprite>();
    private List<Sprite> LoseAndWin = new ArrayList<Sprite>();
    private int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int viewHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private double FridgeX;
    private int UnplacedTowerIndex = -1;
    private final int timerInterval = 100;
    private int time = 0;
    private boolean pause = false;
    public GameView(Context context) {
        super(context);

        levelBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.room);
        level = new Level(viewWidth/ 2, viewHeight / 2, levelBitmap);

        FridgeX = levelBitmap.getWidth() - 650;

        MouseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mouse);

        CatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        CatBitmapUpend = BitmapFactory.decodeResource(getResources(), R.drawable.cat_upend);

        CatAtackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.atack);

        CatSeeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.see);
        CatSeeBitmapUpend = BitmapFactory.decodeResource(getResources(), R.drawable.see_upend);

        InterfaceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_interface);
        Interface = new Sprite(viewWidth - InterfaceBitmap.getWidth() / 8, viewHeight - InterfaceBitmap.getHeight() / 2, InterfaceBitmap, 4, timerInterval, 1000);

        BodyNum = new Number(viewWidth - 240, viewHeight - 175, 2);

        PauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        Pause = new Sprite(PauseBitmap.getWidth() / 2, PauseBitmap.getHeight() / 2, PauseBitmap,2, timerInterval, timerInterval);

        LoseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lose);
        WinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.win);

        Timer t = new Timer();
        t.start();
    }

    protected void update () {
        if(pause){
            return;
        }
        time++;

        for(int i = 0; i < EnemyList.size(); i++){
            EnemyList.get(i).update(0);
            if(EnemyList.get(i).getX() >= FridgeX){
                time = 1;
                EnemyList.clear();
                TowerList.clear();
                AtackList.clear();
                CatSeeList.clear();
                LoseAndWin.add(new Sprite(viewWidth / 2, viewHeight / 2, LoseBitmap, 1, timerInterval, timerInterval));
                pause = true;
                break;
            }
            if(EnemyList.get(i).getHP() <= 0){
                EnemyList.remove(i);
                BodyNum.setNumber(BodyNum.getNumber() + 1);
                if(BodyNum.getNumber() >= 100){
                    time = 1;
                    EnemyList.clear();
                    TowerList.clear();
                    AtackList.clear();
                    CatSeeList.clear();
                    LoseAndWin.add(new Sprite(viewWidth / 2, viewHeight / 2, WinBitmap, 1, timerInterval, timerInterval));
                    pause = true;
                    break;
                }
            }
        }
        for(int i = 0; i < TowerList.size(); i++){
            TowerList.get(i).update(0);
            if(!TowerList.get(i).isPlaced()){
                continue;
            }
            for(int j = 0; j < EnemyList.size(); j++){
                if(TowerList.get(i).getSee().intersect(EnemyList.get(j).getBoundingBoxRect())){
                    if(TowerList.get(i).getRest() == 0){
                        AtackList.add(TowerList.get(i).getAtack());
                        TowerList.get(i).setRest();
                        System.out.println(AtackList.size());
                    }
                }
            }
        }
        for(int i = 0; i < AtackList.size(); i++){
            AtackList.get(i).update(0);
            if(AtackList.get(i).getFrame() == 2){
                for(int j = 0; j < EnemyList.size(); j++){
                    if(AtackList.get(i).intersect(EnemyList.get(j))){
                        EnemyList.get(j).setHP(EnemyList.get(j).getHP() - AtackList.get(i).getDamage());
                    }
                }
            }
            if(AtackList.get(i).isAtackOver()){
                AtackList.get(i).setFrame(0);
                AtackList.remove(i);
            }
        }
        Interface.update();

        if(time % 50 == 0){
            EnemyList.add(new Enemy(5, MouseBitmap, 6, (level.getX() - levelBitmap.getWidth() / 2) + levelBitmap.getWidth() / 13 + MouseBitmap.getWidth() / 12, viewHeight / 2, 100 + (time / 50 - 2) * 20, timerInterval));
        }

        invalidate();
    }

    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }
        @Override
        public void onFinish() {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < PauseBitmap.getWidth() && event.getY() < PauseBitmap.getHeight()){
                    Pause.update();
                    if(pause){
                        pause = false;
                    }
                    else {
                        pause = true;
                    }
                }
                if(!LoseAndWin.isEmpty()){
                    BodyNum.setNumber(2);
                    LoseAndWin.clear();
                    pause = false;
                }
                if(pause){
                    break;
                }
                if (event.getX() > Interface.getX() - InterfaceBitmap.getWidth() / 11 && event.getX() < Interface.getX() - InterfaceBitmap.getWidth() / 25 &&
                        event.getY() > Interface.getY() - InterfaceBitmap.getHeight() / 8 && event.getY() < viewHeight) {
                    TowerList.add(new Tower(CatBitmap, 8, event.getX(), event.getY(), timerInterval, false, new Atack(50, 0, 0, CatAtackBitmap, 5, timerInterval, 100), 8, 2));
                    CatSeeList.add(new Sprite(event.getX(), event.getY(), CatSeeBitmap, 2, timerInterval, 1000));
                    UnplacedTowerIndex = TowerList.size() - 1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(pause){
                    break;
                }
                if(UnplacedTowerIndex != -1) {
                    TowerList.get(UnplacedTowerIndex).setX(event.getX());
                    TowerList.get(UnplacedTowerIndex).setY(event.getY());
                    if(!TowerList.get(UnplacedTowerIndex).isUpend() && TowerList.get(UnplacedTowerIndex).getY() < viewHeight / 2){
                        TowerList.get(UnplacedTowerIndex).setBitmap(CatBitmapUpend);
                        TowerList.get(UnplacedTowerIndex).setUpend(true);
                    }
                    if(TowerList.get(UnplacedTowerIndex).isUpend() && TowerList.get(UnplacedTowerIndex).getY() > viewHeight / 2){
                        TowerList.get(UnplacedTowerIndex).setBitmap(CatBitmap);
                        TowerList.get(UnplacedTowerIndex).setUpend(false);
                    }
                }
                if(!CatSeeList.isEmpty()){
                    CatSeeList.get(0).setX(event.getX());
                    if(!TowerList.get(UnplacedTowerIndex).isUpend()) {
                        CatSeeList.get(0).setBitmap(CatSeeBitmap);
                        CatSeeList.get(0).setY(event.getY() - CatBitmap.getHeight() / 2 - CatSeeBitmap.getHeight() / 2);
                    }
                    else {
                        CatSeeList.get(0).setBitmap(CatSeeBitmapUpend);
                        CatSeeList.get(0).setY(event.getY() + CatBitmap.getHeight() / 2 + CatSeeBitmap.getHeight() / 2);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(pause) {
                    break;
                }
                if(UnplacedTowerIndex != -1) {
                    TowerList.get(UnplacedTowerIndex).setPlaced(true);
                    TowerList.get(UnplacedTowerIndex).setSee(CatSeeList.get(0).getBoundingBoxRect());
                    TowerList.get(UnplacedTowerIndex).setAtackXY(CatSeeList.get(0).getX(), CatSeeList.get(0).getY());
                    if (BodyNum.getNumber() < TowerList.get(UnplacedTowerIndex).getPrise() || TowerList.get(UnplacedTowerIndex).getBoundingBoxRect().intersect(0, viewHeight / 2, viewWidth, viewHeight / 2 + 1) ||
                            TowerList.get(UnplacedTowerIndex).getBoundingBoxRect().intersect((int) FridgeX, 0, viewWidth, viewHeight) ||
                            TowerList.get(UnplacedTowerIndex).getBoundingBoxRect().intersect(Interface.getBoundingBoxRect()) ||
                            TowerList.get(UnplacedTowerIndex).getBoundingBoxRect().intersect((int) level.getX() + (int) levelBitmap.getWidth() / 10 - CatBitmap.getWidth() / 16, 0, viewWidth, (int) ((level.getY() - levelBitmap.getHeight()) / 2 + levelBitmap.getHeight() / 3 + CatBitmap.getHeight() * 1.2)) ||
                            !(TowerList.get(UnplacedTowerIndex).getBoundingBoxRect().intersect((int) ((level.getX() - levelBitmap.getWidth() / 2) + levelBitmap.getWidth() / 13 + CatBitmap.getWidth() / 10),
                                    (int) (level.getY() - levelBitmap.getHeight()) / 2 + levelBitmap.getHeight() / 3 + CatBitmap.getHeight(), (int) (viewWidth),
                                    (int) (viewHeight + levelBitmap.getWidth()) / 2 - levelBitmap.getHeight() / 13 - CatBitmap.getHeight()))) {
                        TowerList.remove(UnplacedTowerIndex);
                    }
                    else {
                        TowerList.get(UnplacedTowerIndex).setPadding(CatBitmap.getWidth() / 32);
                        for(int i = 0; i < TowerList.size(); i++){
                            if(UnplacedTowerIndex == i){
                                continue;
                            }
                            if(TowerList.get(UnplacedTowerIndex).intersect(TowerList.get(i))){
                                TowerList.get(UnplacedTowerIndex).setPlaced(false);
                            }
                        }
                        if(TowerList.get(UnplacedTowerIndex).isPlaced()) {
                            BodyNum.setNumber(BodyNum.getNumber() - TowerList.get(UnplacedTowerIndex).getPrise());
                        }
                        else {
                            TowerList.remove(UnplacedTowerIndex);
                        }
                    }
                }
                if(!CatSeeList.isEmpty()){
                    CatSeeList.remove(0);
                }
                UnplacedTowerIndex = -1;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(250, 0, 0, 0);
        level.draw(canvas);
        Interface.draw(canvas);
        BodyNum.draw(canvas);
        Pause.draw(canvas);
        for(int i = 0; i < LoseAndWin.size(); i++){
            LoseAndWin.get(i).draw(canvas);
        }
        for(int i = 0; i < EnemyList.size(); i++){
            EnemyList.get(i).draw(canvas);
        }
        for(int i = 0; i < TowerList.size(); i++){
            TowerList.get(i).draw(canvas);
        }
        for(int i = 0; i < AtackList.size(); i++){
            AtackList.get(i).draw(canvas);
        }
        for(int i = 0; i < CatSeeList.size(); i++){
            CatSeeList.get(i).draw(canvas);
        }
    }
}

