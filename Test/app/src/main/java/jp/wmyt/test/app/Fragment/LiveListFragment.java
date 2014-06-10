package jp.wmyt.test.app.Fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.wmyt.test.app.Cell.CustomCell;
import jp.wmyt.test.app.Cell.CustomCellAdapter;
import jp.wmyt.test.app.Common;
import jp.wmyt.test.app.DetailActivity;
import jp.wmyt.test.app.Master.LiveInfoTrait;
import jp.wmyt.test.app.SearchActivity;

/**
 * Created by miyata on 2014/05/04.
 */
public class LiveListFragment extends ListFragment {
    private onFragmentListClickedListener listener;

    List<CustomCell> mCellList = new ArrayList<CustomCell>();
    ListView mListView = null;
    CustomCellAdapter mCustomListAdapter = null;
    int mListType;

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
        mListType = getArguments().getInt(Common.KEY_LIST_TYPE);

        /**
         * リストの項目をクリックしたときの処理（今回は違うActivityにタッチした場所ごとの値を渡して呼び出します）
         * @params position：タッチした場所（一番上は0）
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String activityName = getActivity().getLocalClassName();
                if(activityName.equals("SearchActivity")){
                    ((SearchActivity)getActivity()).closeSearchKeyboard();
                }
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                CustomCell cell = mCellList.get(position);
                if(cell.getLiveTrait().getLiveHouseNo() == -1){
                    return;
                }
                detailIntent.putExtra("uniqueId", cell.getLiveTrait().getUniqueID());
                startActivity(detailIntent);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                String activityName = getActivity().getLocalClassName();
                if(activityName.equals("SearchActivity")){
                    ((SearchActivity)getActivity()).closeSearchKeyboard();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });

        setCellList();
        doCellChange();
    }

    public void setCellList(){
        mCellList.clear();

        LiveInfoTrait instance = LiveInfoTrait.getInstance();
        ArrayList<LiveInfoTrait> traitList = null;
        switch (mListType){
            case Common.LIST_TYPE_DATE:
                traitList = instance.getTraitListOfDate(Common.getInstance().getLiveDate());
                break;
            case Common.LIST_TYPE_FAV:
                //TODO
                traitList = instance.getTraitListOfDate(Common.getInstance().getLiveDate());
                break;
            case Common.LIST_TYPE_LIVEHOUSE:
                traitList = instance.getTraitListOfLiveHouseNo(Common.getInstance().getSelectLiveHouseNo());
                break;
            case Common.LIST_TYPE_SEARCH:
                traitList = instance.getTraitListOfContainsText(Common.getInstance().getSearchString());
                break;
            default:
                break;
        }

        if(traitList != null) {
            for (LiveInfoTrait trait : traitList) {
                CustomCell cell = new CustomCell();
                cell.setLiveInfoTrait(trait);
                mCellList.add(cell);
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
