package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.FavoritePage;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收四个参数
        String scurrentPage = request.getParameter("currentPage");
        //String spageSize = request.getParameter("pageSize");
        String scid = request.getParameter("cid");
        String rname = request.getParameter("rname");//路线名称
        //解决乱码问题
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        //处理参数
        int cid = 0;
        if (scid != null && scid.length() > 0 && !"null".equals(scid)) {
            cid = Integer.parseInt(scid);
        }

        int curreentPage = 0;
        if (scurrentPage != null && scurrentPage.length() > 0) {
            curreentPage = Integer.parseInt(scurrentPage);
        } else {
            curreentPage = 1;
        }
//        int pageSize = 0;
//        if (spageSize != null && spageSize.length() > 0) {
//            pageSize = Integer.parseInt(spageSize);
//        } else {
//            pageSize = 5;
//        }
        int  pageSize = 5;
        //调用service查询pageBean对象
        PageBean<Route> pb = service.pageQuery(cid, curreentPage, pageSize, rname);
        //讲pageBean对象序列化为json,返回
        writeValue(pb, response);
    }
    public void topFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service查询全部,按照收藏数量排序
       List<Route> list = service.topFavoriteRoute();
        //讲pageBean对象序列化为json,返回
        writeValue(list, response);
    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收id
        String rid = request.getParameter("rid");
        //调用service查询route对象
        Route route = service.findOne(rid);
        //将route对象转为json写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏了该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路id(rid)
        String rid = request.getParameter("rid");
        //获取当前登录用户的uid
        int uid = 0;
        User user = (User) request.getSession().getAttribute("lu");
        if (user == null) {//游客,未登录
            uid = 0;
        } else {
            uid = user.getUid();
        }
        //调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);
        //写回客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("lu");
        int uid;
        if(user == null){
            return;
        }else {
            uid = user.getUid();
        }
        //调用service添加
        favoriteService.add(rid,uid);
        favoriteService.routeCountAdd(Integer.parseInt(rid));

    }
    public void findFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String scurrentPage = request.getParameter("currentPage");
        String uid = request.getParameter("uid");
        int curreentPage = 0;
        if (scurrentPage != null && scurrentPage.length() > 0) {
            curreentPage = Integer.parseInt(scurrentPage);
        } else {
            curreentPage = 1;
        }
        //调用service查询pageBean对象
        FavoritePage<Route> pb = service.findFavorite(curreentPage,uid);
        //讲pageBean对象序列化为json,返回
        writeValue(pb, response);
    }

}
