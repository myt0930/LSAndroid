package jp.wmyt.test.app;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

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

        //- リスト用のレイアウトを初回のみ作成
        if( convertView == null ) {
            convertView = layoutInflater.inflate(R.layout.custom_list, null);
        }

        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.listContainer);

        //- メッセージのセット
        TextView listMessageTextView = (TextView) convertView.findViewById(R.id.title);
        listMessageTextView .setText("イベントタイトルvol.3\nイベントタイトル");

        TextView dateText = (TextView) convertView.findViewById(R.id.date);
        dateText.setGravity(Gravity.CENTER);

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
