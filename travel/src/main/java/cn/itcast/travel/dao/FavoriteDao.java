package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.FavoriteRid;

import java.util.List;

public interface FavoriteDao {
    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param i
     * @param uid
     */
    public void add(int i, int uid);

    /**
     * route收藏数量增加
     * @param rid
     */
    public void routeCountAdd(int rid);

    /**
     * 通过uid查询该用户收藏的所有rid
     * @param uid
     * @return
     */
    List<FavoriteRid> findAllByUid(String uid);
}
