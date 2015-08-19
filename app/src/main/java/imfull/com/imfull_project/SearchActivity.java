package imfull.com.imfull_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by LeeAh on 15. 8. 6..
 */
public class SearchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    /*
        Close  Method
     */
    public void closeActivity(){
        finish();
//        Intent intent = new Intent();
//        intent.setClass(this, SearchActivity.class);
//        startActivity(intent);
        overridePendingTransition(R.anim.bottom_to_visible, R.anim.bottom_to_invisible);
    }



    /*
        Button Click  Method
     */
    public void btnClick(View v){
        switch ( v.getId() ){
            case R.id.bt_close:
                closeActivity();
                break;
            default:
        }
    }
}
