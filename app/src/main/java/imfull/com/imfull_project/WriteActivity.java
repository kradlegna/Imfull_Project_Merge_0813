package imfull.com.imfull_project;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.AndroidUploader;
import common.FlowLayout;
import common.ImageSelectHelperActivity;

public class WriteActivity extends ImageSelectHelperActivity implements View.OnClickListener, View.OnFocusChangeListener {
    final int REQ_CODE_SELECT_IMAGE = 100;

    //1.글쓴이 제목 상호명 주소 연락처 별점
    String[] contents = new String[6];
    //2.태그
    String[] tags = new String[6];
    //3. resizedPictures<<사진실제 경로 담겨있는 ArrayList

    Button photoCamera,photoGallery,photoCancel;

    /*
    * 1 프랜차이즈
    * 2 개인
    * 3 레스토랑
    * 4 카페
    * 5 뷔페
    * 6 술집/바
    * 7 한식
    * 8 양식
    * 9 일식
    * 10 퓨전
    * 11 중식
    * 12 아시아
    * 13 디저트
    * 14 맛있음
    * 15 맛없음
    * 16 좋음
    * 17 안좋음
    * 18 깨끗함
    * 19 더러움
    * 20 친절함
    * 21 불친함
    * 22 싸다
    * 23 비싸다
    * */



    ArrayList<TextView> checkTags = new ArrayList<TextView>();
    ArrayList<ImageView> stars = new ArrayList<ImageView>();
    ImageView star1, star2, star3, star4, star5;

    ImageButton bt_back;
    Button sendContent;

    private DisplayMetrics metrics;
    private int panelWidth;

    EditText writer, title, shopName, address, phone;
    TextView category_franchise, category_self_employed, category_restourant, category_buffet, category_cafe, category_bar, korean_food, western_food, japanese_food, chinese_food, fusion, asia_food, desert, taste_delicious, taste_awfull, mood_good, mood_normal, mood_bad, cleanliness_good, cleanliness_bad, service_kind, service_unkind, service_cheap, service_expensive;
    ImageButton addPicture;

    AndroidUploader uploader;
    String url = "http://52.69.226.147:8080/app/write";


/*
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    //private ImageView mPhotoImageView;
*/

    FlowLayout flow_layout8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        init();


        uploader = new AndroidUploader(url);

        writer = (EditText) findViewById(R.id.writer);
        writer.setOnFocusChangeListener(this);
        title = (EditText) findViewById(R.id.title);
        title.setOnFocusChangeListener(this);
        shopName = (EditText) findViewById(R.id.shopName);
        shopName.setOnFocusChangeListener(this);
        address = (EditText) findViewById(R.id.address);
        address.setOnFocusChangeListener(this);
        phone = (EditText) findViewById(R.id.phone);
        phone.setOnFocusChangeListener(this);

        checkTags.add(category_franchise = (TextView) findViewById(R.id.category_franchise));
        checkTags.add(category_self_employed = (TextView) findViewById(R.id.category_self_employed));
        checkTags.add(category_restourant = (TextView) findViewById(R.id.category_restourant));
        checkTags.add(category_buffet = (TextView) findViewById(R.id.category_buffet));
        checkTags.add(category_cafe = (TextView) findViewById(R.id.category_cafe));
        checkTags.add(category_bar = (TextView) findViewById(R.id.category_bar));
        checkTags.add(korean_food = (TextView) findViewById(R.id.korean_food));
        checkTags.add(western_food = (TextView) findViewById(R.id.western_food));
        checkTags.add(japanese_food = (TextView) findViewById(R.id.japanese_food));
        checkTags.add(chinese_food = (TextView) findViewById(R.id.chinese_food));
        checkTags.add(fusion = (TextView) findViewById(R.id.fusion));
        checkTags.add(asia_food = (TextView) findViewById(R.id.asia_food));
        checkTags.add(desert = (TextView) findViewById(R.id.desert));
        checkTags.add(taste_delicious = (TextView) findViewById(R.id.taste_delicious));
        checkTags.add(taste_awfull = (TextView) findViewById(R.id.taste_awfull));
        checkTags.add(mood_good = (TextView) findViewById(R.id.mood_good));
        checkTags.add(mood_normal = (TextView) findViewById(R.id.mood_normal));
        checkTags.add(mood_bad = (TextView) findViewById(R.id.mood_bad));
        checkTags.add(cleanliness_good = (TextView) findViewById(R.id.cleanliness_good));
        checkTags.add(cleanliness_bad = (TextView) findViewById(R.id.cleanliness_bad));
        checkTags.add(service_kind = (TextView) findViewById(R.id.service_kind));
        checkTags.add(service_unkind = (TextView) findViewById(R.id.service_unkind));
        checkTags.add(service_cheap = (TextView) findViewById(R.id.service_cheap));
        checkTags.add(service_expensive = (TextView) findViewById(R.id.service_expensive));

