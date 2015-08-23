package imfull.com.imfull_project;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import anim_menu.CloseAnimation;
import anim_menu.ExpandAnimation;
import common.BaseActivity;
import common.Imfull;
import common.ListAsync;
import imageList.ImageListAdapter;

/**
 * Created by LeeAh
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    MainActivity                    activity;
    ImageButton                     bt_write,bt_search, bt_menu;

    DisplayMetrics                  metrics;
    int                             panelWidth;
    static boolean                  isLeftExpanded;
    LinearLayout                    slidingPanel;
    LinearLayout                    leftMenuPanel;
    LinearLayout                    listPanel;
    FrameLayout.LayoutParams        slidingPanelParameters;
    FrameLayout.LayoutParams        leftMenuPanelParameters;

    public              String       url        = "http://192.168.0.31:8080";
//  public              String       url        = "http://52.69.226.147:8080";
    private             String       TAG;

    ListView                         list;
    ImageListAdapter                 adapter;
    ArrayList<Imfull>                imfullList;

    public static int                requestPage  = 1;
    public static boolean            nextDataFlag = true;

    ArrayList                        tagList;               // 검색화면(SearchActivity) 에서 선택된 값
    HashMap                          tagMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        this.TAG = this.getClass().getName();

        IntroActivity.introActivity.finish();

        init();


        list = (ListView)findViewById(R.id.listView);

        // listView, adapter
        imfullList = new ArrayList<Imfull>();
        adapter = new ImageListAdapter(this, imfullList);
        list.setAdapter(adapter);
        checkNetwork(false);


        /* -----------------------------------------------------------------------------------------
            list에 들어갈 item 목록
        ----------------------------------------------------------------------------------------- */

        // Getting adapter by passing xml data ArrayList
        Log.d("########[imfull]", "imfullList : " + imfullList.size());

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Imfull> listData = adapter.getData();
                moveDetail(listData.get(position).getApp_board_id());
            }
        });



        /* -----------------------------------------------------------------------------------------
            ListView Scroll  Event
        ----------------------------------------------------------------------------------------- */
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (list != null && list.getAdapter() != null
                        && list.getChildAt(list.getChildCount() - 1) != null
                        && list.getLastVisiblePosition() == list.getAdapter().getCount() - 1
                        && list.getChildAt(list.getChildCount() - 1).getBottom() <= list.getHeight()) {
                    Log.d("++++++ scroll", "end");
                    checkNetwork(false);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("++++++ scroll", "ing");
            }

        });


    }


    /*
     *  Setting UI  Method
     */
    public void init(){

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
//        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
//        mTitleTextView.setText("암풀[i'm full]");

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        panelWidth = (int) ((metrics.widthPixels) * 0.75);

        bt_menu   = (ImageButton) mCustomView.findViewById(R.id.bt_menu);
        bt_search = (ImageButton) mCustomView.findViewById(R.id.bt_search);
        bt_write = (ImageButton) mCustomView.findViewById(R.id.bt_write);
        bt_menu.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        bt_write.setOnClickListener(this);

        // sliding view Initialize
        slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
        slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
                .getLayoutParams();
        slidingPanelParameters.width = metrics.widthPixels;
        slidingPanel.setLayoutParams(slidingPanelParameters);

        // left slide menu initialize
        leftMenuPanel = (LinearLayout) findViewById(R.id.leftMenuPanel);
        leftMenuPanelParameters = (FrameLayout.LayoutParams) leftMenuPanel
                .getLayoutParams();
        leftMenuPanelParameters.width = panelWidth;
        leftMenuPanel.setLayoutParams(leftMenuPanelParameters);

        listPanel = (LinearLayout) findViewById(R.id.ll_fragment);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }



    /*
     *  Active Menu(Slide Menu)  Method
     */
    public void activeMenu(){
        if (!isLeftExpanded) {
            // networkRequestTimeLineGetNewCnt();

            isLeftExpanded = true;
            leftMenuPanel.setVisibility(View.VISIBLE);
            // Expand
            new ExpandAnimation(slidingPanel, panelWidth, "left",
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);

            // enable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.VISIBLE);

            findViewById(R.id.ll_empty).setEnabled(true);
            findViewById(R.id.ll_empty).setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            activeMenu();
                            return true;
                        }
                    });

        } else {
            isLeftExpanded = false;

            // Collapse
            new CloseAnimation(slidingPanel, panelWidth,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);


            // disable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.GONE);
            findViewById(R.id.ll_empty).setEnabled(false);
        }
    }



    /*
     *  상세 화면 이동  Method
     */
    public void moveDetail(int appBoardId){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("app_board_id", appBoardId);
        startActivity(intent);
    }



    /*
     *  검색 화면 이동  Method
     */
    public void moveSearch(){
        Intent intent = new Intent();
        intent.setClass(this, SearchActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.top_to_visible, R.anim.top_to_invisible);
    }



    /*
     *  글쓰기 화면 이동  Method
     */
    public void moveWrite() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, WriteActivity.class);
        startActivity(intent);

    }



    /*
     *  클리 이벤트 처리  Method
     */
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.bt_menu:
                activeMenu();
                break;
            case R.id.bt_search:
                moveSearch();
                break;
            case R.id.bt_write:
                moveWrite();
                break;
            default:
        }
    }



    /*
     *  하드웨어 Back key 이벤트시 종료 처리  Method
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
            new AlertDialog.Builder(this)
                    .setTitle("프로그램 종료")
                    .setMessage("프로그램을 종료 하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }
        return false;
    }



    /*
     *  네트워크 상태 확인  Method
     */
    public void checkNetwork(boolean _searchList){
        if( !nextDataFlag ) {
            Toast.makeText(this, "마지막 데이터 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG,"checkNetwork");
        ConnectivityManager connMgr = null;
        connMgr= (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "네트워크 상태 유효함");
/*
            String param = "";

            if( _searchList ){
                param        = "&tagList=[]";
                adapter.setData(null);
                param = "&tagList=" + (tagList.toString());
            }else{
                param = "&tagList=[]";
            }
*/
            ListAsync listAsync = new ListAsync(this, adapter);
//            listAsync.execute( url + "/app/list?reqPage=" + requestPage + param);
            listAsync.execute( url + "/app/list?reqPage=" + requestPage);

        } else {
            Toast.makeText(this, "네트워크 상태 문제가 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }



    /*
     *  액티비티 파라미터 확인  Method
     *      - 태그검색 화면(SearchActivity.java)에서 넘어왔을 경우 처리
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle b    = data.getExtras();
        tagList     = (ArrayList) b.get("tagList");
        tagMap      = (HashMap) b.get("tagMap");
        requestPage = 1;

        // UI 처리 (검색 내용 표시 영역)
        LinearLayout layout_tagList = (LinearLayout) findViewById(R.id.layout_tagList);
        if( tagList.size() != 0 ){
            layout_tagList.setVisibility(View.VISIBLE);
            TextView txt_tagList = (TextView) findViewById(R.id.txt_tagList);
            String txtTags       = "";

            for( int i = 0; i < tagList.size(); i++ ){
                txtTags += tagMap.get(tagList.get(i)) + "  ";
            }

            txt_tagList.setText(txtTags);
        }else{
            layout_tagList.setVisibility(View.GONE);
        }

//        checkNetwork(true);
    }
}
