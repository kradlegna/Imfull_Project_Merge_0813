package common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by kircheis on 2015. 8. 12..
 */
public class MyImageView extends ImageView{

    String resizedPath;

    public String getResizedPath() {
        return resizedPath;
    }

    public void setResizedPath(String resizedPath) {
        this.resizedPath = resizedPath;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
