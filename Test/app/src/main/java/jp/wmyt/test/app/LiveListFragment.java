package jp.wmyt.test.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miyata on 2014/05/04.
 */
public class LiveListFragment extends android.app.ListFragment {
    private onFragmentListClickedListener listener;

    /**
     * ListFragmentにどのようなアイテムを入れるかを実装
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        String[] msgList = { "1", "2", "3", "4", "5", "6", "7", "8", } ;
        ListView lv = getListView();

        List<CustomCell> objects = new ArrayList<CustomCell>();
        for(int i = 0; i < msgList.length; i++) {
            CustomCell tmpItem = new CustomCell ();
            //tmpItem.setPlace(msgList[i]);
            objects.add(tmpItem);
        }
        CustomCellAdapter myCustomListAdapter = new CustomCellAdapter (activity, 0, objects);
        setListAdapter(myCustomListAdapter);

        /**
         * リストの項目をクリックしたときの処理（今回は違うActivityにタッチした場所ごとの値を渡して呼び出します）
         * @params position：タッチした場所（一番上は0）
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.d("",String.valueOf(position));

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                startActivity(detailIntent);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        //listener.onFragmentListClick(rows[position]);
    }

    /**
     * ListのClick情報を通知するListener
     *
     */
    public interface onFragmentListClickedListener {
        public void onFragmentListClick(String select);
    }

    /**
     * Interfaceを登録する
     *
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        listener = (onFragmentListClickedListener)activity;
    }
}
