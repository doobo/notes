package net.hncu.notes.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doobo@foxmail.com on 2017/4/27.
 */
public class SystemInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (request.getSession().getAttribute("root") != null) {
            return true;
        }
        String basePath = request.getContextPath();
        basePath = request.getScheme()
                + "://" + request.getServerName()
                + ":" + request.getServerPort() + basePath + "/";
        String url = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            url = url + "?" + request.getQueryString();
        }
        url = java.net.URLEncoder.encode(url, "utf-8");
        response.sendRedirect(basePath + "public/toRoot.html?url=" + url);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
