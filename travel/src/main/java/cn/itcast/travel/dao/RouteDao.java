package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 根据cid查询总记录数
     */
public int findTotalCount(int cid, String rname);

    /**
     * 根据cid和start,pageSize查询当前页面数据集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);

    /**
     * 根据rid查询route对象
     * @param rid
     * @return
     */
    public Route findOne(int rid);

    /**
     * 按照收藏查询全部
     * @return
     */

    public List<Route> findTopFavoriteAll();
}
