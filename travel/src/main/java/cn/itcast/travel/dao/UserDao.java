package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findUsername(String username);

    /**
     * 保存用户
     * @param user
     */
    public void save(User user);

    /**
     * 通过激活码查找用户
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 激活用户
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据账号密码获取用户信息
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);
}
