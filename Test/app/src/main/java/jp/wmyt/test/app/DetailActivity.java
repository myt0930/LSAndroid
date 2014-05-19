package jp.wmyt.test.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by miyata on 2014/05/05.
 */
public class DetailActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String uniqueId = intent.getStringExtra("uniqueId");
        LiveInfoTrait trait = LiveInfoTrait.getInstance().getTraitOfUniqueID(uniqueId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = dateFormat.format(trait.getLiveDate());
        setTitle(dateStr + "(" + trait.getDayOfWeek() + ")");

        //場所
        TextView detailView = (TextView) this.findViewById(R.id.detail_place);
        detailView.setText(LiveHouseTrait.getInstance().getLiveHouseName(trait.getLiveHouseNo()));

        //タイトル
        detailView = (TextView) this.findViewById(R.id.detail_title);
        detailView.setText(trait.getEventTitle());

        //出演者
        String act = trait.getAct();
        act = act.replace("/", "\n");
        act = act.replace("\n ", "\n");
        detailView = (TextView) this.findViewById(R.id.detail_act);
        detailView.setText(act);

        //その他情報
        detailView = (TextView) this.findViewById(R.id.detail_other);
        detailView.setText(trait.getOtherInfo());
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
