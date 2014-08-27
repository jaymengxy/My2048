package com.mxy.My2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.io.*;

public class MyActivity extends Activity {
    private int score = 0;
    private int maxscore;
    private TextView tv_score;
    private TextView tv_max;
    private AlertDialog dialog;
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
        readScore();
        maxscore = Integer.parseInt(tv_max.getText().toString());
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
        if (score > maxscore) {
            maxscore = score;
            tv_max.setText("+" + s);
            tv_max.startAnimation(aa_begin);
            aa_begin.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
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

    public void saveScore() {
        File file = new File(getFilesDir(), "/maxscore.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((maxscore + "").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readScore() {
        File file = new File(getFilesDir(), "/maxscore.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                //把字节流转换成字符流
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String maxscore = br.readLine();
                //把最高分显示
                tv_max.setText(maxscore + "");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        saveScore();
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
        Intent intent = new Intent(this, Dialog.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }
}
