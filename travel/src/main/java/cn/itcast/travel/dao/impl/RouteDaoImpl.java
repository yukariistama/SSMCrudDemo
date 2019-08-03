package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findTotalCount(int cid, String rname) {
        //定义sql模板
        String sql = "select count(*) from tab_route where 1 = 1";
        //定义参数集合
        List params = new ArrayList();
        //判断参数是否为空
        StringBuilder sb =new StringBuilder(sql);
        if(cid!=0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname !=null &&rname.length()>0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();
        System.out.println(sql);
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //定义sql模板
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        //定义参数集合
        List params = new ArrayList();
        if(cid!=0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname !=null &&rname.length()>0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ?");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        System.out.println(sql);
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        System.out.println(sql);
        Route route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        return route;
    }

    @Override
    public List<Route> findTopFavoriteAll() {
        String sql = "SELECT * FROM tab_route ORDER BY COUNT DESC";
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        return query;
    }
}
