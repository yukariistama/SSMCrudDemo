package cn.itcast.travel.service;

public interface FavoriteService {
    /**
     * 判断用户是否收藏该线路
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid,int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    public void add(String rid, int uid);

    /**
     * route添加收藏数量
     * @param rid
     */
    public void routeCountAdd(int rid);
}
