package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao cd = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        List<Category> all = null;
        //从redis中查询
        //获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //使用sortedset排序查询
        //Set<String> category = jedis.zrange("category", 0, -1);
        //查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> tuples = jedis.zrangeWithScores("category", 0, -1);
        if(tuples == null || tuples.size() == 0){//集合为空
            System.out.println("从数据库查询.......了");
            //如果为空,需要从数据库查询,再将数据存入redis
             all = cd.findAll();
             //把all集合中的数据存储到category的key
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("category",all.get(i).getCid(),all.get(i).getCname());
            }
        }else {//category不为空,将set的数据存入list
            System.out.println("从redis中查询........了 ");
            all = new ArrayList<Category>();
            for (Tuple tuple : tuples) {
                Category acategory =new Category();
                acategory.setCname(tuple.getElement());
                acategory.setCid((int)tuple.getScore());
                all.add(acategory);
            }

        }
        return all;
    }
}
