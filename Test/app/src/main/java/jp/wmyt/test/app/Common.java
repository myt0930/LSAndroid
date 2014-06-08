package jp.wmyt.test.app;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        this.favList = new ArrayList<String>();
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
    static public final int LIST_TYPE_SEARCH      = 3;

    private Date liveDate;
    private int selectLiveHouseNo;
    private String searchString;
    private ArrayList<String> favList;

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

    private void addFavoriteList(String uniqueId){
        favList.add(uniqueId);
        saveData();
    }

    private void removeFavoriteList(String uniqueId){
        favList.remove(uniqueId);
        saveData();
    }

    public ArrayList<String> getFavList() {
        return favList;
    }

    public void saveData(){
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("saveData"));
            for (String uniqueId : favList) {
                pw.println(uniqueId);
            }
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readData(){
        favList.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("saveData"));
            String s;
            while ((s = reader.readLine()) != null) {
                favList.add(s);
            }
        }catch (FileNotFoundException e){
            Log.d("Common", "saveData not Found");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
