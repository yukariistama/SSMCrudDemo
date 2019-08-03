package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据id获取seller对象
     * @param id
     * @return
     */
    public Seller findById(int id);
}
