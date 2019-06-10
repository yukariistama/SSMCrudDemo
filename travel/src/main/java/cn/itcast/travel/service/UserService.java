package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean regist(User user);

    /**
     * 激活用户
     * @param code
     * @return
     */
    boolean active(String code);

    /**
     * d登录方法
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 根据激活码判断是否已激活
     * @param code
     * @return
     */
    boolean isActive(String code);
}
