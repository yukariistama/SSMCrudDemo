import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.FavoriteRid;
import cn.itcast.travel.util.JDBCUtils;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class sqltest {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Test
    public void test1(){
        String sql = "SELECT rid FROM tab_favorite WHERE uid =  ?";

        List<FavoriteRid> query = template.query(sql, new BeanPropertyRowMapper<FavoriteRid>(FavoriteRid.class), 1);
        for (FavoriteRid favoriteRid : query) {
            System.out.println(favoriteRid.toString());
        }

    }
}
