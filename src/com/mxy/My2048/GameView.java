package com.mxy.My2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.AlertDialog.Builder;

/**
 * Created by SinTi on 2014/8/9.
 * 自定义游戏区域控件
 */
public class GameView extends LinearLayout {

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private AlertDialog dialog;

    public GameView(Context context) {
        super(context);
        initGameView();
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    private void initGameView() {
        setOrientation(LinearLayout.VERTICAL);
//        setBackgroundColor(0xffbbada0);
        //设置触摸事件
        setOnTouchListener(new View.OnTouchListener() {
            // 定义触摸开始的X,Y坐标
            private float startX
                    ,
                    startY
                    ,
                    offsetX
                    ,
                    offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //根据触摸的开始和结束点的坐标，判断触摸时移动的方向
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h)-10) / 4;
        //定义卡片的宽高
        addCards(cardWidth, cardWidth);
        startGame();
    }

    public void startGame() {
        //游戏开始时清空得分
        MyActivity.getMyActivity().clearScore();
        /*//读取最高分
        MyActivity.getMyActivity().readScore();*/

        //在4*4的方格放16个卡片
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        //在这16个卡片中随机给两个添加数字
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {
        //清空集合
        emptyPoints.clear();
        //循环遍历所有的卡片，如果卡片上的数字小于等于0，将空白卡片加入集合中
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove(((int) (Math.random() * emptyPoints.size())));
        //在空白卡片添加数字2，或者4
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    //添加卡片
    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        LinearLayout line;
        LinearLayout.LayoutParams lineLp;
        for (int y = 0; y < 4; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LayoutParams(-1, cardHeight);
            addView(line,lineLp);
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                line.addView(c,cardWidth,cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    //向下滑
    private void swipeDown() {
        boolean merge = false;
        int addscore = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >=0; y--) {

                for (int y1 = y-1; y1 >=0; y1--) {
                    if (cardsMap[x][y1].getNum()>0) {
                        //同一列如果上面的卡片数字大于0，下面的卡片数字小于等于0，
                        if (cardsMap[x][y].getNum()<=0) {
                            //则将上面的卡片的数字设置给下面的卡片，
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            //同时将上面的卡片数字置为0
                            cardsMap[x][y1].setNum(0);

                            y++;

                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            //如果上下相邻的卡片数字相等，那么将下面的卡片的数字翻倍，
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            //同时将上面的卡片的数字置为0
                            cardsMap[x][y1].setNum(0);
                            //游戏得分增加
                            // MyActivity.getMyActivity().addScore(cardsMap[x][y].getNum());
                            addscore += cardsMap[x][y].getNum();
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }
        //将一次滑动所有的得分加上
        if (addscore != 0) {
            MyActivity.getMyActivity().addScore(addscore);
        }
        if (merge) {
            //在随机位置添加卡片，判断游戏是否结束
            addRandomNum();
            checkComplete();
        }
    }

    //向上划
    private void swipeUp() {
        boolean merge = false;
        int addscore = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y+1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum()>0) {
                        //同一列如果下面的卡片数字大于0，上面的卡片数字小于等于0，
                        if (cardsMap[x][y].getNum()<=0) {
                            //则将下面的卡片的数字设置给上面的卡片，
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            //同时将下面的卡片数字置为0
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            //如果上下相邻的卡片数字相等，那么将上面的卡片的数字翻倍，
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            //同时将下面的卡片的数字置为0
                            cardsMap[x][y1].setNum(0);
                            //游戏得分增加
                            // MyActivity.getMyActivity().addScore(cardsMap[x][y].getNum());
                            addscore += cardsMap[x][y].getNum();
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        //将一次滑动所有的得分加上
        if (addscore != 0) {
            MyActivity.getMyActivity().addScore(addscore);
        }
        if (merge) {
            //在随机位置添加卡片，判断游戏是否结束
            addRandomNum();
            checkComplete();
        }
    }

    //向右划
    private void swipeRight() {
        boolean merge = false;
        int addscore = 0;
        for(int y=0;y<4;y++)
        {
            for(int x=3;x>=0;x--)
            {
                for(int x1= x-1;x1>=0;x1--)
                {
                    if(cardsMap[x1][y].getNum()>0)
                    {
                        if(cardsMap[x][y].getNum()<=0)
                        {
                            //如果同一排左边的卡片数字大于0，右边的卡片数字小于等于0，则将左边的数字设置到右边的卡片，
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            //同时将左边的卡片数字设置为0
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;

                        }
                        else if(cardsMap[x][y].equals(cardsMap[x1][y]))
                        {
                            //如果同一排左右相邻的卡片数字相等，则将右边的卡片数字翻倍，
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            //同时将左边的卡片数字设置为0
                            cardsMap[x1][y].setNum(0);

                            //游戏得分增加
                            // MyActivity.getMyActivity().addScore(cardsMap[x][y].getNum());
                            addscore += cardsMap[x][y].getNum();
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        //将一次滑动所有的得分加上
        if (addscore != 0) {
            MyActivity.getMyActivity().addScore(addscore);
        }
        if(merge){
            //增加随机数字，检查是否结束
            addRandomNum();
            checkComplete();
        }
    }

    //向左划
    private void swipeLeft() {
        boolean merge = false;
        int addscore = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            //如果同一排左边的卡片数字小于等于0，右边的卡片数字大于0，则将右边的数字设置到左边的卡片上，
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            //同时将右边的卡片数字置为0
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            //如果左右的卡片数字相等，则将左边的卡片数字翻倍，右边的卡片数字置为0
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            //游戏得分增加
                            // MyActivity.getMyActivity().addScore(cardsMap[x][y].getNum());
                            addscore += cardsMap[x][y].getNum();
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        //将一次滑动所有的得分加上
        if (addscore != 0) {
            MyActivity.getMyActivity().addScore(addscore);
        }
        //如果进行了运算
        if(merge){
            //随机位置添加一个卡片
            addRandomNum();
            //检测游戏是否结束
            checkComplete();
        }
    }

    private void checkComplete() {
        boolean complete = true;
        //判断所有相邻卡片的数字状态，如果存在任意一个空白卡片 || 左右相邻存在数字相等的卡片 || 上下相邻存在相等的卡片，
        //则游戏不会结束，将complete标记设置为false
        a:for(int y= 0;y<4;y++){
            for(int x =0;x<4;x++){
                if(cardsMap[x][y].getNum()==0 ||
                        (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y])) ||
                        (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y])) ||
                        (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1])) ||
                        (y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))){
                    complete = false;
                    break a;
                }
            }
        }
        if(complete) {
            MyActivity.getMyActivity().complete();
            /*
            //对话框
            MyActivity.getMyActivity().saveScore();
            String finalscore = MyActivity.getMyActivity().getScore();
            Builder builder = new Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
            View view = View.inflate(getContext(), R.layout.complete,null);
            TextView tv_again = (TextView) view.findViewById(R.id.tv_again);
            TextView tv_finalscore = (TextView) view.findViewById(R.id.tv_finalscore);
            tv_finalscore.setText("Your score " + finalscore + " points");
            // tv_finalscore.setTextColor(Color.parseColor("#80ff0000"));
            tv_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame();
                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.setView(view,0,0,0,0);
            dialog.show();
            */
        }
    }
}
