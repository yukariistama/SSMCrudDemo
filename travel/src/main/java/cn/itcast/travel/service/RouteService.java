package cn.itcast.travel.service;

import cn.itcast.travel.domain.FavoritePage;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 线路service
 */
public interface RouteService {
    //查询pageBean
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);

    /**
     * 根据id查询route
     * @param rid
     * @return
     */
    public Route findOne(String rid);

    /**
     * 查询全部路线,按照人气排序
     * @return
     */
    List<Route> topFavoriteRoute();

    /**
     * 根据用户名查询所有收藏路线
     *
     *
     * @param uid
     * @return
     */
    FavoritePage<Route> findFavorite(int currentPage, String uid);
}
