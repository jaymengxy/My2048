package com.mxy.My2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by SinTi on 2014/8/11.
 * 自定义卡片空间 显示数字 并进行移动
 */
public class Card extends FrameLayout {

    private TextView tv;
    private int num = 0;
    private View background;

    //构造函数初始化卡片
    public Card(Context context) {
        super(context);
        LayoutParams lp = null;

        lp = new LayoutParams(-1, -1);
        background = new View(getContext());
        lp.setMargins(10, 10, 0, 0);
        background.setBackgroundColor(0x33ffffff);
        addView(background, lp);


        tv = new TextView(getContext());
        tv.setTextSize(28);
        tv.setTextColor(0x33000000);
        tv.setGravity(Gravity.CENTER);
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(tv, lp);


        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        //如果num小于等于0，那么卡片上显示一个空字符串，如果不为空则显示相应的数字
        if (num <= 0) {
            tv.setText("");
        } else {
            tv.setText(num + "");
        }
        //根据数字不同显示不同的颜色
        switch (num) {
            case 0:
                tv.setBackgroundColor(0x00000000);
                break;
            case 2:
                tv.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                tv.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                tv.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                tv.setBackgroundColor(0xfff59563);
                break;
            case 32:
                tv.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                tv.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                tv.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                tv.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                tv.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                tv.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                tv.setBackgroundColor(0xffedc22e);
                break;
            default:
                tv.setBackgroundColor(0xff3c3a32);
                break;
        }
    }

    public boolean equals(Card c) {
        return getNum() == c.getNum();
    }
}
