package jp.wmyt.test.app;

/**
 * Created by miyata on 2014/04/28.
 */
public class CustomCell {
    private String place;
    private String title;
    private String act;
    private String uniqueId;
    private LiveInfoTrait liveTrait;

    public void setPlace(String msg) {
        place = msg;
    }
    public String getPlace() {
        return place;
    }

    public void setTitle(String msg) {
        title = msg;
    }
    public String getTitle() {
        return title;
    }

    public String getAct() { return act; }
    public void setAct(String act) { this.act = act; }

    public String getUniqueId() { return uniqueId; }

    public LiveInfoTrait getLiveTrait(){ return liveTrait; }

    public void setLiveInfoTrait(LiveInfoTrait trait){
        liveTrait       = trait;
        int liveHouseNo = trait.getLiveHouseNo();
        this.place      = LiveHouseTrait.getInstance().getLiveHouseName(liveHouseNo);
        this.title      = trait.getEventTitle();
        this.act        = trait.getAct();
        this.uniqueId   = trait.getUniqueID();
    }
}
