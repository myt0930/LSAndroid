package jp.wmyt.test.app.Cell;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import jp.wmyt.test.app.Master.LiveHouseTrait;
import jp.wmyt.test.app.Master.LiveInfoTrait;
import jp.wmyt.test.app.R;

/**
 * Created by miyata on 2014/04/28.
 */
public class CustomCellAdapter extends ArrayAdapter<CustomCell> {
    private LayoutInflater layoutInflater;

    private Context myContext;

    public CustomCellAdapter (Context context, int viewResourceId, List<CustomCell> objects) {
        super(context, viewResourceId, objects);
        myContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //- 特定の行(position)のデータを得る
        CustomCell item = (CustomCell)getItem(position);
        LiveInfoTrait trait = item.getLiveTrait();

        //- リスト用のレイアウトを初回のみ作成
        if( convertView == null ) {
            if(trait.getUniqueID().equals("0")){
                convertView = layoutInflater.inflate(R.layout.custom_list_section, null);
            }else {
                convertView = layoutInflater.inflate(R.layout.custom_list, null);
            }
        }

        if(trait.getUniqueID().equals("0")){
            return convertView;
        }

        Log.d("",trait.getUniqueID());
        //- メッセージのセット
        TextView placeView = (TextView) convertView.findViewById(R.id.place);
        Log.d("",String.valueOf(trait.getLiveHouseNo()));
        Log.d("",LiveHouseTrait.getInstance().getLiveHouseName(trait.getLiveHouseNo()));
        placeView .setText(LiveHouseTrait.getInstance().getLiveHouseName(trait.getLiveHouseNo()));
        Log.d("","---" + trait.getUniqueID());
        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        titleView .setText(trait.getEventTitle());

        TextView actView = (TextView) convertView.findViewById(R.id.act);
        actView .setText(trait.getAct().replace("\n", "/"));

        Calendar cal = Calendar.getInstance();
        cal.setTime(trait.getLiveDate());

        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        String date = String.valueOf(cal.get(Calendar.DATE));
        dateView.setText(date);

        //曜日
        String dayOfWeek = trait.getDayOfWeek();
        TextView dayOfWeekView = (TextView) convertView.findViewById(R.id.day_of_week);
        dayOfWeekView .setText("(" + dayOfWeek + ")");

        Resources res = getContext().getResources();
        if(dayOfWeek.equals("日")){
            dayOfWeekView.setTextColor(res.getColor(R.color.red));
        }else if(dayOfWeek.equals("土")){
            dayOfWeekView.setTextColor(res.getColor(R.color.blue));
        }else{
            dayOfWeekView.setTextColor(res.getColor(R.color.black));
        }

        ImageButton favStar = (ImageButton)convertView.findViewById(R.id.list_fav_button);
        favStar.setColorFilter(getContext().getResources().getColor(R.color.fav));

        return convertView;
    }

    /**
     * @param view TextView
     * @param maxLines 最大行数
     */
    public static void setMultilineEllipsize(TextView view, int maxLines) {
        if (maxLines >= view.getLineCount()) {
            // ellipsizeする必要無し
            return;
        }
        float avail = 0.0f;
        for (int i = 0; i < maxLines; i++) {
            avail += view.getLayout().getLineMax(i);
        }
        CharSequence ellipsizedText = TextUtils.ellipsize(
                view.getText(), view.getPaint(), avail, TextUtils.TruncateAt.END);
        view.setText(ellipsizedText);
    }
}
