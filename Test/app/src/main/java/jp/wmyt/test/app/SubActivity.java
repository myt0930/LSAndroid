package jp.wmyt.test.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import jp.wmyt.test.app.Master.LiveHouseTrait;

/**
 * Created by miyata on 2014/06/01.
 */
public class SubActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        int liveHouseNo = intent.getIntExtra("liveHouseNo", 2);

        //タイトル設定
        setTitle(LiveHouseTrait.getInstance().getLiveHouseName(liveHouseNo));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Common.getInstance().setLiveListType(Common.LIST_TYPE_LIVEHOUSE);
        Log.d("SubActivity", "onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("SubActivity", "finish");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
