package com.mxy.My2048;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 模拟对话框
 */
public class Dialog extends Activity {
    private final int RESTART = 0;
    private final int EXIT = 1;
    private final int COMPLETE = 2;
    private TextView tv_title;
    private TextView tv_left;
    private TextView tv_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        switch (intent.getIntExtra("type",-1)) {
            case RESTART:
                setContentView(R.layout.dialog);
                tv_title = (TextView) findViewById(R.id.tv_title);
                tv_left = (TextView) findViewById(R.id.tv_left);
                tv_right = (TextView) findViewById(R.id.tv_right);
                tv_title.setText("Restart?");
                tv_left.setText("Yeah");
                tv_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyActivity.getMyActivity().gameRestart();
                        onBackPressed();
                    }
                });
                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                break;
            case EXIT:
                setContentView(R.layout.dialog);
                tv_title = (TextView) findViewById(R.id.tv_title);
                tv_left = (TextView) findViewById(R.id.tv_left);
                tv_right = (TextView) findViewById(R.id.tv_right);
                tv_title.setText("Leave game?");
                tv_left.setText("Exit");
                tv_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        MyActivity.getMyActivity().finish();
                    }
                });
                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                break;
            case COMPLETE:
                setContentView(R.layout.complete);
                TextView tv_again = (TextView) findViewById(R.id.tv_again);
                TextView tv_finalscore = (TextView) findViewById(R.id.tv_finalscore);
                String finalscore = MyActivity.getMyActivity().getScore();
                tv_finalscore.setText("Your score " + finalscore + " points");
                tv_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyActivity.getMyActivity().gameRestart();

                        onBackPressed();
                    }
                });
                break;
            default:
                break;
        }
    }

}