        stars.add(star1 = (ImageView) findViewById(R.id.star1));
        stars.add(star2 = (ImageView) findViewById(R.id.star2));
        stars.add(star3 = (ImageView) findViewById(R.id.star3));
        stars.add(star4 = (ImageView) findViewById(R.id.star4));
        stars.add(star5 = (ImageView) findViewById(R.id.star5));

        for (int i = 0; i < checkTags.size(); i++) {
            checkTags.get(i).setOnClickListener(this);
        }

        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setOnClickListener(this);
        }



        sendContent = (Button) findViewById(R.id.sendContent);
        sendContent.setOnClickListener(this);
        //mPhotoImageView = (ImageView) findViewById(R.id.image);
        flow_layout8 = (FlowLayout) findViewById(R.id.flow_layout8);

        contents[5]="3";



    }

    public void init() {

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.write_actionbar, null);
        //TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        //mTitleTextView.setText("암풀[i'm full]");


        //디스플레이의 가로 세로를 구할 때 쓴다. 그외에도 기능 많음.
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        panelWidth = (int) ((metrics.widthPixels) * 0.75);

        bt_back = (ImageButton) mCustomView.findViewById(R.id.bt_back);
        addPicture = (ImageButton) mCustomView.findViewById(R.id.addPicture);
        addPicture.setOnClickListener(this);
        bt_back.setOnClickListener(this);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

//        View camera_inflator = mInflater.inflate(R.layout.layout_camera_button, null);
//        View gallery_inflator = mInflater.inflate(R.layout.layout_gallery_button, null);
//        View cancel_inflator = mInflater.inflate(R.layout.layout_cancel_button, null);
//        photoCamera=(Button)findViewById(R.id.photoButton_camera);
//        photoGallery=(Button)findViewById(R.id.photoButton_gallery);
//        photoCancel=(Button)findViewById(R.id.photoButton_cancel);
//
//        setCustomButtons(photoGallery,photoCamera,photoCancel);

    }

