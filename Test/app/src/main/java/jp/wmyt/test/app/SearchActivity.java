package jp.wmyt.test.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

/**
 * Created by JP10733 on 2014/06/05.
 */
public class SearchActivity extends Activity {
    private static final String TAG = "SearchActivity";
    // SearchVIewのリスナー
    private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        /*
         * 文字に変更があったら（１文字ずつ呼ばれる)
         *
         * @see android.support.v7.widget.SearchView.OnQueryTextListener#
         * onQueryTextChange(java.lang.String)
         */
        @Override
        public boolean onQueryTextChange(String newText) {

            Log.d(TAG, "onQueryTextChange "
                    + (TextUtils.isEmpty(newText) ? "" : "Query so far: "
                    + newText));

            return true;
        }

        /*
         * 文字入力を確定した場合
         *
         * @see android.support.v7.widget.SearchView.OnQueryTextListener#
         * onQueryTextSubmit(java.lang.String)
         */
        @Override
        public boolean onQueryTextSubmit(String query) {

            Log.d(TAG, "onQueryTextSubmit Searching for: " + query);


            return true;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sub);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // menu定義のitemのidがaction_searchのものを取得する
        MenuItem searchItem = menu.findItem(R.id.action_search_view);
        //searchItem.expandActionView();

        // SearchViewを取得する
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);
        searchView.onActionViewExpanded();

        // Submitボタンを表示するか
        searchView.setSubmitButtonEnabled(false);

        // SearchViewに何も入力していない時のテキストを設定
        searchView.setQueryHint("検索文字を入力して下さい。");

        // リスナーを登録する
        searchView.setOnQueryTextListener(mOnQueryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Common.getInstance().setLiveListType(Common.LIST_TYPE_LIVEHOUSE);
        Log.d("SearchActivity", "onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("SearchActivity", "finish");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
