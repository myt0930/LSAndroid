package jp.wmyt.test.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    static String LOGTAG = "";
    ProgressDialog progressDialog;

    private static final String TAG = "DownloadActivity";
    private static final String URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/master.bin";
    static final String DOWNLOAD_FILE_URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/";
    static final String VERSION_FILE = "version.bin";
    static final String MASTER_FILE = "master.bin";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.drawer_button)).setOnClickListener(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(LOGTAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(LOGTAG, "onDrawerOpened");
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // ActionBarDrawerToggleクラス内の同メソッドにてアイコンのアニメーションの処理をしている。
                // overrideするときは気を付けること。
                super.onDrawerSlide(drawerView, slideOffset);
                Log.i(LOGTAG, "onDrawerSlide : " + slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // 表示済み、閉じ済みの状態：0
                // ドラッグ中状態:1
                // ドラッグを放した後のアニメーション中：2
                Log.i(LOGTAG, "onDrawerStateChanged  new state : " + newState);
            }
        };

        mDrawer.setDrawerListener(mDrawerToggle);

        // UpNavigationアイコン(アイコン横の<の部分)を有効に
        // NavigationDrawerではR.drawable.drawerで上書き
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // UpNavigationを有効に
        getActionBar().setHomeButtonEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();



        //version.binを取得
            //更新なし→loadMast
            //更新あり→更新ダイアログ表示
                //master.binをDL
                    //loadMast

    }

    private String makeTempPath(String fileName){
        return MainActivity.this.getApplicationInfo().dataDir + File.separator + fileName + ".tmp";
    }

    private void checkUpdateMaster(){

        final String srcFile = VERSION_FILE;
        // ローカルに保存するディレクトリ名
        final String dstFile = makeTempPath(srcFile);
        AsyncDownloadTask task = new AsyncDownloadTask( srcFile, dstFile, new AsyncDownloadTask.AsyncDownloadCallback() {
            @Override
            public void preExecute() {
                //インディケータ表示
                progressDialog.show();
            }
            @Override
            public void callbackExecute(int result){
                progressDialog.dismiss();

                File versionFile = new File(dstFile);

                if(result == 0){

                }else{

                }
            }
        });
    }

    private void showNeedUpdateDialog(){
        // Dialog 表示
        AlertDialog.Builder progress = new AlertDialog.Builder( MainActivity.this );
        progress.setTitle("データ更新");
        progress.setMessage("ライブ情報の更新を行います。");
        progress.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        progress.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isTabletMode(){
        return getResources().getBoolean(R.bool.is_tablet);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
