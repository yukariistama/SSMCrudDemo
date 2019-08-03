package cn.itcast.travel.domain;

public class FavoriteRid {
    private int rid;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "FavoriteRid{" +
                "rid=" + rid +
                '}';
    }
}
