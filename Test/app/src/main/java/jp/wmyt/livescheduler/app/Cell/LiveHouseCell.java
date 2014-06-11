package jp.wmyt.livescheduler.app.Cell;

import jp.wmyt.livescheduler.app.Master.LiveHouseTrait;

/**
 * Created by miyata on 2014/05/25.
 */
public class LiveHouseCell {
    private LiveHouseTrait trait;
    private String place;

    public void setPlace(String msg) {
        place = msg;
    }
    public String getPlace() {
        return place;
    }

    public void setTrait(LiveHouseTrait _trait){ trait = _trait; }
    public LiveHouseTrait getTrait(){ return trait; }
}
