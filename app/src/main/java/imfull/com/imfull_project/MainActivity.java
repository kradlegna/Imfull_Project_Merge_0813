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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import anim_menu.CloseAnimation;
import anim_menu.ExpandAnimation;
import common.BaseActivity;
import common.Imfull;
import common.MyAsync;
import imageList.ImageListAdapter;


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

//  public              String       url        = "http://192.168.0.11:8080";                   // +++ leeah 변경
    public              String       url        = "http://192.168.0.241:8080";
    private             String       TAG;

    ListView                         list;
    ImageListAdapter                 adapter;
    ArrayList<Imfull>                imfullList;

    public static int                requestPage  = 1;
    public static boolean            nextDataFlag = true;


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
        checkNetwork();


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
                    checkNetwork();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("++++++ scroll", "ing");
            }

        });


    }


    /*
        Setting UI  Method
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
        Active Menu(Slide Menu)  Method
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

            // disable all of main view
            // LinearLayout viewGroup = (LinearLayout) findViewById(
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_fragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, false);

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

            // enable all of main view
            // LinearLayout viewGroup = (LinearLayout) findViewById(
            FrameLayout viewGroup = (FrameLayout) findViewById(R.id.ll_fragment)
                    .getParent();
            enableDisableViewGroup(viewGroup, true);

            // disable empty view
            ((LinearLayout) findViewById(R.id.ll_empty))
                    .setVisibility(View.GONE);
            findViewById(R.id.ll_empty).setEnabled(false);
        }
    }


    /*
         뷰의 동작을 제어한다. 하위 모든 뷰들이 enable 값으로 설정된다.
     */
    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled) {
/*
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);

            if (view.getId() != R.id.bt_left) {
                view.setEnabled(enabled);
                if (view instanceof ViewGroup) {
                    enableDisableViewGroup((ViewGroup) view, enabled);
                }
            }
        }
*/
    } 




    /*
        Move Detail Activity  Method
     */
    public void moveDetail(int appBoardId){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("app_board_id", appBoardId);
        startActivity(intent);
    }


    /*
        Move Search Activity  Method
     */
    public void moveSearch(){
        Intent intent = new Intent();
        intent.setClass(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_visible, R.anim.top_to_invisible);
    }


    /*
        Move Wirte Activity  Method
     */
    public void moveWrite() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, WriteActivity.class);
        startActivity(intent);

    }


    /*
        OnClick Method
     */
    @Override
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


    /**
     *  하드웨어 Back key 이벤트시 종료 처리
     */
    @Override

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

    /*-------------------------------------------------------------------
      네트워크의 상태확인한다
      왜?? 네트워크 상태가 유효할때만 데이터를 가져올 수 있으므로.
  -------------------------------------------------------------------*/
    public void checkNetwork(){
        if( !nextDataFlag ) {
            Toast.makeText(this, "마지막 데이터 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG,"checkNetwork");
        ConnectivityManager connMgr = null;
        connMgr= (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this, "네트워크 상태 유효함", Toast.LENGTH_SHORT).show();
            /*웹서버에 연동 시작*/
            MyAsync myAsync = new MyAsync(this, adapter);
            // execute
            Log.d(TAG, "myAsync.execute");
//            myAsync.execute( url + "/app/list" );                         // +++ leeah 변경
            myAsync.execute( url + "/app/list?reqPage=" + requestPage );
        } else {
            Toast.makeText(this, "네트워크 상태 문제가 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
