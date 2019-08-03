package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.AutoUser;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();
    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        //获取激活码
        String code = request.getParameter("code");
        if(code != null) {//激活码不为空,调用service完成激活
            //UserService service = new UserServiceImpl();
            boolean flag1 = service.isActive(code);
            boolean flag2 = service.active(code);
            //判断标记
            String msg = null;
            if (flag1) {//已激活
                msg = "您已激活,请勿重复激活";
            } else {//未激活
                if (flag2) {//激活成功
                    msg = "激活成功,请<a href='http://localhost/travel/login.html'>登录</a>";
                } else {//激活失败
                    msg = "激活失败!";
                }
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write(msg);
            }
        }
    }


    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        //判断是否有memoryUser
        String memoryUser = request.getParameter("memoryUser");
        if("on".equalsIgnoreCase(memoryUser)){
            //用户勾选自动登录选项
            //Cookie cookieMemoryUser = new Cookie(request.getParameter("username"),request.getParameter("password"));
            Cookie cookieMemoryUserName = new Cookie("username",request.getParameter("username"));
            cookieMemoryUserName.setMaxAge(60*60*24*30);
            cookieMemoryUserName.setPath("/");
            Cookie cookieMemoryUserPassword = new Cookie("password",request.getParameter("password"));
            cookieMemoryUserPassword.setMaxAge(60*60*24*30);
            cookieMemoryUserPassword.setPath("/");
            response.addCookie(cookieMemoryUserName);
            response.addCookie(cookieMemoryUserPassword);
        }else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
               if(cookie.getName().equals("username") || cookie.getName().equals("password")){
                   cookie.setMaxAge(0);
                   cookie.setPath("/");
                   response.addCookie(cookie);
               }
            }

            for (Cookie cookie : cookies) {
                System.out.println(cookie.getValue());
            }
        }

        //判断验证码
        //获取用户账号密码,判断是否正确,再判断是否激活
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //UserService service = new UserServiceImpl();
        User lu=service.login(user);
        ResultInfo info = new ResultInfo();
        //判断用户对象是否为空
        if(lu == null){//为空,用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //用户激活判定
        if(lu != null && "N".equalsIgnoreCase(lu.getStatus())){//没有激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活,请激活");
        }
        //登陆成功
        if(lu != null && "Y".equalsIgnoreCase(lu.getStatus())){
            info.setFlag(true);
            HttpSession session = request.getSession();
            session.setAttribute("lu",lu);
        }
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    /**
     * 自动登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void autoLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
        //判断是否有cookieMemoryUser
        Cookie[] cookies = request.getCookies();
        AutoUser user = new AutoUser();
        user.setFlage(false);
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if("username".equalsIgnoreCase(name)){
                user.setName(cookie.getValue());
                user.setFlage(true);
            }
            if("password".equalsIgnoreCase(name)){
                user.setPassword(cookie.getValue());
                user.setFlage(true);
            }
        }
        writeValue(user,response);
    }
    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //验证码
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(checkcode_server==null || !checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flage=service.regist(user);
        //响应结果
        ResultInfo info = new ResultInfo();
        if(flage){
            info.setFlag(true);
        }else {
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }
        //把info序列化位json
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(info);
        //把json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    /**
     * 查询单个用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUser(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("lu");
        //将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
    }
}
