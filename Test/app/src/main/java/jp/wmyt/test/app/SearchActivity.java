package jp.wmyt.test.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import jp.wmyt.test.app.Master.LiveHouseTrait;

/**
 * Created by JP10733 on 2014/06/05.
 */
public class SearchActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sub);

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
