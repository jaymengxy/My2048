package com.mxy.My2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private int score = 0;
    private int maxscore;
    private TextView tv_score;
    private TextView tv_max;


    //单例
    private static MyActivity myActivity = null;
    public static MyActivity getMyActivity(){
        return myActivity;
    }
    public MyActivity(){
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
    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore() {
        tv_score.setText(score+"");
    }
    public void maxScore(){
        tv_max.setText(maxscore+"");
    }
    public void addScore(int s){
        score+=s;
        showScore();
        if(score>maxscore)
        {
            maxscore = score;
            maxScore();
        }
    }

    public void saveScore() {
        File file = new File(getFilesDir(), "/maxscore.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((maxscore+"").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readScore(){
        File file = new File(getFilesDir(), "/maxscore.txt");
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                //把字节流转换成字符流
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String maxscore = br.readLine();
                //把最高分显示
                tv_max.setText(maxscore+"");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void restart(View view) {
        Button bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView gv = (GameView) findViewById(R.id.gv);
                gv.startGame();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveScore();
    }
}
