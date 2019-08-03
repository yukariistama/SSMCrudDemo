package cn.itcast.pinyougou.service.impl;

import cn.itcast.pinyougou.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @RequiresRoles("ROLE_ADMIN")
    public String test() {
        return "test";
    }

    @RequiresPermissions("PER_ADMIN")
    public String test1() {
        return "test1";
    }
}
