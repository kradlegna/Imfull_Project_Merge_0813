package common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseActivity extends Activity{
//    ArrayList<Activity> activityList = new ArrayList<Activity>();
    ArrayList<Activity> activityList = new ArrayList<Activity>();


    public void create(Activity currentActivity){
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);


        // 앱 실행시(Intro 화면) Activity 관리 변수 초기화 처리
        if( currentActivity.getClass().getSimpleName().equals("IntroActivity") ){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Activity", "");
            editor.commit();
        }


        String activity = prefs.getString("Activity", "");
        ArrayList<String> aListNumbers = new ArrayList<String>(Arrays.asList(activity));
        Log.d("****** [BaseActivity]", "aListNumbers : " + aListNumbers);


        activityList.add(currentActivity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Activity", activityList.toString());
        editor.commit();

        Log.d("****** [BaseActivity]", "Activity List Size : " + activityList.size());

        for( int i = 0; i < activityList.size(); i++ ){
            Log.d("****** [BaseActivity]", "Activity List index " + i + " : " + activityList.get(i).getClass().getSimpleName());
        }
    }

}
