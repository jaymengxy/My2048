package com.mxy.My2048;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;


public class MyActivity extends Activity {
    private int score = 0;
    private int maxscore;
    private TextView tv_score;
    private TextView tv_max;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    //单例
    private static MyActivity myActivity = null;

    public static MyActivity getMyActivity() {
        return myActivity;
    }

    public MyActivity() {
        myActivity = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_max = (TextView) findViewById(R.id.tv_max);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        maxscore = sp.getInt("maxscore", 0);
        maxScore();
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tv_score.setText(score + "");
    }

    public void maxScore() {
        tv_max.setText(maxscore + "");
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (maxscore > sp.getInt("maxscore", 0)) {
            editor = sp.edit();
            editor.putInt("maxscore", maxscore);
            editor.commit();
        }
    }

    public void addScore(int s) {
        score += s;
        tv_score.setText("+" + s);
        AlphaAnimation aa_begin = new AlphaAnimation(0.0f, 0.5f);
        aa_begin.setDuration(500);
        aa_begin.setFillAfter(false);
        tv_score.startAnimation(aa_begin);
        aa_begin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showScore();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (score >= maxscore) {
            maxscore = score;
            tv_max.setText("+" + s);
            tv_max.startAnimation(aa_begin);
            aa_begin.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    showScore();
                    maxScore();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public String getScore() {
        return tv_score.getText().toString();
    }

    public void restart(View view) {
        Intent intent = new Intent(this, Dialog.class);
        intent.putExtra("type", 0);
        startActivity(intent);
    }

    public void gameRestart() {
        GameView gv = (GameView) findViewById(R.id.gv);
        gv.startGame();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, Dialog.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            /*
            // 对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            View view = View.inflate(this, R.layout.exit,null);
            TextView tv_exit = (TextView) view.findViewById(R.id.tv_exit);
            TextView tv_nope = (TextView) view.findViewById(R.id.tv_nope);
            tv_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            tv_nope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.setView(view, 0, 0, 0, 0);
            dialog.show();
            */
            return true;
        }
        return false;
    }

    /**
     * 游戏结束
     */
    public void complete() {
        showScore();
        Intent intent = new Intent(this, Dialog.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

}
