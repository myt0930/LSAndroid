package jp.wmyt.test.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.wmyt.test.app.Cell.LiveHouseCell;
import jp.wmyt.test.app.Cell.LiveHouseCellAdapter;
import jp.wmyt.test.app.Common;
import jp.wmyt.test.app.Master.LiveHouseTrait;
import jp.wmyt.test.app.SubActivity;

/**
 * Created by miyata on 2014/05/25.
 */
public class LiveHouseListFragment extends android.app.ListFragment{
    private onFragmentListClickedListener listener;
    List<LiveHouseCell> mCellList = new ArrayList<LiveHouseCell>();
    ListView mListView = null;
    LiveHouseCellAdapter mLiveHouseListAdapter = null;

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
        mLiveHouseListAdapter = new LiveHouseCellAdapter (activity, 0, mCellList);
        setListAdapter(mLiveHouseListAdapter);

        /**
         * リストの項目をクリックしたときの処理（今回は違うActivityにタッチした場所ごとの値を渡して呼び出します）
         * @params position：タッチした場所（一番上は0）
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent subIntent = new Intent(getActivity(), SubActivity.class);
                LiveHouseCell cell = mCellList.get(position);
                //TODO:
                int liveHouseNo = cell.getTrait().getLiveHouseNo();
                Common.getInstance().setSelectLiveHouseNo(liveHouseNo);
                startActivity(subIntent);
            }
        });

        setCellList();
        doCellChange();
    }

    public void setCellList(){
        mCellList.clear();

        ArrayList<LiveHouseTrait> traitList = LiveHouseTrait.getInstance().getTraitList();
        for(LiveHouseTrait trait : traitList){
            LiveHouseCell cell = new LiveHouseCell();
            cell.setPlace(trait.getName());
            cell.setTrait(trait);
            mCellList.add(cell);
        }
    }

    public void doCellChange(){
        mLiveHouseListAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Log.d("out::", String.valueOf(position) + "onListItemClick");

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
