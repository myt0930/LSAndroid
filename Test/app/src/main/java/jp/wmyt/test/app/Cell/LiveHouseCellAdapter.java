package jp.wmyt.test.app.Cell;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import jp.wmyt.test.app.Master.LiveHouseTrait;
import jp.wmyt.test.app.Master.LiveInfoTrait;
import jp.wmyt.test.app.R;

/**
 * Created by miyata on 2014/05/25.
 */
public class LiveHouseCellAdapter extends ArrayAdapter<LiveHouseCell> {
    private LayoutInflater layoutInflater;

    private Context myContext;

    public LiveHouseCellAdapter (Context context, int viewResourceId, List<LiveHouseCell> objects) {
        super(context, viewResourceId, objects);
        myContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //- 特定の行(position)のデータを得る
        LiveHouseCell item = (LiveHouseCell)getItem(position);

        //- リスト用のレイアウトを初回のみ作成
        if( convertView == null ) {
            convertView = layoutInflater.inflate(R.layout.livehouse_list, null);
        }

        //- メッセージのセット
        TextView placeView = (TextView) convertView.findViewById(R.id.live_house);
        placeView .setText(item.getPlace());

        return convertView;
    }
}
