package jp.wmyt.test.app;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private Context context;

    public void setContext(Context c){
        this.context = c;
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

    public int getSelectLiveHouseNo() {
        return selectLiveHouseNo;
    }

    public void setSelectLiveHouseNo(int selectLiveHouseNo) {
        this.selectLiveHouseNo = selectLiveHouseNo;
    }

    public void addFavoriteList(String uniqueId){
        favList.add(uniqueId);
        saveData();
        Log.d("Common::addFavoriteList::",uniqueId);
    }

    public void removeFavoriteList(String uniqueId){
        favList.remove(uniqueId);
        saveData();
        Log.d("Common::removeFavoriteList",uniqueId);
    }

    public ArrayList<String> getFavList() {
        return favList;
    }

    public void saveData(){
        try {
            FileOutputStream out = context.openFileOutput("data.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(out);
            BufferedWriter writer = new BufferedWriter(osw);
            for (String uniqueId : favList) {
                writer.write(uniqueId);
                writer.write("\n");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readData(){
        favList.clear();

        try {
//            BufferedReader reader = new BufferedReader(new FileReader("saveData"));
//            String s;
//            while ((s = reader.readLine()) != null) {
//                favList.add(s);
//            }
            FileInputStream fis = context.openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            String s;
            while ((s = reader.readLine()) != null) {
                favList.add(s);
                Log.d("Common::readData",s);
            }
        }catch (FileNotFoundException e){
            Log.d("Common", "saveData not Found");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
