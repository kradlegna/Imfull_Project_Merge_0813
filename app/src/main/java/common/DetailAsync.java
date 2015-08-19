
package common;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import imfull.com.imfull_project.DetailActivity;

public class DetailAsync extends AsyncTask<String, Void, String> {
    String TAG;

    DetailActivity activity;

    public DetailAsync(DetailActivity activity) {
        this.activity   = activity;
        this.TAG        = this.getClass().getName();
    }

    /*쓰레드 수행전 : main Thread - UI제어가능*/
    protected void onPreExecute() {
        /*주로 값의 초기화 및 웹연동 일경우 프로그래바의 시작에 적절...*/
    }

    /*쓰레드 수행 */
    /*가변형 인자란?  호출자가 인수의 갯수를 결정할 수 있는 매개변수의
    정의 기법, 즉 매개변수의 갯수는 호출시 결정된다!!*/
    protected String doInBackground(String... params) {
        String data = null;
        try {
            Log.d(TAG,"doInBackground, params[0] : " + params[0]);
            data = downloadUrl(params[0],params[1]);
            //publishProgress();
        } catch (IOException e) {
            Log.d(TAG,"downloadUrl IOException");
        }
        return data;
    }

    /*쓰레드 수행 중 UI제어*/
    protected void onProgressUpdate(Void... values) {
        /*프로그래스바의 진행 상태 적용에 적절*/
    }


    /*쓰레드 수행 후*/
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute : " + s);
        /* 웹서버로부터 전달받은 데이터를 ListView의 출력해보자!!*/
        ArrayList<ArrayList<String>> list=null;
        try {
            JSONObject jsonObject = new JSONObject(s);

            Log.d("+++++ : " + TAG, "s : " + s);


            list = new  ArrayList<ArrayList<String>>();

            ArrayList<String> list1 = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();
            ArrayList<String> list3 = new ArrayList<String>();

            list.add(list1);
            list.add(list2);
            list.add(list3);

            JSONArray array = (JSONArray) jsonObject.get("data");

            for(int i=0;i<array.length();i++){
                for(int a=0;a<array.getJSONArray(i).length();a++){
                    list.get(i).add((String)(array.getJSONArray(i).get(a)));
                }

            }

        }
        catch (JSONException e) { e.printStackTrace(); }
        /*프로그래스바의 완료에 적절*/

        activity.setList(list);
    }

    /* 웹서버에 접속하여 스트림을 연결한 후, 그 데이터 읽어오기!!*/
    private String downloadUrl(String myurl,String idx) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl+"?idx="+"3");//+idx);
            Log.d(TAG, "downloadUrl : " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "downloadUrl - conn " + conn);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            Log.d(TAG,"downloadUrl : connect");
            Log.d(TAG, "downloadUrl - conn : " + conn.getURL() );

            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is : " + response);

            is = conn.getInputStream();


            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally { if (is != null) { is.close(); } }
    }

    public String readIt(InputStream stream) throws IOException {
        BufferedReader buffr = null;
        buffr = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String data = null;
        while ((data = buffr.readLine()) != null) {
            sb.append(data);
        }
        return sb.toString();
    }

}


