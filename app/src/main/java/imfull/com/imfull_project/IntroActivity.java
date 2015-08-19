package imfull.com.imfull_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import common.BaseActivity;

public class IntroActivity extends BaseActivity{
    public static IntroActivity introActivity;

    ProgressBar progressBar;

    Handler     handler;
    Thread      thread;

    int         progressCnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introActivity = this;

        init();
    }



    /*
        Init  Method
     */
    public void init(){
        setUI();

        // selectData() 완료 후 setProgress() 처리
        setProgress();
    }


    /*
        set UI  Method
     */
    public void setUI(){
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    /*
        DB  Method
     */
    public void selectData(){

    }


    /*
        Progressbar Loading Method
     */
    public void setProgress(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("====progressCnt", "progressCnt :" + progressCnt);
                progressBar.setProgress(progressCnt);
                progressBar.invalidate();
            }
        };


        thread = new Thread(){
            @Override
            public void run() {
                try {


                    while(progressCnt<=100){
                        thread.sleep(20);
                        progressCnt++;
                        handler.sendEmptyMessage(0);
                    }

                    if( progressCnt > 100 ){
                        moveActivity();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    /*
        Move Activity  Method
     */
    public void moveActivity(){
/*
        MyApplication pref = new MyApplication(this);
        boolean useFlag = Boolean.valueOf(pref.get("useFlag"));

        Log.d("-----[IntroActivity]", "useFlag : " + useFlag);

        Intent intent = null;

        if( useFlag ){
            // rank Activity
            intent = new Intent(this, MainActivity.class);
        }else{
            // login Activity
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
*/

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}


