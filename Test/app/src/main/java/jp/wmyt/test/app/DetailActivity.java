package jp.wmyt.test.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by miyata on 2014/05/05.
 */
public class DetailActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.activity_detail);
        //setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
