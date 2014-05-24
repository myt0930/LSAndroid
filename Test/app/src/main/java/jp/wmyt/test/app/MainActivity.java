package jp.wmyt.test.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends Activity implements View.OnClickListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    static String LOGTAG = "";
    ProgressDialog progressDialog;

    private static final String TAG = "DownloadActivity";
    private static final String URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/master.bin";
    static final String DOWNLOAD_BASE_URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/";
    static final String VERSION_FILE = "version.bin";
    static final String MASTER_FILE = "master.bin";
    public static final String EX_STACK_TRACE = "exStackTrace";
    public static final String PREF_NAME_SAMPLE = "prefLiveScheduler";

    static boolean isCheckUpdate = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("新宿Motion");

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

        // UncaughtExceptionHandlerを実装したクラスをセットする。
        CustomUncaughtExceptionHandler customUncaughtExceptionHandler = new CustomUncaughtExceptionHandler(
                getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(customUncaughtExceptionHandler);
        // SharedPreferencesに保存してある例外発生時のスタックトレースを取得します。
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(PREF_NAME_SAMPLE, Context.MODE_PRIVATE);
        String exStackTrace = preferences.getString(EX_STACK_TRACE, null);

        if (!TextUtils.isEmpty(exStackTrace)) {
            // スタックトレースが存在する場合は、
            // エラー情報を送信するかしないかのダイアログを表示します。
            new ErrorDialogFragment(exStackTrace).show(
                    getFragmentManager(), "error_dialog");
            // スタックトレースを消去します。
            preferences.edit().remove(EX_STACK_TRACE).commit();
        }

        // UpNavigationアイコン(アイコン横の<の部分)を有効に
        // NavigationDrawerではR.drawable.drawerで上書き
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // UpNavigationを有効に
        getActionBar().setHomeButtonEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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

        if(isCheckUpdate) {
            showNeedUpdateDialog(true);
        }
        isCheckUpdate = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }



    private String getTempPath(String fileName){
        return getResourcePath() + fileName + ".tmp";
    }

    private String getResourcePath(){
        return MainActivity.this.getApplicationInfo().dataDir + File.separator;
    }

    private void checkUpdateMaster(){

        File localFile = new File(getResourcePath().toString() + VERSION_FILE);
        if (!localFile.exists()) {
            // 強制リトライ
            showNeedUpdateDialog(true);
            return;
        }

        final String srcFile = DOWNLOAD_BASE_URL + VERSION_FILE;
        // ローカルに保存するディレクトリ名
        final String dstFile = getTempPath(VERSION_FILE);
        AsyncDownloadTask task = new AsyncDownloadTask( srcFile, dstFile, new AsyncDownloadTask.AsyncDownloadCallback() {
            @Override
            public void preExecute() {
                //インディケータ表示
                progressDialog.show();
            }
            @Override
            public void callbackExecute(int result){

                boolean isUpdate = false;
                try {
                    File localFile = new File(getResourcePath().toString() + VERSION_FILE);
                    //DL成功した時のみ判定
                    if(result == 0) {
                        int currentVersion = Common.getInt32FromFile(localFile);
                        int serverVersion = Common.getInt32FromFile(new File(getTempPath(VERSION_FILE).toString()));
                        if (currentVersion < serverVersion) {
                            isUpdate = true;
                        }
                    }
                }catch (Exception e){
                    //TODO: エラー処理
                    e.printStackTrace();
                }

                if(isUpdate){
                    //Updateダイアログ表示
                    showNeedUpdateDialog(false);
                }else{
                    //更新なし
                    loadMaster();

                    //TODO: Appc表示
                }
            }
        });
        task.execute("");
    }

    private void loadMaster(){
        final FragmentManager fragmentManager = this.getFragmentManager();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    File file = new File(getResourcePath() + MASTER_FILE);
                    FileInputStream in = new FileInputStream(file);
                    LoadData loadData  = new LoadData(in);

                    int programVersion = loadData.getInt32();
                    int masterCount = loadData.getInt16();
                    for(int j = 0;j < masterCount;j++){
                        int masterType = loadData.getInt16();
                        switch (masterType){
                            case 1:
                                LiveInfoTrait.getInstance().loadMast(loadData);
                                break;
                            case 2:
                                LiveHouseTrait.getInstance().loadMast(loadData);
                                break;
                            default:
                                break;
                        }
                    }
                }catch(Exception e4){
                    e4.printStackTrace();
                }
                return null;
            }

            String result2;

            @Override
            protected void onPostExecute(Void result) {
                progressDialog.dismiss();
                LiveListFragment fragment = (LiveListFragment)fragmentManager.findFragmentById(R.id.livelist_fragment);
                fragment.setCellList();
            }
        }.execute();
    }

    private void showNeedUpdateDialog(final boolean isConstraint){
        // Dialog 表示
        progressDialog.dismiss();
        AlertDialog.Builder progress = new AlertDialog.Builder( MainActivity.this );
        progress.setTitle("データ更新");
        progress.setMessage("ライブ情報の更新を行います。");
        progress.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadMasterFile(isConstraint);
                    }
                });
        progress.show();
    }

    private void showRetryUpdateDialog(final boolean isConstraint){
        // Dialog 表示
        progressDialog.dismiss();
        AlertDialog.Builder progress = new AlertDialog.Builder( MainActivity.this );
        progress.setTitle("データ更新");
        progress.setMessage(isConstraint    ? "ライブ情報の更新に失敗しました。ネットワーク環境の良い場所でリトライして下さい。"
                                            : "ライブ情報の更新に失敗しました。\\nリトライしますか？\\n(ネットワーク環境の良い場所で行ってください。)");
        progress.setPositiveButton("リトライ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadMasterFile(isConstraint);
                    }
                }
        );
        if(!isConstraint) {
            progress.setNegativeButton("後で",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loadMaster();
                        }
                    }
            );
        }
        progress.show();
    }

    private void showDoneUpdateDialog(){
        // Dialog 表示
        final AlertDialog.Builder progress = new AlertDialog.Builder( MainActivity.this );
        progress.setTitle("データ更新");
        progress.setMessage("ライブ情報の更新が完了しました。");
        progress.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadMaster();
                    }
                });
        progress.show();
    }

    private void downloadMasterFile(final boolean isConstraint){
        if(isConstraint){
            final String srcFile = DOWNLOAD_BASE_URL + VERSION_FILE;
            final String dstFile = getTempPath(VERSION_FILE);
            AsyncDownloadTask task = new AsyncDownloadTask( srcFile, dstFile, new AsyncDownloadTask.AsyncDownloadCallback() {
                @Override
                public void preExecute() { progressDialog.show(); }

                @Override
                public void callbackExecute(int result) {
                    progressDialog.dismiss();

                    if(result == 0){
                        doDownloadMasterFile(true);
                    }else{
                        //リトライ
                        showRetryUpdateDialog(isConstraint);
                    }
                }
            });
            task.execute("");

            //returnする
            return;
        }

        doDownloadMasterFile(false);
    }

    private void doDownloadMasterFile(final boolean isConstraint){
        final String srcFile = DOWNLOAD_BASE_URL + MASTER_FILE;
        // ローカルに保存するディレクトリ名
        final String dstFile = getResourcePath() + MASTER_FILE;
        AsyncDownloadTask task = new AsyncDownloadTask( srcFile, dstFile, new AsyncDownloadTask.AsyncDownloadCallback() {
            @Override
            public void preExecute() {
                //インディケータ表示
                progressDialog.show();
            }

            @Override
            public void callbackExecute(int result) {
                if(result == 0){
                    //version.binをリネーム
                    File tempVersion = new File(getTempPath(VERSION_FILE));
                    tempVersion.renameTo(new File(getResourcePath() + VERSION_FILE));

                    showDoneUpdateDialog();
                }else{
                    //リトライ
                    showRetryUpdateDialog(isConstraint);
                }
            }
        });
        task.execute("");
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
}
