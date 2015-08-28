package imageList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 2eel on 2015-08-11.
 */
public class ImageViewURL extends ImageView {

    public ImageViewURL(Context context) {
        super(context);
    }

    public ImageViewURL(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageFromURL(String url){
        ImageAsync async = new ImageAsync();
        async.execute(url);
    }

    public class ImageAsync extends AsyncTask<String, Void, Bitmap> {
        HttpURLConnection conn;
        URL url;
        InputStream is;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                Log.d("사진 url 확인", params[0].toString());
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }
            catch (MalformedURLException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
            finally {
                if (is != null) { try { is.close(); } catch (IOException e) { e.printStackTrace(); } }
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            setImageBitmap(bitmap);
        }
    }
}
