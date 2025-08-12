package big.data.bigdata.config;

import big.data.bigdata.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class JwtRequestFilter implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //放行Options请求，以支持跨域预检
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        //从请求头提取JWT
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        //如果JWT存在且有效，继续处理请求
        if(username != null && jwtUtil.validateToken(jwt)){
            return true;
        }else {
            //如果JWT无效，返回未授权错误
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            return false;
        }
    }
}
