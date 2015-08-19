package imfull.com.imfull_project;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import common.BaseActivity;
import common.DetailAsync;
import common.FlowLayout;
import common.MyImageView;
import imageList.ImageViewURL;

/**
 * Created by kircheis on 2015. 8. 13..
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener{

    public              String       url        = "http://52.69.226.147:8080/app/selectOne";

    public ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();;

    public ArrayList<ArrayList<String>> getList() {
        return list;
    }

    public void setList(ArrayList<ArrayList<String>> list) {
        this.list = list;
    }

    String[] contents=new String[7];
    //0=writer
    //1=title
    //2=shopName
    //3=address
    //4=phone
    //5=star
    //6= 조회수

    ImageButton detail_bt_back;
    RelativeLayout title_layout;
    LinearLayout star_layout;
    TextView detail_title,detail_writer,detail_shopName,detail_address,detail_phone,count;
    FlowLayout tag_layout,photo_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();

        photo_layout = (FlowLayout)findViewById(R.id.photo_layout);
        star_layout= (LinearLayout)findViewById(R.id.star_layout);
        tag_layout= (FlowLayout)findViewById(R.id.tag_layout);
        detail_phone= (TextView)findViewById(R.id.detail_phone);
        detail_address= (TextView)findViewById(R.id.detail_address);
        detail_shopName= (TextView)findViewById(R.id.detail_shopName);
        detail_writer= (TextView)findViewById(R.id.detail_writer);
        detail_title= (TextView)findViewById(R.id.detail_title);
        title_layout= (RelativeLayout)findViewById(R.id.title_layout);
        count=(TextView)findViewById(R.id.count);

/*
        ArrayList<String> list1 = new ArrayList<String >();
        list1.add("누네띠네");
        list1.add("맛집");
        list1.add("맛있는집");
        list1.add("안드로메다");
        list1.add("010-2010-2921");
        list1.add("5");
        list1.add("15");

        ArrayList<String> list2 = new ArrayList<String >();
        list2.add("레스토랑");
        list2.add("한식");
        list2.add("맛있음");
        list2.add("좋음");
        list2.add("청결함");
        list2.add("싸다");

        ArrayList<String> list3 = new ArrayList<String >();

        list3.add("http://www.nemopan.com/cooking/files/attach/images/1164049/083/934/003/지역별음식_010.jpg");
        list3.add("http://venuswannabe.com/attach/1/3340712468.jpg");
        list3.add("https://pbs.twimg.com/profile_images/1763288040/____.JPG");

        Log.d("리스트 길이는  ", "content 리스트" + list1.size() + "category 리스트" + list2.size() + "photo 리스트" + list3.size());


*/

        String idx = getIntent().getStringExtra("idx");
        DetailAsync async=new DetailAsync(this);
        async.execute(url, idx);

        selectOne(list);

    }

    public void init() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.detail_actionbar, null);

        detail_bt_back = (ImageButton) mCustomView.findViewById(R.id.detail_bt_back);
        detail_bt_back.setOnClickListener(this);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void selectOne(List<ArrayList<String>> list){
        setText(list.get(0));
        addTags(list.get(1));
        setPhotos(list.get(2));
    }

    //별표 만들기
    public void makeStars(String num){
        int number=Integer.parseInt(num);

        for(int i=1;i<=number;i++) {
            MyImageView mPhotoImageView = new MyImageView(getBaseContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.setMargins(10, 10, 10, 10);
            mPhotoImageView.setLayoutParams(params);

            if (i > number) {
                mPhotoImageView.setImageResource(R.drawable.no_star);
            }else{
                mPhotoImageView.setImageResource(R.drawable.star);
            }
            star_layout.addView(mPhotoImageView);      // 레이아웃에 이미지뷰를 등록한다.
        }
    }


    //내용채우기
    public void setText(ArrayList<String> textList){
        for(int i=0;i<contents.length;i++) {
            contents[i]=textList.get(i);
        }
        detail_writer.setText(contents[0]);
        detail_title.setText(contents[1]);
        detail_shopName.setText(contents[2]);
        detail_address.setText(contents[3]);
        detail_phone.setText(contents[4]);
        makeStars(contents[5]);
        count.setText(contents[6]);
    }

    //태그 넣기
    public void addTags(ArrayList<String> tagList){

        for(int i = 0 ;i<tagList.size();i++){
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 20, 0);
            TextView newText=new TextView(this);
            newText.setLayoutParams(params);
            String tag=null;
            if (tagList.get(i)=="1"){
                newText.setText(R.string.brands);
            }else if(tagList.get(i)=="2"){
                newText.setText(R.string.personal);
            }else if(tagList.get(i)=="3"){
                newText.setText(R.string.high_end_restaurant);
            }else if(tagList.get(i)=="4"){
                newText.setText(R.string.cafe);
            }else if(tagList.get(i)=="5"){
                newText.setText(R.string.buffet);
            }else if(tagList.get(i)=="6"){
                newText.setText(R.string.bar);
            }else if(tagList.get(i)=="7"){
                newText.setText(R.string.korean);
            }else if(tagList.get(i)=="8"){
                newText.setText(R.string.western);
            }else if(tagList.get(i)=="9"){
                newText.setText(R.string.japanese);
            }else if(tagList.get(i)=="10"){
                newText.setText(R.string.fusion);
            }else if(tagList.get(i)=="11"){
                newText.setText(R.string.chinese);
            }else if(tagList.get(i)=="12"){
                newText.setText(R.string.asian);
            }else if(tagList.get(i)=="13"){
                newText.setText(R.string.desert);
            }else if(tagList.get(i)=="14"){
                newText.setText(R.string.delicious);
            }else if(tagList.get(i)=="15"){
                newText.setText(R.string.notDelicious);
            }else if(tagList.get(i)=="16"){
                newText.setText(R.string.good);
            }else if(tagList.get(i)=="17"){
                newText.setText(R.string.bad);
            }else if(tagList.get(i)=="18"){
                newText.setText(R.string.clean);
            }else if(tagList.get(i)=="19"){
                newText.setText(R.string.dirty);
            }else if(tagList.get(i)=="20"){
                newText.setText(R.string.kind);
            }else if(tagList.get(i)=="21"){
                newText.setText(R.string.rude);
            }else if(tagList.get(i)=="22"){
                newText.setText(R.string.expensive);
            }else if(tagList.get(i)=="23"){
                newText.setText(R.string.cheap);
            }else if(tagList.get(i)=="24"){
                newText.setText(R.string.brands);
            }else{
                //만약 새로운 태그가 추가됬을 경우 어떻게 해야하나.
            }
            newText.setTextColor(-235971);
            //newText.setTextSize();
            Log.d("태그 ","만들어서 붙였습니다!");
            tag_layout.addView(newText);
        }
    }

    //사진 넣기
    public void setPhotos(ArrayList<String> photoList){

        Toast.makeText(this,"그림 집어오는 중",Toast.LENGTH_SHORT).show();
        ImageViewURL imageViewURL1 = (ImageViewURL)findViewById(R.id.firstImage);
        imageViewURL1.setImageFromURL(photoList.get(0));

        if(photoList.size()>1) {
            for (int i = 1; i < photoList.size(); i++) {
                ImageViewURL imageViewURL = new ImageViewURL(getBaseContext()) {

                    @Override
                    public void setOnClickListener(OnClickListener l) {
                        super.setOnClickListener(l);
                        //flow_layout8.removeView(this);
                    }
                };

                //LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.MATCH_PARENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 10);
                imageViewURL.setLayoutParams(params);
                imageViewURL.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewURL.setImageFromURL(photoList.get(i));
                photo_layout.addView(imageViewURL);      // 레이아웃에 이미지뷰를 등록한다.
            }
        }

    }


    //

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.detail_bt_back:
                finish();
                break;
            case R.id.bt_search:

                break;
            case R.id.bt_write:

                break;
            default:
        }
    }
}
