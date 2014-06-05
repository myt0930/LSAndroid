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
        this.liveListType = LIST_TYPE_TODAY;
        this.liveDate = Calendar.getInstance().getTime();
    }
    public static Common getInstance(){
        return instance;
    }

    static public int LIST_TYPE_TODAY   = 0;
    static public int LIST_TYPE_DATE    = 1;
    static public int LIST_TYPE_FAV     = 2;
    static public int LIST_TYPE_LIVEHOUSE = 3;

    private int liveListType;
    private Date liveDate;
    private String searchString;

    public int getLiveListType() {
        return liveListType;
    }

    public void setLiveListType(int liveListType) {
        this.liveListType = liveListType;
    }

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
}
