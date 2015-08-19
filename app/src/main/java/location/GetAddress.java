package location;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import imfull.com.imfull_project.R;

public class GetAddress extends ActionBarActivity {

    TextView textview1;
    EditText edittext1, edittext2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        textview1 = (TextView) findViewById(R.id.textView1);
        edittext1 = (EditText) findViewById(R.id.editText1);
        edittext2 = (EditText) findViewById(R.id.editText2);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClick(View v){
        double lat, lng;
        String getDouble;
        switch(v.getId()){
            case R.id.button1:
                getDouble = edittext1.getText().toString();
                if(getDouble.equalsIgnoreCase("")){
                    Toast.makeText(GetAddress.this, "위도값을 입력하세요.", Toast.LENGTH_LONG).show();
                    break;
                }else{
                    lat = Double.parseDouble(getDouble);
                }
                getDouble = edittext2.getText().toString();
                if(getDouble.equalsIgnoreCase("")){
                    Toast.makeText(GetAddress.this, "경도값을 입력하세요.", Toast.LENGTH_LONG).show();
                    break;
                }else{
                    lng = Double.parseDouble(getDouble);
                }
                getLocation(lat, lng);
        }
    }

    public void getLocation(double lat, double lng){
        String str = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }

        textview1.setText(str);

    }

}