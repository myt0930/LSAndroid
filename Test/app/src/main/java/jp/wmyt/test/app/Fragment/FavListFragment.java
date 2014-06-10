package jp.wmyt.test.app.Fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.wmyt.test.app.Cell.CustomCell;
import jp.wmyt.test.app.Cell.CustomCellAdapter;
import jp.wmyt.test.app.DetailActivity;
import jp.wmyt.test.app.Master.LiveInfoTrait;

/**
 * Created by JP10733 on 2014/06/05.
 */
public class FavListFragment  extends ListFragment {
    private onFragmentListClickedListener listener;

    List<CustomCell> mCellList = new ArrayList<CustomCell>();
    ListView mListView = null;
    CustomCellAdapter mCustomListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * ListFragmentにどのようなアイテムを入れるかを実装
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("out", "onActivity");
        Activity activity = getActivity();

        mListView = getListView();
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
                if(cell.getLiveTrait().getLiveHouseNo() == -1){
                    return;
                }
                detailIntent.putExtra("uniqueId", cell.getLiveTrait().getUniqueID());
                startActivity(detailIntent);
            }
        });

        setCellList();
        doCellChange();
    }

    public void setCellList(){
        mCellList.clear();

        ArrayList<LiveInfoTrait> traitList = LiveInfoTrait.getInstance().getTraitList();
        //TODO: お気に入りライブのみ
        int debugCount = 0;
        for(LiveInfoTrait trait : traitList){
            debugCount++;
            if(debugCount < 10){
                continue;
            }
            CustomCell cell = new CustomCell();
            cell.setLiveInfoTrait(trait);
            mCellList.add(cell);
            debugCount++;
            if(debugCount > 30){
                break;
            }
        }
    }

    public void doCellChange(){
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