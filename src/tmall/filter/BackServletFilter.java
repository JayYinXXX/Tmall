package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BackServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 假设访问路径 http://127.0.0.1:8080/tmall/admin_category_list
        // 获取根目录 /tmall
        String contextPath = request.getServletContext().getContextPath();
        // 获取URI /tmall/admin_category_list
        String uri = request.getRequestURI();
        // 剪掉URI中的 /tmall
        uri = StringUtils.remove(uri, contextPath);
        // 判断是否以"/admin_"开头
        if (uri.startsWith("/admin_")) {
            // 截取"_"之间的字符串
            String servletPath = StringUtils.substringBetween(uri, "_") + "Servlet";
            // 截取最后一个"_"之后的字符串
            String method = StringUtils.substringAfterLast(uri, "_");
            request.setAttribute("method", method);
            req.getRequestDispatcher("/" + servletPath).forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
