package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User u = userDao.findUsername(user.getUsername());
        if(u != null){
            //用户名存在,注册失败
            return false;
        }
        //注册成功,保存用户
        //生成激活码
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        userDao.save(user);
        //发送激活邮件
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活[黑马旅游网]</a>";
        MailUtils.sendMail(user.getEmail(),content,"黑马旅游网激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
       User user = userDao.findByCode(code);
       if(user != null){
           userDao.updateStatus(user);
           return true;
       }
        return false;
    }

    @Override
    public User login(User user) {
        return userDao.login(user.getUsername(),user.getPassword());
    }

    @Override
    public boolean isActive(String code) {
        User user = userDao.findByCode(code);
        if(user!=null && "Y".equalsIgnoreCase(user.getStatus())){
            //已经激活
            return true;
        }
       return false;
    }
}
