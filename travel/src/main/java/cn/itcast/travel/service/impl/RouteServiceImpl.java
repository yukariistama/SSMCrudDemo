package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao=new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装pagebean然后返回
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        int totalPage = (totalCount%pageSize)==0 ? (totalCount/pageSize) : (totalCount/pageSize)+1;
        pb.setTotalPage(totalPage);
        int start = (currentPage-1)*pageSize;
        List<Route> list = routeDao.findByPage(cid, start, pageSize,rname);
        pb.setList(list);
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //根据rid去tab_route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //根据route的id查询图片集合
        List<RouteImg> routeImgs = routeImgDao.findByRid(Integer.parseInt(rid));
        //将集合设置到route对象
        route.setRouteImgList(routeImgs);
        //根据route的sid查询卖家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
//        //查询收藏次数
//        int count = favoriteDao.findCountByRid(route.getRid());
//        route.setCount(count);
        return route;
    }

    @Override
    public List<Route> topFavoriteRoute() {
        List<Route>list =routeDao.findTopFavoriteAll();
        return list;
    }

    @Override
    public FavoritePage<Route> findFavorite(int currentPage, String uid) {
        FavoritePage<Route> fp = new FavoritePage<>();
        //当前页码
        fp.setCurrentPage(currentPage);
        //查询所有rid(路线id)
        List<FavoriteRid> list=favoriteDao.findAllByUid(uid);
        //通过rid查询route集合
        List<Route> lr=new ArrayList<>();
        for (FavoriteRid favoriteRid : list) {
            int rid = favoriteRid.getRid();
            Route one = routeDao.findOne(rid);
            lr.add(one);
        }
        //设置route集合
        fp.setList(lr);
        //设置每页信息条数
        fp.setPageSize(12);
        //设置总信息条数
        int totalCount = list.size();
        fp.setTotalCount(totalCount);
        int totalPage = (totalCount%12) == 0 ? totalCount/12 : (totalCount/12)+1;
        fp.setTotalPage(totalPage);

        return fp;
    }
}
