package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = new CategoryServiceImpl();
    /**
     * 查询所有
     * @param request
     * @param response
     * @throws ServletException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //调用service查询所有
        List<Category> all = service.findAll();
        //序列化json返回
//        ObjectMapper om = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        om.writeValue(response.getOutputStream(),all);
        writeValue(all,response);
    }
    public void login(HttpServletRequest request, HttpServletResponse response)throws ServletException{
    }
}
