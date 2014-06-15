package jp.wmyt.livescheduler.app.Master;

import java.util.ArrayList;

/**
 * Created by miyata on 2014/05/06.
 */
public class LiveHouseTrait {
    //シングルトン
    private static final LiveHouseTrait instance = new LiveHouseTrait();
    public LiveHouseTrait(){

    }
    public static LiveHouseTrait getInstance(){
        return instance;
    }

    static ArrayList<LiveHouseTrait> traitList;

    static {
        traitList = new ArrayList<LiveHouseTrait>();
    }

    //メンバ変数
    private int     _liveHouseNo;
    private String  _name;
    private String  _info;
    private int     _sortNo;

    public int getLiveHouseNo() {
        return _liveHouseNo;
    }

    public int getSortNo() {
        return _sortNo;
    }

    public String getInfo() {
        return _info;
    }

    public String getName() {
        return _name;
    }
    //-------

    public ArrayList<LiveHouseTrait> getTraitList(){
        return traitList;
    }

    private void removeAllMast(){
        traitList.clear();
    }

    public String getLiveHouseName(int no){
        LiveHouseTrait trait = getTraitOfLiveHouseNo(no);
        if(trait != null){
            return trait._name;
        }
        return null;
    }

    public synchronized LiveHouseTrait getTraitOfLiveHouseNo(int no){
        for( LiveHouseTrait trait : traitList ){
            if(trait._liveHouseNo == no){
                return trait;
            }
        }
        return null;
    }

    public void loadMast(LoadData data){
        //ライブ一覧をクリア
        removeAllMast();

        int masterCount = data.getInt16();

        for( int i = 0;i < masterCount;i++ ){
            int liveHouseNo = data.getInt16();
            String name = data.getString16();
            String info = data.getString16();
            int sortNo = data.getInt16();

            LiveHouseTrait trait = new LiveHouseTrait();
            trait.initWithLiveHouseNo(liveHouseNo, name, info, sortNo);
            traitList.add(trait);
        }


    }

    private LiveHouseTrait initWithLiveHouseNo(int no, String name, String info, int sortNo){
        LiveHouseTrait trait = new LiveHouseTrait();
        _liveHouseNo = no;
        _name = name;
        _info = info;
        _sortNo = sortNo;
        return trait;
    }
}

class LiveHouseTraitComparator implements java.util.Comparator {
    public int compare(Object s, Object t) {
        //               + (x > y)
        // compare x y = 0 (x = y)
        //               - (x < y)
        LiveHouseTrait trait1 = (LiveHouseTrait)s;
        LiveHouseTrait trait2 = (LiveHouseTrait)t;
        return trait1.getSortNo() - trait2.getSortNo();
    }
}