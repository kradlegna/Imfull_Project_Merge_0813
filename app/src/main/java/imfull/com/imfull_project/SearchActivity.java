
package imfull.com.imfull_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import common.BaseActivity;

/**
 * Created by LeeAh
 */
public class SearchActivity extends BaseActivity {
    ArrayList<Integer>       tagList   = new ArrayList<Integer>();
    HashMap<Integer, String> tagMap    = new HashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    /*
        Close  Method
     */
    public void closeActivity(boolean _searchFlag){
        Intent data = new Intent();
        // 닫기 버튼 클릭시 태그 리스트 초기화 처리
        if( !_searchFlag ){
            tagList.clear();
            tagMap.clear();
        }
        data.putExtra("tagList", tagList);
        data.putExtra("tagMap", tagMap);
        setResult(RESULT_OK, data);

        finish();
        overridePendingTransition(R.anim.bottom_to_visible, R.anim.bottom_to_invisible);
    }



    /*
     *  Tag Click  Method
     */
    public void tagClick(View v){
        TextView tagText = (TextView) v;
        int tagValue     = Integer.parseInt((tagText.getTag()).toString());

        if( tagText.getCurrentTextColor() == -8355712 ){
            // 선택되어 있지 않을 경우 선택 처리! (회색=>주황색)
            tagText.setTextColor(-235971);
            tagList.add(tagValue);
            tagMap.put(tagValue, (tagText.getText()).toString());
        }else{
            tagText.setTextColor(-8355712);
            tagList.remove(tagList.indexOf(tagValue));
            tagMap.remove(tagList.indexOf(tagValue));
        }
    }


    /*
     *  Button Click  Method
     */
    public void btnClick(View v){
        switch ( v.getId() ){
            case R.id.bt_close:
                closeActivity(false);
                break;
            case R.id.bt_search:
                closeActivity(true);
                break;
            default:
        }
    }


    /*
     *  하드웨어 Back key 이벤트  Method
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            closeActivity(false);
        }
        return false;
    }


}
