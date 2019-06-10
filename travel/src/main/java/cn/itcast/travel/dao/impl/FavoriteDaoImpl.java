package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.FavoriteRid;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        String sql = "select * from tab_favorite where rid = ? and uid = ? ";
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        Integer integer = template.queryForObject(sql, Integer.class, rid);
        return integer;
    }

    /**
     * 收藏表添加收藏
     * @param i
     * @param uid
     */
    @Override
    public void add(int i, int uid) {
        String sql = "insert into tab_favorite values(? , ? , ?) ";
        template.update(sql,i,new Date(),uid);
    }
    /**
     * route表添加收藏
     *
     */
    public void routeCountAdd(int rid){
        String sql = "UPDATE tab_route SET COUNT=COUNT+1 WHERE rid = ?";
        template.update(sql,rid);
    }

    @Override
    public List<FavoriteRid> findAllByUid(String uid) {
        String sql = "select rid from tab_favorite where uid = ?";
        List<FavoriteRid> query = template.query(sql, new BeanPropertyRowMapper<FavoriteRid>(FavoriteRid.class), Integer.parseInt(uid));
        return query;
    }
}
