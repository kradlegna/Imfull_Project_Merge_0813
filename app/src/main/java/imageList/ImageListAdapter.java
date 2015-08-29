package imageList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import common.Imfull;
import imfull.com.imfull_project.DetailActivity;
import imfull.com.imfull_project.MainActivity;
import imfull.com.imfull_project.R;

/**
 * Created by LeeAh on 15. 8. 7..
 */
public class ImageListAdapter extends BaseAdapter {

    private MainActivity activity;
    private ArrayList<Imfull> data;
    private static LayoutInflater inflater = null;
    private String TAG;

    public ImageListAdapter(MainActivity a, ArrayList<Imfull> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.TAG = this.getClass().getName();
    }

    public void setData(Imfull dto) {
        this.data.add(dto);
//        this.data = data;
    }

    public ArrayList<Imfull> getData(){
        return this.data;
    }


    public void removeData(){
        this.data.removeAll(this.data);
    }


    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row, null);

        ImageView    list_image  = (ImageView)    vi.findViewById(R.id.list_image);
        TextView     txt_title   = (TextView)     vi.findViewById(R.id.txt_title);
        TextView     txt_hit     = (TextView)     vi.findViewById(R.id.txt_hit);
        LinearLayout layout_star = (LinearLayout) vi.findViewById(R.id.layout_star);

        layout_star.removeAllViews();

        Imfull dto = null;
        dto        = data.get(position);

        // Setting all values in listview
        txt_title.setText(dto.getApp_board_title());
        txt_hit.setText("조회수 " + dto.getApp_board_hit());

        String url = dto.getApp_picture_name();

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                            .cacheInMemory(true)
                                            .resetViewBeforeLoading(true)
                                            .build();

        imageLoader.displayImage(url, list_image, options);

        // Set Star layout
//        int grade = Integer.parseInt(list.get(activity.KEY_GRADE));
        int grades = Integer.parseInt("3");
        for (int i = 1; i <= 5; i++) {
            ImageView starImg = new ImageView(vi.getContext());
            LinearLayout.LayoutParams starParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            starImg.setLayoutParams(starParams);

            if (i <= grades) {
//                starImg.setImageResource(R.drawable.star);
            } else {
//                starImg.setImageResource(R.drawable.no_star);
            }
            // add
            layout_star.addView(starImg);
        }
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setClass(activity, DetailActivity.class);
                activity.startActivity(intent);
            }
        });
        return vi;
    }

}