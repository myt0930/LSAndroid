package jp.wmyt.test.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    static String LOGTAG = "";

    private static final String TAG = "DownloadActivity";
    private static final String URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/master.bin";
    static final String DOWNLOAD_FILE_URL = "https://s3-ap-northeast-1.amazonaws.com/tokyolive/";

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

        String[] srcFiles = { "version.bin","master.bin" };
        // ローカルに保存するディレクトリ名
        String dstDir = MainActivity.this.getApplicationInfo().dataDir + File.separator;

        AsyncDownloadTask task = new AsyncDownloadTask( srcFiles, dstDir );
        task.execute(0);
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

    private class AsyncDownloadTask extends AsyncTask<Integer, Integer, Integer> {
        ProgressDialog progress = null;
        Integer[] nFileSize;						// ファイルサイズ
        Integer nFileSizeCount = 0;					// ファイルの現在状況
        Integer nTotalFileSize = 0;
        Integer nTotalFileSizeCount = 0;

        String[] srcFiles;
        String dstDir;

        AsyncDownloadTask( String[] src, String dst ) {
            srcFiles = src;
            dstDir = dst;
        }

        /**
         * 前処理
         */
        @Override
        public void onPreExecute()
        {
            // Dialog 表示
            this.progress = new ProgressDialog( MainActivity.this );
            this.progress.setMessage( "Downloading ..." );
            this.progress.setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            AsyncDownloadTask.this.cancel( false );
                        }
                    }
            );
            this.progress.setIndeterminate( false );
            this.progress.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
            this.progress.setMax( 0 );
            this.progress.setProgress( 0 );
            this.progress.setSecondaryProgress( 0 );
            this.progress.show();
        }

        /**
         * バックグラウンド処理
         */
        @Override
        protected Integer doInBackground(Integer... params) {
            int max = srcFiles.length;
            nFileSize = new Integer[ max ];
            // ファイルの各サイズと合計サイズを取得
            for( int i = 0; i < max; i++ ) {
                try {
                    URL url = new URL( DOWNLOAD_FILE_URL + srcFiles[ i ].toString() );
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                    // ダウンロードするファイルのサイズを取得
                    httpURLConnection.setRequestMethod( "HEAD" );
                    httpURLConnection.connect();
                    if( httpURLConnection.getResponseCode() == 200 ) {
                        this.nFileSize[ i ] = httpURLConnection.getContentLength();
                        this.nTotalFileSize += (int)this.nFileSize[ i ];
                    }
                    httpURLConnection.disconnect();
                    url = null;
                } catch( MalformedURLException e ) {
                    e.printStackTrace();
                } catch( ProtocolException e ) {
                    e.printStackTrace();
                } catch( IOException e ) {
                    e.printStackTrace();
                } finally {}
            }

            // ファイルをダウンロード
            for( int i = 0; i < max; i++ ) {
                // 完了フラグ
                boolean bComplete = false;
                boolean bCancel = false;

                // ファイル検査
                File downFile = new File( dstDir.toString() + srcFiles[ i ].toString() );
                if( downFile.exists() )
                    this.nFileSizeCount = (int)downFile.length();
                downFile = null;
                // 読み込み終了している場合は次のファイルへ
                if( this.nFileSizeCount == (int)this.nFileSize[ i ] ) {
                    this.nTotalFileSizeCount += (int)this.nFileSize[ i ];
//                    continue;
                }

                // ダウンロード先のテンポラリ
                File temporary = new File( dstDir.toString() + srcFiles[ i ].toString() + ".tmp" );
                if( temporary.exists() ) {
                    this.nFileSizeCount = (int)temporary.length();
                }else{
                    this.nFileSizeCount = 0;

                    // ダウンロード用に新規ファイルを作成
                    try {
                        temporary.createNewFile();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }

                try {
                    URL url = new URL( DOWNLOAD_FILE_URL + srcFiles[ i ].toString() );
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                    // 実際のダウンロード処理
                    httpURLConnection.setRequestMethod( "GET" );
                    httpURLConnection.setRequestProperty(
                            "Range",
                            String.format( "byte=%d-%d", this.nFileSizeCount, (int)this.nFileSize[ i ] )
                    );
                    httpURLConnection.connect();

                    // データのダウンロードを開始
                    int code = httpURLConnection.getResponseCode();
                    if( ( code == 200 ) || ( code == 206 ) ) {
                        // HTTP 通信の内容をファイルに保存するためのストリームを生成
                        InputStream inputStream = httpURLConnection.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream( temporary, true );

                        byte[] buffReadBytes = new byte[ 4096 ];
                        for( int sizeReadBytes = inputStream.read( buffReadBytes); sizeReadBytes != -1; sizeReadBytes = inputStream.read( buffReadBytes ) ) {
                            // ファイルに書き出し
                            fileOutputStream.write( buffReadBytes, 0, sizeReadBytes );

                            // 進捗状況を更新する処理
                            this.nFileSizeCount += sizeReadBytes;
                            this.publishProgress( this.nTotalFileSizeCount + this.nFileSizeCount );

                            // キャンセルされているのであればフラグを立てて抜ける
                            if( this.isCancelled() ) {
                                bCancel = true;
                                break;
                            }
                        }
                        fileOutputStream.close();
                    }

                    // ダウンロードの完了フラグを立てる
                    bComplete = true;
                } catch( MalformedURLException e ) {
                    e.printStackTrace();
                } catch( ProtocolException e ) {
                    e.printStackTrace();
                } catch( IOException e ) {
                    e.printStackTrace();
                } finally {
                    // ダウンロードが無事に完了しているのであればリネームする
                    if( bComplete ) {
                        if( !bCancel ) {
                            temporary.renameTo( new File( dstDir.toString() + srcFiles[ i ].toString() ) );
                            this.nTotalFileSizeCount += this.nFileSizeCount;
                        }
                    }

                    try{
                        File file = new File(dstDir.toString() + "master.bin");
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
                }
            }

            // TODO Auto-generated method stub
            return 0;
        }

        /**
         * 進捗処理
         */
        @Override
        protected void onProgressUpdate( Integer...integers ) {
            this.progress.setMax( this.nTotalFileSize );
            this.progress.setProgress( this.nTotalFileSizeCount + this.nFileSizeCount );
        }

        /**
         * キャンセル
         */
        protected void onCancelled() {
            // ProgressDialog の削除
            if( this.progress != null ) {
                this.progress.dismiss();
                this.progress = null;
            }
        }
        /**
         * 後処理
         */
        @Override
        public void onPostExecute( Integer result ) {
            this.nFileSize = null;
            // ProgressDialog の削除
            if( this.progress != null ) {
                this.progress.dismiss();
                this.progress = null;
            }
        }
    }
}
