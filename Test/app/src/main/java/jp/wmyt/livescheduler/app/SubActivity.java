package jp.wmyt.livescheduler.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import jp.wmyt.livescheduler.app.Fragment.LiveListFragment;
import jp.wmyt.livescheduler.app.Master.LiveHouseTrait;

/**
 * Created by miyata on 2014/06/01.
 */
public class SubActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sub);

        int liveHouseNo = Common.getInstance().getSelectLiveHouseNo();

        //タイトル設定
        setTitle(LiveHouseTrait.getInstance().getLiveHouseName(liveHouseNo));

        final LiveListFragment listFragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Common.KEY_LIST_TYPE, Common.LIST_TYPE_LIVEHOUSE);
        listFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.sub_content_frame, listFragment, "live_list");
        transaction.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
