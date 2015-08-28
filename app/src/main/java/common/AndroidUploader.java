package common;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AndroidUploader {
    private String url;
    static String CRLF = "\r\n";
    static String twoHyphens = "--";
    static String boundary = "*****b*o*u*n*d*a*r*y*****";
    private DataOutputStream dataStream = null;

    public enum ReturnCode {noPicture, unknown, http201, http400, http401, http403, http404, http500}
    /**
     * http201 : 요청 수행 성공 및 새로운 리소스 작성
     * http400 : 잘못된 접근
     * http401 : 권한 없음(로그인 기능 필요)
     * http403 : 금지됨(서버가 요청을 거부)
     * http404 : 찾을 수 없음(서버가 요청한 페이지를 찾을 수 없다)
     * http500 : 내부 서버 오류(서버에 오류 발생)
     */
    private String TAG;

    public AndroidUploader(String url) {
        this.url = url;
        this.TAG = this.getClass().getName();
    }

    public ReturnCode uploadForm( List list ) {
        UploadAsync async = new UploadAsync();
        async.execute( list );
        return null;
    }

    private void writeFormField(String fieldName, Object fieldValue, boolean isString) {
        try {
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            if(isString) {
                dataStream.writeBytes((String)fieldValue);
            }else{
                dataStream.writeByte((Integer)fieldValue);
            }
            dataStream.writeBytes(CRLF);
        } catch (Exception e) {
            //System.out.println("AndroidUploader.writeFormField: got: " + e.getMessage());
            Log.d(TAG, "AndroidUploader.writeFormField: " + e.getMessage());
        }
    }

    private void writeFileField(
            String fieldName,
            String fieldValue,
            String type,
            FileInputStream fis) {
        try {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);
            dataStream.writeBytes("Content-Type: " + type + CRLF);
            dataStream.writeBytes(CRLF);

            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }
            // closing CRLF
            dataStream.writeBytes(CRLF);
        } catch (Exception e) {
            //System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            Log.d(TAG, "AndroidUploader.writeFileField: got: " + e.getMessage());
        }
    }

    public class UploadAsync extends AsyncTask<List, Void, ReturnCode> {
        String TAG;
        URL connectURL;
        HttpURLConnection conn;

        public UploadAsync() {
            this.TAG = this.getClass().getName();
        }

        public ReturnCode init() {
            try {
                connectURL = new URL(url);
                Log.d(TAG, "URL : " + url);
                conn = (HttpURLConnection) connectURL.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("User-Agent", "myFileUploader");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.connect();
                dataStream = new DataOutputStream(conn.getOutputStream());
            } catch (MalformedURLException mue) {
                return ReturnCode.http400;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                return ReturnCode.http500;
            }
            return null;
        }

        @Override
        protected ReturnCode doInBackground(List... params){
            if (params != null) {
                init();
                try { setData( params[0] ); } catch (IOException e) { Log.d(TAG, "setData 에러"); }
                Log.d(TAG, "***********전송완료***********");
                String response = getResponse(conn);
                try { Log.d(TAG, "responseCode : " + conn.getResponseCode() ); }
                catch (IOException e) { Log.d(TAG, " 응답코드 오류 "); }
                if ( response.contains("uploaded successfully")) { return ReturnCode.http201; }
                else { return ReturnCode.http401; }
            }
            return null;
        }

        private void setData(List<ArrayList<String>> list) throws IOException {
            Log.d("AndroidUploader-tag", "" + list.size());
            setTextForm(list.get(0));
            setTagForm(list.get(1));
            setFileForm(list.get(2));
        }
        private void setTextForm(ArrayList<String> textData) {
            writeFormField("writer", textData.get(0),true);
            Log.d("AndroidUploader", textData.get(0));
            writeFormField("title", textData.get(1),true);
            Log.d("AndroidUploader", textData.get(1));
            writeFormField("shopName", textData.get(2),true);
            Log.d("AndroidUploader", textData.get(2));
            writeFormField("address", textData.get(3),true);
            Log.d("AndroidUploader", textData.get(3));
            writeFormField("phone", textData.get(4),true);
            Log.d("AndroidUploader", textData.get(4));
            writeFormField("star", textData.get(5),true);
            Log.d("AndroidUploader", textData.get(5));
        }
        private void setTagForm(ArrayList<String> tagData) {
            for (int i = 0; i < tagData.size(); i++) {
                writeFormField("tag", tagData.get(i),true);
                Log.d("AndroidUploader-tag", tagData.get(i));
    }
}
        private void setFileForm(ArrayList<String> filePath) throws IOException {
            for(int i=0;i<filePath.size();i++){
                Log.d("AndroidUploader-photo", filePath.get(i));
            }

            FileInputStream fileInputStream = null;
            for (int i = 0; i < filePath.size(); i++) {
                Log.d(TAG, "filePath : " + filePath.get(i) );

                File uploadFile = new File(filePath.get(i));
                Log.d("AndroidUploader-photo", "파일 겟 "+uploadFile.exists());
                fileInputStream = new FileInputStream(uploadFile);
                Log.d("AndroidUploader-photo", "스트림 겟");
                writeFileField("image", filePath.get(i), "image/jpg", fileInputStream);
                Log.d("AndroidUploader-photo", "파일 전송");
           }
            Log.d("DataStream",dataStream+"");
            dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);
            fileInputStream.close();
            dataStream.flush();
            dataStream.close();
            dataStream = null;
            Log.d("AndroidUploader-photo", "스트림 종료");
        }
        private String getResponse(HttpURLConnection conn) {
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                byte[] data = new byte[1024];
                int len = dis.read(data, 0, 1024);

                dis.close();

                int responseCode = conn.getResponseCode();
                if (len > 0)
                    return new String(data, 0, len);
                else
                    return "";
            } catch (IOException e) {
                //System.out.println("AndroidUploader: "+e);
                Log.d(TAG, "AndroidUploader.getResponse() : " + e);
                return "";
            }
        }
    }

}