//    public boolean shouldOverrideUrlLoading(String url) {
//        final String items[] = {"갤러리에서 가져오기","카메라로 촬영하기"};
//        if (url.startsWith("custom://")) {
//            new AlertDialog.Builder(this)
//                    .setIcon(R.drawable.logo)
//                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int item) {
//                            Toast.makeText(getApplicationContext(), Integer.toString(item), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//
//                            if (item == 0) {
////갤러리 호출
//                                Uri uri = Uri.parse("content://media/external/images/media");
//                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                intent.setAction(Intent.ACTION_GET_CONTENT);
//                                intent.setType("image/*");
//                                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
//                            } else if (item == 1) {
////카메라로 찍기
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        }
//                    })
//                    .show();
//        }
//        return true;
//    }
//    //////////////////////////// 선택 하면 리턴값 받기
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        try {
//            if(!intent.getData().equals(null)){
//                Bitmap selPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
//                selPhoto = Bitmap.createScaledBitmap(selPhoto, 100, 100, true);
////      image_bt.setImageBitmap(selPhoto);//썸네일
//                Log.e("선택 된 이미지 ", "selPhoto : " + selPhoto);
//                pictures.add(selPhoto);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    //    public void selectMultiPhoto(){
//        Integer[] mThumbIds = {
//                R.drawable.sample_thumb_0, R.drawable.sample_thumb_1,
//                R.drawable.sample_thumb_2, R.drawable.sample_thumb_3,
//        };
//        ImageView imageView[] = new ImageView[mThumbIds.length];
//        for(int i = 0; i < mThumbIds.length; i++){
//            imageView[i] = new ImageView(this);
//            imageView[i].setLayoutParams(new TableLayout.LayoutParams(
//                    FlowLayout.LayoutParams.WRAP_CONTENT,
//                    FlowLayout.LayoutParams.WRAP_CONTENT));
//            imageView[i].setImageResource(mThumbIds[i]);
//            flow_layout8.addView(imageView[i]);
//        }
//        for(int i = 0; i < mThumbIds.length; i++){
//            imageView[i].setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    // 하고싶은처리.....
//                }
//            });
//        }
//
//    }

    public void changeStar(int num) {
        for (int i = 0; i < stars.size(); i++) {
            if (i <= num) {
                stars.get(i).setImageResource(R.drawable.star);
            } else {
                stars.get(i).setImageResource(R.drawable.no_star);
            }

        }
    }

    public void changeColor(TextView textView) {

        int currentColor = textView.getCurrentTextColor();


        if (textView == category_franchise || textView == category_self_employed || textView == category_restourant || textView == category_buffet || textView == category_cafe || textView == category_bar) {
            category_franchise.setTextColor(-8355712);
            category_self_employed.setTextColor(-8355712);
            category_restourant.setTextColor(-8355712);
            category_buffet.setTextColor(-8355712);
            category_cafe.setTextColor(-8355712);
            category_bar.setTextColor(-8355712);
        }
        if (textView == korean_food || textView == western_food || textView == japanese_food || textView == chinese_food || textView == fusion || textView == asia_food || textView == desert) {
            korean_food.setTextColor(-8355712);
            western_food.setTextColor(-8355712);
            japanese_food.setTextColor(-8355712);
            chinese_food.setTextColor(-8355712);
            fusion.setTextColor(-8355712);
            asia_food.setTextColor(-8355712);
            desert.setTextColor(-8355712);
        }
        if (textView == taste_delicious || textView == taste_awfull) {
            taste_delicious.setTextColor(-8355712);
            taste_awfull.setTextColor(-8355712);

        }
        if (textView == mood_good || textView == mood_normal || textView == mood_bad) {
            mood_good.setTextColor(-8355712);
            mood_normal.setTextColor(-8355712);
            mood_bad.setTextColor(-8355712);

        }
        if (textView == cleanliness_good || textView == cleanliness_bad) {
            cleanliness_good.setTextColor(-8355712);
            cleanliness_bad.setTextColor(-8355712);
        }
        if (textView == service_kind || textView == service_unkind || textView == service_cheap || textView == service_expensive) {
            service_kind.setTextColor(-8355712);
            service_unkind.setTextColor(-8355712);
            service_cheap.setTextColor(-8355712);
            service_expensive.setTextColor(-8355712);
        }


        if (currentColor == -8355712) {
            textView.setTextColor(-235971);
        } else {
            textView.setTextColor(-8355712);
        }
        //Log.d("현재 글씨 색깔은 =",currentColor+"입니다.");
        //Toast.makeText(this,"현재 pictures에 저장된 사진은 "+pictures.size()+"개 입니다."+"누르신 view="+textView.getText().toString()+"현재 글씨 색깔은 ="+currentColor+"입니다.",Toast.LENGTH_SHORT).show();
    }

    /*
        OnClick Method
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_franchise:
                changeColor(category_franchise);
                tags[0] = "1";
                break;
            case R.id.category_self_employed:
                changeColor(category_self_employed);
                tags[0] = "2";
                break;
            case R.id.category_restourant:
                changeColor(category_restourant);
                tags[0] = "3";
                break;
            case R.id.category_buffet:
                changeColor(category_buffet);
                tags[0] = "4";
                break;
            case R.id.category_cafe:
                changeColor(category_cafe);
                tags[0] = "5";
                break;
            case R.id.category_bar:
                changeColor(category_bar);
                tags[0] = "6";
                break;
            case R.id.korean_food:
                changeColor(korean_food);
                tags[1] = "7";
                break;
            case R.id.western_food:
                changeColor(western_food);
                tags[1] = "8";
                break;
            case R.id.japanese_food:
                changeColor(japanese_food);
                tags[1] = "9";
                break;
            case R.id.chinese_food:
                changeColor(chinese_food);
                tags[1] = "11";
                break;
            case R.id.fusion:
                changeColor(fusion);
                tags[1] = "10";
                break;
            case R.id.asia_food:
                changeColor(asia_food);
                tags[1] = "12";
                break;
            case R.id.desert:
                changeColor(desert);
                tags[1] = "13";
                break;
            case R.id.taste_delicious:
                changeColor(taste_delicious);
                tags[2] = "14";
                break;
            case R.id.taste_awfull:
                changeColor(taste_awfull);
                tags[2] = "15";
                break;
            case R.id.mood_good:
                changeColor(mood_good);
                tags[3] = "16";
                break;
            case R.id.mood_normal:
                changeColor(mood_normal);
                tags[3] = "24";
                break;
            case R.id.mood_bad:
                changeColor(mood_bad);
                tags[3] = "17";
                break;
            case R.id.cleanliness_good:
                changeColor(cleanliness_good);
                tags[4] = "18";
                break;
            case R.id.cleanliness_bad:
                changeColor(cleanliness_bad);
                tags[4] = "19";
                break;
            case R.id.service_kind:
                changeColor(service_kind);
                tags[5] = "20";
                break;
            case R.id.service_unkind:
                changeColor(service_unkind);
                tags[5] = "21";
                break;
            case R.id.service_cheap:
                changeColor(service_cheap);
                tags[5] = "23";
                break;
            case R.id.service_expensive:
                changeColor(service_expensive);
                tags[5] = "22";
                break;
            case R.id.star1:
                changeStar(0);
                contents[5]="1";
                break;
            case R.id.star2:
                changeStar(1);
                contents[5]="2";
                break;
            case R.id.star3:
                changeStar(2);
                contents[5]="3";
                break;
            case R.id.star4:
                changeStar(3);
                contents[5]="4";
                break;
            case R.id.star5:
                changeStar(4);
                contents[5]="5";
                break;
            case R.id.bt_back:
                goBack();
                break;
            case R.id.addPicture:
                startSelectImage();
                /*DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new android.app.AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            */
                break;
            case R.id.sendContent:
                if (makeList()) {
                    Toast.makeText(this,"전송시도했음",Toast.LENGTH_SHORT).show();
                    AndroidUploader.ReturnCode result = uploader.uploadForm(doUpload());
                    if (result.equals("http201")) {
                        deletePicture();
                        finish();
                    } else {
                        showAlert("오류코드 : " + result + "가 발생하였습니다." + "\n" + "문제가 계속된다면 홈페이지에 문의 주세요");
                    }
                }



        break;
        default:
        ;
        break;
    }


}

    /**
     * 카메라에서 이미지 가져오기
     *//*
    private void doTakePhotoAction() {
    *//*
     * 참고 해볼곳
     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
     * http://www.damonkohler.com/2009/02/android-recipes.html
     * http://www.firstclown.us/tag/android/
     *//*

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        pictures.add(mImageCaptureUri);
        Log.d("add된 파일은", mImageCaptureUri.getPath() + "입니다.");
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
*/

    /*protected ImageView makeImage(){


        ImageView mPhotoImageView = new ImageView(getBaseContext()){

            @Override
            public void setOnClickListener(OnClickListener l) {
                super.setOnClickListener(l);
                flow_layout8.removeView(this);
            }
        };

        //LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        FlowLayout.LayoutParams params=new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mPhotoImageView.setLayoutParams(params);
        //mPhotoImageView.setScaleType(ImageView.ScaleType.MATRIX);  // 매트릭스로 이동시키기 때문에 scaleType은 반드시 Matrix로 지정해야한다.
        //mPhotoImageView.setImageBitmap(bm);      // 이미지를 등록한다.
        //Matrix m = new Matrix();
        //m.postTranslate(0,0);    // 이미지뷰에 등록할 비트맵의 초기위치.
        //mPhotoImageView.setImageMatrix(m);       // 매트릭스를 이미지뷰에 적용한다.
        mPhotoImageView.setBackgroundColor(Color.BLUE);
        flow_layout8.addView(mPhotoImageView);      // 레이아웃에 이미지뷰를 등록한다.


        return mPhotoImageView;
    }*/




    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }



        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();
                resizedPictures.add(data.getData());
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ImageView mPhotoImageView = makeImage();
                    mPhotoImageView.setImageBitmap(photo);
                }

                break;
            }

            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
                pictures.add(mImageCaptureUri);
                Log.d("add된 파일은", new File(mImageCaptureUri.getPath()).getAbsolutePath() + "입니다.");
            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image*//*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }
    }*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
            goBack();
        }
        return false;
    }

    public void goBack(){

        new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.cancelWritingTitle)
                .setMessage(R.string.cancelWriting)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePicture();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public boolean makeList() {

        EditText[] txt=new EditText[5];
        txt[0]=writer ;
        txt[1]=title ;
        txt[2]=shopName ;
        txt[3]=address ;
        txt[4]=phone ;
        contents[0] = writer.getText().toString();
        contents[1] = title.getText().toString();
        contents[2] = shopName.getText().toString();
        contents[3] = address.getText().toString();
        contents[4] = phone.getText().toString();

        for (int i = 0; i < txt.length; i++) {
            if (!checkText(txt[i])) {
                txt[i].requestFocus();
                return false;
            }
        }

        for (int i = 0; i < 6; i++) {
            if ((contents[i] == null) || (tags[i] == null)) {
                showAlert(getString(R.string.moreThanTwo));
                return false;
            }
        }

        if (resizedPictures.size() == 0) {
            showAlert(getString(R.string.selectPhoto));
            return false;
        }

        return true;

    }

    public List<ArrayList<String>> doUpload() {
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                list1.add(contents[i]);
            } else {
                return list = null;
            }

        }
        list.add(list1);
        ArrayList<String> list2 = new ArrayList<String>();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] != null) {
                list2.add(tags[i]);
            } else {
                return list = null;
            }

        }
        list.add(list2);
        list.add(resizedPictures);


        for(int i=0;i<list.size();i++){
            for (int a=0;a<list.get(i).size();a++){
                Log.d("List의 모든것을 불러온다!!",list.get(i).get(a));
            }
        }





        return list;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        deletePicture();
    }

    protected void deletePicture() {
        File f = null;
        if (resizedPictures.size() > 0) {
            for (int i = 0; i < resizedPictures.size(); i++) {
                // 임시 파일 삭제
                Log.d("리사이즈된 파일은", resizedPictures.get(i) + "입니다.");
                f = new File(resizedPictures.get(i));
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch(v.getId()){
            case R.id.writer:if(!hasFocus){checkText((EditText) v);};break;
            case R.id.title:if(!hasFocus){checkText((EditText) v);};break;
            case R.id.shopName:if(!hasFocus){checkText((EditText) v);};break;
            case R.id.address:if(!hasFocus){checkText((EditText) v);};break;
            case R.id.phone:if(!hasFocus){checkPhone();};break;
        }

    }

    public Boolean checkText(EditText txt){

        String name=txt.getText().toString();

        Pattern patternName= Pattern.compile("[0-9a-zA-Z-\\s가-힣u4e00-u9fa5]{1,50}$");
        //2~50 숫자 영문 소문 대문  한글 공백 허용 한자?
        Matcher matcherName=patternName.matcher(name);
        boolean flagName=matcherName.matches();
        if(!flagName){
            if(txt==writer) {
                txt.setError(getString(R.string.wrongWriter));
            }else if(txt==title){
                txt.setError(getString(R.string.wrongTitle));
            }else if(txt==shopName){
                txt.setError(getString(R.string.wrongShopName));
            }else if(txt==address){
                txt.setError(getString(R.string.wrongAddress));
            }
        }
        filterText("?",txt);
        return flagName;

    }
    public Boolean checkPhone(){
        String number=phone.getText().toString();
        Pattern patternName=Pattern.compile("^[0-9-]{1,16}$");
        //9~15 숫자만 허용가능
        Matcher matcherName=patternName.matcher(number);
        boolean flagName=matcherName.matches();
        if(!flagName){
            phone.setError(getString(R.string.wrongPhone));
        }
        return flagName;
    }

    /**
     * 금지어 필터링하기
     *
     * @author   Sehwan Noh <sehnoh at java2go.net>
     * @version  1.0 - 2006. 08. 22
     * @since    JDK 1.4
     */

        public void filterText(String sText,EditText txt) {
            Pattern p = Pattern.compile("fuck|shit|개새끼", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(txt.getText().toString());

            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                //System.out.println(m.group());
                m.appendReplacement(sb, maskWord(m.group()));
            }
            m.appendTail(sb);

            //System.out.println(sb.toString());
            txt.setText(sb);
            //return sb.toString();
        }

        public String maskWord(String word) {
            StringBuffer buff = new StringBuffer();
            char[] ch = word.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (i < 1) {
                    buff.append(ch[i]);
                } else {
                    buff.append("*");
                }
            }
            return buff.toString();
        }

}
