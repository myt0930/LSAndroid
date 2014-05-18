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

    List<CustomCell> mCellList = new ArrayList<CustomCell>();
    ListView mListView = null;
    CustomCellAdapter mCustomListAdapter = null;

    /**
     * ListFragmentにどのようなアイテムを入れるかを実装
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("out", "onActivity");
        Activity activity = getActivity();
        mListView = getListView();

//        for(int i = 0; i < 4; i++) {
//            CustomCell tmpItem = new CustomCell ();
//            tmpItem.setPlace("aa");
//            mCellList.add(tmpItem);
//        }
        mCustomListAdapter = new CustomCellAdapter (activity, 0, mCellList);
        setListAdapter(mCustomListAdapter);

        /**
         * リストの項目をクリックしたときの処理（今回は違うActivityにタッチした場所ごとの値を渡して呼び出します）
         * @params position：タッチした場所（一番上は0）
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                CustomCell cell = mCellList.get(position);
                detailIntent.putExtra("cellId", cell.getUniqueId());
                startActivity(detailIntent);
            }
        });
    }

    public void setCellList(){
        mCellList.clear();

        ArrayList<LiveInfoTrait> traitList = LiveInfoTrait.getInstance().getTraitList();
        for(LiveInfoTrait trait : traitList){
            CustomCell cell = new CustomCell();
            cell.setLiveInfoTrait(trait);
            mCellList.add(cell);
        }

        mCustomListAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Log.d("out::",String.valueOf(position) + "onListItemClick");

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
