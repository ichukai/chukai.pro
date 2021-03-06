package pro.chukai.web.interceptor;

import pro.chukai.web.exception.HttpStatusException;
import pro.chukai.web.session.WebSessionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pro.chukai.web.exception.HttpStatusException;
import pro.chukai.web.session.WebSessionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * backend登录拦截器
 *
 * @author chukai
 */
@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private WebSessionSupport webSessionSupport;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (webSessionSupport.islogIn()) {
            return super.preHandle(request, response, handler);
        } else {
            throw HttpStatusException.of(HttpStatus.UNAUTHORIZED);
        }
    }

}
