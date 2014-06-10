package jp.wmyt.test.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import jp.wmyt.test.app.Fragment.LiveListFragment;

/**
 * Created by JP10733 on 2014/06/05.
 */
public class SearchActivity extends Activity {
    private static final String TAG = "SearchActivity";
    private static LiveListFragment mListFragment;
    private static SearchView mSearchView;

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

            Log.d(TAG, "onQueryTextChange Searching for: " + newText);

            final String searchText = newText;

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Common.getInstance().setSearchString(searchText);
                    mListFragment.setCellList();
                    return null;
                }

                protected void onPostExecute(Void result) {
                    mListFragment.doCellChange();
                }
            }.execute();


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
            mSearchView.onActionViewCollapsed();
            setTitle(Common.getInstance().getSearchString());
            return true;
        }
    };

    public void closeSearchKeyboard(){
        mSearchView.onActionViewCollapsed();
    }

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
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.onActionViewExpanded();

        // Submitボタンを表示するか
        mSearchView.setSubmitButtonEnabled(false);

        // SearchViewに何も入力していない時のテキストを設定
        mSearchView.setQueryHint("イベント名、出演者を検索");

        // リスナーを登録する
        mSearchView.setOnQueryTextListener(mOnQueryTextListener);

        mListFragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Common.KEY_LIST_TYPE, Common.LIST_TYPE_SEARCH);
        mListFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.sub_content_frame, mListFragment, "live_list");
        transaction.commit();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
