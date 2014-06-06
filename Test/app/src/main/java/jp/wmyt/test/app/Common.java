package jp.wmyt.test.app;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by JP10733 on 2014/05/07.
 */
public class Common {

    //シングルトン
    private static final Common instance = new Common();
    public Common(){
        this.liveDate = Calendar.getInstance().getTime();
    }
    public static Common getInstance(){
        return instance;
    }

    static public String KEY_LIST_TYPE      = "kListType";
    static public String KEY_DATE           = "kDate";
    static public String KEY_LIVE_HOUSE_NO  = "kLiviHouseNo";
    static public final int LIST_TYPE_DATE        = 0;
    static public final int LIST_TYPE_FAV         = 1;
    static public final int LIST_TYPE_LIVEHOUSE   = 2;

    private Date liveDate;
    private int selectLiveHouseNo;
    private String searchString;

    public Date getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(Date liveDate) {
        this.liveDate = liveDate;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getSelectLiveHouseNo() {
        return selectLiveHouseNo;
    }

    public void setSelectLiveHouseNo(int selectLiveHouseNo) {
        this.selectLiveHouseNo = selectLiveHouseNo;
    }
}
