package jp.wmyt.test.app;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
/**
 * Created by miyata on 2014/05/06.
 */
public class LiveInfoTrait {
    //シングルトン
    private static final LiveInfoTrait instance = new LiveInfoTrait();
    public LiveInfoTrait(){

    }
    public static LiveInfoTrait getInstance(){
        return instance;
    }

    static ArrayList<LiveInfoTrait> traitList;
    static Date minDate;
    static Date maxDate;

    static {
        traitList = new ArrayList<LiveInfoTrait>();
    }

    //メンバ変数
    private int     _liveHouseNo;
    private Date    _liveDate;
    private int     _subNo;
    private String  _eventTitle;
    private String  _act;
    private String  _otherInfo;
    private String  _uniqueID;
    private String  _dayOfWeek;
    private int     _sortNo;

    public int getSortNo() {
        return _sortNo;
    }

    public int getLiveHouseNo() {
        return _liveHouseNo;
    }

    public Date getLiveDate() {
        return _liveDate;
    }

    public int getSubNo() {
        return _subNo;
    }

    public String getAct() {
        return _act;
    }

    public String getDayOfWeek() {
        return _dayOfWeek;
    }

    public String getEventTitle() {
        return _eventTitle;
    }

    public String getOtherInfo() {
        return _otherInfo;
    }

    public String getUniqueID() {
        return _uniqueID;
    }
    //-------

    static String[] dayName = {"日", "月", "火", "水", "木", "金", "土"};

    public ArrayList<LiveInfoTrait> getTraitList(){
        return traitList;
    }

    private void removeAllMast(){
        traitList.clear();
    }

    public ArrayList<LiveInfoTrait> getTraitListOfDate(Date date){
        ArrayList liveList = new ArrayList();
        long dateTime1 = date.getTime();
        for(LiveInfoTrait trait : traitList){
            long dateTime2 = trait._liveDate.getTime();
            int diffDays = (int)((dateTime1 - dateTime2) / 1000*60*24*24);

            if( diffDays == 0 ){
                liveList.add(trait);
            }
        }

        //ソート
        Collections.sort(liveList, new LiveInfoTraitComparator());

        return liveList;
    }

    public ArrayList<LiveInfoTrait> getTraitListOfLiveHouseNo(int liveHouseNo){
        ArrayList liveList = new ArrayList();

        Date currentDate = new Date();
        for(LiveInfoTrait trait : traitList){
            if(trait._liveHouseNo == liveHouseNo && !trait.isPastLive()){
                liveList.add(trait);
            }
        }

        return liveList;
    }

    public LiveInfoTrait getTraitOfUniqueID(String uniqueID){
        for(LiveInfoTrait trait : traitList){
            if(uniqueID.equals(trait._uniqueID)){
                return trait;
            }
        }
        return null;
    }

    public Date getMinDate(){
        if(minDate == null){
            return new Date();
        }
        return minDate;
    }

    public Date getMaxDate(){
        if(maxDate == null){
            return new Date();
        }
        return maxDate;
    }

    public void loadMast(LoadData data){
        //ライブ一覧をクリア
        removeAllMast();

        minDate = null;
        maxDate = null;

        int masterCount = data.getInt16();

        for( int i = 0;i < masterCount;i++ ){
            int     liveHouseNo  = data.getInt16();
            String  liveDate     = String.valueOf(data.getInt32());
            int     subNo        = data.getInt16();
            String  title        = data.getString16();
            String  act          = data.getString16();
            String  otherInfo    = data.getString16();

            LiveInfoTrait trait = new LiveInfoTrait();
            trait.initWithLiveHouseNo(liveHouseNo, liveDate, subNo, title, act, otherInfo);
            traitList.add(trait);
        }
    }

    private LiveInfoTrait initWithLiveHouseNo(int liveHouseNo,
                                              String liveDate,
                                              int subNo,
                                              String eventTitle,
                                              String act,
                                              String otherInfo){
        LiveInfoTrait trait = new LiveInfoTrait();

        trait._liveHouseNo  = liveHouseNo;
        trait._subNo        = subNo;
        trait._eventTitle   = eventTitle;
        trait._act          = act;
        trait._otherInfo    = otherInfo;

        try{
            trait._liveDate = new SimpleDateFormat("yyyyMMdd").parse(liveDate);
        }catch (ParseException e){
            Log.e("parse Error",e.getMessage());
            return null;
        }

        trait._uniqueID = trait._liveDate.toString() + trait._subNo + String.format("%03d", trait._liveHouseNo);

        Calendar day = Calendar.getInstance();
        day.setTime(trait._liveDate);
        trait._dayOfWeek = dayName[day.DAY_OF_WEEK-1];

        LiveHouseTrait liveHouseTrait = LiveHouseTrait.getInstance().getTraitOfLiveHouseNo(liveHouseNo);
        if(liveHouseTrait != null){
            trait._sortNo = liveHouseTrait.getSortNo();
        }

        if( minDate == null || minDate.compareTo(trait._liveDate) > 0 ){
            minDate = trait._liveDate;
        }
        if( maxDate == null || maxDate.compareTo(trait._liveDate) < 0 ){
            maxDate = trait._liveDate;
        }

        return trait;
    }

    //TODO:
    private boolean isFavorite(){
        return false;
    }

    private boolean isPastLive(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date date = cal.getTime();

        return _liveDate.before(date);
    }
}

class LiveInfoTraitComparator implements java.util.Comparator {
    public int compare(Object s, Object t) {
        //               + (x > y)
        // compare x y = 0 (x = y)
        //               - (x < y)
        LiveInfoTrait trait1 = (LiveInfoTrait)s;
        LiveInfoTrait trait2 = (LiveInfoTrait)t;
        return trait1.getSortNo() - trait2.getSortNo();
    }
}