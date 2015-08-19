package location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import imfull.com.imfull_project.R;

/**
 * Created by kircheis on 2015. 8. 11..
 */
public class LocationActivity extends Activity implements LocationListener{

    TextView txt_lati,txt_longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        txt_lati=(TextView)findViewById(R.id.txt_lati);
        txt_longi=(TextView)findViewById(R.id.txt_longi);

    }

    public void getLocation(){

        LocationManager locationManager=null;
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,this);

    }

    public void btnnClick(View view){
        getLocation();
    }
    /*5초마다 호출*/
    public void onLocationChanged(Location location) {
        double lati=location.getLatitude();     /*위도*/
        double longi=location.getLongitude();   /*경도*/

        txt_lati.setText("현재 위도는 "+lati);
        txt_longi.setText("현재 경도는 "+longi);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
